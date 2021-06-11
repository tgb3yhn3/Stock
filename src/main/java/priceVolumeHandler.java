import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class priceVolumeHandler {
    private Map<String,priceVolume>data;
    String CSEHTML;
    String OTCHTML;
    List<String> numbers;
    Date volumedate;
    public priceVolumeHandler( List<String> stockNumbers,Date date){//取得某天的價跟量
        this.volumedate=date;
        this.numbers=stockNumbers;
        data=new HashMap<>();
        ArrayList<String> numbers=new ArrayList<>(stockNumbers);
        Date current = date;
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
        Long todayToNumber=Long.parseLong(sdFormat.format(current));
                String startDayURL="https://www.twse.com.tw/exchangeReport/MI_INDEX?response=html&date="+ todayToNumber+"&type=ALLBUT0999";

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
            String stockNum=numbers.get(i);
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
                priceVolume stock=new priceVolume(numbers.get(i),thatDayPrice,thatDayVolume);
                data.put(numbers.get(i),stock);
                //System.out.println(numbers.get(i)+stock);
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
                priceVolume stock=new priceVolume(numbers.get(i),thatDayPrice,thatDayVolume);
                data.put(numbers.get(i),stock);

            }else{

                numbers.remove(i);
            }


        }
    }
    public Map<String,Long> getDayVolume(int n){//拿到date的量
        return new volumeCSV().continuousDayVolume(n);
    }
    public Map<String,Double> getDayPrice(){//拿到date的price
        System.out.println(volumedate.toString());
        if(nDayisHoliday(volumedate)){
            Calendar d=Calendar.getInstance();
            d.setTime(volumedate);
            d.add(Calendar.DAY_OF_YEAR,-1);
            priceVolumeHandler tmp=new priceVolumeHandler(numbers,d.getTime());
            return tmp.getDayPrice();
        }else {
            Map<String, Double> price = new HashMap<>();

            for (int i = 0; i < numbers.size(); i++) {
                priceVolume tmp = data.get(numbers.get(i));
                if (tmp != null) {
                    //System.out.println(numbers.get(i) + ":" + tmp.getPrice());
                    price.put(numbers.get(i), tmp.getPrice());
                } else {
                    //System.out.println("NULL!!");
                }
            }
            return price;
        }
    }
//    public Map<String,Long> getNDaysVolume(int n)  {//拿到n天之前的價格Map//old
//        System.out.println("n="+n);
//        volumeCSV volumeCSV=new volumeCSV();
//        Map<String,Long>totalVolume=new HashMap<>();
//        Calendar date=Calendar.getInstance();
//        boolean first=false;
//        for(int day=0;day<n;day++){
//
//                Map<String, Long> temp = volumeCSV.getDateVolume(date.getTime());
//                for (int i = 0; i < numbers.size(); i++) {
//                    if (!first) {
//                        first=true;
//                        totalVolume = temp;
//                    } else {
//                        try {
//                            totalVolume.put(numbers.get(i), totalVolume.get(numbers.get(i)) + temp.get(numbers.get(i)));
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
            String stockNum = numbers.get(i);
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

                    PEMap.put(numbers.get(i),thatDayPE);

                //System.out.println(numbers.get(i)+stock);
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


                    PEMap.put(numbers.get(i),thatDayPE);

                // System.out.println(numbers.get(i)+stock);
            } else {
                // System.out.println(stockNum+"NOT FOUND!!!!!");
                //numbers.remove(i);
            }
            //System.out.println(numbers.get(i)+":"+thatDayPE);
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
