import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;

import com.github.onblog.AiPa;
import com.github.onblog.executor.AiPaExecutor;
import com.github.onblog.util.AiPaUtil;
import com.github.onblog.worker.AiPaWorker;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
public class Fundamental{
    private Map<String, List<String>> revenue;      //營收:[當月營收, 上月營收, 去年當月營收, 月增, 年增]
    private Map<String, String> grossMargin;     //營業毛利率
    private Map<String, String> operatingMargin;     //營業利益率
    private Map<String, String> netMargin;      //稅後淨利率
    private Map<String, String> currentRatio;   //流動比率
    private Map<String, String> quickRatio;     //速動比率
    private Map<String, String> debtRatio;      //負債比率
    private Map<String, String> ROE;    //股東權益報酬率
    private Map<String, String> EPS;    //稅後每股盈餘
    private static int nowStock=0;//現在在第幾筆股票

    public Fundamental()throws InstantiationException, IllegalAccessException, ExecutionException, InterruptedException{
        updateRevenue();     //更新營收
        updateOthers();     //更新財報
    }



    public static int getNow(){
        return nowStock;
    }
    public void updateRevenue(){    //更新營收
        revenue = new HashMap<String, List<String>>();
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
        System.out.println(tseUrlData);
        System.out.println(otcUrlData);
        int start = 0, end = 0;
        String stockNum = "";
        String thisMonth = "";
        String lastMonth = "";
        String lastYear = "";
        String monthIncreasing = "";
        String yearIncreasing = "";
        int nownum=0;
        while(!stockNum.equals("9955")){
            //股票代號
            start = tseUrlData.indexOf("<tr align=right><td align=center>", end) + 33;
            end = tseUrlData.indexOf("</td>", start + 1);
            stockNum = tseUrlData.substring(start, end).replaceAll("\\s+","");

            //當月營收
            start = tseUrlData.indexOf("nowrap", end) + 7;
            end = tseUrlData.indexOf("</td>", start + 1);
            thisMonth = tseUrlData.substring(start, end).replaceAll("\\s+","");

            //上月營收
            start = tseUrlData.indexOf("nowrap", end) + 7;
            end = tseUrlData.indexOf("</td>", start + 1);
            lastMonth = tseUrlData.substring(start, end).replaceAll("\\s+","");

            //去年當月營收
            start = tseUrlData.indexOf("nowrap", end) + 7;
            end = tseUrlData.indexOf("</td>", start + 1);
            lastYear = tseUrlData.substring(start, end).replaceAll("\\s+","");

            //月增
            start = tseUrlData.indexOf("nowrap", end) + 7;
            end = tseUrlData.indexOf("</td>", start + 1);
            monthIncreasing = tseUrlData.substring(start, end).replaceAll("\\s+","");

            //年增
            start = tseUrlData.indexOf("nowrap>", end) + 7;
            end = tseUrlData.indexOf("</td>", start + 1);
            yearIncreasing = tseUrlData.substring(start, end).replaceAll("\\s+","");
            nownum++;
            System.out.println(nownum);
            if(nownum>10000)
                break;
            revenue.put(stockNum, List.of(thisMonth, lastMonth, lastYear, monthIncreasing, yearIncreasing));
        }
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println(otcUrlData);
        nownum=0;
        while(!stockNum.equals("8477")){
            //股票代號
            start = otcUrlData.indexOf("<tr align=right><td align=center>", end) + 33;
            end = otcUrlData.indexOf("</td>", start + 1);
            stockNum = otcUrlData.substring(start, end).replaceAll("\\s+","");

            //當月營收
            start = otcUrlData.indexOf("nowrap", end) + 7;
            end = otcUrlData.indexOf("</td>", start + 1);
            thisMonth = otcUrlData.substring(start, end).replaceAll("\\s+","");

            //上月營收
            start = otcUrlData.indexOf("nowrap", end) + 7;
            end = otcUrlData.indexOf("</td>", start + 1);
            lastMonth = otcUrlData.substring(start, end).replaceAll("\\s+","");

            //去年當月營收
            start = otcUrlData.indexOf("nowrap", end) + 7;
            end = otcUrlData.indexOf("</td>", start + 1);
            lastYear = otcUrlData.substring(start, end).replaceAll("\\s+","");

            //月增
            start = otcUrlData.indexOf("nowrap", end) + 7;
            end = otcUrlData.indexOf("</td>", start + 1);
            monthIncreasing = otcUrlData.substring(start, end).replaceAll("\\s+","");

            //年增
            start = otcUrlData.indexOf("nowrap>", end) + 7;
            end = otcUrlData.indexOf("</td>", start + 1);
            yearIncreasing = otcUrlData.substring(start, end).replaceAll("\\s+","");
            nownum++;
            System.out.println(nownum);
            revenue.put(stockNum, List.of(thisMonth, lastMonth, lastYear, monthIncreasing, yearIncreasing));
            if(nownum>10000)
                break;
        }
        for (String keys : revenue.keySet()) System.out.println(keys + revenue.get(keys));    //輸出測試
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println(tseURL);
        System.out.println(otcURL);
    }

