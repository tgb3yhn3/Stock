import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StocksGUI extends JFrame{
    private Fundamental data;
    private StocksGUI_PriceNotification pzNotice;
    public StocksGUI(){
        //創建主頁面視窗
        super("韭菜同學會");
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 250);

        //創建視窗內的各個GUI子元件
        JPanel functionPanel = new JPanel(new GridLayout(4,2,20,20));

        JButton function1Button = new JButton("1. 查詢上市櫃股");
        JButton function2Button = new JButton("2. 自選股清單");
        JButton function3Button = new JButton("3. 帳務查詢");
        JButton function4Button = new JButton("4. 模擬下單");
        JButton function5Button = new JButton("5. 選股機器人");
        JButton function6Button = new JButton("6. 更新資料庫");
        JButton function7Button = new JButton("7. 到價通知");
        JButton function8Button = new JButton("8. 結束");

        //為每個JPanel新增GUI子元件
        functionPanel.add(function1Button);
        functionPanel.add(function2Button);
        functionPanel.add(function3Button);
        functionPanel.add(function4Button);
        functionPanel.add(function5Button);
        functionPanel.add(function6Button);
        functionPanel.add(function7Button);
        functionPanel.add(function8Button);

        //為視窗新增GUI子元件
        add(functionPanel);

        pzNotice = new StocksGUI_PriceNotification(StocksGUI.this);
        pzNotice.setVisible(false);
        JTable pzTable = pzNotice.getTable();
        RealTimeThread pzThread = new RealTimeThread(pzNotice);
        pzThread.setDaemon(true);
        pzThread.start();

        //為選股機器人按鈕(function5Button)註冊事件
        function5Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StocksGUI_StockPickingRobot(StocksGUI.this);
            }
        });
        //為更新資料庫(function6Button)註冊事件
        function6Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                data = new Fundamental();
            }
        });

        //為到價通知(function7Button)註冊事件
        function7Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pzNotice.setVisible(true);
            }
        });

        function8Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });


        setVisible(true);
    }
}
