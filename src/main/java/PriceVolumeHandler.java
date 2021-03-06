import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PriceVolumeHandler {
    private Map<String, PriceVolume>data;//存價格和價格
    private String CSEHTML;
    private String OTCHTML;
    private List<String> numbers;
    private Date volumedate;
    public PriceVolumeHandler(List<String> stockNumbers, Date date){//取得某天的價跟量
        this.volumedate=date;
        this.numbers=stockNumbers;
        data=new HashMap<>();
        ArrayList<String> numbers=new ArrayList<>(stockNumbers);
        Date current = date;
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
        Long todayToNumber=Long.parseLong(sdFormat.format(current));
        String startDayURL="https://www.twse.com.tw/exchangeReport/MI_INDEX?response=html&date="+ todayToNumber+"&type=ALLBUT0999";
        long time1=System.currentTimeMillis();

        Connection startDayHTML=new Connection(startDayURL,"UTF-8");
        CSEHTML=startDayHTML.getUrlData();
        SimpleDateFormat chineseYearSDF=new SimpleDateFormat("yyyy");
        SimpleDateFormat mdSDF=new SimpleDateFormat("/MM/dd");
        String  year=Long.toString(Long.parseLong(chineseYearSDF.format(current).toString())-1911);
        String md=mdSDF.format(current);
        String OTCHtmlURL="https://www.tpex.org.tw/web/stock/aftertrading/otc_quotes_no1430/stk_wn1430_result.php?l=zh-tw&o=htm&d="+year+md+"&se=EW&s=0,asc,0";
        Connection OTCHTMLConnection=new Connection(OTCHtmlURL,"UTF-8");

        CSEHTML=startDayHTML.getUrlData();
        OTCHTML=OTCHTMLConnection.getUrlData();
        for(int i=0;i<numbers.size();i++){
            String stockNum= numbers.get(i).substring(numbers.get(i).length()-12, numbers.get(i).length()-8);
            if(CSEHTML.indexOf("<td>"+stockNum+"</td>")!=-1){
                int startIndex=CSEHTML.indexOf("<td>"+stockNum+"</td>");
                String stockInfo=CSEHTML.substring(startIndex ,startIndex+1000);
                for(int k=0;k<3;k++){//跳到量的那格
                    stockInfo=stockInfo.substring(stockInfo.indexOf("<td>")+"<td>".length(),stockInfo.length());
                }

                DecimalFormat df= new DecimalFormat("#,###");
                Long thatDayVolume=0L;
                try {
                   thatDayVolume = Long.parseLong(String.valueOf(df.parse(stockInfo.substring(0, stockInfo.indexOf("</td>")))));
                }catch (ParseException e){
                    e.printStackTrace();
                }
                for(int k=0;k<6;k++){//跳到量的那格
                    stockInfo=stockInfo.substring(stockInfo.indexOf("<td>")+"<td>".length(),stockInfo.length());

                }

                Double thatDayPrice=0d;
                try {
                    thatDayPrice = Double.parseDouble(stockInfo.substring(0, stockInfo.indexOf("</td>")));
                }catch (NumberFormatException e){
                    numbers.remove(i);
                }
                PriceVolume stock=new PriceVolume(stockNum,thatDayPrice,thatDayVolume);
                data.put(stockNum,stock);
                //System.out.println(stockNum+stock);
            }else if(OTCHTML.indexOf("<td>"+stockNum+"</td>")!=-1){
                int startIndex=OTCHTML.indexOf("<td>"+stockNum+"</td>");
                String stockInfo=OTCHTML.substring(startIndex ,startIndex+1000);
                for(int k=0;k<8;k++){//跳到量的那格
                   // System.out.println(stockInfo);
                    stockInfo=stockInfo.substring(stockInfo.indexOf("<td")+"<td".length(),stockInfo.length());
                }
                //System.out.println(td1);
                DecimalFormat df= new DecimalFormat("#,###");
                Long thatDayVolume=0L;
                try {
                    thatDayVolume = Long.parseLong(String.valueOf(df.parse(stockInfo.substring(stockInfo.indexOf("align=\"right\">")+"align=\"right\">".length(), stockInfo.indexOf("</td>")))));
                }catch (ParseException e){
                    e.printStackTrace();
                }
                stockInfo=OTCHTML.substring(startIndex ,startIndex+1000);
                for(int k=0;k<3;k++){//跳到量的那格
                    stockInfo=stockInfo.substring(stockInfo.indexOf("<td")+"<td".length(),stockInfo.length());

                }

                Double thatDayPrice=0d;
                try {
                    thatDayPrice = Double.parseDouble(stockInfo.substring(stockInfo.indexOf("align=\"right\">")+"align=\"right\">".length(), stockInfo.indexOf("</td>")));
                }catch (NumberFormatException e){
                    numbers.remove(i);
                }
                PriceVolume stock=new PriceVolume(stockNum,thatDayPrice,thatDayVolume);
                data.put(stockNum,stock);

            }else{

                numbers.remove(i);
            }


        }

    }
    public Map<String, PriceVolume> getData(){return data;}
    public PriceVolumeHandler(List<String> stockNumbers){
        Calendar lastWorkDay=Calendar.getInstance();
        if(Calendar.getInstance().getTime().getDay()==0){
            lastWorkDay.add(Calendar.DAY_OF_YEAR,-2);
        }else if(Calendar.getInstance().getTime().getDay()==6||Calendar.getInstance().getTime().getHours()<15){
            lastWorkDay.add(Calendar.DAY_OF_YEAR,-1);
        }
        volumedate=lastWorkDay.getTime();
        numbers=stockNumbers;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        try {
            CSVReader reader = new CSVReader(new FileReader("csvFile\\priceVolume.csv"));
            String date[]= reader.readNext();
            if(date[0].equals(sdf.format(Calendar.getInstance().getTime()))){
                //讀出
                String[]price=reader.readNext();
                String[]volume=reader.readNext();
                data=new HashMap<>();
                for(int i=0;i<numbers.size();i++){
                    String stockNum = numbers.get(i).substring(numbers.get(i).length()-12, numbers.get(i).length()-8);
                    PriceVolume tmp=new PriceVolume(stockNum,Double.parseDouble(price[i]),Long.parseLong(volume[i]));
                    data.put(stockNum,tmp);
                }
                reader.close();
                System.out.println("讀取成功");
            }else{
                throw new IOException();
            }
        }catch (ArrayIndexOutOfBoundsException | IOException | CsvValidationException e){//找不到或錯天就寫入新的

            System.out.println(lastWorkDay.getTime());
            String[]price=new String[stockNumbers.size()];
            String[]volume=new String[stockNumbers.size()];
            String date[]=new String[1];

          PriceVolumeHandler pV= new PriceVolumeHandler(stockNumbers, lastWorkDay.getTime());
           try {
               CSVWriter writer = new CSVWriter(new FileWriter("csvFile\\priceVolume.csv"));
               date[0]=sdf.format(Calendar.getInstance().getTime());


               int nullCount=0;
               boolean notOpen=false;
               for(int i=0;i<stockNumbers.size();i++){
                   String stockNum = numbers.get(i).substring(numbers.get(i).length()-12, numbers.get(i).length()-8);
                   Map<String, PriceVolume> thisData=pV.getData();
                   PriceVolume tmp=thisData.get(stockNum);
                   if(tmp==null){
                       nullCount++;
                       price[i]="0";
                       volume[i]="0";
                       if(nullCount>50) {
                           notOpen = true;
                           break;
                       }
                   }else {
                       price[i] = tmp.getPrice().toString();
                       volume[i] = tmp.getVolume().toString();
                   }
               }
               if(notOpen){
                   System.out.println("沒開門喔~");
                   lastWorkDay.add(Calendar.DAY_OF_YEAR,-1);
                   date[0]=sdf.format(lastWorkDay.getTime());
                    pV= new PriceVolumeHandler(stockNumbers, lastWorkDay.getTime());
                   for(int i=0;i<stockNumbers.size();i++){
                       String stockNum = numbers.get(i).substring(numbers.get(i).length()-12, numbers.get(i).length()-8);
                       Map<String, PriceVolume> thisData=pV.getData();
                       PriceVolume tmp=thisData.get(stockNum);
                       if(tmp==null){
                           price[i]="0";
                           volume[i]="0";
                       }else {
                           price[i] = tmp.getPrice().toString();
                           volume[i] = tmp.getVolume().toString();
                       }
                   }
               }
               data= pV.getData();
               volumedate= pV.getVolumedate();
               numbers=stockNumbers;
               writer.writeNext(date);
               writer.writeNext(price);
               writer.writeNext(volume);
               writer.close();
               System.out.println("寫入成功");
           }catch (IOException out){
               out.printStackTrace();
           }

        }
    }
    public Map<String,Long> getDayVolume(int n){//拿到date的量
        return new VolumeCSV().continuousDayVolume(n);
    }
    public Map<String,Double> getDayPrice(){//拿到date的price
        //System.out.println(volumedate.toString());
        if(nDayisHoliday(volumedate)){
            Calendar d=Calendar.getInstance();
            d.setTime(volumedate);
            d.add(Calendar.DAY_OF_YEAR,-1);
            PriceVolumeHandler tmp = new PriceVolumeHandler(numbers,d.getTime());
            return tmp.getDayPrice();
        }else {
            Map<String, Double> price = new HashMap<>();

            for (int i = 0; i < numbers.size(); i++) {
                String stockNum = numbers.get(i).substring(numbers.get(i).length()-12, numbers.get(i).length()-8);
                PriceVolume tmp = data.get(stockNum);
                if (tmp != null) {
                    //System.out.println(stockNum + ":" + tmp.getPrice());
                    price.put(stockNum, tmp.getPrice());
                } else {
                    //System.out.println("NULL!!");
                }
            }
            return price;
        }
    }
//    public Map<String,Long> getNDaysVolume(int n)  {//拿到n天之前的價格Map//old
//        System.out.println("n="+n);
//        VolumeCSV VolumeCSV=new VolumeCSV();
//        Map<String,Long>totalVolume=new HashMap<>();
//        Calendar date=Calendar.getInstance();
//        boolean first=false;
//        for(int day=0;day<n;day++){
//
//                Map<String, Long> temp = VolumeCSV.getDateVolume(date.getTime());
//                for (int i = 0; i < numbers.size(); i++) {
//                    String stockNum = numbers.get(i).substring(numbers.get(i).length()-12, numbers.get(i).length()-8);
//                    if (!first) {
//                        first=true;
//                        totalVolume = temp;
//                    } else {
//                        try {
//                            totalVolume.put(stockNum, totalVolume.get(stockNum) + temp.get(stockNum));
//                        } catch (NullPointerException e) {
//
//                            numbers.remove(i);
//                        }
//
//                    }
//                }
//
//
//            date.add(Calendar.DAY_OF_YEAR, -1);
//        }
//        return totalVolume;
//    }

    public Map<String ,Double>getPER(Date date){//取得某天本益比
        Map<String ,Double> PEMap=new HashMap<>();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
        Long todayToNumber=Long.parseLong(sdFormat.format(date));
        String startDayURL="https://www.twse.com.tw/exchangeReport/MI_INDEX?response=html&date="+ todayToNumber+"&type=ALLBUT0999";
        System.out.println(startDayURL);
        Connection startDayHTML=new Connection(startDayURL,"UTF-8");
        ///處理OTC---------
        SimpleDateFormat chineseYearSDF=new SimpleDateFormat("yyyy");//處理年分
        SimpleDateFormat mdSDF=new SimpleDateFormat("/MM/dd");//處理月跟日
        String  year=Long.toString(Long.parseLong(chineseYearSDF.format(date).toString())-1911);//民國
        String md=mdSDF.format(date);
        String OTCHtmlURL="https://www.tpex.org.tw/web/stock/aftertrading/peratio_analysis/pera_result.php?l=zh-tw&o=htm&d="+year+md+"&c=&s=0,asc";
        Connection OTCHTMLConnection=new Connection(OTCHtmlURL,"UTF-8");
        ////取得HTML
        String CSEHTML=startDayHTML.getUrlData();
        String OTCHTML=OTCHTMLConnection.getUrlData();
        ///取得Volume 欄位的資料

        for(int i=0;i<numbers.size();i++) {
            String stockNum = numbers.get(i).substring(numbers.get(i).length()-12, numbers.get(i).length()-8);
            Double thatDayPE = 0D;
            if (CSEHTML.indexOf("<td>" + stockNum + "</td>") != -1) {
                int startIndex = CSEHTML.indexOf("<td>" + stockNum + "</td>");
                String stockInfo = CSEHTML.substring(startIndex, startIndex + 1200);
                for (int k = 0; k < 16; k++) {//跳到本益比的那格
                    //System.out.println(stockInfo);
                    stockInfo = stockInfo.substring(stockInfo.indexOf("<td>") + "<td>".length(), stockInfo.length());
                }
                //System.out.println(td1);
                //DecimalFormat df = new DecimalFormat("#,###");

                thatDayPE = Double.parseDouble(stockInfo.substring(0, stockInfo.indexOf("</td>")).replace( ",", ""));

                ///放入資料

                    PEMap.put(stockNum,thatDayPE);

                //System.out.println(stockNum+stock);
            } else if (OTCHTML.indexOf("<td>" + stockNum + "</td>") != -1) {
                int startIndex = OTCHTML.indexOf("<td>" + stockNum + "</td>");
                String stockInfo = OTCHTML.substring(startIndex, startIndex + 1200>OTCHTML.length()?OTCHTML.length():startIndex+1200);
                for (int k = 0; k < 3; k++) {//跳到量的那格
                    // System.out.println(stockInfo);
                    stockInfo = stockInfo.substring(stockInfo.indexOf("<td") + "<td".length(), stockInfo.length());
                }
                //System.out.println(td1);

                try {
                    thatDayPE = Double.parseDouble(stockInfo.substring(stockInfo.indexOf("align=\"right\">") + "align=\"right\">".length(), stockInfo.indexOf("</td>")));

                }catch (NumberFormatException e){
                    thatDayPE=0D;
                }


                    PEMap.put(stockNum,thatDayPE);

                // System.out.println(stockNum+stock);
            } else {
                // System.out.println(stockNum+"NOT FOUND!!!!!");
                //numbers.remove(i);
            }
            //System.out.println(stockNum+":"+thatDayPE);
        }
        return PEMap;
    }
    public Map<String ,Double>getPER(){
        return getPER(volumedate);
    }
    public Date getVolumedate(){
        return volumedate;
    }
    public boolean nDayisHoliday(Date date){//是不是假日
        // System.out.println(date.toString());
        //System.out.println(date.toString().substring(0,3));
        if(date.toString().substring(0,3).equals("Sun")||date.toString().substring(0,3).equals("Sat")){
            // System.out.println("isholiday!!!");
            return true;
        }
        else{
            return false;
        }


    }
    public static Date addDate(Date dateTime/*待處理的日期*/,int n/*加減天數*/){
//日期格式
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //System.out.println(df.format(new Date(dateTime.getTime()  +n * 24 * 60 * 60 * 1000L)));
//System.out.println(dd.format(new Date(dateTime.getTime()   n * 24 * 60 * 60 * 1000L)));
//注意這裡一定要轉換成Long型別，要不n超過25時會出現範圍溢位，從而得不到想要的日期值
        return new Date(dateTime.getTime()   +n * 24 * 60 * 60 * 1000L);
    }
}
