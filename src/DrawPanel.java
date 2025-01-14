import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

/**
 * Draw panel is where the drawing into the display is created.
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
        private boolean halo;

        public ShapeInfo(Shape shape, Color color, boolean halo) {
            this.shape = shape;
            this.color = color;
            this.halo = halo;
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

        public boolean getHalo() {
            return halo;
        }

    }
    // keeping track of shapes, plus Sprites
    private ArrayList<Sprite> sprites = new ArrayList<Sprite>();

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
        // using Graphics2D to draw
        Graphics2D g2d = (Graphics2D) g;
        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);

        //looping through all the items and drawing them
        for (ShapeInfo si : items) {
            g2d.setColor(si.getColor());
            if (si.getText() != null) {
                //if a sting draw string
                g2d.drawString(si.getText(), si.x, si.y);
            } else if(si.getHalo()){
                // if the shape is halo draw the boders only
                g2d.draw(si.getShape());
            } else{
                // otherwise fill in the shape
                g2d.fill(si.getShape());
            }
        }

        //looping through all the sprites and drawing them
        for (Sprite s : sprites) {
            s.draw(g);
        }

    }

    /**
     * Adds a rectangle to the list of items to be drawn.
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param c the color of the rectangle
     * @param halo if true then the rectangle will be drawn halo without being filled in.
     */
    public void addRectangle(int x, int y, int width, int height, Color c, boolean halo) {
        items.add(new ShapeInfo(new Rectangle(x, y, width, height), c, halo));
        repaint();
    }

    /**
     * Adds a rectangle to the list of items to be drawn.
     * @param dimensions this array will contain the following in order: the x-coordinate, the y-coordinate, the width of the rectangle, the color of the rectangle
     * @param c the color of the rectangle
     * @param halo if true then the rectangle will be drawn halo without being filled in.
     */
    public void addRectangle(int[] dimensions, Color c, boolean halo){
        items.add(new ShapeInfo(new Rectangle(dimensions[0], dimensions[1], dimensions[2], dimensions[3]), c, halo));
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
    public void addCircle(int centerX, int centerY, int radius, Color c, boolean halo) {
        // https://docs.oracle.com/javase/8/docs/api/java/awt/geom/Ellipse2D.html
        Shape circle = new Ellipse2D.Double(centerX - radius,
                centerY - radius,
                radius * 2,
                radius * 2);
        items.add(new ShapeInfo(circle, c, halo));
        repaint();
    }

    /**
     * Adds text to the list of items to be drawn.
     *
     * @param text the text to add
     * @param x    the x-coordinate for the text
     * @param y    the y-coordinate for the text
     * @param c the color of the text
     */
    public void addText(String text, int x, int y, Color c) {
        items.add(new ShapeInfo(text, x, y, c));
        repaint();
    }

    /* -----------------------
     * Methods for sprites
     * ----------------------- */

    /**
     * Adding a sprite(image).
     * @param sprite the sprite added.2
     */
    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
        repaint();
    }



    /**
     * Clears all shapes and text and sprites from the panel.
     */
    public void clear(){
        items.clear();
        sprites.clear();
        repaint();
    }
}