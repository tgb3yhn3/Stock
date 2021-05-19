import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import static java.lang.Integer.parseInt;

public class StocksGUI_PriceNotification extends JFrame{

    public StocksGUI_PriceNotification(StocksGUI mainFrame){
        //創建到價通知頁面視窗
        super("韭菜同學會_到價通知");
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int windowWidth = 420;
        int windowHeight = 250;
        setSize(windowWidth, windowHeight);
        setLocation(mainFrame.getX(),mainFrame.getY());


        //創建視窗內的各個GUI子元件_加入一筆的部分
        JPanel addInPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addInPanel.setBorder(BorderFactory.createTitledBorder(""));
        JLabel addIn_StockNumLabel = new JLabel("股票代碼:");
        JTextField addIn_StockNumTextField = new JTextField("",3);
        JLabel addIn_PriceLabel = new JLabel("價格:");
        JTextField addIn_PriceTextField = new JTextField("",3);
        JLabel addIn_NumberLabel = new JLabel("張數:");
        JTextField addIn_NumberTextField = new JTextField("",2);
        JRadioButton addIn_BuyRadioButton = new JRadioButton("買",true);
        JRadioButton addIn_SellRadioButton = new JRadioButton("賣",false);
        ButtonGroup Buy_SellRadioGroup = new ButtonGroup();
        Buy_SellRadioGroup.add(addIn_BuyRadioButton);
        Buy_SellRadioGroup.add(addIn_SellRadioButton);
        JButton addInButton = new JButton("加入");

        //創建視窗內的各個GUI子元件_刪除一筆的部分
        JPanel deletePanel = new JPanel(new BorderLayout());
        deletePanel.setPreferredSize(new Dimension(windowWidth-35,25));
        JButton deleteButton = new JButton("刪除");

        //創建到價通知的table
        String [] PriceTable_Headings= new String[] {"股票代碼","價格","張數","買或賣"};
        DefaultTableModel tableModel = new DefaultTableModel(PriceTable_Headings, 0);//0是初始列數，代表一開始沒有任何一筆資料
        JTable PriceTable = new JTable(tableModel);
        int PriceTable_Width = windowWidth-100;//table寬度
        int PriceTable_RowHeight = 20;//table列高
        PriceTable.setPreferredScrollableViewportSize(new Dimension(PriceTable_Width, PriceTable_RowHeight*5));//設定table高度和寬度
        PriceTable.setRowHeight(PriceTable_RowHeight);//設定table列高
        PriceTable.getTableHeader().setResizingAllowed(false);//table的行寬為固定
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();//renderer用來使table裡面的文字靠中
        renderer.setHorizontalAlignment(JTextField.CENTER);//renderer用來使table裡面的文字靠中
        PriceTable.getColumnModel().getColumn(1).setCellRenderer(renderer);//讓第1行的內容文字全部靠中
        PriceTable.getColumnModel().getColumn(2).setCellRenderer(renderer);//讓第2行的內容文字全部靠中
        PriceTable.getColumnModel().getColumn(3).setCellRenderer(renderer);//讓第3行的內容文字全部靠中
        PriceTable.getColumnModel().getColumn(0).setPreferredWidth(PriceTable_Width*3/10);//設定每一行行寬
        PriceTable.getColumnModel().getColumn(1).setPreferredWidth(PriceTable_Width*3/10);//設定每一行行寬
        PriceTable.getColumnModel().getColumn(2).setPreferredWidth(PriceTable_Width*2/10);//設定每一行行寬
        PriceTable.getColumnModel().getColumn(3).setPreferredWidth(PriceTable_Width*2/10);//設定每一行行寬

        //為每個JPanel新增GUI子元件
        addInPanel.add(addIn_StockNumLabel);
        addInPanel.add(addIn_StockNumTextField);
        addInPanel.add(addIn_PriceLabel);
        addInPanel.add(addIn_PriceTextField);
        addInPanel.add(addIn_NumberLabel);
        addInPanel.add(addIn_NumberTextField);
        addInPanel.add(addIn_BuyRadioButton);
        addInPanel.add(addIn_SellRadioButton);
        addInPanel.add(addInButton);


        deletePanel.add(deleteButton,BorderLayout.EAST);

        //為視窗新增GUI子元件
        add(addInPanel);
        getContentPane().add(new JScrollPane(PriceTable));
        add(deletePanel);

        //為加入一筆按鈕註冊事件
        addInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!addIn_StockNumTextField.getText().equals("") && !addIn_PriceTextField.getText().equals("")){
                    try{
                        int stockNum = Integer.parseInt(addIn_StockNumTextField.getText());
                        double price = Double.parseDouble(addIn_PriceTextField.getText());
                        int number = Integer.parseInt(addIn_NumberTextField.getText());
                        if(addIn_BuyRadioButton.isSelected()){
                            tableModel.addRow(new Object[]{stockNum,price,number,"買"});
                        }
                        else if(addIn_SellRadioButton.isSelected()){
                            tableModel.addRow(new Object[]{stockNum,price,number,"賣"});
                        }
                    }
                    catch(NumberFormatException err){
                        JOptionPane.showMessageDialog(StocksGUI_PriceNotification.this,"輸入格式有誤");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(StocksGUI_PriceNotification.this,"股票代碼及價格不得為空");
                }
            }
        });

        //為刪除一筆按鈕註冊事件
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(PriceTable.getSelectedRow()!=-1){
                    int delete = JOptionPane.showConfirmDialog(StocksGUI_PriceNotification.this,"確認刪除?","", JOptionPane.YES_NO_OPTION);
                    if(delete==JOptionPane.YES_OPTION){
                        int deleteRowNum = PriceTable.getSelectedRows().length;//deleteRowNum代表即將刪除幾筆資料
                        System.out.print("\n");
                        for(int i=0;i<deleteRowNum;i++){
                            tableModel.removeRow(PriceTable.getSelectedRows()[0]);
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
}
