import com.github.onblog.AiPa;
import com.github.onblog.executor.AiPaExecutor;


import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.Future;
import java.util.Calendar;
import java.util.Date;

public class Investors {

    List<String> stockNum;

    public Investors(){
        //讀取股票代號
        stockNum = new ArrayList<String>();
        try {
            File stockNumCsv = new File("csvFile/stockNum.csv");  // CSV檔案路徑
            BufferedReader stockNumBr = new BufferedReader(new FileReader(stockNumCsv));
            String line = "";
            while ((line = stockNumBr.readLine()) != null) { //讀取到的內容給line變數
                stockNum.add(line.substring(0,4));
            }

            //關閉檔案
            stockNumBr.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getInfo(){
        //---------------------取得日期-------------------
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) +1 ;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int last_month = (month==1)? 12:month-1;
        String thisMonth = String.format("%d-%d-%d", year, month, day);
        String lastMonth = String.format("%d-%d", year, last_month);
        //-------------------------------------------------

        long time1, time2;      //時間測試
        time1 = System.currentTimeMillis();

        //來源:https://github.com/onblog/AiPa
        //儲存URL的list
        List<String> linkList = new ArrayList<>();
        //放入URL
        for (int i = 0; i < stockNum.size(); i++) linkList.add("https://concords.moneydj.com/z/zc/zcl/zcl.djhtm?a=" + stockNum.get(i) + "&c=" + lastMonth + "&d=" + thisMonth);

        AiPaExecutor aiPaExecutor = AiPa.newInstance(new MyAiPaWorker()).setThreads(10).setCharset(Charset.forName("big5")).setMaxFailCount(10); //10線程

        //提交任务
        for (int i = 0; i < linkList.size(); i++) aiPaExecutor.submit(linkList);

        //讀取返回值
        List<Future> futureList = aiPaExecutor.getFutureList(); //取回來的資料


        try {
            File foreignCsv = new File("csvFile/foreign.csv");//外資CSV檔案
            File trustCsv = new File("csvFile/trust.csv");    //投信CSV檔案
            File dealerCsv = new File("csvFile/dealer.csv");  //自營商CSV檔案
            BufferedWriter foreignBw = new BufferedWriter(new FileWriter(foreignCsv, false));
            BufferedWriter trustBw = new BufferedWriter(new FileWriter(trustCsv, false));
            BufferedWriter dealerBw = new BufferedWriter(new FileWriter(dealerCsv, false));

            List<String> tmp = new ArrayList<String>(); //暫存取到網頁資訊之list
            String urlData = "";
            for (int i = 0; i < stockNum.size(); i++) {
                //get(); //方法会阻塞当前线程直到获取返回值
                try {
                    urlData = futureList.get(i).get().toString();
                } catch (Exception e) { //任何抓取資料之錯誤
                    continue;   //忽略此筆
                }
                tmp = Arrays.asList(urlData.substring(urlData.indexOf("三大法人") + 5, urlData.indexOf("合計買賣超")).replaceAll(",", "").replaceAll(" %", "").split(" "));
                foreignBw.write(stockNum.get(i));   //寫入股票代號
                trustBw.write(stockNum.get(i));     //寫入股票代號
                dealerBw.write(stockNum.get(i));    //寫入股票代號
                for (int j = 1; j < tmp.size(); j += 11) {  //每檔有11個資訊
                    foreignBw.write("," + tmp.get(j));      //外資買賣超get，write
                    trustBw.write("," + tmp.get(j + 1));      //投信買賣超get，write
                    dealerBw.write("," + tmp.get(j + 2));     //自營商買賣超get，write
                }
                //換行
                foreignBw.newLine();
                trustBw.newLine();
                dealerBw.newLine();

            }
            //寫入日期
            foreignBw.write("date");
            for (int j = 0; j < tmp.size(); j += 11) foreignBw.write("," + tmp.get(j));
            //關閉檔案
            foreignBw.close();
            trustBw.close();
            dealerBw.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //關閉線程池
        aiPaExecutor.shutdown();

        //時間測試結束
        time2 = System.currentTimeMillis();
        System.out.println("抓取三大法人資訊花了：" + (time2-time1)/1000 + "秒");
    }
}
