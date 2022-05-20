package View.GraphicsComponents;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;

public class RoundedBorder implements Border{
    private int r;
    private Color c;
    public RoundedBorder (int r, Color c) {
        this.r = r;
        this.c = c;
    }
    public Insets getBorderInsets(Component c) {
        return new Insets(this.r+1, this.r+1, this.r+2, this.r);
    }
    public boolean isBorderOpaque() {
        return true;
    }
    public void paintBorder(Component c, Graphics g, int x, int y, 
    int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        BasicStroke bs = new BasicStroke(2);
        g2.setStroke(bs);
        g2.setPaint(this.c);
        g2.drawRoundRect(x, y, width-1, height-1, r, r);
    }
}
