import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseEvent;

/**
 * Mouse is the class that handles all inputs from the mouse. It implements MouseListener,
 * MouseMotionListener, and MouseWheelListener to capture all mouse-related events including 
 * clicks, movement, and wheel scrolling.
 *
 * @author Jerry Cui
 */
public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {
    private static int mouseX = 0;
    private static int mouseY = 0;
    private static boolean[] mouseButtons = new boolean[3];  // Left, Middle, Right buttons

    /**
     * Handles mouse button press events. When a mouse button is pressed, this method updates
     * the button state and creates a MOUSEBUTTONDOWN event with the button index and coordinates.
     *
     * @param e The MouseEvent containing information about the button press
     */
    @Override
    public void mousePressed(MouseEvent e) {
        int button = e.getButton() - 1;  // Convert to 0-based index
        if (button >= 0 && button < mouseButtons.length) {
            mouseButtons[button] = true;
            Events.Event event = new Events.Event(Locals.MOUSEBUTTONDOWN);
            event.setAttribute("button", button);
            event.setAttribute("x", e.getX());
            event.setAttribute("y", e.getY());
            Events.post(event);
        }
    }

    /**
     * Handles mouse button release events. When a mouse button is released, this method updates
     * the button state and creates a MOUSEBUTTONUP event with the button index and coordinates.
     *
     * @param e The MouseEvent containing information about the button release
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        int button = e.getButton() - 1;
        if (button >= 0 && button < mouseButtons.length) {
            mouseButtons[button] = false;
            Events.Event event = new Events.Event(Locals.MOUSEBUTTONUP);
            event.setAttribute("button", button);
            event.setAttribute("x", e.getX());
            event.setAttribute("y", e.getY());
            Events.post(event);
        }
    }

    /**
     * Handles mouse movement events. Updates the stored mouse coordinates and creates
     * a MOUSEMOTION event with the new coordinates.
     *
     * @param e The MouseEvent containing information about the mouse movement
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        Events.Event event = new Events.Event(Locals.MOUSEMOTION);
        event.setAttribute("x", mouseX);
        event.setAttribute("y", mouseY);
        Events.post(event);
    }

    /**
     * Handles mouse drag events.
     *
     * @param e The MouseEvent containing information about the mouse drag
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);  // Handle the same way as mouseMoved
    }

    /**
     * Required by the MouseListener interface but not used in this implementation.
     *
     * @param e The MouseEvent containing information about the mouse click
     */
    @Override
    public void mouseClicked(MouseEvent e) {}

    /**
     * Required by the MouseListener interface but not used in this implementation.
     *
     * @param e The MouseEvent containing information about the mouse entering the component
     */
    @Override
    public void mouseEntered(MouseEvent e) {}

    /**
     * Required by the MouseListener interface but not used in this implementation.
     *
     * @param e The MouseEvent containing information about the mouse exiting the component
     */
    @Override
    public void mouseExited(MouseEvent e) {}

    /**
     * Handles mouse wheel events. When the mouse wheel is scrolled, this method creates
     * a MOUSEWHEEL event with the scroll amount.
     *
     * @param e The MouseWheelEvent containing information about the wheel movement
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        Events.Event event = new Events.Event(Locals.MOUSEWHEEL);
        // getWheelRotation() returns positive for scroll down, negative for scroll up
        event.setAttribute("scroll", e.getWheelRotation());
        Events.post(event);
    }

    /**
     * Checks if a specific mouse button is currently pressed.
     *
     * @param button The button index to check (0 for left, 1 for middle, 2 for right)
     * @return true if the specified button is currently pressed, false otherwise
     */
    public static boolean isButtonPressed(int button) {
        return button >= 0 && button < mouseButtons.length && mouseButtons[button];
    }

    /**
     * Gets the current X coordinate of the mouse cursor.
     *
     * @return The current X coordinate of the mouse cursor
     */
    public static int getX() {
        return mouseX;
    }

    /**
     * Gets the current Y coordinate of the mouse cursor.
     *
     * @return The current Y coordinate of the mouse cursor
     */
    public static int getY() {
        return mouseY;
    }
}