import com.github.onblog.AiPa;
import com.github.onblog.executor.AiPaExecutor;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.Calendar;
import java.util.Date;

public class Investors {

    List<String> stockNum;

    public Investors(){
        stockNum = new ArrayList<String>();
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
                stockNum.add(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getInfo()throws ExecutionException, InterruptedException, IOException {
        //---------------------取得日期-------------------
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) +1 ;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int last_month = (month==1)? 12:month-1;
        String thisMonth = String.format("%d-%d-%d", year, month, day);
        String lastMonth = String.format("%d-%d-%d", year, last_month, day);
        //-------------------------------------------------

        long time1, time2;      //時間測試
        time1 = System.currentTimeMillis();

        //來源:https://github.com/onblog/AiPa
        List<String> linkList = new ArrayList<>();
        //放入URL
        for (int i = 0; i < stockNum.size(); i++) linkList.add("https://concords.moneydj.com/z/zc/zcl/zcl.djhtm?a=" + stockNum.get(i) + "&c=" + lastMonth + "&d=" + thisMonth);

        AiPaExecutor aiPaExecutor = AiPa.newInstance(new MyAiPaWorker()).setThreads(10).setCharset(Charset.forName("big5")); //15線程
        //提交任务
        for (int i = 0; i < linkList.size(); i++) aiPaExecutor.submit(linkList);

        //讀取返回值
        List<Future> futureList = aiPaExecutor.getFutureList();//取回來的資料

        File foreignCsv = new File("C:/Users/user/Desktop/csv_file/foreign.csv");//外資CSV檔案
        File trustCsv = new File("C:/Users/user/Desktop/csv_file/trust.csv");    //投信CSV檔案
        File dealerCsv = new File("C:/Users/user/Desktop/csv_file/dealer.csv");  //自營商CSV檔案
        BufferedWriter foreignBw = new BufferedWriter(new FileWriter(foreignCsv, false));
        BufferedWriter trustBw = new BufferedWriter(new FileWriter(trustCsv, false));
        BufferedWriter dealerBw = new BufferedWriter(new FileWriter(dealerCsv, false));
        List<String> tmp = new ArrayList<String>();
        for (int i = 0; i < stockNum.size(); i++){
            //get(); //方法会阻塞当前线程直到获取返回值
            String urlData=futureList.get(i).get().toString();
            tmp = Arrays.asList(urlData.substring(urlData.indexOf("三大法人")+ 5, urlData.indexOf("合計買賣超")).replaceAll(",","").replaceAll(" %","").split(" "));
            foreignBw.write(stockNum.get(i));
            trustBw.write(stockNum.get(i));
            dealerBw.write(stockNum.get(i));
            for(int j = 1; j < tmp.size(); j+=11){
                foreignBw.write("," + tmp.get(j));
                trustBw.write("," + tmp.get(j+1));
                dealerBw.write("," + tmp.get(j+2));
            }
            foreignBw.newLine();
            trustBw.newLine();
            dealerBw.newLine();
        }
        foreignBw.write("date");
        for(int j = 0; j < tmp.size(); j+=11) foreignBw.write("," + tmp.get(j));
        foreignBw.close();
        trustBw.close();
        dealerBw.close();
        //關閉線程池
        aiPaExecutor.shutdown();

        time2 = System.currentTimeMillis();  //時間測試
        System.out.println("三大法人花了：" + (time2-time1)/1000 + "秒");
    }
}
