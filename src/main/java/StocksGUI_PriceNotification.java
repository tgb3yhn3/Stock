import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import static java.lang.Integer.parseInt;

public class StocksGUI_PriceNotification extends JFrame{

        private JPanel addInPanel;
        private JLabel addIn_StockNumLabel;
        private JTextField addIn_StockNumTextField;
        private JLabel addIn_PriceLabel;
        private JTextField addIn_PriceTextField;
        private JRadioButton moreThan;
        private JRadioButton lessThan;
        private ButtonGroup Buy_SellRadioGroup;
        private JButton addInButton;
        private JPanel deletePanel;
        private JButton deleteButton;
        private JTable priceTable;
    public StocksGUI_PriceNotification(StocksGUI mainFrame){
        //創建到價通知頁面視窗
        super("韭菜同學會_到價通知");
        this.setIconImage(new ImageIcon("imageFile\\韭菜.png").getImage());
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int windowWidth = 400;//設定視窗寬度
        int windowHeight = 250;//設定視窗高度
        setSize(windowWidth, windowHeight);
        setLocation(mainFrame.getX(),mainFrame.getY());//此視窗出現的位置將在主頁面的位置


        //創建視窗內的各個GUI子元件_加入一筆的部分
        addInPanel              = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addInPanel.setBorder(BorderFactory.createTitledBorder(""));
        addIn_StockNumLabel     = new JLabel("股票代號:");
        addIn_StockNumTextField = new JTextField("",4);
        addIn_PriceLabel        = new JLabel("價格:");
        addIn_PriceTextField    = new JTextField("",4);
        ButtonGroup priceButtonGroup = new ButtonGroup();
        moreThan                = new JRadioButton("高於", false);
        lessThan                = new JRadioButton("低於", true);
        priceButtonGroup.add(moreThan);
        priceButtonGroup.add(lessThan);
        addInButton             = new JButton("加入");

        //創建視窗內的各個GUI子元件_刪除一筆的部分
        deletePanel  = new JPanel(new BorderLayout());
        deletePanel.setPreferredSize(new Dimension(windowWidth-92,25));
        deleteButton = new JButton("刪除");

        //創建到價通知的table
        String [] priceTableHeadings = new String[] {"股票代號","價格","高或低於"};
        DefaultTableModel tableModel = new DefaultTableModel(priceTableHeadings, 0);//0是初始列數，代表一開始沒有任何一筆資料
        priceTable = new JTable(tableModel);
        int PriceTable_Width = windowWidth-100;//table寬度
        int PriceTable_RowHeight = 20;//table列高
        priceTable.setPreferredScrollableViewportSize(new Dimension(PriceTable_Width, PriceTable_RowHeight*5));//設定table高度和寬度
        priceTable.setRowHeight(PriceTable_RowHeight);//設定table列高
        priceTable.getTableHeader().setResizingAllowed(false);//table的行寬為固定
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();//renderer用來使table裡面的文字靠中
        renderer.setHorizontalAlignment(JTextField.CENTER);//renderer用來使table裡面的文字靠中
        priceTable.getColumnModel().getColumn(1).setCellRenderer(renderer);//讓第1行的內容文字全部靠中
        priceTable.getColumnModel().getColumn(2).setCellRenderer(renderer);//讓第1行的內容文字全部靠中
        priceTable.getColumnModel().getColumn(0).setPreferredWidth(PriceTable_Width*4/10);//設定每一行行寬
        priceTable.getColumnModel().getColumn(1).setPreferredWidth(PriceTable_Width*4/10);//設定每一行行寬
        priceTable.getColumnModel().getColumn(1).setPreferredWidth(PriceTable_Width*2/10);//設定每一行行寬

        //為每個JPanel新增GUI子元件
        addInPanel.add(addIn_StockNumLabel);
        addInPanel.add(addIn_StockNumTextField);
        addInPanel.add(addIn_PriceLabel);
        addInPanel.add(addIn_PriceTextField);
        addInPanel.add(moreThan);
        addInPanel.add(lessThan);
        addInPanel.add(addInButton);


        deletePanel.add(deleteButton,BorderLayout.EAST);

        //為視窗新增GUI子元件
        add(addInPanel);
        getContentPane().add(new JScrollPane(priceTable));
        add(deletePanel);

        //為加入一筆按鈕註冊事件
        addInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!addIn_StockNumTextField.getText().equals("") && !addIn_PriceTextField.getText().equals("")){
                    try{
                        String stockNum = addIn_StockNumTextField.getText();
                        double price = Double.parseDouble(addIn_PriceTextField.getText());
                        if(moreThan.isSelected()) tableModel.addRow(new Object[]{stockNum, price, ">"});
                        if(lessThan.isSelected()) tableModel.addRow(new Object[]{stockNum, price, "<"});
                    }
                    catch(NumberFormatException err){
                        JOptionPane.showMessageDialog(StocksGUI_PriceNotification.this,"輸入格式有誤");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(StocksGUI_PriceNotification.this,"股票代號及價格不得為空");
                }
            }
        });

        //為刪除一筆按鈕註冊事件
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(priceTable.getSelectedRow()!=-1){
                    int delete = JOptionPane.showConfirmDialog(StocksGUI_PriceNotification.this,"確認刪除?","", JOptionPane.YES_NO_OPTION);
                    if(delete==JOptionPane.YES_OPTION){
                        int deleteRowNum = priceTable.getSelectedRows().length;//deleteRowNum代表即將刪除幾筆資料
                        System.out.print("\n");
                        for(int i=0;i<deleteRowNum;i++){
                            tableModel.removeRow(priceTable.getSelectedRows()[0]);
                        }
                    }
                }
                else{
                    JOptionPane.showMessageDialog(StocksGUI_PriceNotification.this,"請選取您要刪除的股票(可拖曳滑鼠一次選取多筆)");
                }
            }
        });
        setVisible(true);
    }
    public JTable getTable(){
        return priceTable;
    }
}
