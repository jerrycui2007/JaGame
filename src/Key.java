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

    /**
     * Handles key press events. When a key is pressed, this method:
     * 1. Updates the internal key state to mark the key as pressed
     * 2. Creates and posts a KEYDOWN event with the key code and character
     *
     * @param e The KeyEvent containing information about the key press,
     *          including the key code and character
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pressedKeys.put(keyCode, true);
        Events.Event event = new Events.Event(Locals.KEYDOWN);
        event.setAttribute("key", keyCode);
        event.setAttribute("char", e.getKeyChar());
        Events.post(event);
    }

    /**
     * Handles key release events. When a key is released, this method:
     * 1. Updates the internal key state to mark the key as released
     * 2. Creates and posts a KEYUP event with the key code and character
     *
     * @param e The KeyEvent containing information about the key release,
     *          including the key code and character
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pressedKeys.put(keyCode, false);
        Events.Event event = new Events.Event(Locals.KEYUP);
        event.setAttribute("key", keyCode);
        event.setAttribute("char", e.getKeyChar());
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

    /**
     * Checks if a specific key is currently pressed.
     * 
     * @param keyCode The key code to check (use KeyEvent.VK_* constants)
     *                For example: KeyEvent.VK_SPACE for space bar,
     *                KeyEvent.VK_A for 'A' key, etc.
     * @return true if the specified key is currently pressed, false otherwise
     */
    public static boolean isKeyPressed(int keyCode) {
        return pressedKeys.getOrDefault(keyCode, false);
    }

    /**
     * Clears the state of all pressed keys. This is useful when:
     * - The game window loses focus
     * - Switching between game states
     * - Resetting the input state
     * This ensures no keys are falsely registered as being held down.
     */
    public static void clearKeys() {
        pressedKeys.clear();
    }
}