import com.github.onblog.AiPa;
import com.github.onblog.executor.AiPaExecutor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.nio.charset.Charset;
import java.util.*;
import java.text.SimpleDateFormat;

import java.io.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

//------------------------------------------營收，財報-----------------------------------------

public class Fundamental{
    private List<String> numbers;                   //股票代號清單

    public Fundamental(){
        numbers = new ArrayList<String>();
        File stockNumCsv = new File("C:/Users/user/Desktop/csv_file/stockNum.csv");  // CSV檔案路徑
        BufferedReader stockNumBr = null;
        try {
            stockNumBr = new BufferedReader(new FileReader(stockNumCsv));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";
        try {
            while ((line = stockNumBr.readLine()) != null){ //讀取到的內容給line變數
                numbers.add(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateRevenue(){    //更新營收
        SimpleDateFormat sdFormatYear = new SimpleDateFormat("yyyy");   //年份格式
        SimpleDateFormat sdFormatMonth = new SimpleDateFormat("MM");    //月份格式
        SimpleDateFormat sdFormatDay = new SimpleDateFormat("dd");
        Date date = new Date();     //現在日期
        String year = sdFormatYear.format(date);
        String month = sdFormatMonth.format(date);
        String day = sdFormatDay.format(date);
        Integer tmp = Integer.valueOf(year) - 1911;     //轉換民國
        year = tmp.toString();
        if(Integer.valueOf(day)>=10)
            tmp = Integer.valueOf(month) - 1;   //取上月月份
        else
            tmp = Integer.valueOf(month) - 2;   //取上上月月份
        month = tmp.toString();

        String tseURL="";
        String otcURL="";
        tseURL="https://mops.twse.com.tw/nas/t21/sii/t21sc03_" + year + "_" + month + "_0.html";
        otcURL="https://mops.twse.com.tw/nas/t21/otc/t21sc03_" + year + "_" + month + "_0.html";
        Connection tseConn = new Connection(tseURL, "BIG-5");     //建立上市營收URL連線
        Connection otcConn = new Connection(otcURL, "BIG-5");     //建立上櫃營收URL連線
        String tseUrlData = tseConn.getUrlData();       //取得URL data
        String otcUrlData = otcConn.getUrlData();
        //--------Find Month Test---------
        int testStar=0,testEnd=0;
        testStar = tseUrlData.indexOf("<tr align=right><td align=center>", testEnd) + 33;
        if(tseUrlData.indexOf("</td>", testStar + 1)==-1||otcUrlData.indexOf("</td>", testStar + 1)==-1){//測試會不會找不到尾巴
            Integer correctMonth=(Integer.valueOf(month)-2);
            month=correctMonth.toString();
            tseURL="https://mops.twse.com.tw/nas/t21/sii/t21sc03_" + year + "_" + month + "_0.html";
            otcURL="https://mops.twse.com.tw/nas/t21/otc/t21sc03_" + year + "_" + month + "_0.html";
            tseConn = new Connection(tseURL, "BIG-5");     //建立上市營收URL連線
            otcConn = new Connection(otcURL, "BIG-5");     //建立上櫃營收URL連線
            tseUrlData = tseConn.getUrlData();
            otcUrlData = otcConn.getUrlData();
        }
        //-------------test end---------------
        int start = 0, end = 0;
        String stockNum = "";
        String thisMonth = "";
        String lastMonth = "";
        String lastYear = "";
        String monthIncreasing = "";
        String yearIncreasing = "";
        File csv, stockNumCsv;
        BufferedWriter bw, stockNumBw;

        //-----------------------------------------------上市----------------------------------------------
        try {
            csv = new File("C:/Users/user/Desktop/csv_file/revenue.csv");//營收CSV檔案
            stockNumCsv = new File("C:/Users/user/Desktop/csv_file/stockNum.csv");//股票代號CSV檔案
            bw = new BufferedWriter(new FileWriter(csv, false));
            stockNumBw = new BufferedWriter(new FileWriter(stockNumCsv, false));
            bw.write("stockNum,thisMonth,monthIncreasing,yearIncreasing");
            while (!stockNum.equals("9955")) {
                //股票代號
                start = tseUrlData.indexOf("<tr align=right><td align=center>", end) + 33;
                end = tseUrlData.indexOf("</td>", start + 1);
                stockNum = tseUrlData.substring(start, end).replaceAll("\\s+", "").replaceAll(",", "");
                stockNumBw.write(stockNum);
                stockNumBw.newLine();

                //當月營收
                start = tseUrlData.indexOf("nowrap", end) + 7;
                end = tseUrlData.indexOf("</td>", start + 1);
                thisMonth = tseUrlData.substring(start, end).replaceAll("\\s+", "").replaceAll(",", "");

                //上月營收
                start = tseUrlData.indexOf("nowrap", end) + 7;
                end = tseUrlData.indexOf("</td>", start + 1);
                lastMonth = tseUrlData.substring(start, end).replaceAll("\\s+", "").replaceAll(",", "");

                //去年當月營收
                start = tseUrlData.indexOf("nowrap", end) + 7;
                end = tseUrlData.indexOf("</td>", start + 1);
                lastYear = tseUrlData.substring(start, end).replaceAll("\\s+", "").replaceAll(",", "");

                //月增
                start = tseUrlData.indexOf("nowrap", end) + 7;
                end = tseUrlData.indexOf("</td>", start + 1);
                monthIncreasing = tseUrlData.substring(start, end).replaceAll("\\s+", "").replaceAll(",", "");

                //年增
                start = tseUrlData.indexOf("nowrap>", end) + 7;
                end = tseUrlData.indexOf("</td>", start + 1);
                yearIncreasing = tseUrlData.substring(start, end).replaceAll("\\s+", "").replaceAll(",", "");

                bw.newLine();   //下一行
                bw.write(stockNum + ',' + thisMonth + "," + monthIncreasing + "," + yearIncreasing);    //寫入csv
            }
            end = 0;
            while (!stockNum.equals("8477")) {
                //股票代號
                start = otcUrlData.indexOf("<tr align=right><td align=center>", end) + 33;
                end = otcUrlData.indexOf("</td>", start + 1);
                stockNum = otcUrlData.substring(start, end).replaceAll("\\s+", "").replaceAll(",", "");
                stockNumBw.write(stockNum);
                stockNumBw.newLine();

                //當月營收
                start = otcUrlData.indexOf("nowrap", end) + 7;
                end = otcUrlData.indexOf("</td>", start + 1);
                thisMonth = otcUrlData.substring(start, end).replaceAll("\\s+", "").replaceAll(",", "");

                //上月營收
                start = otcUrlData.indexOf("nowrap", end) + 7;
                end = otcUrlData.indexOf("</td>", start + 1);
                lastMonth = otcUrlData.substring(start, end).replaceAll("\\s+", "").replaceAll(",", "");

                //去年當月營收
                start = otcUrlData.indexOf("nowrap", end) + 7;
                end = otcUrlData.indexOf("</td>", start + 1);
                lastYear = otcUrlData.substring(start, end).replaceAll("\\s+", "").replaceAll(",", "");

                //月增
                start = otcUrlData.indexOf("nowrap", end) + 7;
                end = otcUrlData.indexOf("</td>", start + 1);
                monthIncreasing = otcUrlData.substring(start, end).replaceAll("\\s+", "").replaceAll(",", "");

                //年增
                start = otcUrlData.indexOf("nowrap>", end) + 7;
                end = otcUrlData.indexOf("</td>", start + 1);
                yearIncreasing = otcUrlData.substring(start, end).replaceAll("\\s+", "").replaceAll(",", "");

                bw.newLine();   //下一行
                bw.write(stockNum + ',' + thisMonth + "," + monthIncreasing + "," + yearIncreasing);  //寫入csv
            }
            bw.close();
            stockNumBw.close();
        }
        catch (FileNotFoundException e) {
            //捕獲File物件生成時的異常
            e.printStackTrace();
        }
        catch (IOException e) {
            //捕獲BufferedWriter物件關閉時的異常
            e.printStackTrace();
        }
    }

    public void updateFinancialReport() throws ExecutionException, InterruptedException, IOException{ //更新財報
        List<String> profitability;

        File profitabilityCsv = new File("C:/Users/user/Desktop/csv_file/profitability.csv");
        BufferedWriter bw = new BufferedWriter(new FileWriter(profitabilityCsv, false));
        long time1, time2;      //時間測試
        time1 = System.currentTimeMillis();

        //來源:https://github.com/onblog/AiPa
        List<String> linkList = new ArrayList<>();
        //放入URL
        for (int i = 0; i < 1; i++) linkList.add("https://concords.moneydj.com/z/zc/zce/zce_" + "2892" + ".djhtm");

        AiPaExecutor aiPaExecutor = AiPa.newInstance(new MyAiPaWorker()).setThreads(15).setCharset(Charset.forName("big5")); //15線程
        //提交任务
        for (int i = 0; i < linkList.size(); i++) aiPaExecutor.submit(linkList);

        //讀取返回值
        List<Future> futureList = aiPaExecutor.getFutureList();//取回來的資料

        bw.write("grossMargin" + "," + "isGreater" + "," + "eps1" + "," + "eps2" + "," + "esp3" + "," + "eps4");
        bw.newLine();
        for (int i = 0; i < 1; i++){
            //get(); //方法会阻塞当前线程直到获取返回值
            String urlData=futureList.get(i).get().toString();
            String subUrlData = urlData.substring(urlData.indexOf("EPS(元)")+7, urlData.indexOf("以上資料僅供參考"));
            profitability = Arrays.asList(subUrlData.replaceAll(",","").replaceAll("%", "").split(" "));
            System.out.println(numbers.get(i) + " " + profitability);
            String[] tmp = {"0", "0", "0", "0", "0", "0", "0", "0"};
            if(profitability.size()%11==0){
                tmp[0] = (profitability.size()<11)? "0":profitability.get(4);
                tmp[1] = (profitability.size()<22)? "0":((Double.parseDouble(profitability.get(4))>Double.parseDouble(profitability.get(15)))? "1":"0");
                tmp[2] = (profitability.size()<11)? "0":profitability.get(10);
                tmp[3] = (profitability.size()<22)? "0":profitability.get(21);
                tmp[4] = (profitability.size()<33)? "0":profitability.get(32);
                tmp[5] = (profitability.size()<44)? "0":profitability.get(43);
            }
            bw.write(numbers.get(i) + "," + tmp[0] + "," + tmp[1] + "," + tmp[2] + "," + tmp[3] + "," + tmp[4] + "," + tmp[5]);
            bw.newLine();
        }
        bw.close();
        //關閉線程池
        aiPaExecutor.shutdown();

        time2 = System.currentTimeMillis();  //時間測試
        System.out.println("doSomething()花了：" + (time2-time1)/1000 + "秒");

    }

    public List<String> getNumbers() {
        return numbers;
    }
}
