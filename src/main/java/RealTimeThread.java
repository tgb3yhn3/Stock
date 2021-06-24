import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.*;

public class RealTimeThread extends Thread {

    private List<OrderData> stockList;  // key為股價 value為price
    private Map<String, RealTimeInfo> realTimeList;
    private JTable pzTable;
    private boolean noticeOrTrans;  //true為委託單 false為到價通知

    public RealTimeThread(JTable table, boolean noticeOrTrans) {
        this.pzTable = table;
        this.noticeOrTrans = noticeOrTrans;
    }
    @Override
    public void run() {
        RealTimeInfo real;
        while (true) {
            stockList = new ArrayList<OrderData>();                 //存入table之每列資訊
            realTimeList = new HashMap<String, RealTimeInfo>();     //存入即時資訊
            for(int count = 0; count < pzTable.getRowCount(); count++) {
                stockList.add(new OrderData(pzTable.getValueAt(count, 0).toString(), pzTable.getValueAt(count, 1).toString(), pzTable.getValueAt(count, 2).toString()));
                realTimeList.put(pzTable.getValueAt(count, 0).toString(), new RealTimeInfo(pzTable.getValueAt(count, 0).toString()));
            }
            //檢查table每列資訊
            for(int row = stockList.size()-1; row >=0 ; row--){
                String stockNum = stockList.get(row).getStockNum();                 //股票代號
                double price = Double.parseDouble(stockList.get(row).getPrice());   //委託或通知價格
                String choose = stockList.get(row).getChoose();                     //買或賣 高於或低於
                int number = 0;                                                     //委託單張數
                realTimeList.get(stockNum).getInfo();                               //取得該股票之最新報價
                //到價通知股價高於設定股價
                try {
                    if (price <= realTimeList.get(stockNum).getBuyTopPrice() && !noticeOrTrans && choose.equals(">")) {
                        //通知
                        JOptionPane.showMessageDialog(null, String.format("%s%s高於%.2f", stockNum, realTimeList.get(stockNum).getStockName(), price));
                        //將table中該列移除
                        ((DefaultTableModel) pzTable.getModel()).removeRow(row);
                    }
                    //到價通知股價低於設定股價
                    else if (price >= realTimeList.get(stockNum).getBuyTopPrice() && !noticeOrTrans && choose.equals("<")) {
                        //通知
                        JOptionPane.showMessageDialog(null, String.format("%s%s低於%.2f", stockNum, realTimeList.get(stockNum).getStockName(), price));
                        //將table中該列移除
                        ((DefaultTableModel) pzTable.getModel()).removeRow(row);
                    }
                    //買進低於內盤價格，成交
                    else if (price >= realTimeList.get(stockNum).getBuyTopPrice() && noticeOrTrans && choose.equals("買進")) {    //低於掛買價格，成交
                        //通知
                        JOptionPane.showMessageDialog(null, String.format("買進%s%s成交%s", stockNum, realTimeList.get(stockNum).getStockName(), price));
                        //取得table該列之張數
                        number = Integer.parseInt(pzTable.getValueAt(row, 3).toString());
                        //將table中該列移除
                        ((DefaultTableModel) pzTable.getModel()).removeRow(row);
                        try {
                            //寫入對帳單之csv
                            File statementCsv = new File("csvFile/statement.csv");   //對帳單CSV檔案
                            BufferedWriter bw = new BufferedWriter(new FileWriter(statementCsv, true));     //接下去寫入
                            bw.write(stockNum + ",buy," + (long) (price * 0.001425 * number * 1000) + ",0," + price + "," + number + "," + -(long) (price * 1.001425 * number * 1000));
                            bw.newLine();
                            bw.close();

                            //讀取庫存之csv
                            File inventoryCsv = new File("csvFile/inventory.csv");   //庫存CSV檔案
                            BufferedReader br = new BufferedReader(new FileReader(inventoryCsv));
                            Map<String, List<String>> inventory = new HashMap<String, List<String>>(); //庫存讀出暫存
                            String line = "";
                            while ((line = br.readLine()) != null) //讀取到的內容給line變數
                                inventory.put(line.substring(0, 4), Arrays.asList(line.substring(5).split(","))); //庫存讀出暫存
                            //關閉檔案
                            br.close();

                            //寫入庫存之csv
                            inventoryCsv = new File("csvFile/inventory.csv");    //庫存CSV檔案
                            bw = new BufferedWriter(new FileWriter(inventoryCsv, false));   //全部清除，再寫入
                            int existNum = 0;
                            double cost = 0;
                            if (inventory.containsKey(stockNum)) {
                                existNum = Integer.parseInt(inventory.get(stockNum).get(1));
                                cost = (existNum * Double.parseDouble(inventory.get(stockNum).get(0)) + number * price) / (existNum + number);
                                existNum += number;
                                if (existNum == 0) inventory.remove(stockNum);
                                else
                                    inventory.put(stockNum, Arrays.asList(String.valueOf(cost), String.valueOf(existNum)));
                            } else {
                                inventory.put(stockNum, Arrays.asList(String.valueOf(price), String.valueOf(number)));
                            }
                            for (String num : inventory.keySet()) {
                                bw.write(num + "," + inventory.get(num).get(0) + "," + inventory.get(num).get(1));
                                bw.newLine();
                            }
                            bw.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //賣出高於掛賣價格，成交
                    else if (price <= realTimeList.get(stockNum).getSellTopPrice() && noticeOrTrans && choose.equals("賣出")) {
                        JOptionPane.showMessageDialog(null, String.format("賣出%s%s成交%s", stockNum, realTimeList.get(stockNum).getStockName(), price));
                        number = Integer.parseInt(pzTable.getValueAt(row, 3).toString());
                        ((DefaultTableModel) pzTable.getModel()).removeRow(row);
                        try {
                            File statementCsv = new File("csvFile/statement.csv");//外資CSV檔案
                            BufferedWriter bw = new BufferedWriter(new FileWriter(statementCsv, true));
                            bw.write(stockNum + ",sell,0," + (long) (price * 0.003 * number * 1000) + "," + price + "," + number + "," + (long) (price * 0.997 * number * 1000));
                            bw.newLine();
                            bw.close();

                            //inventory.csv
                            File inventoryCsv = new File("csvFile/inventory.csv");//庫存CSV檔案
                            BufferedReader br = new BufferedReader(new FileReader(inventoryCsv));
                            Map<String, List<String>> inventory = new HashMap<String, List<String>>();
                            String line = "";
                            while ((line = br.readLine()) != null) //讀取到的內容給line變數
                                inventory.put(line.substring(0, 4), Arrays.asList(line.substring(5).split(",")));
                            br.close();

                            inventoryCsv = new File("csvFile/inventory.csv");//庫存CSV檔案
                            bw = new BufferedWriter(new FileWriter(inventoryCsv, false));
                            int existNum = 0;
                            double cost = 0;
                            if (inventory.containsKey(stockNum)) {
                                existNum = Integer.parseInt(inventory.get(stockNum).get(1)) - number;
                                if (existNum == 0) inventory.remove(stockNum);
                                else
                                    inventory.put(stockNum, Arrays.asList(inventory.get(stockNum).get(0), String.valueOf(existNum)));
                            } else {
                                inventory.put(stockNum, Arrays.asList(String.valueOf(price), String.valueOf(-number)));
                            }
                            for (String num : inventory.keySet()) {
                                bw.write(num + "," + inventory.get(num).get(0) + "," + inventory.get(num).get(1));
                                bw.newLine();
                            }
                            bw.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(null, "取得報價錯誤，請稍後重試");
                    ((DefaultTableModel) pzTable.getModel()).removeRow(row);
                    break;
                }
            }
            try {
                sleep(5000); //暫停，每一秒輸出一次
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
