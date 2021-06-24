import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ThreeMajorCorporationsFrame extends JFrame{

    private Map<String, List<String>> foreignMap;
    private Map<String, List<String>> trustMap;
    private Map<String, List<String>> dealerMap;
    private List<String> foreign;
    private List<String> trust;
    private List<String> dealer;

    public ThreeMajorCorporationsFrame(SearchForListedStocksFrame fatherFrame, String stockNum){
        //創建到價通知頁面視窗
        super("韭菜同學會_三大法人");
        this.setIconImage(new ImageIcon("imageFile\\韭菜.png").getImage());
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int windowWidth = 450;//設定視窗寬度
        int windowHeight = 800;//設定視窗高度
        setSize(windowWidth, windowHeight);
        setLocation(fatherFrame.getX(),fatherFrame.getY());//此視窗出現的位置將在主頁面的位置

        //initialize
        this.foreignMap = fatherFrame.getForeign();
        this.trustMap = fatherFrame.getTrust();
        this.dealerMap = fatherFrame.getDealer();
        this.foreign = foreignMap.get(stockNum);
        this.trust = trustMap.get(stockNum);
        this.dealer = dealerMap.get(stockNum);

        //創建三大法人的table
        String [] threeMajorCorporationsTableHeadings = new String[] {"日期","外資","投信","自營商"};
        DefaultTableModel tableModel = new DefaultTableModel(threeMajorCorporationsTableHeadings, 0){ //0是初始列數，代表一開始沒有任何一筆資料
            public boolean isCellEditable(int row, int column) { return false; } //設定JTable不可更改
        };
        JTable threeMajorCorporationsTable = new JTable(tableModel);
        try{
            for(int i = 0; i < fatherFrame.getForeign().get(stockNum).size(); i++)
                tableModel.addRow(new Object[]{foreignMap.get("date").get(i), foreignMap.get(stockNum).get(i), trustMap.get(stockNum).get(i), dealerMap.get(stockNum).get(i)});
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(ThreeMajorCorporationsFrame.this, "讀取資料錯誤");
        }

        int threeMajorCorporationsTable_Width = windowWidth-100;//table寬度
        int threeMajorCorporationsTable_RowHeight = 30;//table列高
        threeMajorCorporationsTable.setPreferredScrollableViewportSize(new Dimension(threeMajorCorporationsTable_Width, threeMajorCorporationsTable_RowHeight*foreignMap.get(stockNum).size()));//設定table高度和寬度
        threeMajorCorporationsTable.setRowHeight(threeMajorCorporationsTable_RowHeight);//設定table列高
        threeMajorCorporationsTable.getTableHeader().setResizingAllowed(false);//table的行寬為固定
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();//renderer用來使table裡面的文字靠中
        renderer.setHorizontalAlignment(JTextField.CENTER);//renderer用來使table裡面的文字靠中
        threeMajorCorporationsTable.getColumnModel().getColumn(0).setCellRenderer(renderer);//讓第0行的內容文字全部靠中
        threeMajorCorporationsTable.getColumnModel().getColumn(1).setCellRenderer(renderer);//讓第1行的內容文字全部靠中
        threeMajorCorporationsTable.getColumnModel().getColumn(2).setCellRenderer(renderer);//讓第2行的內容文字全部靠中
        threeMajorCorporationsTable.getColumnModel().getColumn(3).setCellRenderer(renderer);//讓第2行的內容文字全部靠中


        //為視窗新增GUI子元件
        getContentPane().add(new JScrollPane(threeMajorCorporationsTable));
        setVisible(true);
    }
}
