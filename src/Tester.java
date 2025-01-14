import java.awt.*;
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
        window.getDisplay().addKeyListener(keyListener);

        Mouse mouseListener = new Mouse();
        window.getDisplay().addMouseListener(mouseListener);
        window.getDisplay().addMouseMotionListener(mouseListener);
        window.getDisplay().addMouseWheelListener(mouseListener);

        Mixer.Sound sound = new Mixer.Sound("menu_music.mp3");

        Time clock = new Time();

        int x = 50;
        int y = 50;

        // Main game loop

        final int FPS = 60;
        while (true) {
            // Process all events
            ArrayList<Events.Event> events = Events.get();

            for (Events.Event event : events) {
                if (event.getType() == Locals.KEYDOWN) {
                    int keyCode = (int) event.getAttribute("key");
                    char keyChar = (char) event.getAttribute("char");

                    System.out.println(event.getAttribute("key"));
                    System.out.println(event.getAttribute("char"));

                    System.out.println("Key pressed: " + keyChar + " (code: " + keyCode + ")");

                    if (keyChar == 'w') {
                        y -= 5;
                    }
                    if (keyChar == 'a') {
                        x -= 5;
                    }
                    if (keyChar == 's') {
                        y += 5;
                    }
                    if (keyChar == 'd') {
                        x += 5;
                    }
                }
                else if (event.getType() == Locals.MOUSEBUTTONDOWN) {
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
            window.drawRectangle(x, y, 100, 200, Color.RED, true);

            window.update();
            clock.tick(FPS);
        }
    }
}
