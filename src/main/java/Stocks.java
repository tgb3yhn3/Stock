import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;
import java.text.SimpleDateFormat;

import org.json.JSONArray;

//---------------------------------------------個股----------------------------------

public class Stocks{
    private final String stockNum;
    private RealTimeInfo realTime;                  //即時資訊
    private InstitutionalInvestors institution;     //三大法人買賣超
    private List<Double> historcalPrices;
    private List<Integer> historcalVolumes;
    private List<String> historcalDates;

    public Stocks(String stockNum){
        this.stockNum = stockNum;
        realTime = new RealTimeInfo(stockNum);
        institution = new InstitutionalInvestors(stockNum);
        historcalPrices = new ArrayList<Double>();
        historcalVolumes = new ArrayList<Integer>();
        historcalDates = new ArrayList<String>();
    }

    public void updateRealTime(){       //更新即時資訊
        realTime.getInfo();
    }

    public void updateInstitution(){        //更新三大法人買賣超
        institution.getInfo();
    }

    public void realTime() throws InterruptedException{     //即時資訊，五秒更新一次，持續更新
        while(true){
            Thread.sleep(5000);
            realTime.getInfo();
            System.out.println(this.toString());
        }
    }

    public void getHistoricalPrice(){
        SimpleDateFormat sdFormatYear = new SimpleDateFormat("yyyyMM");
        Date date = new Date();     //現在日期
        String today = sdFormatYear.format(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        String lastMonth = sdFormatYear.format(cal.getTime());      //上月份日期
        String thisMonth = sdFormatYear.format(date);               //這月份日期
        String lastMonthUrl = "https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=" + lastMonth + "01&stockNo=" + stockNum + "&_=1600694538744";
        String thisMonthUrl = "https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=" + thisMonth + "01&stockNo=" + stockNum + "&_=1600694538744";
        Connection lastMonthConn = new Connection(lastMonthUrl, "UTF-8");
        Connection thisMonthConn = new Connection(thisMonthUrl, "UTF-8");
        String lastMonthUrlData = lastMonthConn.getUrlData();
        String thisMonthUrlData = thisMonthConn.getUrlData();
        JSONObject lastMonthJson = new JSONObject(lastMonthUrlData);
        JSONObject thisMonthJson = new JSONObject(thisMonthUrlData);
        JSONArray lastMonthPrices = lastMonthJson.getJSONArray("data");
        JSONArray thisMonthPrices = thisMonthJson.getJSONArray("data");
        String tmp = "";
        for(int i = 0; i < lastMonthPrices.length(); i++) {
            tmp = lastMonthPrices.getJSONArray(i).getString(6);
            historcalPrices.add(Double.valueOf(tmp));
            tmp = lastMonthPrices.getJSONArray(i).getString(1).replace(",","");
            historcalVolumes.add(Integer.valueOf(tmp)/1000);
            historcalDates.add(lastMonthPrices.getJSONArray(i).getString(0).replace("/",""));
        }
        for(int i = 0; i < thisMonthPrices.length(); i++) {
            tmp = thisMonthPrices.getJSONArray(i).getString(6);
            historcalPrices.add(Double.valueOf(tmp));
            tmp = thisMonthPrices.getJSONArray(i).getString(1).replace(",", "");
            historcalVolumes.add(Integer.valueOf(tmp) / 1000);
            historcalDates.add(thisMonthPrices.getJSONArray(i).getString(0).replace("/", ""));
        }
        /*
        //test print
        System.out.println(historcalDates);
        System.out.println(historcalPrices);
        System.out.println(historcalVolumes);
        */
    }

    public String toString(){
        return stockNum + realTime;
    }

    public RealTimeInfo getRealTime(){
        return realTime;
    }       //取得即時資訊
    public void getNews(){      //前往新聞區
        try { 
            String url = "https://tw.stock.yahoo.com/q/h?s=" + stockNum;
            java.net.URI uri = java.net.URI.create(url); 
            // 獲取當前系統桌面擴充套件 
            java.awt.Desktop dp = java.awt.Desktop.getDesktop(); 
            // 判斷系統桌面是否支援要執行的功能 
            if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                dp.browse(uri);// 獲取系統預設瀏覽器開啟連結 
            } 
        } 
        catch (java.lang.NullPointerException e) { 
            // 此為uri為空時丟擲異常 
            e.printStackTrace(); 
        } 
        catch (java.io.IOException e) { 
            // 此為無法獲取系統預設瀏覽器 
            e.printStackTrace(); 
        }
    } 
}