import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;

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

    public Fundamental(){
        updateRevenue();     //更新營收
        updateOthers();     //更新財報
    }

    public void updateRevenue(){    //更新營收
        revenue = new HashMap<String, List<String>>();
        SimpleDateFormat sdFormatYear = new SimpleDateFormat("yyyy");   //年份格式
        SimpleDateFormat sdFormatMonth = new SimpleDateFormat("MM");    //月份格式
        Date date = new Date();     //現在日期
        String year = sdFormatYear.format(date);
        String month = sdFormatMonth.format(date);
        Integer tmp = Integer.valueOf(year) - 1911;     //轉換民國
        year = tmp.toString();
        tmp = Integer.valueOf(month) - 1;   //取上月月份
        month = tmp.toString();

        Connection tseConn = new Connection("https://mops.twse.com.tw/nas/t21/sii/t21sc03_" + year + "_" + month + "_0.html", "BIG-5");     //建立上市營收URL連線
        Connection otcConn = new Connection("https://mops.twse.com.tw/nas/t21/otc/t21sc03_" + year + "_" + month + "_0.html", "BIG-5");     //建立上櫃營收URL連線
        String tseUrlData = tseConn.getUrlData();       //取得URL data
        String otcUrlData = otcConn.getUrlData();       //取得URL data
        
        int start = 0, end = 0;
        String stockNum = "";
        String thisMonth = "";
        String lastMonth = "";
        String lastYear = "";
        String monthIncreasing = "";
        String yearIncreasing = "";

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
            
            revenue.put(stockNum, List.of(thisMonth, lastMonth, lastYear, monthIncreasing, yearIncreasing));
        }
        while(!stockNum.equals("8477")){
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
            
            revenue.put(stockNum, List.of(thisMonth, lastMonth, lastYear, monthIncreasing, yearIncreasing));
        }
        //for (String keys : revenue.keySet()) System.out.println(keys + revenue.get(keys));    //輸出測試
    }

    public void updateOthers(){
        grossMargin = new HashMap<String, String>();
        operatingMargin = new HashMap<String, String>();
        netMargin = new HashMap<String, String>();
        currentRatio = new HashMap<String, String>();
        quickRatio = new HashMap<String, String>();
        debtRatio = new HashMap<String, String>();
        ROE = new HashMap<String, String>();
        EPS = new HashMap<String, String>();
        int start = 0, end = 0;

        /*long time1, time2;      //時間測試
        time1 = System.currentTimeMillis();*/

        for (String keys : revenue.keySet()){       //取得所有股票代號
            Connection conn = new Connection("https://stock.wearn.com/financial.asp?kind=" + keys , "BIG-5");       //建立個股財報網站連線
            String urlData = conn.getUrlData();
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

        //輸出測試
        /*for (String keys : grossMargin.keySet()) System.out.println(keys + grossMargin.get(keys)); 
        for (String keys : operatingMargin.keySet()) System.out.println(keys + operatingMargin.get(keys)); 
        for (String keys : netMargin.keySet()) System.out.println(keys + netMargin.get(keys)); 
        for (String keys : currentRatio.keySet()) System.out.println(keys + currentRatio.get(keys));  
        for (String keys : quickRatio.keySet()) System.out.println(keys + quickRatio.get(keys));  
        for (String keys : debtRatio.keySet()) System.out.println(keys + debtRatio.get(keys)); 
        for (String keys : ROE.keySet()) System.out.println(keys + ROE.get(keys));
        for (String keys : EPS.keySet()) System.out.println(keys + " " + EPS.get(keys)); */

        /*time2 = System.currentTimeMillis();  //時間測試
        System.out.println("doSomething()花了：" + (time2-time1)/1000 + "秒");*/

    }

    public List<String> getRevenue(String stockNum){  //取得個股營收
        return revenue.get(stockNum);
    }

    public List<String> getRevenue(String stockNum){  //取得個股營業毛利率
        return grossMargin.get(stockNum);
    }

    public List<String> getRevenue(String stockNum){  //取得個股營業利益率
        return operatingMargin.get(stockNum);
    }

    public List<String> getRevenue(String stockNum){  //取得個股稅後淨利率
        return netMargin.get(stockNum);
    }

    public List<String> getRevenue(String stockNum){  //取得個股流動比率
        return currentRatio.get(stockNum);
    }

    public List<String> getRevenue(String stockNum){  //取得個股速動比率
        return quickRatio.get(stockNum);
    }

    public List<String> getRevenue(String stockNum){  //取得個股負債比率
        return debtRatio.get(stockNum);
    }

    public List<String> getRevenue(String stockNum){  //取得個股股東權益報酬率
        return ROE.get(stockNum);
    }

    public List<String> getRevenue(String stockNum){  //取得個股稅後每股盈餘
        return EPS.get(stockNum);
    }
}
