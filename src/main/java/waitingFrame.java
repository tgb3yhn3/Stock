import javax.swing.*;

public class waitingFrame {
    public static JFrame  frame;
    public waitingFrame(){
        frame = new JFrame("loading");

        ImageIcon loading = new ImageIcon("waiting.gif");
        frame.add(new JLabel("loading... ",loading, JLabel.CENTER));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }
    public static void show(){
        frame = new JFrame("loading");

        ImageIcon loading = new ImageIcon("waiting.gif");
        frame.add(new JLabel("loading... ",loading, JLabel.CENTER));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }
    public static void stop(){
        frame.setVisible(false);
    }
    public static void start(){frame.setVisible(true);}

    public static void main(String[] args) {
        show();
    }
}
