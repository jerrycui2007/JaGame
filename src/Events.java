import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * System that uses a queue to store al events
 *
 * @author Jerry Cui
 */
public class Events {
    private static Queue<Event> eventQueue = new LinkedList<>();

    /**
     * Event is an inner class representing a single game event. They have an attributes dictionary
     *
     * @author Jerry Cui
     */
    public static class Event {
        private int type;
        private HashMap<String, Object> attributes;

        /**
         * Creates a new Event with the specified type.
         *
         * @param type The type identifier for this event (using a constant from Locals)
         */
        public Event(int type) {
            this.type = type;
            this.attributes = new HashMap<>();
        }

        /**
         * Returns the type of this event.
         *
         * @return The integer type of this event
         */
        public int getType() {
            return type;
        }

        /**
         * Sets an attribute for this event.
         *
         * @param key The name of the attribute to set
         * @param value The value to associate with the attribute name
         */
        public void setAttribute(String key, Object value) {
            attributes.put(key, value);
        }

        /**
         * Gets the value of a specific attribute.
         *
         * @param key The name of the attribute to get
         * @return The value, or null if the attribute doesn't exist
         */
        public Object getAttribute(String key) {
            return attributes.get(key);
        }
    }

    /**
     * Retrieves and removes all events currently in the queue.
     *
     * @return An ArrayList containing all Event objects that were in the queue
     */
    public static ArrayList<Event> get() {
        ArrayList<Event> events = new ArrayList<>();
        while (!eventQueue.isEmpty()) {
            events.add(eventQueue.poll());
        }
        return events;
    }

    /**
     * Adds a new event to the end of the event queue.
     *
     * @param event The Event object to add to the queue. Must not be null.
     */
    public static void post(Event event) {
        eventQueue.offer(event);
    }

    /**
     * Removes all events from the queue.
     */
    public static void clear() {
        eventQueue.clear();
    }
}
