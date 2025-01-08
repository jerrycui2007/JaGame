import java.util.ArrayList;

/**
 * Class to test out functionality
 */
public class Tester {
    public static void main(String[] args) {
        Display window = new Display("Title");
        window.setMode(800, 400);

        // Create and register input listeners
        Key keyListener = new Key();
        Mouse mouseListener = new Mouse();
        window.getDisplay().addKeyListener(keyListener);
        window.getDisplay().addMouseListener(mouseListener);
        window.getDisplay().addMouseMotionListener(mouseListener);

        // Main game loop
        while (true) {
            // System.out.println("Loop");
            // Process all events
            ArrayList<Events.Event> events = Events.get();
            for (Events.Event event : events) {
                if (event.getType() == Locals.KEYDOWN) {
                    int keyCode = (int) event.getAttribute("key");
                    char keyChar = (char) event.getAttribute("char");
                    System.out.println("Key pressed: " + keyChar + " (code: " + keyCode + ")");
                }
                else if (event.getType() == Locals.MOUSEBUTTONDOWN) {
                    int button = (int) event.getAttribute("button");
                    int x = (int) event.getAttribute("x");
                    int y = (int) event.getAttribute("y");
                    String buttonType = button == 0 ? "LEFT" : button == 1 ? "MIDDLE" : "RIGHT";
                    System.out.println("Mouse " + buttonType + " clicked at coordinates: (" + x + ", " + y + ")");
                }
            }

            window.update();
        }
    }
}
