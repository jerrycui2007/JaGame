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
    public void drawRectangle(int x, int y, int width, int height, Color c){
        drawPanel.addRectangle(x,y,width,height,c);
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
