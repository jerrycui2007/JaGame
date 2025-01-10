import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

/**
 *
 *  @author Sam Ghasemzadeh
 */

public class DrawPanel extends JPanel {


    private static class ShapeInfo {
        // variables
        private Shape shape;
        private int x;
        private int y;
        private String text;
        private Color color;

        public ShapeInfo(Shape shape, Color color) {
            this.shape = shape;
            this.color = color;
        }

        public ShapeInfo(String text, int x, int y, Color color) {
            this.text = text;
            this.color = color;
            this.x = x;
            this.y = y;
        }

        //setters
        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public void setShape(Shape shape) {
            this.shape = shape;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        //getters
        public Shape getShape() {
            return shape;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String getText() {
            return text;
        }

        public Color getColor() {
            return color;
        }

    }


    private ArrayList<ShapeInfo> items = new ArrayList<ShapeInfo>();

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

        for (ShapeInfo si : items) {
            g2d.setColor(si.getColor());
            if (si.text != null) {
                g2d.drawString(si.getText(), si.x, si.y);
            } else {
                g2d.fill(si.getShape());
            }
        }

    }

    /**
     * Adds a rectangle to the list of items to be drawn.
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param c the color of the rectangle
     */
    public void addRectangle(int x, int y, int width, int height, Color c) {
        items.add(new ShapeInfo(new Rectangle(x, y, width, height), c));
        repaint();
    }

    /**
     * Adds a circle to the list of items to be drawn.
     *
     * @param centerX the x-coordinate of the circle's center
     * @param centerY the y-coordinate of the circle's center
     * @param radius  the radius of the circle
     * @param c the color of the circle
     */
    public void addCircle(int centerX, int centerY, int radius, Color c) {
        // https://docs.oracle.com/javase/8/docs/api/java/awt/geom/Ellipse2D.html
        Shape circle = new Ellipse2D.Double(centerX - radius,
                centerY - radius,
                radius * 2,
                radius * 2);
        items.add(new ShapeInfo(circle, c));
        repaint();
    }



    /**
     * Clears all shapes and text from the panel.
     */
    public void clear(){
        items.clear();
        repaint();
    }
}
