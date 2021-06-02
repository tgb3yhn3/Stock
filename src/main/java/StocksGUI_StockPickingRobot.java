import javax.swing.*;
import java.awt.*;
import java.awt.desktop.SystemEventListener;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class StocksGUI_StockPickingRobot extends JFrame{

    private final List<String> originNumbers;
    private List<String> numbers;
    private final Map<String, List<String>> revenue;
    private final Map<String, List<String>> foreign;
    private final Map<String, List<String>> trust;
    private final Map<String, List<String>> dealer;
    private final Map<String, List<String>> profitability;
    private JList resultList;
    private JPanel filterPanel;
    private JPanel buttonPanel;
    private JPanel resultPanel;
    private JButton searchButton;
    private JButton resetButton;

    public StocksGUI_StockPickingRobot(StocksGUI mainFrame, List<String> stockNumbers, Map<String, List<String>> revenue, Map<String, List<String>> foreign, Map<String, List<String>> trust, Map<String, List<String>> dealer, Map<String, List<String>> profitability){
        //創建選股機器人頁面視窗
        super("韭菜同學會_選股機器人");
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(540, 540);
        setLocation(mainFrame.getX(),mainFrame.getY());

        //-------------------------------------------initialize----------------------------------------
        this.revenue = revenue;
        this.foreign = foreign;
        this.trust = trust;
        this.dealer = dealer;
        this.profitability = profitability;
        this.originNumbers = stockNumbers;
        this.numbers = new ArrayList<String>(originNumbers);

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
        JCheckBox filter6CheckBox = new JCheckBox("股價在",null,false);
        JTextField filter6TextField1 = new JTextField("",5);
        JLabel filter6Label1 = new JLabel("到");
        JTextField filter6TextField2 = new JTextField("",5);
        JLabel filter6Label2 = new JLabel("之間");

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
        filter6.add(filter6TextField1);
        filter6.add(filter6Label1);
        filter6.add( filter6TextField2);
        filter6.add(filter6Label2 );

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
            public void actionPerformed(ActionEvent event) {
                if(filter1CheckBox.isSelected() && !filter1TextField.getText().equals("")) { //營收月增篩選
                    List<String> stockRevenue;
                    for (int i = numbers.size() - 1; i >= 0; i--) {
                        stockRevenue = revenue.get(numbers.get(i));
                        if (Double.parseDouble(stockRevenue.get(0)) < Double.parseDouble(filter1TextField.getText()))
                            numbers.remove(i);
                    }
                }
                if(filter2CheckBox.isSelected() && !filter2TextField.getText().equals("")) { //營收年增增篩選
                    List<String> stockRevenue;
                    for (int i = numbers.size() - 1; i >= 0; i--) {
                        stockRevenue = revenue.get(numbers.get(i));
                        try {       //可能沒有去年資料 直接pass
                            if (Double.parseDouble(stockRevenue.get(1)) < Double.parseDouble(filter2TextField.getText()))
                                numbers.remove(i);
                        }
                        catch(Exception e){
                            numbers.remove(i);
                        }
                    }
                }
                if(filter3CheckBox.isSelected()) { //毛利率增篩選
                    List<String> stockProfitability;
                    for (int i = numbers.size() - 1; i >= 0; i--) {
                        stockProfitability = profitability.get(numbers.get(i));
                        try {       //可能沒有去年資料 直接pass
                            if (Double.parseDouble(stockProfitability.get(0)) < Double.parseDouble(stockProfitability.get(1)))
                                numbers.remove(i);
                        }
                        catch(Exception e){
                            numbers.remove(i);
                        }
                    }
                }

                if(filter4CheckBox.isSelected()) { //毛利率增篩選
                    List<String> stockProfitability;
                    for (int i = numbers.size() - 1; i >= 0; i--) {
                        stockProfitability = profitability.get(numbers.get(i));
                        try {       //可能沒有去年資料 直接pass
                            if ((Double.parseDouble(stockProfitability.get(0)) < Double.parseDouble(stockProfitability.get(1))) || (Double.parseDouble(stockProfitability.get(2)) < Double.parseDouble(stockProfitability.get(3))) || (Double.parseDouble(stockProfitability.get(4)) < Double.parseDouble(stockProfitability.get(5))))
                                numbers.remove(i);
                        }
                        catch(Exception e){
                            numbers.remove(i);
                        }
                    }
                }

                if(filter5CheckBox.isSelected() && !filter5TextField.getText().equals("")) { //ROE篩選
                    List<String> stockProfitability;
                    for (int i = numbers.size() - 1; i >= 0; i--) {
                        stockProfitability = profitability.get(numbers.get(i));
                        try {       //可能沒有去年資料 直接pass
                            if (Double.parseDouble(stockProfitability.get(6)) < Double.parseDouble(filter5TextField.getText()))
                                numbers.remove(i);
                        }
                        catch(Exception e){
                            numbers.remove(i);
                        }
                    }
                }
                if(filter6CheckBox.isSelected() && !filter6TextField1.getText().equals("")&&!filter6TextField2.getText().equals("")) { //股價篩選
                    Calendar that= Calendar.getInstance();
                    priceVolumeHandler today=new priceVolumeHandler(numbers,that.getTime());
                    Map<String ,Double>priceMap=today.getDayPrice();
                    Double first=Double.parseDouble(filter6TextField1.getText());//使用者輸入沒有大小誰要擺前面的限制，所以要自己判斷
                    Double second=Double.parseDouble(filter6TextField2.getText());

                    Double high =Math.max(first,second);
                    Double low = Math.min(first,second);
                    for(int i=numbers.size()-1;i>=0;i--){
                        try {
                            Double price = priceMap.get(numbers.get(i));
                            if (price > high || price < low) {
                                numbers.remove(i);
                            }
                        }catch (NullPointerException e){
                            numbers.remove(i);
                        }
                    }
                }
                if(filter7CheckBox.isSelected() && !filter7TextField.getText().equals("")) {//本益比篩選
                    Calendar that= Calendar.getInstance();
                    priceVolumeHandler today=new priceVolumeHandler(numbers,that.getTime());
                    Map<String,Double>PERMap=today.getPER(that.getTime());
                    for(int i=numbers.size()-1;i>=0;i--){
                        try {
                            if (PERMap.get(numbers.get(i)) >= Double.parseDouble(filter7TextField.getText())) {
                                numbers.remove(i);
                            }
                        }catch (NullPointerException e){
                            numbers.remove(i);
                        }
                    }
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
                }if(filter11CheckBox.isSelected() && !filter11_1TextField.getText().equals("")&&!filter11_2TextField.getText().equals("")) { //N天內股價上升M%
                    Calendar that= Calendar.getInstance();
                    priceVolumeHandler today=new priceVolumeHandler(numbers,that.getTime());
                    if(Integer.parseInt( filter11_1TextField.getText())!=0){//輸入為0天
                        that.add(Calendar.DAY_OF_YEAR,-1*Integer.parseInt( filter11_1TextField.getText())+1);
                    }
                    priceVolumeHandler thatDay=new priceVolumeHandler(numbers,that.getTime());
                    Map<String ,Double>todayPrice=today.getDayPrice();
                    Map<String ,Double>thatDayPrice=thatDay.getDayPrice();
                    Double upDownRate=Double.parseDouble(filter11_2TextField.getText())/100;
                    for(int i=0;i<numbers.size();i++) {
                        if(numbers.get(i)=="1104"){
                            System.out.println("!1104-2");
                        }
                    }
                    for(int i=numbers.size()-1;i>=0;i--) {
                        try {
                            if(thatDayPrice.get(numbers.get(i))==0){
                                numbers.remove(i);
                                continue;
                            }
                           if (upDownRate > 0) {
                                if ((todayPrice.get(numbers.get(i)) - thatDayPrice.get(numbers.get(i))) / thatDayPrice.get(numbers.get(i)) < upDownRate) {
                                     numbers.remove(i);
                                }
                            } else {
                                if ((todayPrice.get(numbers.get(i)) - thatDayPrice.get(numbers.get(i))) / thatDayPrice.get(numbers.get(i)) > upDownRate) {
                                    numbers.remove(i);
                                }
                            }
                        }catch(NullPointerException e){
                            //System.out.println(numbers.get(i)+":NULL");
                            numbers.remove(i);
                        }
                    }
                }

                if(filter12CheckBox.isSelected() && !filter12TextField.getText().equals("")) {
                priceVolumeHandler priceVolumeHandler=new priceVolumeHandler(numbers,new Date());
                    Map<String,Long>todayVolume=priceVolumeHandler.getDayVolume();
                    Map<String,Long>nDaysVolume=priceVolumeHandler.getNDaysVolume(Integer.parseInt(filter12TextField.getText()));
                    for(int i=numbers.size()-1;i>=0;i--){
                        try {

                            if (todayVolume.get(numbers.get(i)) * Integer.parseInt(filter12TextField.getText()) < nDaysVolume.get(numbers.get(i))) {
                                numbers.remove(i);
                            }
                        }catch (NullPointerException e){
                            numbers.remove(i);
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
