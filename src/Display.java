import javax.swing.*;
import java.awt.*;

/**
 * Display is the class is responsible for creating and managing
 * a graphical user interface (GUI) window for an application.
 *
 * @author Sam Ghasemzadeh
 */
public class Display {
    // the Display contains a JFrame
    /*
     * The main JFrame object that serves as the application's main window.
     */
    private JFrame display = new JFrame();

    // creating a DrawPanel
    /*
     * A JPanel(DrawPanel) that is added to the JFrame.
     * It can be used to draw or add other components.
     */
    private DrawPanel drawPanel = new DrawPanel();

    /**
     * The constructor initializes a display object.
     * Constructs a default Display object with no title.
     * Closes the application when the user clicks the close button.
     * Displays a new JPanel.
     * Is set to be visible.
     */
    public Display(){
        //setting the close operation on exit button
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //adding the panel to the frame
        display.add(drawPanel);
        display.pack();
        //making JFrame visible
        display.setVisible(true);
    }

    /**
     * The constructor initializes a display object with a specific title.
     * Closes the application when the user clicks the close button.
     * Displays a new JPanel.
     * Is set to be visible.
     * @param title the title to be displayed in the window's title bar.
     */
    public Display(String title){
        display = new JFrame(title);
        //setting the close operation on exit button
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //adding the panel to the frame
        display.add(drawPanel);
        //making frame visible
        display.setVisible(true);
    }

    /**
     * Set mode is for setting the size (width and height) of the display.
     * @param screenWidth the width of the display.
     * @param screenHeight the hight of the display.
     */
    public void setMode(int screenWidth, int screenHeight){
        // set the size of the display (JFrame) to the specified width and height
        display.setSize(screenWidth, screenHeight);
    }

    /**
     * Repaints the inside the window, which can be used to
     * refresh the display after graphical changes
     * */
    public void update(){
        // Repaint the panel to reflect any changes in the UI
        drawPanel.repaint();
    }

    /**
     * Draws a rectangle on the display.
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param c the color of the rectangle
     */
    public void drawRectangle(int x, int y, int width, int height, Color c, boolean halo){
        drawPanel.addRectangle(x,y,width,height,c, halo);
    }

    /**
     * Adds a rectangle to the list of items to be drawn.
     * @param rect this array will contain the following in order: the x-coordinate, the y-coordinate, the width of the rectangle, the color of the rectangle
     * @param c the color of the rectangle
     * @param halo if true then the rectangle will be drawn halo without being filled in.
     */
    public void drawRectangle(int[] rect, Color c, boolean halo){
        drawPanel.addRectangle(rect[0], rect[1], rect[2], rect[3], c, halo);
    }

    /**
     * Draws a circle on the display.
     */
    public void drawCircle(int centerX, int centerY, int radius, Color c, boolean halo) {
        drawPanel.addCircle(centerX, centerY, radius, c , halo);
    }

    /**
     * Draws text on the display.
     * @param text the text to add
     * @param x    the x-coordinate for the text
     * @param y    the y-coordinate for the text
     * @param c the color of the text
     */
    public void drawText(String text, int x, int y, Color c) {
        drawPanel.addText(text, x, y, c);
    }

    /**
     * Clears all shapes and text from the panel.
     */
    public void clear() {
        drawPanel.clear();
    }


    /**
     * Fill the window with a specific color
     *
     * @param color color to fill with
     */
    public void fill(Color color) {
        this.drawRectangle(0, 0, this.display.getWidth(), this.display.getHeight(), color, false);
    }

    /**
     * Adds a Sprite to the panel to be drawn.
     *
     * @param sprite the sprite to add
     */
    public void addSprite(Sprite sprite) {
        drawPanel.addSprite(sprite);
    }



    //getters
    public JFrame getDisplay() {
        return display;
    }
    //setters
    public void setDisplay(JFrame display) {
        this.display = display;
    }
}