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
    public void getInfo() throws ExecutionException, InterruptedException, IOException, TimeoutException{
        long time1, time2;      //時間測試
        time1 = System.currentTimeMillis();

        //來源:https://github.com/onblog/AiPa
        List<String> linkList = new ArrayList<>();
        //放入URL
        for (int i = 0; i < stockNum.size(); i++) linkList.add("https://concords.moneydj.com/z/zc/zch/zch_" + stockNum.get(i) + ".djhtm");

        AiPaExecutor aiPaExecutor = AiPa.newInstance(new MyAiPaWorker()).setThreads(10).setCharset(Charset.forName("big5")); //15線程
        //提交任务
        for (int i = 0; i < linkList.size(); i++) aiPaExecutor.submit(linkList);

        //讀取返回值
        List<Future> futureList = aiPaExecutor.getFutureList();//取回來的資料

        File revenueCsv = new File("C:/Users/user/Desktop/csv_file/revenueNew.csv");//外資CSV檔案
        BufferedWriter bw = new BufferedWriter(new FileWriter(revenueCsv, false));
        List<String> tmp = new ArrayList<String>();
        for (int i = 0; i < stockNum.size(); i++){
            String urlData=futureList.get(i).get().toString();
            tmp = Arrays.asList(urlData.substring(urlData.indexOf("累計營收")+ 9, urlData.indexOf("說明")).replaceAll(",","").replaceAll("%","").split(" "));
            bw.write(stockNum.get(i));
            bw.write("," + tmp.get(2) + "," + tmp.get(4));
            for(int j = 1; j < 7*24 && j<tmp.size(); j+=7){
                bw.write("," + tmp.get(j));
            }
            bw.newLine();
        }
        bw.write("date,0,0");
        for(int j = 0; j < 7*24 && j<tmp.size(); j+=7) bw.write("," + tmp.get(j));
        bw.close();
        //關閉線程池
        aiPaExecutor.shutdown();

        time2 = System.currentTimeMillis();  //時間測試
        System.out.println("營收花了：" + (time2-time1)/1000 + "秒");
    }
}
