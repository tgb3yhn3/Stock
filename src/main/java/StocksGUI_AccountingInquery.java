import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StocksGUI_AccountingInquery extends JFrame{

    public StocksGUI_AccountingInquery(StocksGUI mainFrame) {
        //創建到價通知頁面視窗
        super("韭菜同學會_帳務查詢");
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocation(mainFrame.getX(), mainFrame.getY());//此視窗出現的位置將在主頁面的位置
        int tableDisplayRow  =  10;//庫存table與對帳單table最多顯示幾筆資料，超過則table出現滾動條(scrollbar)(會影響到視窗高度)
        int tableRowHeight   =  20;//設定庫存table與對帳單table列高(會影響到視窗高度)
        int windowWidth = 850;//設定視窗寬度
        int windowHeight = tableDisplayRow * tableRowHeight + 100;//設定視窗高度
        setSize(windowWidth, windowHeight);

        //創建視窗內的各個GUI子元件
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setPreferredSize(new Dimension(100,windowHeight*5/10));
        JButton inventoryButton = new XrButton("庫存");//庫存按鈕
        JButton statementButton = new XrButton("對帳單");//對帳單按鈕

        JTextField empty = new JTextField("");//empty用來隔開庫存按鈕與對帳單按鈕
        empty.setFont(new Font("微軟正黑體" ,Font.BOLD,25));
        empty.setBorder(null);
        empty.setEditable(false);

        JPanel tablePanel = new JPanel();//tablePanel放置庫存table與對帳單table
        tablePanel.setBorder(BorderFactory.createTitledBorder("您的庫存"));

        //為buttonPanel新增GUI子元件,並設定網格約束(g)
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.BOTH;
        g.gridx = 0;
        g.gridy = 0;
        buttonPanel.add(inventoryButton,g);
        g.gridx = 0;
        g.gridy = 1;
        buttonPanel.add(empty,g);
        g.gridx = 0;
        g.gridy = 2;
        buttonPanel.add(statementButton,g);

        //創建庫存的table
        String[] inventoryTableHeadings = new String[]{"股票代號", "平均成本", "張數"};
        DefaultTableModel inventoryTableModel = new DefaultTableModel(inventoryTableHeadings, 0);//0是初始列數，代表一開始沒有任何一筆資料
        JTable inventoryTable = new JTable(inventoryTableModel);
        int inventoryTable_Width = 300;//庫存table寬度
        inventoryTable.setPreferredScrollableViewportSize(new Dimension(inventoryTable_Width, tableRowHeight * tableDisplayRow));//設定table高度和寬度
        inventoryTable.setRowHeight(tableRowHeight);//設定庫存table列高
        inventoryTable.getTableHeader().setResizingAllowed(false);//庫存table的行寬為固定
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();//renderer用來使庫存table裡面的文字靠中
        renderer.setHorizontalAlignment(JTextField.CENTER);//renderer用來使table裡面的文字靠中
        inventoryTable.getColumnModel().getColumn(0).setCellRenderer(renderer);//讓第0行的內容文字全部靠中
        inventoryTable.getColumnModel().getColumn(1).setCellRenderer(renderer);//讓第1行的內容文字全部靠中
        inventoryTable.getColumnModel().getColumn(2).setCellRenderer(renderer);//讓第2行的內容文字全部靠中
        inventoryTable.getColumnModel().getColumn(0).setPreferredWidth(inventoryTable_Width * 3 / 10);//設定每一行行寬
        inventoryTable.getColumnModel().getColumn(1).setPreferredWidth(inventoryTable_Width * 3 / 10);//設定每一行行寬
        inventoryTable.getColumnModel().getColumn(2).setPreferredWidth(inventoryTable_Width * 3 / 10);//設定每一行行寬
        //---------------------------------------讀取庫存csv----------------------------------
        try {
            File inventoryCsv = new File("csvfile\\inventory.csv");
            BufferedReader br = new BufferedReader(new FileReader(inventoryCsv));
            String line = "";
            List<String> tmp;
            while ((line = br.readLine()) != null) { //讀取到的內容給line變數
                tmp = Arrays.asList(line.split(","));   //切割回傳給list
                inventoryTableModel.addRow(new Object[]{tmp.get(0), tmp.get(1), tmp.get(2)});   //放入table
            }
            //關閉檔案
            br.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        //在tablePanel中放入庫存的table(即一開始顯示庫存table)
        tablePanel.add(new JScrollPane(inventoryTable));
        inventoryButton.setSelected(true);
        statementButton.setSelected(false);

        //為視窗新增GUI子元件
        add(buttonPanel);
        add(tablePanel);
        //為庫存按鈕註冊事件
        inventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                inventoryButton.setSelected(true);
                statementButton.setSelected(false);
                tablePanel.removeAll();
                tablePanel.setBorder(BorderFactory.createTitledBorder("您的庫存"));
                //創建庫存的table
                String[] inventoryTableHeadings = new String[]{"股票代號", "平均成本", "張數"};
                DefaultTableModel inventoryTableModel = new DefaultTableModel(inventoryTableHeadings, 0);//0是初始列數，代表一開始沒有任何一筆資料
                JTable inventoryTable = new JTable(inventoryTableModel);
                int inventoryTable_Width = 300;//庫存table寬度
                inventoryTable.setPreferredScrollableViewportSize(new Dimension(inventoryTable_Width, tableRowHeight * tableDisplayRow));//設定table高度和寬度
                inventoryTable.setRowHeight(tableRowHeight);//設定庫存table列高
                inventoryTable.getTableHeader().setResizingAllowed(false);//庫存table的行寬為固定
                DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();//renderer用來使庫存table裡面的文字靠中
                renderer.setHorizontalAlignment(JTextField.CENTER);//renderer用來使table裡面的文字靠中
                inventoryTable.getColumnModel().getColumn(0).setCellRenderer(renderer);//讓第0行的內容文字全部靠中
                inventoryTable.getColumnModel().getColumn(1).setCellRenderer(renderer);//讓第1行的內容文字全部靠中
                inventoryTable.getColumnModel().getColumn(2).setCellRenderer(renderer);//讓第2行的內容文字全部靠中
                inventoryTable.getColumnModel().getColumn(0).setPreferredWidth(inventoryTable_Width * 1 / 3);//設定每一行行寬
                inventoryTable.getColumnModel().getColumn(1).setPreferredWidth(inventoryTable_Width * 1 / 3);//設定每一行行寬
                inventoryTable.getColumnModel().getColumn(2).setPreferredWidth(inventoryTable_Width * 1 / 3);//設定每一行行寬

                //讀取庫存csv
                try {
                    File inventoryCsv = new File("csvFile/inventory.csv");
                    BufferedReader br = new BufferedReader(new FileReader(inventoryCsv));
                    String line = "";
                    List<String> tmp;
                    while ((line = br.readLine()) != null) { //讀取到的內容給line變數
                        tmp = Arrays.asList(line.split(","));   //切割回傳給list
                        inventoryTableModel.addRow(new Object[]{tmp.get(0), tmp.get(1), tmp.get(2)});   //放入table
                    }
                    //關閉檔案
                    br.close();
                }
                catch(FileNotFoundException e){
                    e.printStackTrace();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
                //加入panel
                tablePanel.add(new JScrollPane(inventoryTable));
                tablePanel.revalidate();
            }
        });

        //為對帳單按鈕註冊事件
        statementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                inventoryButton.setSelected(false);
                statementButton.setSelected(true);
                tablePanel.removeAll();
                tablePanel.setBorder(BorderFactory.createTitledBorder("您的對帳單"));
                //創建對帳單的table
                String[] statementTableHeadings = new String[]{"股票代號","狀態","手續費","交易稅","價格","張數","應收付淨額"};
                DefaultTableModel statementTableModel = new DefaultTableModel(statementTableHeadings, 0);//0是初始列數，代表一開始沒有任何一筆資料
                JTable statementTable = new JTable(statementTableModel);
                int statementTable_Width = 700;//對帳單table寬度
                statementTable.setPreferredScrollableViewportSize(new Dimension(statementTable_Width, tableRowHeight * tableDisplayRow));//設定table高度和寬度
                statementTable.setRowHeight(tableRowHeight);//設定對帳單table列高
                statementTable.getTableHeader().setResizingAllowed(false);//對帳單table的行寬為固定
                DefaultTableCellRenderer renderer1 = new DefaultTableCellRenderer();//renderer用來使對帳單table裡面的文字靠中
                renderer1.setHorizontalAlignment(JTextField.CENTER);//renderer用來使對帳單table裡面的文字靠中
                statementTable.getColumnModel().getColumn(0).setCellRenderer(renderer1);//讓第0行的內容文字全部靠中
                statementTable.getColumnModel().getColumn(1).setCellRenderer(renderer1);//讓第1行的內容文字全部靠中
                statementTable.getColumnModel().getColumn(2).setCellRenderer(renderer1);//讓第2行的內容文字全部靠中
                statementTable.getColumnModel().getColumn(3).setCellRenderer(renderer1);//讓第3行的內容文字全部靠中
                statementTable.getColumnModel().getColumn(4).setCellRenderer(renderer1);//讓第4行的內容文字全部靠中
                statementTable.getColumnModel().getColumn(5).setCellRenderer(renderer1);//讓第5行的內容文字全部靠中
                statementTable.getColumnModel().getColumn(6).setCellRenderer(renderer1);//讓第6行的內容文字全部靠中
                statementTable.getColumnModel().getColumn(0).setPreferredWidth(statementTable_Width * 1 / 7);//設定每一行行寬
                statementTable.getColumnModel().getColumn(1).setPreferredWidth(statementTable_Width * 1 / 7);//設定每一行行寬
                statementTable.getColumnModel().getColumn(2).setPreferredWidth(statementTable_Width * 1 / 7);//設定每一行行寬
                statementTable.getColumnModel().getColumn(3).setPreferredWidth(statementTable_Width * 1 / 7);//設定每一行行寬
                statementTable.getColumnModel().getColumn(4).setPreferredWidth(statementTable_Width * 1 / 7);//設定每一行行寬
                statementTable.getColumnModel().getColumn(5).setPreferredWidth(statementTable_Width * 1 / 7);//設定每一行行寬
                statementTable.getColumnModel().getColumn(6).setPreferredWidth(statementTable_Width * 1 / 7);//設定每一行行寬
                try {
                    File statementCsv = new File("csvFile/statement.csv");  // CSV檔案路徑
                    BufferedReader bw = null;
                    List<String> tmp = new ArrayList<String>();
                    bw = new BufferedReader(new FileReader(statementCsv));
                    String line = "";
                    int i = 0;
                    while ((line = bw.readLine()) != null) { //讀取到的內容給line變數
                        tmp = Arrays.asList(line.split(","));
                        statementTableModel.addRow(new Object[]{tmp.get(0), tmp.get(1), tmp.get(2), tmp.get(3), tmp.get(4), tmp.get(5), tmp.get(6)});
                    }
                    //關閉檔案
                    bw.close();
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                //加入panel
                tablePanel.add(new JScrollPane(statementTable));
                tablePanel.revalidate();
            }
        });

        setVisible(true);
    }
}
