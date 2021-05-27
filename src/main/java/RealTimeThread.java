import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.*;

public class RealTimeThread extends Thread {

    private List<OrderData> stockList;  // key為股價 value為price
    private Map<String, RealTimeInfo> realTimeList;
    private JTable pzTable;
    private boolean noticeOrTrans;

    public RealTimeThread(JTable table, boolean noticeOrTrans) {
        this.pzTable = table;
        this.noticeOrTrans = noticeOrTrans;
    }
    @Override
    public void run() {
        RealTimeInfo real;
        while (true) {
            stockList = new ArrayList<OrderData>();
            realTimeList = new HashMap<String, RealTimeInfo>();
            for(int count = 0; count < pzTable.getRowCount(); count++) {
                stockList.add(new OrderData(pzTable.getValueAt(count, 0).toString(), pzTable.getValueAt(count, 1).toString(), pzTable.getValueAt(count, 2).toString()));
                realTimeList.put(pzTable.getValueAt(count, 0).toString(), new RealTimeInfo(pzTable.getValueAt(count, 0).toString()));
            }
            for(int row = stockList.size()-1; row >=0 ; row--){
                String stockNum = stockList.get(row).getStockNum();
                double price = Double.parseDouble(stockList.get(row).getPrice());
                String choose = stockList.get(row).getChoose();
                int number = 0;
                realTimeList.get(stockNum).getInfo();   //取得最新報價
                //到價通知股價高於設定股價
                if(price <= realTimeList.get(stockNum).getBuyTopPrice() && !noticeOrTrans && choose.equals(">")) {
                    JOptionPane.showMessageDialog(null, String.format("%s%s高於%.2f", stockNum, realTimeList.get(stockNum).getStockName(), price));
                    ((DefaultTableModel) pzTable.getModel()).removeRow(row);
                }
                //到價通知股價低於設定股價
                else if(price >= realTimeList.get(stockNum).getBuyTopPrice() && !noticeOrTrans && choose.equals("<")) {
                    JOptionPane.showMessageDialog(null, String.format("%s%s低於%.2f", stockNum, realTimeList.get(stockNum).getStockName(), price));
                    ((DefaultTableModel) pzTable.getModel()).removeRow(row);
                }
                //低於內盤價格，成交
                else if(price >= realTimeList.get(stockNum).getBuyTopPrice() && noticeOrTrans && choose.equals("買進")) {    //低於掛買價格，成交
                    JOptionPane.showMessageDialog(null, String.format("買進%s%s成交%s", stockNum, realTimeList.get(stockNum).getStockName(), price));
                    number = Integer.parseInt(pzTable.getValueAt(row, 3).toString());
                    ((DefaultTableModel) pzTable.getModel()).removeRow(row);
                    try {
                        File statementCsv = new File("C:/Users/user/Desktop/csv_file/statement.csv");//外資CSV檔案
                        BufferedWriter bw = new BufferedWriter(new FileWriter(statementCsv, true));
                        bw.write(stockNum + ",buy," + (long)(price * 0.001425 * number * 1000) + ",0," + price + "," + number + "," + -(long)(price * 1.001425 * number * 1000));
                        bw.newLine();
                        bw.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(price <= realTimeList.get(stockNum).getSellTopPrice() && noticeOrTrans && choose.equals("賣出")) {    //高於掛賣價格，成交
                    JOptionPane.showMessageDialog(null, String.format("賣出%s%s成交%s", stockNum, realTimeList.get(stockNum).getStockName(), price));
                    number = Integer.parseInt(pzTable.getValueAt(row, 3).toString());
                    ((DefaultTableModel) pzTable.getModel()).removeRow(row);
                    try {
                        File statementCsv = new File("C:/Users/user/Desktop/csv_file/statement.csv");//外資CSV檔案
                        BufferedWriter bw = new BufferedWriter(new FileWriter(statementCsv, true));
                        bw.write(stockNum + ",sell,0," + (long)(price * 0.003 * number * 1000) + "," + price + "," + number + "," + (long)(price * 0.997 * number * 1000));
                        bw.newLine();
                        bw.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
