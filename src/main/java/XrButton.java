import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class XrButton extends JButton implements MouseListener {

    public static Color BUTTON_COLOR2 ;
    public static Color BUTTON_COLOR1 ;
    String text;
    boolean mouseIn = false;
    boolean click = false;
    boolean origin ;///代表是不是跟原本的一樣

    public XrButton(String s, boolean origin) {
        super(s);
        this.origin = origin;
        System.out.println(origin);
        if (origin) {
           BUTTON_COLOR2 = new Color(186, 208, 230);
           BUTTON_COLOR1 = Color.WHITE;
        }else{
              BUTTON_COLOR2 = new Color(51, 154, 47);
              BUTTON_COLOR1 = new Color(205, 255, 205);
        }
            text = s;
            setBorderPainted(false);
            addMouseListener(this);
            setContentAreaFilled(false);

    }

    public XrButton(String s) {
        this(s, true);
    }

    @Override
    public void paintComponent(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint p1, p2;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));


        float[] hsb = new float[3];
        System.out.println(BUTTON_COLOR1.toString());
        System.out.println(BUTTON_COLOR2.toString());
        if (origin) {
            BUTTON_COLOR2 = new Color(186, 208, 230);
            BUTTON_COLOR1 = Color.WHITE;
        }else{
            BUTTON_COLOR2 = new Color(51, 154, 47);
            BUTTON_COLOR1 = new Color(205, 255, 205);
        }
        GradientPaint gp = new GradientPaint(0.0F, 0.0F, BUTTON_COLOR1, 0.0F, h, BUTTON_COLOR2, true);
        g2.setPaint(gp);

        if (getModel().isPressed()) {

            p1 = new GradientPaint(0, 0, new Color(0, 0, 0), 0, h - 1, new Color(100, 100, 100));
            p2 = new GradientPaint(0, 1, new Color(0, 0, 0, 50), 0, h - 3, new Color(255, 255, 255, 100));
        } else {
            p1 = new GradientPaint(0, 0, new Color(100, 100, 100), 0, h - 1, new Color(0, 0, 0));
            p2 = new GradientPaint(0, 1, new Color(255, 255, 255, 100), 0, h - 3, new Color(0, 0, 0, 50));
        }

        //g2.drawRoundRect(0,0,getWidth()-5,getHeight()-5,20,20);
        RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0,
                w - 1, h - 1, 20, 20);
        Shape clip = g2.getClip();
        g2.clip(r2d);
        g2.fillRect(0, 0, w, h);
        g2.setClip(clip);
        g2.setPaint(p1);
        g2.drawRoundRect(0, 0, w - 1, h - 1, 20, 20);
        g2.setPaint(p2);
        g2.drawRoundRect(1, 1, w - 3, h - 3, 18, 18);

        g2.dispose();
        super.paintComponent(g);
    }





    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());

        XrButton xrButton = new XrButton("XrBRRRRRRRRutton",false);

        XrButton OriButton = new XrButton("JButton",true);

        frame.getContentPane().add(xrButton);

        frame.getContentPane().add(OriButton);

        frame.setSize(150, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {
        mouseIn = true;
    }

    public void mouseExited(MouseEvent e) {
        mouseIn = false;
    }

    public void mousePressed(MouseEvent e) {
        click = true;
    }

    public void mouseReleased(MouseEvent e) {
        click = false;
    }

}