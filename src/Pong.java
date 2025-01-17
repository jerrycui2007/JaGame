import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Demo game: Classic Pong game
 * 
 * @author Jerry Cui
 */
public class Pong {
    // Constants
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final int PADDLE_WIDTH = 15;
    private static final int PADDLE_HEIGHT = 90;
    private static final int BALL_SIZE = 15;
    private static final int PADDLE_SPEED = 5;
    private static final int BALL_SPEED = 5;
    
    // Game state
    private static final int STATE_MENU = 0;
    private static final int STATE_GAME = 1;

    // Game variables
    private int leftPaddleY;
    private int rightPaddleY;
    
    private int ballX;
    private int ballY;
    
    private int ballXVelocity;
    private int ballYVelocity;
    
    private int leftScore;
    private int rightScore;
    
    private Text leftScoreText;
    private Text rightScoreText;
    
    private Sprite menuSprite;
    
    private Mixer mixer;
    
    // Music
    private Mixer.Music menuMusic;
    private Mixer.Music gameMusic;

    /**
     * Starts the game
     * 
     * @param args run arguments
     */
    public static void main(String[] args) {
        Pong game = new Pong();
        game.run();
    }

    /**
     * Run the program. Either display the main menu or run the game.
     */
    public void run() {
        // Initialize display
        Display window = new Display("Pong");
        window.setMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        
        // Create and register input listeners
        Key keyListener = new Key();
        window.getDisplay().addKeyListener(keyListener);
        
        // Initialize game state
        int gameState = STATE_MENU;
        setupGame();
        
        // Load menu sprite
        menuSprite = new Sprite(0, 0, "assets/pong_menu.png");
        
        // Initialize audio
        mixer = new Mixer();
        menuMusic = new Mixer.Music("assets/menu_music.wav");
        gameMusic = new Mixer.Music("assets/game_music.wav");
        mixer.playBackgroundMusic(menuMusic);
        
        // Create score text objects
        leftScoreText = new Text("0", SCREEN_WIDTH / 4, 50, new Font("Times New Roman", Font.BOLD, 36), Color.WHITE);
        rightScoreText = new Text("0", 3 * SCREEN_WIDTH / 4, 50, new Font("Times New Roman", Font.BOLD, 36), Color.WHITE);

        Time clock = new Time();
        final int FPS = 60;

        // Game loop
        while (true) {
            // Process events
            ArrayList<Events.Event> events = Events.get();
            
            // Update held keys
            Key.update();
            
            // Clear screen
            window.clear();
            window.fill(Color.BLACK);  // background colour
            
            if (gameState == STATE_MENU) {
                // Draw menu
                window.addSprite(menuSprite);
                
                // Check input
                // Space bar to start the game
                for (Events.Event event : events) {
                    if (event.getType() == Locals.KEYDOWN) {
                        int keyCode = (int) event.getAttribute("key");
                        if (keyCode == KeyEvent.VK_SPACE) {
                            // Change music
                            menuMusic.stop();
                            mixer.stopAll();
                            
                            // Change to game
                            gameState = STATE_GAME;
                            setupGame();
                            gameMusic = new Mixer.Music("assets/game_music.wav");
                            mixer.playBackgroundMusic(gameMusic);
                        }
                    }
                }
            } else {
                // Handle game input
                for (Events.Event event : events) {
                    if (event.getType() == Locals.KEYDOWN) {
                        int keyCode = (int) event.getAttribute("key");
                        if (keyCode == KeyEvent.VK_W && leftPaddleY > 0) {
                            leftPaddleY -= PADDLE_SPEED;
                        }
                        if (keyCode == KeyEvent.VK_S && leftPaddleY < SCREEN_HEIGHT - PADDLE_HEIGHT) {
                            leftPaddleY += PADDLE_SPEED;
                        }
                        if (keyCode == KeyEvent.VK_UP && rightPaddleY > 0) {
                            rightPaddleY -= PADDLE_SPEED;
                        }
                        if (keyCode == KeyEvent.VK_DOWN && rightPaddleY < SCREEN_HEIGHT - PADDLE_HEIGHT) {
                            rightPaddleY += PADDLE_SPEED;
                        }
                    }
                }

                // Update ball position
                updateBall();
                
                // Draw paddles
                window.drawRectangle(50, leftPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT, Color.WHITE, false);
                window.drawRectangle(SCREEN_WIDTH - 50 - PADDLE_WIDTH, rightPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT, Color.WHITE, false);
                
                // Draw ball
                window.drawCircle(ballX + BALL_SIZE/2, ballY + BALL_SIZE/2, BALL_SIZE/2, Color.WHITE, false);
                
                // Display score text
                leftScoreText.setText(String.valueOf(leftScore));
                rightScoreText.setText(String.valueOf(rightScore));
                window.drawText(leftScoreText);
                window.drawText(rightScoreText);
            }
            
            // Update display
            window.update();
            clock.tick(FPS);
        }
    }

