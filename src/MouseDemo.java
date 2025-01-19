import java.awt.*;
import java.util.ArrayList;

/**
 * Class to test out functionality
 */
public class MouseDemo {
    public static void main(String[] args) {
        Display window = new Display("Mouse Demo");
        window.setMode(800, 400);

        // Create and register input listeners
        Mouse mouseListener = new Mouse();
        window.getDisplay().addMouseListener(mouseListener);
        window.getDisplay().addMouseMotionListener(mouseListener);
        window.getDisplay().addMouseWheelListener(mouseListener);

        Time clock = new Time();

        // Main game loop
        final int FPS = 60;
        while (true) {
            // Process all events
            ArrayList<Events.Event> events = Events.get();

            for (Events.Event event : events) {

               if (event.getType() == Locals.MOUSEBUTTONDOWN) {
                    int button = (int) event.getAttribute("button");
                    int mouseX = (int) event.getAttribute("x");
                    int mouseY = (int) event.getAttribute("y");
                    System.out.println("Mouse " + button + " clicked at coordinates: (" + mouseX + ", " + mouseY + ")");
                }
                else if (event.getType() == Locals.MOUSEWHEEL) {
                    int scroll = (int) event.getAttribute("scroll");
                    System.out.println("Mouse wheel scrolled " + scroll);
                }
            }
            for (int button = 0; button < 3; button++) {
                if (Mouse.isButtonPressed(button)) {
                    System.out.println(button + " mouse button is being held at coordinates: (" +
                        Mouse.getX() + ", " + Mouse.getY() + ")");
                }
            }

            window.clear();
            window.update();
            clock.tick(FPS);
        }
    }
}
