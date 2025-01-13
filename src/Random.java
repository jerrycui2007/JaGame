public class Random {
    public static int randint(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}
