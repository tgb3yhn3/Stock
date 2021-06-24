import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class volumeCSV {
    private Date today;
    private DateFormat df;
    private Calendar now;
    private Map<String,Long> volumeMap;
    private Map<String,Long> volume;
    private List<String> numbers;
    private int nofind=0;
    //private Object NoSuchElementException;

    public volumeCSV() throws NoSuchElementException{
        today=new Date();
        df=new SimpleDateFormat("yyyyMMdd");//設定日期格式
        now=Calendar.getInstance();//作為日期加減用
        volumeMap=new HashMap<>();
        numbers=new ArrayList<>();
        try{
            CSVReader reader = new CSVReader(new FileReader("csvFile\\stockNum.csv"));
            String [] nextLine;
            int i=0;
            while ((nextLine = reader.readNext()) != null) {//從stockNum讀出來改成橫排
                // nextLine[] is an array of values ​​from the line
                volumeMap.put(nextLine[0].substring(0,4), 0L);//初始化Map;
                numbers.add(i,nextLine[0].substring(0,4));//初始化numbers(股票代號)
                i++;
        }
        }catch (IOException | CsvValidationException e){
            e.printStackTrace();
        }
    }
    public String[] dataGetter(Date date) {//取得某天資料
          volume=volumeMap;
        //String []thatDay=new String[2000];
        //處理CSE----
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
        String OTCHtmlURL="https://www.tpex.org.tw/web/stock/aftertrading/otc_quotes_no1430/stk_wn1430_result.php?l=zh-tw&o=htm&d="+year+md+"&se=EW&s=0,asc,0";
        Connection OTCHTMLConnection=new Connection(OTCHtmlURL,"UTF-8");
        ////取得HTML
        String CSEHTML=startDayHTML.getUrlData();
        String OTCHTML=OTCHTMLConnection.getUrlData();
        ///取得Volume 欄位的資料
        nofind=0;
        for(int i=0;i<numbers.size();i++) {

            String stockNum = numbers.get(i);
            if (CSEHTML.indexOf("<td>" + stockNum + "</td>") != -1) {
                int startIndex = CSEHTML.indexOf("<td>" + stockNum + "</td>");
                String stockInfo = CSEHTML.substring(startIndex, startIndex + 1000);
                for (int k = 0; k < 3; k++) {//跳到量的那格
                    stockInfo = stockInfo.substring(stockInfo.indexOf("<td>") + "<td>".length(), stockInfo.length());
                }
                //System.out.println(td1);
                DecimalFormat df = new DecimalFormat("#,###");
                Long thatDayVolume = 0L;
                try {
                    thatDayVolume = Long.parseLong(String.valueOf(df.parse(stockInfo.substring(0, stockInfo.indexOf("</td>")))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for (int k = 0; k < 6; k++) {//跳到量的那格
                    stockInfo = stockInfo.substring(stockInfo.indexOf("<td>") + "<td>".length(), stockInfo.length());
                    //System.out.println(stockInfo);
                }
                // System.out.println(stockInfo);
                Double thatDayPrice = 0d;
                try {
                    thatDayPrice = Double.parseDouble(stockInfo.substring(0, stockInfo.indexOf("</td>")));
                } catch (NumberFormatException e) {
                    //numbers.remove(i);
                }
                ///放入資料
               if( volume.containsKey(numbers.get(i))){
                   volume.put(numbers.get(i),thatDayVolume);
               }else{
                   System.out.println("沒找到"+numbers.get(i));
               }
                //System.out.println(numbers.get(i)+stock);
            } else if (OTCHTML.indexOf("<td>" + stockNum + "</td>") != -1) {
                int startIndex = OTCHTML.indexOf("<td>" + stockNum + "</td>");
                String stockInfo = OTCHTML.substring(startIndex, startIndex + 1000);
                for (int k = 0; k < 8; k++) {//跳到量的那格
                    // System.out.println(stockInfo);
                    stockInfo = stockInfo.substring(stockInfo.indexOf("<td") + "<td".length(), stockInfo.length());
                }
                //System.out.println(td1);
                DecimalFormat df = new DecimalFormat("#,###");
                Long thatDayVolume = 0L;
                try {
                    thatDayVolume = Long.parseLong(String.valueOf(df.parse(stockInfo.substring(stockInfo.indexOf("align=\"right\">") + "align=\"right\">".length(), stockInfo.indexOf("</td>")))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                stockInfo = OTCHTML.substring(startIndex, startIndex + 1000);
                for (int k = 0; k < 3; k++) {//跳到量的那格
                    stockInfo = stockInfo.substring(stockInfo.indexOf("<td") + "<td".length(), stockInfo.length());
                    //System.out.println(stockInfo);
                }
                // System.out.println(stockInfo);
                Double thatDayPrice = 0d;
                try {
                    thatDayPrice = Double.parseDouble(stockInfo.substring(stockInfo.indexOf("align=\"right\">") + "align=\"right\">".length(), stockInfo.indexOf("</td>")));
                } catch (NumberFormatException e) {
                    //numbers.remove(i);
                }
                if( volume.containsKey(numbers.get(i))){
                    volume.put(numbers.get(i),thatDayVolume);
                }
                // System.out.println(numbers.get(i)+stock);
            } else {
                nofind++;
                // System.out.println(stockNum+"NOT FOUND!!!!!");
                //numbers.remove(i);
                if(nofind >50){
                    System.out.println("假日");
                    volume=volumeMap;
                    return null;

                }
            }
        }
            return MaptoString(date);
    }
    public  Map<String,Long> getVolume(){
        return volumeMap;
    }


    public Map<String,Long> getDateVolume(Date date){//old
        Map<String,Long> temp=new HashMap<>();
        try {
            CSVReader reader = new CSVReader(new FileReader("csvFile\\temp.csv"));
            String []nextLine;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String[]stockNum=reader.readNext();


            //reader.readNext();
            while ((nextLine = reader.readNext()) != null) {

                //System.out.println(nextLine[0]+":"+sdf.format(date));
                if(nextLine[0].equals(sdf.format(date))){
//                    for (int i = 0; i < nextLine.length; i++) {
//                        System.out.print(nextLine[i]+" ");
//                    }
//                    System.out.println();

                    for(int i=1;i<numbers.size();i++){
                        //System.out.print(i);
                        try {

                            //System.out.println("put:"+stockNum[i]+"->"+ Long.parseLong(nextLine[i]));
                            temp.put(stockNum[i], Long.parseLong(nextLine[i]));
                        }catch (NumberFormatException e){
                            //System.out.println("formatEXCEPTION"+stockNum[j++]);
                        }
                    }
                }
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();

        } catch (CsvValidationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public  Map<String,Long> continuousDayVolume(int Days){
        //日期轉字串
        Calendar lastWorkDay=Calendar.getInstance();
        if(Calendar.getInstance().getTime().getDay()==0){
            lastWorkDay.add(Calendar.DAY_OF_YEAR,-2);
        }else if(Calendar.getInstance().getTime().getDay()==6||Calendar.getInstance().getTime().getHours()<15){
            lastWorkDay.add(Calendar.DAY_OF_YEAR,-1);
        }
        Calendar thatDay=lastWorkDay;
        thatDay.add(Calendar.DAY_OF_YEAR,-1*(Days-1));
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        String thatDayStr=sdf.format(thatDay.getTime());
        System.out.println(thatDayStr);
        Map<String,Long> volumeTotal=new HashMap<>();
        for (String i:numbers){//map初始化
            volumeTotal.put(i,0L);
            //System.out.println(i);
        }
        try { //開啟CSV搜尋
            CSVReader reader = new CSVReader(new FileReader("csvFile\\temp.csv"));
            List<String[]> getData=stringReverse.reverse(reader.readAll());
            for(int i=0;i<Days;i++){
                String[] tmp=getData.get(i);
                for(int j=0;j<numbers.size();j++){
                    volumeTotal.put(numbers.get(j),Long.parseLong(tmp[j+1])+volumeTotal.get(numbers.get(j)));
                }
            }
        }catch (FileNotFoundException fe){
            System.out.println("找不到temp.csv檔案");
        } catch (CsvValidationException e) {
            System.out.println("reader錯誤!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }


        return volumeTotal;
    }
    public  String[] MaptoString(Date date) {
        String [] thatDay=new String[2000];
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
        thatDay[0]=sdFormat.format(date);
        for(int i=1;i<=numbers.size();i++){
            thatDay[i]=(volume.get(numbers.get(i-1))).toString();
        }
        return thatDay;
    }
    public  void writeInCSV(int n){//寫入csv 日期由今天往回推

        try {
            // adding header to csv
            now.add(Calendar.DAY_OF_YEAR,-1*(n-1));
            for(int i=0;i<n;i++){
                File file=new File("csvFile\\temp.csv");
                FileWriter outputfile = new FileWriter(file,true);//檔案輸出 以append方式

                // create volumeCSV object filewriter object as parameter
                CSVWriter writer = new CSVWriter(outputfile);
                String[] thatDay=new String[2000];

                    thatDay = dataGetter(now.getTime());


                    writer.writeNext(thatDay);
                    writer.close();
//                System.out.println(now.getTime());
                    now.add(Calendar.DAY_OF_YEAR,1);



                try {
                    Thread.sleep(5000);
                }catch  (Exception e){
                    e.printStackTrace();
                }finally {
                    outputfile.close();
                }

            }
            // closing writer connection

        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
    }
    public void initialize(){//初始化 把股票代號寫入csv
        File file=new File("csvFile\\temp.csv");
        try {
            FileWriter outputfile = new FileWriter(file, false);//檔案輸出 以append方式
            // create volumeCSV object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);
            String[] stock=new String[2000];
            int now=1;//第一格留空位;
            CSVReader reader = new CSVReader(new FileReader("csvFile\\stockNum.csv"));
            String [] nextLine;
            stock[0]="0000";
            while ((nextLine = reader.readNext()) != null) {//從stockNum讀出來改成橫排
                // nextLine[] is an array of values ​​from the line
                volumeMap.put(nextLine[0].substring(0,4),0L);//初始化Map;
               stock[now]=nextLine[0].substring(0,4);
               now++;
            }

            writer.writeNext(stock);
            writer.close();
        }
        catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
    public  static  int  needToUpdate() {
        long Days = 0;
        try {
            CSVReader reader = new CSVReader(new FileReader("csvFile\\temp.csv"));
            String[] nextLine;
            String[] fin = new String[2000];
            while ((nextLine = reader.readNext()) != null) {//從stockNum讀出來改成橫排
                // nextLine[] is an array of values ​​from the line
                fin = nextLine;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            System.out.println(fin[0]);
            Date date = sdf.parse(fin[0]);
            Date date1 = new Date();

            try {
                long time = date.getTime();
                long time1 = date1.getTime();
                Days = (int) ((time1 - time) / (24
                        * 60 * 60 * 1000));
            } catch (Exception e) {


                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return (int)Days;
    }
    public void  updater(int Days){//資料更新 抓今天 到 最後一次登記的日期

//            System.out.println(Days);
//            System.out.println(date);
//            System.out.println(date1);
//            System.out.println(date.getTime()-date1.getTime());
            writeInCSV( Days);

    }


}
