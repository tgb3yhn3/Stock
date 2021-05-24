import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class StocksGUI_SearchForListedStocks extends JFrame{

    private Map<String, List<String>> revenue;
    private Map<String, List<String>> foreign;
    private Map<String, List<String>> trust;
    private Map<String, List<String>> dealer;
    private Map<String, List<String>> profitability;
    public StocksGUI_SearchForListedStocks(StocksGUI mainFrame, Map<String, List<String>> revenue, Map<String, List<String>> foreign, Map<String, List<String>> trust, Map<String, List<String>> dealer, Map<String, List<String>> profitability){
        //創建查詢上市櫃股頁面視窗
        super("韭菜同學會_查詢上市櫃股");
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int windowWidth = 350;//設定視窗寬度
        int windowHeight = 500;//設定視窗高度
        setSize(windowWidth, windowHeight);
        setLocation(mainFrame.getX(),mainFrame.getY());//此視窗出現的位置將在主頁面的位置

        //-----------------------initialize--------------------
        this.revenue = revenue;
        this.foreign = foreign;
        this.trust = trust;
        this.dealer = dealer;
        this.profitability = profitability;

        //查詢列GUI
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(BorderFactory.createTitledBorder("請輸入欲查詢的股票代號:"));
        JTextField searchInputTextField = new JTextField("",20);
        JButton searchButton = new JButton("查詢");
        searchPanel.add(searchInputTextField);
        searchPanel.add(searchButton);

        /*查詢結果GUI
        * resultPanel
        *  ├──resultButtonPanel
        *  │    └──基本面、新聞、三大法人、技術線圖、買、賣按鈕(JButton)
        *  └──BestFivePanel
        *       └──最佳五檔(BestFiveTextArea)
        * */
        JPanel resultPanel = new JPanel();
        resultPanel.setBorder(BorderFactory.createTitledBorder("查詢結果:"));
        resultPanel.setPreferredSize(new Dimension(windowWidth-50,windowHeight-150));
        JPanel resultButtonPanel = new JPanel(new GridBagLayout());//
        resultButtonPanel.setBorder(BorderFactory.createTitledBorder("功能按鈕:"));
        //在resultButtonPanel中新增基本面按鈕，並設定網格約束
        JButton fundamentalsButton = new JButton("基本面");
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.BOTH;
        g.gridx = 0;
        g.gridy = 0;
        resultButtonPanel.add(fundamentalsButton,g);
        //在resultButtonPanel中新增新聞按鈕，並設定網格約束
        JButton newsButton = new JButton("新聞");
        g.gridx = 1;
        g.gridy = 0;
        resultButtonPanel.add(newsButton,g);
        //在resultButtonPanel中新增三大法人按鈕，並設定網格約束
        JButton threeMajorCorporationsButton = new JButton("三大法人");
        g.gridx = 0;
        g.gridy = 1;
        resultButtonPanel.add(threeMajorCorporationsButton,g);
        //在resultButtonPanel中新增技術線圖按鈕，並設定網格約束
        JButton klineButton = new JButton("技術線圖");
        g.gridx = 1;
        g.gridy = 1;
        resultButtonPanel.add(klineButton,g);
        //在resultButtonPanel中新增買按鈕，並設定網格約束
        JButton buyButton = new JButton("買");
        g.gridx = 2;
        g.gridy = 0;
        g.gridheight = 2;
        resultButtonPanel.add(buyButton,g);
        //在resultButtonPanel中新增賣按鈕，並設定網格約束
        JButton sellButton = new JButton("賣");
        g.gridx = 4;
        g.gridy = 0;
        resultButtonPanel.add(sellButton,g);
        //把resultButtonPanel加進resultPanel
        resultPanel.add(resultButtonPanel);
        //在resultPanel中新增最佳五檔區
        JPanel BestFivePanel = new JPanel();
        BestFivePanel.setBorder(BorderFactory.createTitledBorder("最佳五檔:"));
        JTextArea BestFiveTextArea  = new JTextArea (13,25);
        BestFivePanel.add(BestFiveTextArea);
        resultPanel.add(BestFivePanel);

        //為視窗新增GUI子元件
        add(searchPanel);

        //為查詢按鈕註冊事件
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                add(resultPanel);
                revalidate();
                /*if(!searchInputTextField.getText().equals("")){
                    add(resultPanel);
                    revalidate();
                }else{
                    JOptionPane.showMessageDialog(StocksGUI_SearchForListedStocks.this,"股票代號不得為空");
                }*/
            }
        });

        searchInputTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                add(resultPanel);
                revalidate();
                /*if(!searchInputTextField.getText().equals("")){
                    add(resultPanel);
                    revalidate();
                }else{
                    JOptionPane.showMessageDialog(StocksGUI_SearchForListedStocks.this,"股票代號不得為空");
                }*/
            }
        });

        //為基本面按鈕註冊事件
        fundamentalsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StocksGUI_Fundamentals(StocksGUI_SearchForListedStocks.this, searchInputTextField.getText(), revenue, profitability);
            }
        });

        newsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    String url = "https://tw.stock.yahoo.com/q/h?s=" + searchInputTextField.getText();
                    java.net.URI uri = java.net.URI.create(url);
                    // 獲取當前系統桌面擴充套件
                    java.awt.Desktop dp = java.awt.Desktop.getDesktop();
                    // 判斷系統桌面是否支援要執行的功能
                    if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                        dp.browse(uri);
                        // 獲取系統預設瀏覽器開啟連結
                    }
                } catch (java.lang.NullPointerException e) {
                    // 此為uri為空時丟擲異常
                    e.printStackTrace();
                } catch (java.io.IOException e) {
                    // 此為無法獲取系統預設瀏覽器
                    e.printStackTrace();
                }
            }
        });

        //為三大法人按鈕註冊事件
        threeMajorCorporationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StocksGUI_ThreeMajorCorporations(StocksGUI_SearchForListedStocks.this, searchInputTextField.getText(), foreign, trust, dealer);
            }
        });

        //為技術線圖註冊事件
        klineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Kline(searchInputTextField.getText());
                revalidate();
            }
        });

        //為買、賣button註冊事件
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StocksGUI_BuyAndSell(mainFrame, searchInputTextField.getText(), true);
            }
        });
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StocksGUI_BuyAndSell(mainFrame, searchInputTextField.getText(), false);
            }
        });

        setVisible(true);
    }
}
