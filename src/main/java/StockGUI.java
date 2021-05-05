import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

public class StockGUI extends JFrame{
    private JButton updateButton;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JTextArea textArea1;
    private JPanel JP;
    private JTextArea textArea2;

    public StockGUI(){
        super("Stock App");
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        setContentPane(JP);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100,100,500,300);


        setVisible(true);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Fundamental fund = null;

                try {
                    fund = new Fundamental();
                } catch (InstantiationException instantiationException) {
                    instantiationException.printStackTrace();
                } catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                } catch (ExecutionException executionException) {
                    executionException.printStackTrace();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }finally {
                    textArea1.setText("Fundmental更新完成");
                }


            }

        });


        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Stocks temp=new Stocks(textArea1.getText());
                temp.updateRealTime();
                textArea2.setText(temp.toString());
            }
        });
    }
    public  void setText1(String s){
        textArea1.setText(Integer.toString(Fundamental.getNow()));
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public void setData(Fundamental data) {
    }

    public void getData(Fundamental data) {
    }

    public boolean isModified(Fundamental data) {
        return false;
    }
}
