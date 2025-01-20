import java.util.ArrayList;

/**
 * Some random utility methods that are useful
 */
public class Random {
    /**
     * Returns a random integer between two numbers inclusive
     *
     * @param min min
     * @param max max
     * @return    random
     */
    public static int randint(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    /**
     * Randomly returns either 1 or -1
     *
     * @return 1 or -1
     */
    public static int randomMultiplier() {
        if (randint(1, 2) == 1) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Returns a random item from an array
     *
     * @param array array
     * @return      random value
     * @param <T>   data type of the array
     */
    public static <T> T choice(T[] array) {
        return array[randint(0, array.length - 1)];
    }

    /**
     * Returns a random item from an ArrayList
     *
     * @param list ArrayList
     * @return     random item
     * @param <T>  type of ArrayList
     */
    public static <T> T choice(ArrayList<T> list) {
        return list.get(randint(0, list.size()) - 1);
    }
}
