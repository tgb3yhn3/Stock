import javax.swing.*;
import java.awt.*;
import java.awt.desktop.SystemEventListener;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class StocksGUI_StockPickingRobot extends JFrame{

    private List<String> originNumbers;
    private List<String> numbers;
    private Map<String, List<String>> revenue;
    private Map<String, List<String>> foreign;
    private Map<String, List<String>> trust;
    private Map<String, List<String>> dealer;
    private JList resultList;
    private JPanel filterPanel;
    private JPanel buttonPanel;
    private JPanel resultPanel;
    private JButton searchButton;
    private JButton resetButton;

    public StocksGUI_StockPickingRobot(StocksGUI mainFrame) throws IOException{
        //創建選股機器人頁面視窗
        super("韭菜同學會_選股機器人");
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 540);
        setLocation(mainFrame.getX(),mainFrame.getY());

        //----------------revenue and financialReport and numbers initialize---------------------------------

        //創建視窗內的各個GUI子元件_篩選區塊
        filterPanel = new JPanel(new GridLayout(13,1,0,0));
        filterPanel.setBorder(BorderFactory.createTitledBorder("請設定篩選條件"));

        // 在filterPanel內的panel
        JPanel filter1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter1CheckBox = new JCheckBox("營收月增",null,false);
        JTextField filter1TextField = new JTextField("",3);
        JLabel filter1Label = new JLabel("%");

        JPanel filter2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter2CheckBox = new JCheckBox("營收年增",null,false);
        JTextField filter2TextField = new JTextField("",3);
        JLabel filter2Label = new JLabel("%");

        JPanel filter3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter3CheckBox = new JCheckBox("毛利率增",null,false);

        JPanel filter4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter4CheckBox = new JCheckBox("三率三升",null,false);

        JPanel filter5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter5CheckBox = new JCheckBox("ROE大於",null,false);
        JTextField filter5TextField = new JTextField("",3);

        JPanel filter6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter6CheckBox = new JCheckBox("EPS大於",null,false);
        JTextField filter6TextField = new JTextField("",3);

        JPanel filter7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter7CheckBox = new JCheckBox("本益比小於",null,false);
        JTextField filter7TextField = new JTextField("",3);

        JPanel filter8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter8CheckBox = new JCheckBox("外資連買",null,false);
        JTextField filter8TextField = new JTextField("",3);
        JLabel filter8Label = new JLabel("天");

        JPanel filter9 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter9CheckBox = new JCheckBox("投信連買",null,false);
        JTextField filter9TextField = new JTextField("",3);
        JLabel filter9Label = new JLabel("天");

        JPanel filter10 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter10CheckBox = new JCheckBox("自營商連買",null,false);
        JTextField filter10TextField = new JTextField("",3);
        JLabel filter10Label = new JLabel("天");

        JPanel filter11 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter11CheckBox = new JCheckBox("",null,false);
        JTextField filter11_1TextField = new JTextField("",3);
        JLabel filter11_1Label = new JLabel("天內股價上升");
        JTextField filter11_2TextField = new JTextField("",3);
        JLabel filter11_2Label = new JLabel("%");

        JPanel filter12 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter12CheckBox = new JCheckBox("成交量大於",null,false);
        JTextField filter12TextField = new JTextField("",3);
        JLabel filter12Label = new JLabel("天均量");

        //buttons
        buttonPanel = new JPanel(new FlowLayout()); // button的panel
        searchButton = new JButton("搜尋");
        resetButton = new JButton("reset");
        buttonPanel.add(searchButton);
        buttonPanel.add(resetButton);

        //創建視窗內的各個GUI子元件_結果區塊
        resultPanel = new JPanel();
        resultPanel.setBorder(BorderFactory.createTitledBorder("搜尋結果"));

        //處理stockNum
        DefaultListModel listModel = new DefaultListModel();    //建立ListModel
        originNumbers = new ArrayList<String>();

        //----------------------------------------file read----------------------------------

        //stockNum.csv讀取
        File stockNumCsv = new File("C:/Users/user/Desktop/csv_file/stockNum.csv");  // stockNum CSV檔案路徑
        BufferedReader stockNumBr = null;
        try {
            stockNumBr = new BufferedReader(new FileReader(stockNumCsv));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";
        try {
            while ((line = stockNumBr.readLine()) != null) //讀取到的內容給line變數
                originNumbers.add(line);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //revenue.csv讀取
        File revenueCsv = new File("C:/Users/user/Desktop/csv_file/revenue.csv");  // CSV檔案路徑
        BufferedReader revenueBr = null;
        revenue = new HashMap<String, List<String>>();
        try {
            revenueBr = new BufferedReader(new FileReader(revenueCsv));
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        line = "";
        revenueBr.readLine();
        try {
            while ((line = revenueBr.readLine()) != null) //讀取到的內容給line變數
                revenue.put(line.substring(0,4), Arrays.asList(line.substring(5,line.length()).split(",")));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        //foreign.csv讀取
        File foreignCsv = new File("C:/Users/user/Desktop/csv_file/foreign.csv");  // CSV檔案路徑
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
        File trustCsv = new File("C:/Users/user/Desktop/csv_file/trust.csv");  // CSV檔案路徑
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
        File dealerCsv = new File("C:/Users/user/Desktop/csv_file/dealer.csv");  // CSV檔案路徑
        BufferedReader dealerBr = null;
        dealer = new HashMap<String, List<String>>();
        try {
            dealerBr = new BufferedReader(new FileReader(dealerCsv));
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
        //---------------------------------------------------------------------------------

        numbers = new ArrayList<String>(originNumbers);
        listModel.addAll(numbers);  //初始化ListModel
        resultList = new JList(listModel);
        resultList.setFixedCellWidth(200);
        resultList.setFixedCellHeight(25);
        resultList.setVisibleRowCount(10); //一次只顯示五條資料
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//一次只能選一個


        //把每個元件放入其filer
        filter1.add(filter1CheckBox);
        filter1.add(filter1TextField);
        filter1.add(filter1Label);

        filter2.add(filter2CheckBox);
        filter2.add(filter2TextField);
        filter2.add(filter2Label);

        filter3.add(filter3CheckBox);

        filter4.add(filter4CheckBox);

        filter5.add(filter5CheckBox);
        filter5.add(filter5TextField);

        filter6.add(filter6CheckBox);
        filter6.add(filter6TextField);

        filter7.add(filter7CheckBox);
        filter7.add(filter7TextField);

        filter8.add(filter8CheckBox);
        filter8.add(filter8TextField);
        filter8.add(filter8Label);

        filter9.add(filter9CheckBox);
        filter9.add(filter9TextField);
        filter9.add(filter9Label);

        filter10.add(filter10CheckBox);
        filter10.add(filter10TextField);
        filter10.add(filter10Label);

        filter11.add(filter11CheckBox);
        filter11.add(filter11_1TextField);
        filter11.add(filter11_1Label);
        filter11.add(filter11_2TextField);
        filter11.add(filter11_2Label);

        filter12.add(filter12CheckBox);
        filter12.add(filter12TextField);
        filter12.add(filter12Label);

        //將所有filter放入 filterPanel
        filterPanel.add(filter1);
        filterPanel.add(filter2);
        filterPanel.add(filter3);
        filterPanel.add(filter4);
        filterPanel.add(filter5);
        filterPanel.add(filter6);
        filterPanel.add(filter7);
        filterPanel.add(filter8);
        filterPanel.add(filter9);
        filterPanel.add(filter10);
        filterPanel.add(filter11);
        filterPanel.add(filter12);
        filterPanel.add(buttonPanel);

        //放入resultList
        resultPanel.add(new JScrollPane(resultList));

        //把filterPanel和resultPanel放入Frame
        add(filterPanel);
        add(resultPanel);

        setVisible(true);

        //listener of resetButton
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultListModel listModel = new DefaultListModel();
                numbers = new ArrayList<String>(originNumbers);
                listModel.addAll(numbers);
                resultList.setModel(listModel);
            }
        });

        //listener of searchButton
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(filter1CheckBox.isSelected() && !filter1TextField.getText().equals("")) { //營收月增篩選
                    List<String> stockRevenue;
                    for (int i = numbers.size() - 1; i >= 0; i--) {
                        stockRevenue = revenue.get(numbers.get(i));
                        if (Double.parseDouble(stockRevenue.get(1)) < Double.parseDouble(filter1TextField.getText()))
                            numbers.remove(i);
                    }
                }
                if(filter2CheckBox.isSelected() && !filter2TextField.getText().equals("")) { //營收年增增篩選
                    List<String> stockRevenue;
                    for (int i = numbers.size() - 1; i >= 0; i--) {
                        stockRevenue = revenue.get(numbers.get(i));
                        if (Double.parseDouble(stockRevenue.get(2)) < Double.parseDouble(filter2TextField.getText()))
                            numbers.remove(i);
                    }
                }
                if(filter5CheckBox.isSelected() && !filter5TextField.getText().equals("")) { //ROE篩選
                    /*for (int i = numbers.size() - 1; i >= 0; i--)
                        if (Double.parseDouble(financialReportMap.get(numbers.get(i)).get(6)) < Double.parseDouble(filter5TextField.getText()))
                            numbers.remove(i);*/
                }
                if(filter6CheckBox.isSelected() && !filter6TextField.getText().equals("")) { //EPS篩選
                    /*for (int i = numbers.size() - 1; i >= 0; i--)
                        if (Double.parseDouble(financialReportMap.get(numbers.get(i)).get(7)) < Double.parseDouble(filter6TextField.getText()))
                            numbers.remove(i);*/
                }
                if(filter8CheckBox.isSelected() && !filter8TextField.getText().equals("")) { //外資連買篩選
                    int days = Integer.parseInt(filter8TextField.getText());
                    List<String> stockForeign;
                    for (int i = numbers.size() - 1; i >= 0; i--) {
                        stockForeign = foreign.get(numbers.get(i));
                        for (int day = 1; day < days; day++)
                            if(stockForeign.get(day).equals("--") || Integer.parseInt(stockForeign.get(day))<=0 ) {
                                numbers.remove(i);
                                break;
                            }
                    }
                }

                if(filter9CheckBox.isSelected() && !filter9TextField.getText().equals("")) { //投信連買篩選
                    int days = Integer.parseInt(filter9TextField.getText());
                    List<String> stockTrust;
                    for (int i = numbers.size() - 1; i >= 0; i--) {
                        stockTrust = trust.get(numbers.get(i));
                        for (int day = 1; day < days; day++)
                            if(stockTrust.get(day).equals("--") || Integer.parseInt(stockTrust.get(day))<=0 ) {
                                numbers.remove(i);
                                break;
                            }
                    }
                }

                if(filter10CheckBox.isSelected() && !filter10TextField.getText().equals("")) { //自營商連買篩選
                    int days = Integer.parseInt(filter10TextField.getText());
                    List<String> stockDealer;
                    for (int i = numbers.size() - 1; i >= 0; i--) {
                        stockDealer = trust.get(dealer.get(i));
                        for (int day = 1; day < days; day++)
                            if(stockDealer.get(day).equals("--") || Integer.parseInt(stockDealer.get(day))<=0 ) {
                                numbers.remove(i);
                                break;
                            }
                    }
                }

                DefaultListModel listModel = new DefaultListModel();
                listModel.addAll(numbers);
                resultList.setModel(listModel);
            }
        });
    }
}
