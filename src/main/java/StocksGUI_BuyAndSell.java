import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StocksGUI_BuyAndSell extends JFrame{
    private JTable buyAndSellTable;
    public StocksGUI_BuyAndSell(StocksGUI mainFrame, String stockNum, boolean isBuy){
        //創建到價通知頁面視窗
        super("韭菜同學會_模擬下單");
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int windowWidth = 500;//設定視窗寬度
        int windowHeight = 250;//設定視窗高度
        setSize(windowWidth, windowHeight);
        setLocation(mainFrame.getX(),mainFrame.getY());//此視窗出現的位置將在主頁面的位置


        //創建視窗內的各個GUI子元件_加入一筆的部分
        JPanel addInPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addInPanel.setBorder(BorderFactory.createTitledBorder(""));
        JLabel addIn_StockNumLabel = new JLabel("股票代號:");
        JTextField addIn_StockNumTextField = new JTextField(stockNum, 4);
        JLabel addIn_PriceLabel = new JLabel("價格:");
        JTextField addIn_PriceTextField = new JTextField("", 4);
        JLabel addIn_lotNumLabel = new JLabel("張數:");
        JTextField addIn_lotNumTextField = new JTextField("", 4);
        JRadioButton buyRadioButton = new JRadioButton("買",false);
        JRadioButton sellRadioButton = new JRadioButton("賣",false);
        if(isBuy==true){
            buyRadioButton.setSelected(true);
        }
        else{
            sellRadioButton.setSelected(true);
        }
        ButtonGroup buy_sellButtonGroup = new ButtonGroup();
        buy_sellButtonGroup.add(buyRadioButton);
        buy_sellButtonGroup.add(sellRadioButton);
        JButton addInButton = new JButton("加入");

        //創建視窗內的各個GUI子元件_刪除一筆的部分
        JPanel deletePanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createTitledBorder("委托:"));
        deletePanel.setPreferredSize(new Dimension(windowWidth-92,25));
        JButton deleteButton = new JButton("刪除");

        //創建到價通知的table
        String [] buyAndSellTableHeadings = new String[] {"股票代號","價格","買或賣","張數"};
        DefaultTableModel tableModel = new DefaultTableModel(buyAndSellTableHeadings, 0);//0是初始列數，代表一開始沒有任何一筆資料
        buyAndSellTable = new JTable(tableModel);
        int buyAndSellTable_Width = windowWidth-100;//table寬度
        int buyAndSellTable_RowHeight = 20;//table列高
        buyAndSellTable.setPreferredScrollableViewportSize(new Dimension(buyAndSellTable_Width, buyAndSellTable_RowHeight*5));//設定table高度和寬度
        buyAndSellTable.setRowHeight(buyAndSellTable_RowHeight);//設定table列高
        buyAndSellTable.getTableHeader().setResizingAllowed(false);//table的行寬為固定
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();//renderer用來使table裡面的文字靠中
        renderer.setHorizontalAlignment(JTextField.CENTER);//renderer用來使table裡面的文字靠中
        buyAndSellTable.getColumnModel().getColumn(0).setCellRenderer(renderer);//讓第0行的內容文字全部靠中
        buyAndSellTable.getColumnModel().getColumn(1).setCellRenderer(renderer);//讓第1行的內容文字全部靠中
        buyAndSellTable.getColumnModel().getColumn(2).setCellRenderer(renderer);//讓第2行的內容文字全部靠中
        buyAndSellTable.getColumnModel().getColumn(3).setCellRenderer(renderer);//讓第3行的內容文字全部靠中
        buyAndSellTable.getColumnModel().getColumn(0).setPreferredWidth(buyAndSellTable_Width*4/10);//設定每一行行寬
        buyAndSellTable.getColumnModel().getColumn(1).setPreferredWidth(buyAndSellTable_Width*4/10);//設定每一行行寬

        //為每個JPanel新增GUI子元件
        addInPanel.add(addIn_StockNumLabel);
        addInPanel.add(addIn_StockNumTextField);
        addInPanel.add(addIn_PriceLabel);
        addInPanel.add(addIn_PriceTextField);
        addInPanel.add(addIn_lotNumLabel);
        addInPanel.add(addIn_lotNumTextField);
        addInPanel.add(buyRadioButton);
        addInPanel.add(sellRadioButton);
        addInPanel.add(addInButton);


        deletePanel.add(deleteButton,BorderLayout.EAST);

        //為視窗新增GUI子元件
        add(addInPanel);
        buttonPanel.add(new JScrollPane(buyAndSellTable));
        getContentPane().add(buttonPanel);
        add(deletePanel);

        //為加入一筆按鈕註冊事件
        addInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!addIn_StockNumTextField.getText().equals("") && !addIn_PriceTextField.getText().equals("")&& !addIn_lotNumTextField.getText().equals("")){
                    try{
                        String stockNum = addIn_StockNumTextField.getText();
                        double price = Double.parseDouble(addIn_PriceTextField.getText());
                        int lotNum = Integer.parseInt(addIn_lotNumTextField.getText());
                        if(buyRadioButton.isSelected()){
                            tableModel.addRow(new Object[]{stockNum,price,"買進",lotNum});
                        }
                        else if(sellRadioButton.isSelected()){
                            tableModel.addRow(new Object[]{stockNum,price,"賣出",lotNum});
                        }
                    }
                    catch(NumberFormatException err){
                        JOptionPane.showMessageDialog(StocksGUI_BuyAndSell.this,"輸入格式有誤");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(StocksGUI_BuyAndSell.this,"輸入不得為空");
                }
            }
        });

        //為刪除一筆按鈕註冊事件
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(buyAndSellTable.getSelectedRow()!=-1){
                    int delete = JOptionPane.showConfirmDialog(StocksGUI_BuyAndSell.this,"確認刪除?","", JOptionPane.YES_NO_OPTION);
                    if(delete==JOptionPane.YES_OPTION){
                        int deleteRowNum = buyAndSellTable.getSelectedRows().length;//deleteRowNum代表即將刪除幾筆資料
                        System.out.print("\n");
                        for(int i=0;i<deleteRowNum;i++){
                            tableModel.removeRow(buyAndSellTable.getSelectedRows()[0]);
                        }
                    }
                }
                else{
                    JOptionPane.showMessageDialog(StocksGUI_BuyAndSell.this,"請選取您要刪除的股票(可拖曳滑鼠一次選取多筆)");
                }
            }
        });
        setVisible(true);
    }
    public JTable getTable(){
        return buyAndSellTable;
    }
}
