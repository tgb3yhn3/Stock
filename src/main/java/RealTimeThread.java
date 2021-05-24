import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class RealTimeThread extends Thread {

    private Map<String, String> stockList;  // key為股價 value為price
    private List<RealTimeInfo> realTimeList;
    private JTable pzTable;
    private boolean noticeOrTrans;

    public RealTimeThread(JTable table, boolean noticeOrTrans) {
        this.pzTable = table;
        this.noticeOrTrans = noticeOrTrans;
    }
    @Override
    public void run() {
        RealTimeInfo tmp;
        while (true) {
            stockList = new HashMap<String, String>();
            realTimeList = new ArrayList<RealTimeInfo>();
            for(int count = 0; count < pzTable.getRowCount(); count++) {
                stockList.put(pzTable.getValueAt(count, 0).toString(), pzTable.getValueAt(count, 1).toString());
                realTimeList.add(new RealTimeInfo(pzTable.getValueAt(count, 0).toString()));
            }
            for(int i = 0; i < realTimeList.size(); i++){
                tmp = realTimeList.get(i);
                tmp.getInfo();
                String stockNum = tmp.getStockNum();
                if(Double.parseDouble(tmp.getPrice())==Double.parseDouble(stockList.get(stockNum))){
                    if(!noticeOrTrans)
                        JOptionPane.showMessageDialog(null, String.format("%s%s已到價格%s", stockNum, tmp.getStockName(), tmp.getPrice()));
                    else
                        JOptionPane.showMessageDialog(null, String.format("%s%s已成交%s", stockNum, tmp.getStockName(), tmp.getPrice()));
                    for(int row = 0; row < pzTable.getRowCount(); row++)
                        if (stockNum.equals(pzTable.getValueAt(row, 0).toString())) ((DefaultTableModel) pzTable.getModel()).removeRow(row);
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
