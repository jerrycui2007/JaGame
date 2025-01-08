import javax.swing.*;
import java.awt.*;



public class DrawPanel extends JPanel {

    /**
     * Overrides the panel's paintComponent to draw shapes and text.
     *
     * @param g the Graphics object used for drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //https://docs.oracle.com/javase/tutorial/2d/advanced/quality.html
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
        /*
        for (ShapeInfo si : items) {
            g2d.setColor(si.color);
            if (si.text != null) {
                g2d.drawString(si.text, si.x, si.y);
            } else {
                g2d.fill(si.shape);
            }
        }
         */
    }

    public void clear(){

    }
}
