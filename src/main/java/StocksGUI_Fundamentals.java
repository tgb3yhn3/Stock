import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StocksGUI_Fundamentals extends JFrame{

    public StocksGUI_Fundamentals(StocksGUI_SearchForListedStocks fatherFrame) {
        //創建到價通知頁面視窗
        super("韭菜同學會_基本面");
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int windowWidth = 300;//設定視窗寬度
        int windowHeight = 200;//設定視窗高度
        setSize(windowWidth, windowHeight);
        setLocation(fatherFrame.getX(), fatherFrame.getY());//此視窗出現的位置將在主頁面的位置

        //創建視窗內的各個GUI子元件
        JLabel monthlyRevenueLabel  = new JLabel("月營收:");
        JLabel tripleRateLabel      = new JLabel("三率:");
        JLabel ROELabel             = new JLabel("ROE:");
        JLabel EPSLabel             = new JLabel("EPS:");
        JLabel PELabel              = new JLabel("PE:");
        JLabel monthlyIncreaseLabel = new JLabel("月增:");
        JLabel annualIncreaseLabel  = new JLabel("年增:");

        JTextField monthlyRevenueTextField  = new JTextField("",5);
        JTextField tripleRateTextField      = new JTextField("",5);
        JTextField ROETextField             = new JTextField("",5);
        JTextField EPSTextField             = new JTextField("",5);
        JTextField PETextField              = new JTextField("",5);
        JTextField monthlyIncreaseTextField = new JTextField("",5);
        JTextField annualIncreaseTextField  = new JTextField("",5);

        //為視窗新增GUI子元件，並設定網格約束
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.BOTH;
        g.insets = new Insets(10,10,10,10);//調整間距
        g.gridx = 0;
        g.gridy = 0;
        add(monthlyRevenueLabel,g);
        g.gridx = 1;
        g.gridy = 0;
        add(monthlyRevenueTextField,g);
        g.gridx = 0;
        g.gridy = 1;
        add(tripleRateLabel,g);
        g.gridx = 1;
        g.gridy = 1;
        add(tripleRateTextField,g);
        g.gridx = 0;
        g.gridy = 2;
        add(ROELabel,g);
        g.gridx = 1;
        g.gridy = 2;
        add(ROETextField,g);
        g.gridx = 0;
        g.gridy = 3;
        add(EPSLabel,g);
        g.gridx = 1;
        g.gridy = 3;
        add(EPSTextField,g);
        g.gridx = 3;
        g.gridy = 0;
        add(PELabel,g);
        g.gridx = 4;
        g.gridy = 0;
        add(PETextField,g);
        g.gridx = 3;
        g.gridy = 1;
        add(monthlyIncreaseLabel,g);
        g.gridx = 4;
        g.gridy = 1;
        add(monthlyIncreaseTextField,g);
        g.gridx = 3;
        g.gridy = 2;
        add(annualIncreaseLabel,g);
        g.gridx = 4;
        g.gridy = 2;
        add(annualIncreaseTextField,g);

        setVisible(true);
    }
}
