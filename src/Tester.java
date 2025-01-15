import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
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
        
        // Create sprite once, outside the game loop
        Sprite s = new Sprite(5,5,"images/classic_menu.png");
        /*
        try {
            File imageFile = new File("images/classic_menu.png");
            System.out.println("Attempting to load image from: " + imageFile.getAbsolutePath());
            if (!imageFile.exists()) {
                System.out.println("Image file does not exist!");
            }
            s = new Sprite(5, 5, "images/classic_menu.png");
            window.addSprite(s);
        } catch (Exception e) {
            System.out.println("Error loading sprite: " + e.getMessage());
            e.printStackTrace();
        }
         */


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
            window.addSprite(s);

            window.update();
            clock.tick(FPS);
        }
    }
}
