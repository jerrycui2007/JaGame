
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * Key is the class that handles all keyboard inputs
 *
 * @author Jerry Cui
 */
public class Key implements KeyListener {
    private static HashMap<Integer, Boolean> pressedKeys = new HashMap<>();

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pressedKeys.put(keyCode, true);
        Events.Event event = new Events.Event(Locals.KEYDOWN);
        event.setAttribute("key", keyCode);
        event.setAttribute("char", e.getKeyChar());
        Events.post(event);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pressedKeys.put(keyCode, false);
        Events.Event event = new Events.Event(Locals.KEYUP);
        event.setAttribute("key", keyCode);
        event.setAttribute("char", e.getKeyChar());
        Events.post(event);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used, we handle keyPressed and keyReleased instead
    }

    /**
     * Check if a key is currently pressed
     * @param keyCode The key code to check (use KeyEvent.VK_* constants)
     * @return true if the key is pressed, false otherwise
     */
    public static boolean isKeyPressed(int keyCode) {
        return pressedKeys.getOrDefault(keyCode, false);
    }

    /**
     * Clear all pressed keys
     */
    public static void clearKeys() {
        pressedKeys.clear();
    }
}