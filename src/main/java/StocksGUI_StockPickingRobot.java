import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StocksGUI_StockPickingRobot extends JFrame{

    public StocksGUI_StockPickingRobot(StocksGUI mainFrame){
        //創建選股機器人頁面視窗
        super("韭菜同學會_選股機器人");
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 475);
        setLocation(mainFrame.getX(),mainFrame.getY());

        //創建視窗內的各個GUI子元件_篩選區塊
        JPanel filterPanel = new JPanel(new GridLayout(12,1,0,0));
        filterPanel.setBorder(BorderFactory.createTitledBorder("請設定篩選條件"));

        JPanel filter1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter1CheckBox = new JCheckBox("營收月增",null,false);
        JTextField filter1TextField = new JTextField("",3);
        JLabel filter1Label = new JLabel("%");

        JPanel filter2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter2CheckBox = new JCheckBox("營收年增",null,false);
        JTextField filter2TextField = new JTextField("",3);
        JLabel filter2Label = new JLabel("%");

        JPanel filter3Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter3CheckBox = new JCheckBox("毛利率增",null,false);

        JPanel filter4Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter4CheckBox = new JCheckBox("三利三升",null,false);

        JPanel filter5Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter5CheckBox = new JCheckBox("ROE大於",null,false);
        JTextField filter5TextField = new JTextField("",3);

        JPanel filter6Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter6CheckBox = new JCheckBox("EPS大於",null,false);
        JTextField filter6TextField = new JTextField("",3);

        JPanel filter7Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter7CheckBox = new JCheckBox("本益比大於",null,false);
        JTextField filter7TextField = new JTextField("",3);

        JPanel filter8Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter8CheckBox = new JCheckBox("小資連買",null,false);
        JTextField filter8TextField = new JTextField("",3);
        JLabel filter8Label = new JLabel("天");

        JPanel filter9Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter9CheckBox = new JCheckBox("投信連買",null,false);
        JTextField filter9TextField = new JTextField("",3);
        JLabel filter9Label = new JLabel("天");

        JPanel filter10Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter10CheckBox = new JCheckBox("",null,false);
        JTextField filter10_1TextField = new JTextField("",3);
        JLabel filter10_1Label = new JLabel("天內股價上升");
        JTextField filter10_2TextField = new JTextField("",3);
        JLabel filter10_2Label = new JLabel("%");

        JPanel filter11Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox filter11CheckBox = new JCheckBox("成交量大於",null,false);
        JTextField filter11TextField = new JTextField("",3);
        JLabel filter11Label = new JLabel("天均量");

        JButton searchResultsButton = new JButton("搜尋");

        //創建視窗內的各個GUI子元件_結果區塊
        JPanel ResultsPanel = new JPanel();
        ResultsPanel.setBorder(BorderFactory.createTitledBorder("搜尋結果"));

        String[] temp = {"TEST1","TEST2","TEST3","TEST4","TEST5","TEST6","TEST7","TEST8","TEST9"};//TEST!!
        JList Results = new JList<String>(temp);
        Results.setFixedCellWidth(200);
        Results.setFixedCellHeight(50);
        Results.setVisibleRowCount(5); //一次只顯示五條資料
        Results.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//一次只能選一個


        //為每個JPanel新增GUI子元件
        filter1Panel.add(filter1CheckBox);
        filter1Panel.add(filter1TextField);
        filter1Panel.add(filter1Label);

        filter2Panel.add(filter2CheckBox);
        filter2Panel.add(filter2TextField);
        filter2Panel.add(filter2Label);

        filter3Panel.add(filter3CheckBox);

        filter4Panel.add(filter4CheckBox);

        filter5Panel.add(filter5CheckBox);
        filter5Panel.add(filter5TextField);

        filter6Panel.add(filter6CheckBox);
        filter6Panel.add(filter6TextField);

        filter7Panel.add(filter7CheckBox);
        filter7Panel.add(filter7TextField);

        filter8Panel.add(filter8CheckBox);
        filter8Panel.add(filter8TextField);
        filter8Panel.add(filter8Label);

        filter9Panel.add(filter9CheckBox);
        filter9Panel.add(filter9TextField);
        filter9Panel.add(filter9Label);

        filter10Panel.add(filter10CheckBox);
        filter10Panel.add(filter10_1TextField);
        filter10Panel.add(filter10_1Label);
        filter10Panel.add(filter10_2TextField);
        filter10Panel.add(filter10_2Label);

        filter11Panel.add(filter11CheckBox);
        filter11Panel.add(filter11TextField);
        filter11Panel.add(filter11Label);

        filterPanel.add(filter1Panel);
        filterPanel.add(filter2Panel);
        filterPanel.add(filter3Panel);
        filterPanel.add(filter4Panel);
        filterPanel.add(filter5Panel);
        filterPanel.add(filter6Panel);
        filterPanel.add(filter7Panel);
        filterPanel.add(filter8Panel);
        filterPanel.add(filter9Panel);
        filterPanel.add(filter10Panel);
        filterPanel.add(filter11Panel);
        filterPanel.add(searchResultsButton);

        ResultsPanel.add(new JScrollPane(Results));

        //為視窗新增GUI子元件
        add(filterPanel);
        add(ResultsPanel);



        setVisible(true);
    }
}
