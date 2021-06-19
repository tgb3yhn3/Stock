import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class XrButton extends JButton implements MouseListener {


    String text;
    boolean mouseIn = false;
    boolean click = false;
    boolean origin= true;///代表是不是跟原本的一樣
    public XrButton(String s,boolean origin) {
        super(s);
        if(!origin) {
            text = s;
            setBorderPainted(true);
            addMouseListener(this);
            setContentAreaFilled(false);
            this.origin = origin;
        }
        this.setBorder(new RoundedBorder(10));
    }
    public XrButton(String s) {
        this(s,true);
    }

    @Override
    public void paintComponent(Graphics g) {
        if(!origin) {
            float[] hsb = new float[3];

            Graphics2D g2 = (Graphics2D) g.create();

            if (!click) {
                Color.RGBtoHSB(102, 204, 255, hsb);
                Color tmp = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
                g2.setPaint(new GradientPaint(new Point(0, 0), getBackground(), new Point(0, getHeight() / 3), getBackground()));
                g2.setPaint(new GradientPaint(new Point(0, getHeight() / 3), tmp, new Point(0, getHeight() * 2 / 3), tmp));
                g2.setPaint(new GradientPaint(new Point(0, getHeight() * 2 / 3), tmp, new Point(0, getHeight()), getBackground()));
            } else {

                g2.setPaint(new GradientPaint(
                        new Point(0, 0),
                        Color.gray,
                        new Point(0, getHeight()),
                        Color.white));
            }
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.dispose();


        }
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

    private static class RoundedBorder implements Border {

        private int radius;

        private int h;
        private int l;
        RoundedBorder(int radius) {
            this.radius = radius;
        }


        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }


        public boolean isBorderOpaque() {
            return true;
        }


        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}