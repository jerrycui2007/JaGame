import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

/**
 * Mouse is the class that handles all inputs from the mouse
 *
 * @author Jerry Cui
 */
public class Mouse implements MouseListener, MouseMotionListener {
    private static int mouseX = 0;
    private static int mouseY = 0;
    private static boolean[] mouseButtons = new boolean[3];  // Left, Middle, Right buttons

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

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        Events.Event event = new Events.Event(Locals.MOUSEMOTION);
        event.setAttribute("x", mouseX);
        event.setAttribute("y", mouseY);
        Events.post(event);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);  // Handle the same way as mouseMoved
    }

    // Required by interface but not used
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public static boolean isButtonPressed(int button) {
        return button >= 0 && button < mouseButtons.length && mouseButtons[button];
    }

    // Getter methods
    public static int getX() {
        return mouseX;
    }

    public static int getY() {
        return mouseY;
    }
}