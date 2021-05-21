import java.util.Date;
import java.text.SimpleDateFormat;

import javax.swing.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

//------------------------------------------營收，財報-----------------------------------------

public class Fundamental{
    private List<String> numbers;                   //股票代號清單

    public Fundamental(){
        numbers = new ArrayList<String>();
        updateRevenue();     //更新營收
        updateFinancialReport();      //更新財報
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

        //-----------------------------------------------上市----------------------------------------------
        try {
            File csv = new File("C:/Users/user/Desktop/csv_file/revenue.csv");//CSV檔案
            BufferedWriter bw = new BufferedWriter(new FileWriter(csv, false));
            bw.write("stockNum,thisMonth,monthIncreasing,yearIncreasing");
            while (!stockNum.equals("9955")) {
                //股票代號
                start = tseUrlData.indexOf("<tr align=right><td align=center>", end) + 33;
                end = tseUrlData.indexOf("</td>", start + 1);
                stockNum = tseUrlData.substring(start, end).replaceAll("\\s+", "").replaceAll(",", "");
                numbers.add(stockNum);

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
            bw.close();
        }
        catch (FileNotFoundException e) {
            //捕獲File物件生成時的異常
            e.printStackTrace();
        }
        catch (IOException e) {
            //捕獲BufferedWriter物件關閉時的異常
            e.printStackTrace();
        }

        //-----------------------------------------上櫃--------------------------------------------------
        try {
            File csv = new File("C:/Users/user/Desktop/csv_file/revenue.csv");//CSV檔案
            BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true));
            while (!stockNum.equals("8477")) {
                //股票代號
                start = otcUrlData.indexOf("<tr align=right><td align=center>", end) + 33;
                end = otcUrlData.indexOf("</td>", start + 1);
                stockNum = otcUrlData.substring(start, end).replaceAll("\\s+", "").replaceAll(",", "");
                numbers.add(stockNum);

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

    public void updateFinancialReport(){ //更新財報
        int start = 0, end = 0;

        long time1, time2;      //時間測試
        time1 = System.currentTimeMillis();
        String grossMargin, operatingMargin, netMargin, currentRatio, quickRatio, debtRatio, ROE, EPS;
        try {
            File csv = new File("C:/Users/user/Desktop/csv_file/financialReport.csv");//CSV檔案
            BufferedWriter bw = new BufferedWriter(new FileWriter(csv, false));
            bw.write("stockNum,grossMargin,operatingMargin,netMargin,currentRatio,quickRatio,debtRatio,ROE,EPS");
            for (String keys : numbers) {
                String url = "https://stock.wearn.com/financial.asp?kind=" + keys;
                Connection myConn = new Connection(url, "UTF-8");     //建立連線
                String urlData = myConn.getUrlData();
                end = urlData.indexOf("<tr class=\"stockalllistbg2\">");
                if (end == -1) continue;       //無股票代號之財報 跳過

                //營業毛利率
                start = urlData.indexOf("<td align=\"right\">", end) + 18;
                end = urlData.indexOf("</td>", start + 1);
                grossMargin = urlData.substring(start, end).replaceAll("%&nbsp;", "").replaceAll(",", "");

                //營業利益率
                start = urlData.indexOf("<td align=\"right\">", end) + 18;
                end = urlData.indexOf("</td>", start + 1);
                operatingMargin = urlData.substring(start, end).replaceAll("%&nbsp;", "").replaceAll(",", "");

                //稅後淨利率
                start = urlData.indexOf("<td align=\"right\">", end) + 18;
                end = urlData.indexOf("</td>", start + 1);
                netMargin = urlData.substring(start, end).replaceAll("%&nbsp;", "").replaceAll(",", "");

                //流動比率
                start = urlData.indexOf("<td align=\"right\">", end) + 18;
                end = urlData.indexOf("</td>", start + 1);
                currentRatio = urlData.substring(start, end).replaceAll("%&nbsp;", "").replaceAll(",", "");

                //速動比率
                start = urlData.indexOf("<td align=\"right\">", end) + 18;
                end = urlData.indexOf("</td>", start + 1);
                quickRatio = urlData.substring(start, end).replaceAll("%&nbsp;", "").replaceAll(",", "");

                //負債比率
                start = urlData.indexOf("<td align=\"right\">", end) + 18;
                end = urlData.indexOf("</td>", start + 1);
                debtRatio = urlData.substring(start, end).replaceAll("%&nbsp;", "").replaceAll(",", "");

                //股東權益報酬率
                start = urlData.indexOf("<td align=\"right\">", end + 100) + 18;
                end = urlData.indexOf("</td>", start + 1);
                ROE = urlData.substring(start, end).replaceAll("%&nbsp;", "").replaceAll(",", "");

                //稅後每股盈餘
                start = urlData.indexOf("<td align=\"right\">", end) + 18;
                end = urlData.indexOf("</td>", start + 1);
                EPS = urlData.substring(start, end).replaceAll("&nbsp;", "").replaceAll(",", "");

                //放入csv
                bw.newLine();
                bw.write(keys + ',' + grossMargin + "," + operatingMargin + "," + netMargin + "," + currentRatio + "," + quickRatio + "," + debtRatio + "," + ROE + "," + EPS);

            }
            bw.close();
        }
        catch (FileNotFoundException e) {
            //捕獲File物件生成時的異常
            e.printStackTrace();
        }
        catch (IOException e) {
            //捕獲BufferedWriter物件關閉時的異常
            e.printStackTrace();
        }

        time2 = System.currentTimeMillis();  //時間測試
        System.out.println("花了：" + (time2-time1)/1000 + "秒");

    }

}
