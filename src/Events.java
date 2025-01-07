import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Event is the class for interacting with events and queues
 *
 * @author Jerry Cui
 */
public class Events {
    private static Queue<Event> eventQueue = new LinkedList<>();

    /**
     * Object for representing events
     *
     * @author Jerry Cui
     */
    public static class Event {
        private int type;
        private HashMap<String, Object> attributes;

        /**
         * Constructor for the event object
         *
         * @param type the type of event it is
         */
        public Event(int type) {
            this.type = type;
            this.attributes = new HashMap<>();
        }

        public int getType() {
            return type;
        }

        public void setAttribute(String key, Object value) {
            attributes.put(key, value);
        }

        public Object getAttribute(String key) {
            return attributes.get(key);
        }
    }

    /**
     * Get all events currently in the queue
     *
     * @return ArrayList of Event objects
     */
    public static ArrayList<Event> get() {
        ArrayList<Event> events = new ArrayList<>();
        while (!eventQueue.isEmpty()) {
            events.add(eventQueue.poll());
        }
        return events;
    }

    /**
     * Add an event to the queue
     * @param event The event to add
     */
    public static void post(Event event) {
        eventQueue.offer(event);
    }

    /**
     * Clear all events from the queue
     */
    public static void clear() {
        eventQueue.clear();
    }
}
