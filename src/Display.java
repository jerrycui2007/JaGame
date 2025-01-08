import javax.swing.*;
import java.awt.*;

/**
 * Display is the class that creates the display for the user.
 *
 * @author Sam Ghasemzadeh
 */
public class Display {
    // the Display contains a JFrame
    private JFrame display = new JFrame();

    // creating a panel
    private JPanel panel = new JPanel();



    /**
     * Set mode is for setting the size of the display.
     * @param screenWidth the width of the display.
     * @param screenHeight the hight of the display.
     */
    public void setMode(int screenWidth, int screenHeight){
        display.setSize(screenWidth, screenHeight);
    }

    /**
     * The constructor initializes a display object.
     */
    public Display(){
        //setting the close operation on exit button
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //adding the panel to the frame
        display.add(panel);
        //making frame visible
        display.setVisible(true);
    }

    /**
     *
     * @param title
     */
    public Display(String title){
        display = new JFrame(title);
        //setting the close operation on exit button
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //adding the panel to the frame
        display.add(panel);
        //making frame visible
        display.setVisible(true);
    }

    /**
     * This method updates the display.
     */
    public void update(){
        panel.repaint();
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