    /**
     * Sets up a game from the beginning
     */
    private void setupGame() {
        // Initialize paddle positions
        leftPaddleY = (SCREEN_HEIGHT - PADDLE_HEIGHT) / 2;
        rightPaddleY = (SCREEN_HEIGHT - PADDLE_HEIGHT) / 2;
        
        // Initialize ball position and direction
        ballX = (SCREEN_WIDTH - BALL_SIZE) / 2;
        ballY = (SCREEN_HEIGHT - BALL_SIZE) / 2;

        // Randomize initial direction
        ballXVelocity = Random.randomMultiplier() * BALL_SPEED;
        ballYVelocity = Random.randomMultiplier() * BALL_SPEED;
        
        // Initialize scores
        leftScore = 0;
        rightScore = 0;
    }

    /**
     * Main logic method for the ball
     */
    private void updateBall() {
        // Update position
        ballX += ballXVelocity;
        ballY += ballYVelocity;
        
        // Check for collisions with top and bottom
        if (ballY <= 0 || ballY >= SCREEN_HEIGHT - BALL_SIZE) {
            ballYVelocity = -ballYVelocity;
        }
        
        // Check for collisions with paddles
        if (ballX <= 50 + PADDLE_WIDTH && ballY + BALL_SIZE >= leftPaddleY && ballY <= leftPaddleY + PADDLE_HEIGHT) {
            ballXVelocity = -ballXVelocity;
            ballX = 50 + PADDLE_WIDTH;
        }
        
        if (ballX + BALL_SIZE >= SCREEN_WIDTH - 50 - PADDLE_WIDTH && ballY + BALL_SIZE >= rightPaddleY && ballY <= rightPaddleY + PADDLE_HEIGHT) {
            ballXVelocity = -ballXVelocity;
            ballX = SCREEN_WIDTH - 50 - PADDLE_WIDTH - BALL_SIZE;
        }
        
        // Check for scoring
        if (ballX <= 0) {
            rightScore++;

            // Initialize ball position and direction
            ballX = (SCREEN_WIDTH - BALL_SIZE) / 2;
            ballY = (SCREEN_HEIGHT - BALL_SIZE) / 2;

            // Randomize initial direction
            ballXVelocity = Random.randomMultiplier() * BALL_SPEED;
            ballYVelocity = Random.randomMultiplier() * BALL_SPEED;
        }
        if (ballX >= SCREEN_WIDTH - BALL_SIZE) {
            leftScore++;

            // Initialize ball position and direction
            ballX = (SCREEN_WIDTH - BALL_SIZE) / 2;
            ballY = (SCREEN_HEIGHT - BALL_SIZE) / 2;

            // Randomize initial direction
            ballXVelocity = Random.randomMultiplier() * BALL_SPEED;
            ballYVelocity = Random.randomMultiplier() * BALL_SPEED;
        }
    }
}
