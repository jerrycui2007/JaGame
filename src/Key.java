import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * Key is the class that handles all keyboard inputs. It implements KeyListener to process
 * keyboard events and maintains the state of all keys. This class provides functionality
 * to track key presses and releases, and query the current state of any key.
 *
 * The class uses KeyEvent virtual key codes (KeyEvent.VK_*) to identify specific keys.
 * It generates events for key presses and releases that can be handled by the game loop.
 *
 * @author Jerry Cui
 */
public class Key implements KeyListener {
    private static HashMap<Integer, Boolean> pressedKeys = new HashMap<>();
    private static HashMap<Character, Boolean> pressedChars = new HashMap<>();

    /**
     * Updates the state of held keys and generates continuous events for them.
     * This should be called once per frame from the game loop.
     */
    public static void update() {
        for (Integer keyCode : pressedKeys.keySet()) {
            if (pressedKeys.get(keyCode)) {
                Events.Event event = new Events.Event(Locals.KEYDOWN);
                event.setAttribute("key", keyCode);
                event.setAttribute("char", (char) keyCode.intValue());
                Events.post(event);
            }
        }
    }

    /**
     * Handles key press events. When a key is pressed, it gets marked as pressed, and adds a KEYDOWN Event
     *
     * @param e The KeyEvent containing information about the key press, including the key code and character
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        char keyChar = e.getKeyChar();
        
        // Ignore system auto-repeat
        if (!pressedKeys.getOrDefault(keyCode, false)) {
            Events.Event event = new Events.Event(Locals.KEYDOWN);
            event.setAttribute("key", keyCode);
            event.setAttribute("char", keyChar);
            Events.post(event);
            
            pressedKeys.put(keyCode, true);
            pressedChars.put(keyChar, true);
        }
    }

    /**
     * Updates keys that are released so they are no longer held down
     *
     * @param e The KeyEvent containing information about the key release,
     *          including the key code and character
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        char keyChar = e.getKeyChar();

        pressedKeys.put(keyCode, false);
        pressedChars.put(keyChar, false);

        Events.Event event = new Events.Event(Locals.KEYUP);

        event.setAttribute("key", keyCode);
        event.setAttribute("char", keyChar);

        Events.post(event);
    }

    /**
     * Handles key typed events. This method is not used in the current implementation
     * as we handle all keyboard input through keyPressed and keyReleased instead.
     *
     * @param e The KeyEvent containing information about the key typed
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // Not used, we handle keyPressed and keyReleased instead
    }
}