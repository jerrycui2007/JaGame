import java.awt.Color;
import java.util.ArrayList;

/**
 * Demo game to demonstrate a example of a game made in JaGame
 *
 * @author Jerry Cui
 */
public class Snake {
    // Constants
    final static String FONT = "freesansbold.ttf";

    final static int SCREEN_WIDTH = 1440;
    final static int SCREEN_HEIGHT = 1000;

    final static int TITLE_FONT_SIZE = 48;
    final static int SUBTITLE_FONT_SIZE = 36;
    final static int SCORE_FONT_SIZE = 18;
    final static int BIG_FONT_SIZE = 80;

    final static int SQUARE_SIZE = 40;

    final static int FPS = 180;

    final static int[] UP = {0, -1};
    final static int[] RIGHT = {1, 0};
    final static int[] DOWN = {0, 1};
    final static int[] LEFT = {-1, 0};

    final static double SPEED_INCREASE = 0.5;
    final static double TIME_INCREASE = 5;
    final static double FRUIT_LIFETIME = 5;

    // Global variables
    static String gameMode = "Classic";
    static boolean music = true;
    static boolean sound_fx = true;

    static int score = 0;
    static double squareTickerMax;
    static double timeLeft;

    // Load assets
    static Mixer.Sound increaseSound = new Mixer.Sound("increase.wav");


    public static void main(String[] args) {
        // Setting up the display
        Display window = new Display("Snake - JaGame Demo Game");
        window.setMode(SCREEN_WIDTH, SCREEN_HEIGHT);

        Key keyListener = new Key();
        window.getDisplay().addKeyListener(keyListener);

        Mouse mouseListener = new Mouse();
        window.getDisplay().addMouseListener(mouseListener);
        window.getDisplay().addMouseMotionListener(mouseListener);


    }

    /**
     * Private inner class for a square object, used for the game
     */
    private class Square {
        private int x;
        private int y;

        private ArrayList<int[]> queue;  // List containing directions to move in
        private int[] drawRect;
        private Color color;

        /**
         * Constructor for the square object
         *
         * @param x     x-coordinate
         * @param y     y-coordinate
         * @param color color of the square
         */
        public Square(int x, int y, Color color) {
            this.x = x;
            this.y = y;

            this.queue = new ArrayList<int[]>();
            this.drawRect = new int[] {this.x, this.y, SQUARE_SIZE, SQUARE_SIZE};
            this.color = color;
        }

        /**
         * Logic method for the square object
         *
         * @param number
         * @param player
         * @param gameMode
         * @param fruit
         * @param squares
         * @param squares2
         */
        public void main(int number, int player, String gameMode, Fruit fruit, ArrayList<Square> squares, ArrayList<Square> squares2) {
            // Move
            this.x += SQUARE_SIZE * this.queue.get(0)[0];
            this.y += SQUARE_SIZE * this.queue.get(0)[1];
            this.drawRect = new int[] {this.x, this.y, SQUARE_SIZE, SQUARE_SIZE};

            // Check for collision with fruit
            if (this.x == fruit.getY()) {
                // Add a new square to the player who found it
                if (player == 1) {
                    squares.add(new Square(squares.get(squares.size() - 1).getX(), squares.get(squares.size() - 1).getY(), Color.RED));
                } else if (player == 2) {
                    squares2.add(new Square(squares2.get(squares.size() - 1).getX(), squares.get(squares2.size() - 1).getY(), Color.RED));
                }

                // Replace the fruit
                fruit = new Fruit(Random.randint(1, SCREEN_WIDTH / SQUARE_SIZE - 1) * SQUARE_SIZE, Random.randint(1, SCREEN_HEIGHT / SQUARE_SIZE - 1) * SQUARE_SIZE, Color.GREEN);

                // Play sound effects
                if (sound_fx) {
                    increaseSound.play();
                }

                if (gameMode.equals("Classic") || gameMode.equals("Obstacle Course")) {  // Game mode specific changes
                    score++;
                    squareTickerMax -= SPEED_INCREASE;
                    timeLeft += TIME_INCREASE;
                }
            }
        }

        /**
         * Draws the square object onto the screen
         */
        private void draw(Display window) {
            window.drawRectangle(this.drawRect, color, false);
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }
    }

    /**
     * Class for the fruit object, which extends Square
     */
    private class Fruit extends Square {
        private int timer;

        public Fruit(int x, int y, Color color) {
            super(x, y, color);
            this.timer = FPS * (int) FRUIT_LIFETIME;  // how many frames left before the fruit deletes itself
        }

        /**
         * Logic method for the fruit object
         *
         * @return true if the fruit's time has run out and needs to self-delete, false otherwise
         */
        public boolean main() {
            this.timer--;
            return this.timer <= 0;
        }
    }

}
