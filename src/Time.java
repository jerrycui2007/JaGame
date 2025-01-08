import java.util.TimerTask;

/**
 * Time is the class for monitoring time
 */
public class Time {
    // delay to control FPS
    // fps for the current frames per second
    private int delay;
    private float fps;
    // curTime for current time in milliseconds
    // prevTime for time of last frame in milliseconds
    // elapsedTime for time elapsed between frames in milliseconds
    private long curTime;
    private long prevTime;
    private long elapsedTime;

    public Time(){
        this.prevTime = System.currentTimeMillis();
        this.curTime = prevTime;
        this.elapsedTime = 0;
        this.delay = 0;
        this.fps = 0;
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
     * @param targetFPS
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
        while (elapsedTime < delay){
            pause(delay - elapsedTime);
        }

        // Update the current time after pausing
        curTime = System.currentTimeMillis();
        elapsedTime = curTime-prevTime;
        prevTime = curTime;

        // Calculate FPS
        fps = 1000.0f / elapsedTime;
    }

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


    }

    /**
     *
     * @param task
     * @param milliseconds
     * @param period
     */
    public void setTimer(TimerTask task, long milliseconds, long period){

    }

}



