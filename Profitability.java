import com.github.onblog.AiPa;
import com.github.onblog.executor.AiPaExecutor;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Profitability {
    public Profitability(){}
    public void getInfo() throws ExecutionException, InterruptedException, IOException{
        List<String> stockNum = new ArrayList<String>();
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


        long time1, time2;      //時間測試
        time1 = System.currentTimeMillis();

        File ProfitabilityCsv = new File("C:/Users/user/Desktop/csv_file/Profitability.csv");  //自營商CSV檔案
        BufferedWriter bw = new BufferedWriter(new FileWriter(ProfitabilityCsv, false));

        //來源:https://github.com/onblog/AiPa
        List<String> linkList = new ArrayList<>();
        //放入URL
        for (int i = 0; i < stockNum.size(); i++) linkList.add("https://ww2.money-link.com.tw/TWStock/StockBasic.aspx?TWMId=Basic_IRA02&SymId=" + stockNum.get(i));

        AiPaExecutor aiPaExecutor = AiPa.newInstance(new MyAiPaWorker()).setThreads(10).setCharset(Charset.forName("UTF-8")); //15線程
        //提交任务
        for (int i = 0; i < linkList.size(); i++) aiPaExecutor.submit(linkList);

        //讀取返回值
        List<Future> futureList = aiPaExecutor.getFutureList();//取回來的資料

        String number = "0", grossMargin1 = "0", grossMargin2 = "0", operatingProfit1 = "0", operatingProfit2 = "0", netProfitMargin1 = "0", netProfitMargin2 = "0", ROE = "0", EPS1 = "0", EPS2 = "0", EPS3 = "0", EPS4 = "0";
        List<String> tmp = new ArrayList<String>();
        for (int i = 0; i < stockNum.size(); i++){
            String urlData=futureList.get(i).get().toString();
            number = urlData.substring(0,urlData.indexOf("公開資料"));
            number = number.substring(number.length()-4);
            if(urlData.indexOf("毛利率")!=-1) {
                tmp = Arrays.asList(urlData.substring(urlData.indexOf("毛利率") + 4).replaceAll(",", "").split(" "));
                grossMargin1 = (tmp.get(0).equals("-"))? "0":tmp.get(0);
                grossMargin2 = (tmp.get(1).equals("-"))? "0":tmp.get(1);
            }
            if(urlData.indexOf("營業利益率")!=-1) {
                tmp = Arrays.asList(urlData.substring(urlData.indexOf("營業利益率") + 6).replaceAll(",", "").split(" "));
                operatingProfit1 = (tmp.get(0).equals("-"))? "0":tmp.get(0);
                operatingProfit2 = (tmp.get(1).equals("-"))? "0":tmp.get(1);
            }
            if(urlData.indexOf("稅前淨利率")!=-1) {
                tmp = Arrays.asList(urlData.substring(urlData.indexOf("稅前淨利率") + 6).replaceAll(",", "").split(" "));
                netProfitMargin1 = (tmp.get(0).equals("-"))? "0":tmp.get(0);
                netProfitMargin2 = (tmp.get(1).equals("-"))? "0":tmp.get(1);
            }
            if(urlData.indexOf("股東權益報酬率(稅後)")!=-1) {
                tmp = Arrays.asList(urlData.substring(urlData.indexOf("股東權益報酬率(稅後)") + 12).replaceAll(",", "").split(" "));
                ROE = (tmp.get(0).equals("-"))? "0":tmp.get(0);
            }
            if(urlData.indexOf("基本每股盈餘(元)")!=-1) {
                tmp = Arrays.asList(urlData.substring(urlData.indexOf("基本每股盈餘(元)") + 10).replaceAll(",", "").split(" "));
                EPS1 = (tmp.get(0).equals("-"))? "0":tmp.get(0);
                EPS2 = (tmp.get(1).equals("-"))? "0":tmp.get(1);
                EPS3 = (tmp.get(2).equals("-"))? "0":tmp.get(2);
                EPS4 = (tmp.get(3).equals("-"))? "0":tmp.get(3);
            }
            bw.write(number + "," + grossMargin1 + "," + grossMargin2 + "," + operatingProfit1 + "," + operatingProfit2 + "," + netProfitMargin1 + "," + netProfitMargin2 + "," + ROE + "," + EPS1 + "," + EPS2 + "," + EPS3 + "," + EPS4);
            bw.newLine();
        }
        bw.close();
        //關閉線程池
        aiPaExecutor.shutdown();

        time2 = System.currentTimeMillis();  //時間測試
        System.out.println("獲利能力花了：" + (time2-time1)/1000 + "秒");
    }
}
