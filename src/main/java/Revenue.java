import com.github.onblog.AiPa;
import com.github.onblog.executor.AiPaExecutor;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Revenue {
    private List<String> stockNum;
    public Revenue(){
        //讀取股票代號
        stockNum = new ArrayList<String>();
        try{
            File stockNumCsv = new File("csvFile/stockNum.csv");  // CSV檔案路徑
            BufferedReader stockNumBr = new BufferedReader(new FileReader(stockNumCsv));
            String line = "";
            while ((line = stockNumBr.readLine()) != null) //讀取到的內容給line變數
                stockNum.add(line.substring(0,4));
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
        long time1, time2;      //時間測試
        time1 = System.currentTimeMillis();

        //來源:https://github.com/onblog/AiPa
        List<String> linkList = new ArrayList<>();
        //放入URL
        for (int i = 0; i < stockNum.size(); i++) linkList.add("https://concords.moneydj.com/z/zc/zch/zch_" + stockNum.get(i) + ".djhtm");

        AiPaExecutor aiPaExecutor = AiPa.newInstance(new MyAiPaWorker()).setThreads(10).setCharset(Charset.forName("big5")).setMaxFailCount(10); //15線程
        //提交任务
        for (int i = 0; i < linkList.size(); i++) aiPaExecutor.submit(linkList);

        //讀取返回值
        List<Future> futureList = aiPaExecutor.getFutureList();//取回來的資料
        String urlData = "";

        try {
            File revenueCsv = new File("csvFile/revenueNew.csv");//外資CSV檔案
            BufferedWriter bw = new BufferedWriter(new FileWriter(revenueCsv, false));
            List<String> tmp = new ArrayList<String>(); //暫存取到網頁資訊之list
            for (int i = 0; i < stockNum.size(); i++) {
                try {
                    urlData = futureList.get(i).get().toString();
                } catch (Exception e) {     //任何抓取資料之錯誤
                    continue;   //忽略此筆
                }

                tmp = Arrays.asList(urlData.substring(urlData.indexOf("累計營收") + 9, urlData.indexOf("說明")).replaceAll(",", "").replaceAll("%", "").split(" "));
                bw.write(stockNum.get(i));  //寫入股票代號
                bw.write("," + tmp.get(2) + "," + tmp.get(4));  //寫入營收月增 年增
                for (int j = 1; j < 7 * 24 && j < tmp.size(); j += 7) {     //寫入24個月營收
                    bw.write("," + tmp.get(j));
                }
                //換行
                bw.newLine();
            }
            //寫入日期
            bw.write("date,0,0");
            for (int j = 0; j < 7 * 24 && j < tmp.size(); j += 7) bw.write("," + tmp.get(j));  //寫入24個月月份
            bw.close();
            //關閉線程池
            aiPaExecutor.shutdown();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //時間測試結束
        time2 = System.currentTimeMillis();
        System.out.println("抓取營收資訊花了：" + (time2-time1)/1000 + "秒");
    }
}
