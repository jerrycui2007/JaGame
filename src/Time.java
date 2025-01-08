/**
 * Timer is the class for handling timers
 */
public class Time {

    int delay;
    String name = "";

    // 4 Constructors
    public Time(int delay){
        this.delay = delay;
    }
    public Time(String name){
        this.name = name;
    }
    public Time(String name, int delay){
        this.delay = delay;
        this.name = name;
    }

}



