import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Time is the class for monitoring time
 * @author Vincent Han
 */
public class Time {
    // delay to control FPS
    // fps for the current frames per second
    private int delay;
    private float fps;
    // curTime for current time in milliseconds
    // prevTime for time of last frame in milliseconds
    // elapsedTime for time elapsed between frames in milliseconds
    // startTime for the starting time in milliseconds
    private long curTime;
    private long prevTime;
    private long elapsedTime;
    private long startTime;
    private Timer jTimer;
    private Map<Integer, TimerTask> tasks;

    public Time(){
        this.startTime = System.currentTimeMillis();
        this.prevTime = startTime;
        this.curTime = prevTime;
        this.elapsedTime = 0;
        this.delay = 0;
        this.fps = 0;
        jTimer = new Timer();
        tasks = new HashMap<>();
    }

    /**
     * Pauses the program for an amount of time by sleeping the process
     * @param milliseconds, the number of milliseconds to pause for
     */
    public void pause(long milliseconds){
        if (milliseconds <= 0){
            throw new IllegalArgumentException("Delay must be positive");
        }
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Wait interrupted: " + e.getMessage());
        }
    }

    /**
     * Pauses the program for an amount of time by using the processor instead of sleeping
     * @param milliseconds
     */
    public void delay(long milliseconds){
        if (milliseconds <= 0){
            throw new IllegalArgumentException("Delay must be positive");
        }
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < milliseconds){

        }
    }

    /**
     * Computes how many milliseconds have passed since the previous call
     */
    public void tick(){
        curTime = System.currentTimeMillis();
        elapsedTime = curTime-prevTime;
        prevTime = curTime;

        // Calculate FPS
        fps = 1000.0f / elapsedTime;
    }

    /**
     * Computes how many milliseconds have passed since the previous call, limited by FPS
     * @param targetFPS the FPS to limit runtime speed
     */
    public void tick(int targetFPS){
        if (targetFPS <= 0 || targetFPS > 60){
            throw new IllegalArgumentException("FPS must be a reasonable positive number ");
        }
        // Calculate delay per frame in milliseconds
        delay = 1000 / targetFPS;

        curTime = System.currentTimeMillis();
        elapsedTime = curTime-prevTime;

        // Pauses to control FPS
        if (elapsedTime < delay){
            pause(delay - elapsedTime);
        }

        // Update the current time after pausing
        curTime = System.currentTimeMillis();
        elapsedTime = curTime-prevTime;
        prevTime = curTime;

        // Calculate FPS while avoiding division by 0
        fps = elapsedTime > 0 ? 1000.0f / elapsedTime : targetFPS;
    }

    /**
     * Gets the elapsed time in milliseconds
     * @return elapsedTime
     */
    public long getElapsedTime(){
        return elapsedTime;
    }

    /**
     * Gets the current FPS
     * @return fps
     */
    public float getFps(){
        return fps;
    }

    /**
     * Gets the number of milliseconds since the start of the program
     * @return ticks
     */
    public long getTicks(){
        return System.currentTimeMillis() - startTime;
    }

    /**
     * Sets an event to be triggered at regular intervals
     * @param eventID the id of the event to be put in a map
     * @param intervalMillis the interval at which the event runs
     * @param task the event to run
     */
    public void setTimer(int eventID, long intervalMillis, Runnable task){
        if (intervalMillis <= 0) {
            throw new IllegalArgumentException("Interval must be positive");
        }
        if (task == null){
            throw new NullPointerException();
        }
        // Cancel any duplicate timers for the same event ID
        cancelTimer(eventID);
        TimerTask tTask = new TimerTask(){

            @Override
            public void run() {
                task.run();
            }
        };
        tasks.put(eventID, tTask);
        jTimer.scheduleAtFixedRate(tTask, intervalMillis, intervalMillis);

    }

    /**
     * Cancels a specific timer by its event ID
     * @param eventID the ID of the timer to remove
     */
    public void cancelTimer(int eventID) {
        if (tasks.containsKey(eventID)) {
            tasks.get(eventID).cancel();
            tasks.remove(eventID);
        }
    }

    /**
     * Cancels all active timers
     */
    public void cancelAllTimers(){
        jTimer.cancel();
        jTimer = new Timer();
        tasks.clear();
    }

}




