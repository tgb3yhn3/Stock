import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class StocksGUI_Fundamentals extends JFrame{

    private Map<String, List<String>> revenue;
    private Map<String, List<String>> profitability;
    public StocksGUI_Fundamentals(StocksGUI_SearchForListedStocks fatherFrame, String stockNum, double price) {
        //創建到價通知頁面視窗
        super("韭菜同學會_基本面");
        this.setIconImage(new ImageIcon("imageFile\\韭菜.png").getImage());
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int windowWidth = 400;//設定視窗寬度
        int windowHeight = 560;//設定視窗高度
        setSize(windowWidth, windowHeight);
        setLocation(fatherFrame.getX(), fatherFrame.getY());//此視窗出現的位置將在主頁面的位置
        this.revenue = fatherFrame.getRevenue();
        this.profitability = fatherFrame.getProfitability();

        //創建視窗內的各個GUI子元件
        JLabel grossMarginLabel             = new JLabel("毛利率: " + profitability.get(stockNum).get(0) + "%");
        JLabel operatingProfitMarginLabel   = new JLabel("營益率: " + profitability.get(stockNum).get(2) + "%");
        JLabel netProfitMarginLabel         = new JLabel("稅益率: " + profitability.get(stockNum).get(4) + "%");
        JLabel ROELabel                     = new JLabel("ROE: " + profitability.get(stockNum).get(6));
        JLabel EPSLabel                     = new JLabel("EPS: " + profitability.get(stockNum).get(7));
        JLabel PELabel                      = new JLabel("本益比: " +  String.format("%.2f",price/(Double.parseDouble(profitability.get(stockNum).get(7)) + Double.parseDouble(profitability.get(stockNum).get(8)) + Double.parseDouble(profitability.get(stockNum).get(9)) + Double.parseDouble(profitability.get(stockNum).get(10)))));

        //header
        String [] revenueTableHeadings = new String[] {"日期","營收(億元)","月增", "年增"};
        DefaultTableModel tableModel = new DefaultTableModel(revenueTableHeadings, 0){ //0是初始列數，代表一開始沒有任何一筆資料
            public boolean isCellEditable(int row, int column) { return false; } //設定JTable不可更改
        };
        JTable revenueTable = new JTable(tableModel);
        for(int i = 2; i < 14; i++){
            try {
                tableModel.addRow(new Object[]{revenue.get("date").get(i), String.format("%.2f", Long.parseLong(revenue.get(stockNum).get(i)) / 100000.0), String.format("%.2f", ((Double.parseDouble(revenue.get(stockNum).get(i)) - Double.parseDouble(revenue.get(stockNum).get(i + 1))) * 100 / Double.parseDouble(revenue.get(stockNum).get(i + 1)))) + "%", String.format("%.2f", ((Double.parseDouble(revenue.get(stockNum).get(i)) - Double.parseDouble(revenue.get(stockNum).get(i + 12))) * 100 / Double.parseDouble(revenue.get(stockNum).get(i + 12)))) + "%"});
            }
            catch(Exception e){ //營收格式有錯
                tableModel.addRow(new Object[]{revenue.get("date").get(i), 0, "0%", "0%"});
            }
        }

        int threeMajorCorporationsTable_Width = windowWidth-100;//table寬度
        int threeMajorCorporationsTable_RowHeight = 30;//table列高
        revenueTable.setPreferredScrollableViewportSize(new Dimension(threeMajorCorporationsTable_Width, threeMajorCorporationsTable_RowHeight*12));//設定table高度和寬度
        revenueTable.setRowHeight(threeMajorCorporationsTable_RowHeight);//設定table列高
        revenueTable.getTableHeader().setResizingAllowed(false);//table的行寬為固定
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();//renderer用來使table裡面的文字靠中
        renderer.setHorizontalAlignment(JTextField.CENTER);//renderer用來使table裡面的文字靠中
        revenueTable.getColumnModel().getColumn(0).setCellRenderer(renderer);//讓第0行的內容文字全部靠中
        revenueTable.getColumnModel().getColumn(1).setCellRenderer(renderer);//讓第1行的內容文字全部靠中
        revenueTable.getColumnModel().getColumn(2).setCellRenderer(renderer);//讓第2行的內容文字全部靠中
        revenueTable.getColumnModel().getColumn(3).setCellRenderer(renderer);//讓第2行的內容文字全部靠中

        JPanel topPanel = new JPanel(new GridLayout(2,3));
        topPanel.setPreferredSize(new Dimension(300, 60));
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("上季獲利:"));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("營收:"));

        topPanel.add(grossMarginLabel);
        topPanel.add(operatingProfitMarginLabel);
        topPanel.add(netProfitMarginLabel);
        topPanel.add(EPSLabel);
        topPanel.add(ROELabel);
        topPanel.add(PELabel);

        buttonPanel.add(new JScrollPane(revenueTable));
        add(topPanel);
        add(buttonPanel);

        setVisible(true);
    }
}