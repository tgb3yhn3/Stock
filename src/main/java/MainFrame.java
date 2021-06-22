import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class MainFrame extends JFrame {
    private FramePanel functionPanel;
    private JLabel titleLabel;
    private JButton function1Button;
    private JButton function2Button;
    private JButton function3Button;
    private JButton function4Button;
    private JButton function5Button;
    private JButton function6Button;
    private JButton function7Button;
    private JButton function8Button;
    private PriceNotificationFrame pzNotice;
    private SimulatedTradingFrame transaction;
    private List<String> numbers;
    private Map<String, List<String>> revenue;
    private Map<String, List<String>> foreign;
    private Map<String, List<String>> trust;
    private Map<String, List<String>> dealer;
    private Map<String, List<String>> profitability;
    private priceVolumeHandler priceVolumeHandler;
    public MainFrame() {
        //-----------------------------GUI設定---------------------------------
        //創建主頁面視窗
        super("韭菜同學會");
        this.setIconImage(new ImageIcon("imageFile\\韭菜.png").getImage());
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //設置背景
        Container ct = this.getContentPane();
        this.setLayout(null);
        functionPanel = new FramePanel((new ImageIcon("imageFile\\股市.jpg")).getImage());//設置背景圖片
        int screenWidth = 500;
        int screenHeight = 375;
        functionPanel.setBounds(0,0,screenWidth,screenHeight);
        ct.add(functionPanel);
        setSize(screenWidth, screenHeight);

        //為視窗新增GUI子元件
        add(functionPanel);

        //--------------------------啟動GUI時背景執行三大法人更新thread----------------------------
        Thread updateInvestor = new Thread() {
            public void run() {
                try {
                    /*JOptionPane.showMessageDialog(null, "請稍等更新完成");
                    new Investors().getInfo();
                    JOptionPane.showMessageDialog(null, "三大法人更新完成");*/
                    csvFileRead(); //讀檔
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateInvestor.setDaemon(true);
        updateInvestor.start();
        //--------------------------啟動GUI時背景執行三大法人更新thread----------------------------
        Thread updateVolume = new Thread() {
            public void run() {
                //waitingFrame.show();
                try {


                    //csvFileRead(); //讀檔

                    priceVolumeHandler = new priceVolumeHandler(numbers);
                    System.out.println("價格讀取完了");
                    int needUpDays=volumeCSV.needToUpdate();
                    System.out.println("需要更新"+needUpDays+"天");
                    if(needUpDays!=0){
                        if(needUpDays==1||needUpDays==2){
                            if(Calendar.getInstance().getTime().getDay()==6||Calendar.getInstance().getTime().getHours()<15||Calendar.getInstance().getTime().getDay()==0){
                                System.out.println("發現是假日不用更新");
                            }
                        }else {
                            volumeCSV volumeCSV = new volumeCSV();
                            volumeCSV.updater(needUpDays);
                        }
                    }
                    //waitingFrame.stop();
                    JOptionPane.showMessageDialog(null, "均量更新完成");

                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateVolume.setDaemon(true);
        updateVolume.start();
        //--------------------------到價通知，成交背景背景thread----------------------------
        pzNotice = new PriceNotificationFrame(MainFrame.this);
        pzNotice.setVisible(false);
        RealTimeThread pzThread = new RealTimeThread(pzNotice.getTable(), false);
        pzThread.setDaemon(true);
        pzThread.start();

        transaction = new SimulatedTradingFrame(MainFrame.this, "", true);
        transaction.setVisible(false);
        RealTimeThread transactionThread = new RealTimeThread(transaction.getTable(), true);
        transactionThread.setDaemon(true);
        transactionThread.start();

        //--------------------------------------listeners-------------------------------------------

        function1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SearchForListedStocksFrame("", MainFrame.this);
            }
        });
        //為自選股清單按鈕(function2Button)註冊事件
        function2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {new SelfSelectedListFrame(MainFrame.this);}
        });
        //為帳務查詢按鈕(function3Button)註冊事件
        function3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AccountingInquiryFrame(MainFrame.this);
            }
        });
        //為模擬下單按鈕(function4Button)註冊事件
        function4Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transaction.setVisible(true);
            }
        });
        //為選股機器人按鈕(function5Button)註冊事件
        function5Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                new StockPickingRobotFrame(MainFrame.this);
            }
        });
        //為更新資料庫(function6Button)註冊事件
        function6Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event){
                try {
                    JOptionPane.showMessageDialog(null, "請稍等更新完成");
                    /*new Investors().getInfo();  //更新三大法人
                    JOptionPane.showMessageDialog(null, "三大法人更新完成");*/
                    new Revenue().getInfo();  //更新營收
                    JOptionPane.showMessageDialog(null, "營收更新完成");
                    new Profitability().getInfo();  //更新獲利能力
                    JOptionPane.showMessageDialog(null, "獲利能力完成");
                }
                catch(Exception e){
                    e.printStackTrace();
                }
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

    public void csvFileRead(){
        //stockNum.csv讀取
        File stockNumCsv = new File("csvFile\\stockNum.csv");  // stockNum CSV檔案路徑
        BufferedReader stockNumBr = null;
        numbers = new ArrayList<String>();
        try {
            stockNumBr = new BufferedReader(new FileReader(stockNumCsv));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";
        try {
            while ((line = stockNumBr.readLine()) != null) //讀取到的內容給line變數
                numbers.add(line);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //revenueNew.csv讀取
        File revenueCsv = new File("csvFile\\revenueNew.csv");  // CSV檔案路徑
        BufferedReader revenueBr = null;
        revenue = new HashMap<String, List<String>>();
        try {
            revenueBr = new BufferedReader(new FileReader(revenueCsv));
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        line = "";
        try {
            while ((line = revenueBr.readLine()) != null) //讀取到的內容給line變數
                revenue.put(line.substring(0,4), Arrays.asList(line.substring(5,line.length()).split(",")));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        //foreign.csv讀取
        File foreignCsv = new File("csvFile\\foreign.csv");  // CSV檔案路徑
        BufferedReader foreignBr = null;
        foreign = new HashMap<String, List<String>>();
        try {
            foreignBr = new BufferedReader(new FileReader(foreignCsv));
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        line = "";
        try {
            while ((line = foreignBr.readLine()) != null) //讀取到的內容給line變數
                foreign.put(line.substring(0,4), Arrays.asList(line.substring(5,line.length()).split(",")));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        //trust.csv
        File trustCsv = new File("csvFile\\trust.csv");  // CSV檔案路徑
        BufferedReader trustBr = null;
        trust = new HashMap<String, List<String>>();
        try {
            trustBr = new BufferedReader(new FileReader(trustCsv));
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        line = "";
        try {
            while ((line = trustBr.readLine()) != null) //讀取到的內容給line變數
                trust.put(line.substring(0,4), Arrays.asList(line.substring(5,line.length()).split(",")));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        //dealer.csv
        File dealerCsv = new File("csvFile\\dealer.csv");  // CSV檔案路徑
        BufferedReader dealerBr = null;
        dealer = new HashMap<String, List<String>>();
        try {
            dealerBr = new BufferedReader(new FileReader(trustCsv));
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        line = "";
        try {
            while ((line = dealerBr.readLine()) != null) //讀取到的內容給line變數
                dealer.put(line.substring(0,4), Arrays.asList(line.substring(5,line.length()).split(",")));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        //profitability.csv
        File profitabilityCsv = new File("csvFile\\profitability.csv");  // CSV檔案路徑
        BufferedReader profitabilityBr = null;
        profitability = new HashMap<String, List<String>>();
        try {
            profitabilityBr = new BufferedReader(new FileReader(profitabilityCsv));
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        line = "";
        try {
            while ((line = profitabilityBr.readLine()) != null) //讀取到的內容給line變數
                profitability.put(line.substring(0,4), Arrays.asList(line.substring(5,line.length()).split(",")));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public Map<String, List<String>> getRevenue() {
        return revenue;
    }

    public List<String> getNumbers() {
        return numbers;
    }

    public Map<String, List<String>> getForeign() {
        return foreign;
    }

    public Map<String, List<String>> getTrust() {
        return trust;
    }

    public Map<String, List<String>> getDealer() {
        return dealer;
    }

    public Map<String, List<String>> getProfitability() {
        return profitability;
    }

    public priceVolumeHandler getPriceVolumeHandler(){return  priceVolumeHandler;}

    class FramePanel extends JPanel {
        Image img;//背景圖片
        public FramePanel(Image img) {
            super(new GridBagLayout());
            this.img=img;
            this.setOpaque(true);

            Font functionButtonTextStyle = new Font("微軟正黑體" ,Font.BOLD,15);//功能按鈕內的文字屬性

            //創建JPanel中的各個GUI子元件
            titleLabel = new JLabel("韭菜同學會股票系統",new ImageIcon("imageFile\\韭菜.png"),JLabel.CENTER);
            titleLabel.setFont(new Font("微軟正黑體", Font.BOLD, 20));
            titleLabel.setForeground(Color.cyan);

            function1Button = new XrButton(" 查詢上市櫃股",false);
            function2Button = new XrButton(" 自選股清單",false);
            function3Button = new XrButton(" 帳務查詢",false);
            function4Button = new XrButton(" 模擬下單",false);
            function5Button = new XrButton(" 選股機器人",false);
            function6Button = new XrButton(" 更新資料庫",false);
            function7Button = new XrButton(" 到價通知",false);
            function8Button = new XrButton(" 結束",false);
            function1Button.setFont(functionButtonTextStyle);
            function2Button.setFont(functionButtonTextStyle);
            function3Button.setFont(functionButtonTextStyle);
            function4Button.setFont(functionButtonTextStyle);
            function5Button.setFont(functionButtonTextStyle);
            function6Button.setFont(functionButtonTextStyle);
            function7Button.setFont(functionButtonTextStyle);
            function8Button.setFont(functionButtonTextStyle);

            GridBagConstraints g = new GridBagConstraints();
            g.insets = new Insets(0,10,20,10);//按鈕間的間距
            g.gridwidth = 2;
            g.fill = GridBagConstraints.BOTH;
            g.gridx = 0;
            g.gridy = 0;
            this.add(titleLabel,g);
            g.gridwidth = 1;
            g.ipadx = 50; //功能按鈕寬度
            g.ipady = 10; //功能按鈕高度
            g.gridx = 0;
            g.gridy = 1;
            this.add(function1Button,g);
            g.gridx = 1;
            g.gridy = 1;
            this.add(function2Button,g);
            g.gridx = 0;
            g.gridy = 2;
            this.add(function3Button,g);
            g.gridx = 1;
            g.gridy = 2;
            this.add(function4Button,g);
            g.gridx = 0;
            g.gridy = 3;
            this.add(function5Button,g);
            g.gridx = 1;
            g.gridy = 3;
            this.add(function6Button,g);
            g.gridx = 0;
            g.gridy = 4;
            this.add(function7Button,g);
            g.gridx = 1;
            g.gridy = 4;
            this.add(function8Button,g);
        }
        //添加背景圖片
        public void paintComponent(Graphics grape) {
            super.paintComponents(grape);
            grape.drawImage(img,0,0,this.getWidth(),this.getHeight(),this);
        }
    }
}