    public void updateOthers() throws ExecutionException, InterruptedException {
        grossMargin = new HashMap<String, String>();
        operatingMargin = new HashMap<String, String>();
        netMargin = new HashMap<String, String>();
        currentRatio = new HashMap<String, String>();
        quickRatio = new HashMap<String, String>();
        debtRatio = new HashMap<String, String>();
        ROE = new HashMap<String, String>();
        EPS = new HashMap<String, String>();
        int start = 0, end = 0;

        long time1, time2;      //時間測試
        time1 = System.currentTimeMillis();



        //來源:https://github.com/onblog/AiPa
        List<String> linkList = new ArrayList<>();
        for (String keys : revenue.keySet()){

            linkList.add("https://stock.wearn.com/financial.asp?kind=" + keys);

            //第一步：新建AiPa实例

        }
            AiPaExecutor aiPaExecutor = AiPa.newInstance(new MyAiPaWorker()).setThreads(50);
            //第二步：提交任务
            for (int i = 0; i < linkList.size(); i++) {
                aiPaExecutor.submit(linkList);
            }
            //第三步：读取返回值
            List<Future> futureList = aiPaExecutor.getFutureList();//取回來的資料
        nowStock=0;//抓到第幾個了
        for (String keys : revenue.keySet()){

                //get() 方法会阻塞当前线程直到获取返回值
                System.out.println(nowStock);
                String urlData=futureList.get(nowStock++).get().toString();
                end = urlData.indexOf("<tr class=\"stockalllistbg2\">");
                if(end==-1) continue;       //無股票代號之財報 跳過

                //營業毛利率
                start = urlData.indexOf("<td align=\"right\">", end) + 18;
                end = urlData.indexOf("</td>", start + 1);
                grossMargin.put(keys, urlData.substring(start, end).replaceAll("%&nbsp;",""));

                //營業利益率
                start = urlData.indexOf("<td align=\"right\">", end) + 18;
                end = urlData.indexOf("</td>", start + 1);
                operatingMargin.put(keys, urlData.substring(start, end).replaceAll("%&nbsp;",""));

                //稅後淨利率
                start = urlData.indexOf("<td align=\"right\">", end) + 18;
                end = urlData.indexOf("</td>", start + 1);
                netMargin.put(keys, urlData.substring(start, end).replaceAll("%&nbsp;",""));

                //流動比率
                start = urlData.indexOf("<td align=\"right\">", end) + 18;
                end = urlData.indexOf("</td>", start + 1);
                currentRatio.put(keys, urlData.substring(start, end).replaceAll("%&nbsp;",""));

                //速動比率
                start = urlData.indexOf("<td align=\"right\">", end) + 18;
                end = urlData.indexOf("</td>", start + 1);
                quickRatio.put(keys, urlData.substring(start, end).replaceAll("%&nbsp;",""));

                //負債比率
                start = urlData.indexOf("<td align=\"right\">", end) + 18;
                end = urlData.indexOf("</td>", start + 1);
                debtRatio.put(keys, urlData.substring(start, end).replaceAll("%&nbsp;",""));

                //股東權益報酬率
                start = urlData.indexOf("<td align=\"right\">", end + 100) + 18;
                end = urlData.indexOf("</td>", start + 1);
                ROE.put(keys, urlData.substring(start, end).replaceAll("%&nbsp;",""));

                //稅後每股盈餘
                start = urlData.indexOf("<td align=\"right\">", end) + 18;
                end = urlData.indexOf("</td>", start + 1);
                EPS.put(keys, urlData.substring(start, end).replaceAll("&nbsp;",""));
            }
            //第四步：关闭线程池
            aiPaExecutor.shutdown();
            // old version
           /* for (String keys : revenue.keySet()){       //取得所有股票代號
            System.out.println(nownum++);
            Connection conn = new Connection("https://stock.wearn.com/financial.asp?kind=" + keys , "BIG-5");       //建立個股財報網站連線
            String urlData = conn.getUrlData();
            }*/

        //輸出測試
        for (String keys : grossMargin.keySet()) System.out.println(keys + grossMargin.get(keys));
        for (String keys : operatingMargin.keySet()) System.out.println(keys + operatingMargin.get(keys)); 
        for (String keys : netMargin.keySet()) System.out.println(keys + netMargin.get(keys)); 
        for (String keys : currentRatio.keySet()) System.out.println(keys + currentRatio.get(keys));  
        for (String keys : quickRatio.keySet()) System.out.println(keys + quickRatio.get(keys));  
        for (String keys : debtRatio.keySet()) System.out.println(keys + debtRatio.get(keys)); 
        for (String keys : ROE.keySet()) System.out.println(keys + ROE.get(keys));
        for (String keys : EPS.keySet()) System.out.println(keys + " " + EPS.get(keys));

        time2 = System.currentTimeMillis();  //時間測試
        System.out.println("doSomething()花了：" + (time2-time1)/1000 + "秒");

    }

    public List<String> getRevenue(String stockNum){  //取得個股營收
        return revenue.get(stockNum);
    }

    public String getGrossMargin(String stockNum){  //取得個股營業毛利率
        return grossMargin.get(stockNum);
    }

    public String getOperatingMargin(String stockNum){  //取得個股營業利益率
        return operatingMargin.get(stockNum);
    }

    public String geNetMargin(String stockNum){  //取得個股稅後淨利率
        return netMargin.get(stockNum);
    }

    public String getCurrentRatio(String stockNum){  //取得個股流動比率
        return currentRatio.get(stockNum);
    }

    public String getQuickRatio(String stockNum){  //取得個股速動比率
        return quickRatio.get(stockNum);
    }

    public String getDebtRatio(String stockNum){  //取得個股負債比率
        return debtRatio.get(stockNum);
    }

    public String getROE(String stockNum){  //取得個股股東權益報酬率
        return ROE.get(stockNum);
    }

    public String getEPS(String stockNum){  //取得個股稅後每股盈餘
        return EPS.get(stockNum);
    }
}
