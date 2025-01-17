import java.awt.*;
import java.util.ArrayList;

/**
 * Demo game to demonstrate a example of a game made in JaGame
 *
 * @author Jerry Cui
 */
public class Snake {
    // Constants
    final static Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 24);

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
    static boolean music = true;
    static boolean sound_fx = true;

    static int score = 0;
    static int squareTickerMax;
    static double timeLeft;

    static String latestMessage = "";

    // Load assets
    static Mixer mixer = new Mixer();

    static Mixer.Sound increaseSound = new Mixer.Sound("./assets/increase.wav");


    public static void main(String[] args) {
        // Setting up the display
        Display window = new Display("Snake - JaGame Demo Game");
        window.setMode(SCREEN_WIDTH, SCREEN_HEIGHT);

        Key keyListener = new Key();
        window.getDisplay().addKeyListener(keyListener);

        Mouse mouseListener = new Mouse();
        window.getDisplay().addMouseListener(mouseListener);
        window.getDisplay().addMouseMotionListener(mouseListener);

        playMenu(window, "Classic");


    }

    /**
     * Main game method
     *
     * @param mode   the current game mode
     * @param window the Display to draw on
     */
    public static void game(String mode, Display window) {
        // Clear any existing sprites and graphics
        window.clear();
        window.fill(Color.black);
        window.update();

        // Initialize the game
        ArrayList<Square> squares = new ArrayList<>();
        squares.add(new Square(0, 0, Color.orange));
        squares.add(new Square(-SQUARE_SIZE, 0, Color.blue));
        squares.add(new Square(-SQUARE_SIZE * 2, 0, Color.blue));

        ArrayList<Square> squares2 = new ArrayList<>();
        squares2.add(new Square(SCREEN_WIDTH - SQUARE_SIZE * 3, SCREEN_HEIGHT - SQUARE_SIZE, Color.PINK));
        squares2.add(new Square(SCREEN_WIDTH - SQUARE_SIZE * 2, SCREEN_HEIGHT - SQUARE_SIZE, Color.blue));
        squares2.add(new Square(SCREEN_WIDTH - SQUARE_SIZE, SCREEN_HEIGHT - SQUARE_SIZE, Color.blue));

        // Set initial directions
        for (Square square : squares) {
            square.queue.clear();  // Clear any existing directions
            square.queue.add(RIGHT);
        }

        for (Square square : squares2) {
            square.queue.clear();  // Clear any existing directions
            square.queue.add(LEFT);
        }

        squareTickerMax = 15;  // Square animate once every squareTickerMax frames
        int squareTicker = 15;  // Countdown until it reaches 0

        score = 0;
        timeLeft = 15;  // seconds left

        // Fruits
        ArrayList<Fruit> fruits = new ArrayList<>();
        fruits.add(new Fruit(Random.randint(1, SCREEN_WIDTH / SQUARE_SIZE - 1) * SQUARE_SIZE, 
                           Random.randint(1, SCREEN_HEIGHT / SQUARE_SIZE - 1) * SQUARE_SIZE, 
                           Color.GREEN));

        String turn = "Player 1";  // whose turn it is in the strategy game mode
        boolean dead = false;  // if either of the players died
        int fadeOutValue = 0;  // Increases up to 255 while screen fades to black

        // Default directions for both players
        int[] direction = RIGHT;
        int[] direction2 = LEFT;

        Time clock = new Time();

        // Stop menu music and start game music
        mixer.stopAll();
        if (music) {
            Mixer.Music musicBG;
            if (mode.equals("Classic")) {
                musicBG = new Mixer.Music("./assets/game_music.wav");
            } else if (mode.equals("Two Player")) {
                musicBG = new Mixer.Music("./assets/two_player_music.wav");
            } else if (mode.equals("Obstacle Course")) {
                musicBG = new Mixer.Music("./assets/obstacle_music.wav");
            } else {
                musicBG = new Mixer.Music("./assets/strategy_music.wav");
            }
            mixer.playBackgroundMusic(musicBG);
        }

        String deathMessage = "";
        boolean player1Moved = false;
        boolean player2Moved = false;

        // Setup obstacles
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        if (mode.equals("Obstacle Course")) {
            for (int i = 0; i < 30; i++) {
                obstacles.add(new Obstacle(Random.randint(1, SCREEN_WIDTH / SQUARE_SIZE - 1) * SQUARE_SIZE,
                                        Random.randint(1, SCREEN_HEIGHT / SQUARE_SIZE - 1) * SQUARE_SIZE));
            }
        }

        // Debug text
        Text debugText = new Text("Game Started - Mode: " + mode, 10, SCREEN_HEIGHT - 30, FONT, Color.WHITE);

        while (true) {  // main game loop
            window.clear();
            window.fill(Color.black);  // default background colour

            // Key presses
            ArrayList<Events.Event> events = Events.get();

            if (!mode.equals("Strategy")) {
                for (Events.Event event : events) {
                    if (event.getType() == Locals.KEYDOWN) {
                        char keyChar = (char) event.getAttribute("char");

                        if (keyChar == 'w' && squares.get(0).queue.get(0) != DOWN) {  // Can't travel in the opposite direction
                            direction = UP;
                        } else if (keyChar == 'd' && squares.get(0).queue.get(0) != LEFT) {  // Can't travel in the opposite direction
                            direction = RIGHT;
                        } else if (keyChar == 's' && squares.get(0).queue.get(0) != UP) {  // Can't travel in the opposite direction
                            direction = DOWN;
                        } else if (keyChar == 'a' && squares.get(0).queue.get(0) != RIGHT) {  // Can't travel in the opposite direction
                            direction = LEFT;
                        }
                    }
                }

                // Animate squares for player 1
                if (squareTicker <= 0) {
                    squares.get(0).queue.add(direction);

                    // Call main methods of the squares
                    for (int i = 0; i < squares.size(); i++) {
                        squares.get(i).main(i, 1, mode, fruits, squares, squares2, window);
                    }
                } else {  // just draw the squares instead
                    for (Square square : squares) {
                        square.draw(window);
                    }
                }

                // Now check player 2 movements
                for (Events.Event event : events) {
                    if (event.getType() == Locals.KEYDOWN) {
                        int keyCode = (int) event.getAttribute("key");

                        if (keyCode == 38 && squares.get(0).queue.get(0) != DOWN) {  // Can't travel in the opposite direction
                            direction2 = UP;
                        } else if (keyCode == 39 && squares.get(0).queue.get(0) != LEFT) {  // Can't travel in the opposite direction
                            direction2 = RIGHT;
                        } else if (keyCode == 40 && squares.get(0).queue.get(0) != UP) {  // Can't travel in the opposite direction
                            direction2 = DOWN;
                        } else if (keyCode == 37 && squares.get(0).queue.get(0) != RIGHT) {  // Can't travel in the opposite direction
                            direction2 = LEFT;
                        }
                    }
                }

                // Animate squares for player 2
                if (squareTicker <= 0) {
                    squares2.get(0).queue.add(direction2);

                    // Call main methods of the squares
                    for (int i = 0; i < squares2.size(); i++) {
                        squares2.get(i).main(i, 1, mode, fruits, squares, squares2, window);
                    }
                } else {  // just draw the squares instead
                    for (Square square : squares2) {
                        square.draw(window);
                    }
                }

                // Square ticker
                if (squareTicker > 0) {
                    squareTicker--;
                } else {
                    squareTicker = squareTickerMax;
                }
            } else {  // Strategy game mode
                // Display turn text
                Text turnText = new Text(turn + "'s Turn", SCREEN_WIDTH / 2, 50, FONT, Color.WHITE);
                window.drawText(turnText);
                if (turn.equals("Player 1")) {
                    for (Events.Event event : events) {
                        if (event.getType() == Locals.KEYDOWN) {
                            char keyChar = (char) event.getAttribute("char");

                            if (keyChar == 'w' && squares.get(0).queue.get(0) != DOWN) {  // Can't travel in the opposite direction
                                direction = UP;
                                player1Moved = true;
                            } else if (keyChar == 'd' && squares.get(0).queue.get(0) != LEFT) {  // Can't travel in the opposite direction
                                direction = RIGHT;
                                player1Moved = true;
                            } else if (keyChar == 's' && squares.get(0).queue.get(0) != UP) {  // Can't travel in the opposite direction
                                direction = DOWN;
                                player1Moved = true;
                            } else if (keyChar == 'a' && squares.get(0).queue.get(0) != RIGHT) {  // Can't travel in the opposite direction
                                direction = LEFT;
                                player1Moved = true;
                            }

                            if (player1Moved) {
                                squares.get(0).queue.clear();
                                squares.get(0).queue.add(direction);
                                squares.get(0).queue.add(direction);  // queue needs to have two items to prevent an error
                            }

                            for (int i = 0; i < squares2.size(); i++) {
                                squares2.get(i).main(i, 1, mode, fruits, squares, squares2, window);
                            }
                            turn = "Player 2";
                            player1Moved = false;
                            
                        }
                    }
                } else {
                    for (Events.Event event : events) {
                        if (event.getType() == Locals.KEYDOWN) {
                            int keyCode = (int) event.getAttribute("key");

                            if (keyCode == 38 && squares.get(0).queue.get(0) != DOWN) {  // Can't travel in the opposite direction
                                direction2 = UP;
                                player2Moved = true;
                            } else if (keyCode == 39 && squares.get(0).queue.get(0) != LEFT) {  // Can't travel in the opposite direction
                                direction2 = RIGHT;
                                player2Moved = true;
                            } else if (keyCode == 40 && squares.get(0).queue.get(0) != UP) {  // Can't travel in the opposite direction
                                direction2 = DOWN;
                                player2Moved = true;
                            } else if (keyCode == 37 && squares.get(0).queue.get(0) != RIGHT) {  // Can't travel in the opposite direction
                                direction2 = LEFT;
                                player2Moved = true;
                            }

                            if (player2Moved) {
                                squares2.get(0).queue.clear();
                                squares2.get(0).queue.add(direction);
                                squares2.get(0).queue.add(direction);  // queue needs to have two items to prevent an error
                            }

                            for (int i = 0; i < squares2.size(); i++) {
                                squares2.get(i).main(i, 2, mode, fruits, squares, squares2, window);
                            }
                            turn = "Player 1";
                            player2Moved = false;
                        }
                    }
                }

                for (Square square : squares) {
                    square.draw(window);
                }

                for (Square square : squares2) {
                    square.draw(window);
                }
            }

            // Check for collision with game borders
            if (squares.get(0).getX() < 0 || squares.get(0).getX() + SQUARE_SIZE > SCREEN_WIDTH || squares.get(0).getY() < 0 || squares.get(0).getY() + SQUARE_SIZE > SCREEN_HEIGHT) {
                if (!mode.equals("Two Player") && !mode.equals("Strategy")) {
                    deathMessage = "You lost by colliding wit the border. Your score was " + score + ".";
                } else {
                    deathMessage = "Player 1 lost by colliding with the border.";
                }
                dead = true;
            }

            if (mode.equals("Two Player") || mode.equals("Strategy")) {
                if (squares2.get(0).getX() < 0 || squares2.get(0).getX() + SQUARE_SIZE > SCREEN_WIDTH || squares2.get(0).getY() < 0 || squares2.get(0).getY() + SQUARE_SIZE > SCREEN_HEIGHT) {
                    deathMessage = "Player 2 lost by colliding with the border.";
                    dead = true;
                }
            }

            // Check square with square collision
            for (int i = 1; i < squares.size(); i++) {
                if (squareCollision(squares.get(0), squares.get(i))) {
                    dead = true;
                    if (!mode.equals("Two Player") && !mode.equals("Strategy")) {
                        deathMessage = "You lost by colliding with yourself. Your score was " + score + ".";
                    } else {
                        deathMessage = "Player 1 lost by colliding with itself.";
                    }
                }
            }

            // Check for player 2
            if (mode.equals("Two Player") || mode.equals("Strategy")) {
                for (int i = 1; i < squares.size(); i++) {
                    if (squareCollision(squares2.get(0), squares2.get(i))) {
                        dead = true;
                        deathMessage = "Player 2 lost by colliding with itself.";
                    }
                }

                // Collisions against each other
                for (Square square : squares) {
                    if (squareCollision(squares2.get(0), square)) {
                        dead = true;
                        deathMessage = "Player 2 lost by colliding with Player 1's body.";
                    }
                }

                for (Square square : squares2) {
                    if (squareCollision(squares.get(0), square)) {
                        dead = true;
                        deathMessage = "Player 1 lost by colliding with Player 2's body.";
                    }
                }
            }
            
            // Check for obstacle collision
            if (mode.equals("Obstacle Course")) {
                for (Square obstacle : obstacles) {
                    if (squareCollision(squares.get(0), obstacle)) {
                        dead = true;
                        deathMessage = "You lost by colliding with an obstacle. Your score was " + score + ".";
                    }
                }
            }

            // Go through fruits
            for (Fruit fruit : fruits) {
                boolean result = fruit.main();
                if (!mode.equals("Strategy")) {
                    if (result) {
                        fruits.clear();
                        fruits.add(new Fruit(Random.randint(1, SCREEN_WIDTH / SQUARE_SIZE - 1) * SQUARE_SIZE, Random.randint(1, SCREEN_HEIGHT / SQUARE_SIZE - 1) * SQUARE_SIZE, Color.GREEN));
                    }
                }
            }

            // Obstacles
            if (mode.equals("Obstacle Course")) {
                for (Obstacle obstacle : obstacles) {
                    obstacle.main(window);
                }
            }

            // Score and timer
            if (mode.equals("Classic") || mode.equals("Obstacle")) {
                // Display score and timer text
                Text scoreText = new Text("Score: " + score, 10, 10, FONT, Color.WHITE);
                Text timerText = new Text("Time Left: " + String.format("%.1f", timeLeft), SCREEN_WIDTH - 150, 10, FONT, Color.WHITE);
                window.drawText(scoreText);
                window.drawText(timerText);
                timeLeft -= (double) 1 / FPS;

                if (timeLeft < 0) {
                    deathMessage = "You lost by running out of time.";
                    dead = true;
                }
            }

            if (dead) {
                if (fadeOutValue == 0) {
                    String latestMessage = deathMessage;  // Save the current death message
                }
                if (music) {
                    // Fade out music
                    // mixer.fadeOutMusic(500);
                }

                fadeOutValue += 1;

                // Fade out screen to black
                Color fadeColor = new Color(0, 0, 0, Math.min(255, fadeOutValue));
                window.drawRectangle(new int[]{0, 0, SCREEN_WIDTH, SCREEN_HEIGHT}, fadeColor, true);
                
                if (fadeOutValue == 500) {
                    deathMessage = latestMessage;
                    break;
                }
            }

            clock.tick(FPS);
            window.update();
        }

        gameOver(deathMessage, window, mode);
    }

    /**
     * Displays the death message after losing
     *
     * @param deathMessage the death message
     * @param window       Display to draw on
     * @param mode         the game mode
     */
    public static void gameOver(String deathMessage, Display window, String mode) {
        if (deathMessage == null) {
            deathMessage = "Game Over";
        }
        
        if (music) {
            // Play defeat music

            Mixer.Music defeatMusic = new Mixer.Music("./assets/defeat_music.wav");
            mixer.playBackgroundMusic(defeatMusic);
        } else {
            // Play defeat music on mute for timing
            Mixer.Music defeatMusic = new Mixer.Music("./assets/defeat_music.wav");
            mixer.playBackgroundMusic(defeatMusic);
            mixer.setMasterVolume(0);
        }

        boolean moveOn = false;
        int gameOverFadeOutValue = 0;

        Time clock = new Time();

        while (true) {
            window.fill(Color.BLACK);

            // Display title and game over text
            Text gameOverText = new Text("Game Over", SCREEN_WIDTH / 2, SCREEN_HEIGHT / 3, FONT, Color.WHITE);
            Text deathMessageText = new Text(deathMessage, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, FONT, Color.WHITE);
            Text continueText = new Text("Press any key to continue", SCREEN_WIDTH / 2, SCREEN_HEIGHT * 2 / 3, FONT, Color.WHITE);
            window.drawText(gameOverText);
            window.drawText(deathMessageText);
            window.drawText(continueText);

            // Check if music is done playing
            if (!mixer.isActive()) {
                moveOn = true;
            }

            if (moveOn) {
                gameOverFadeOutValue += 5;

                // Slowly fade out to black screen
                Color fadeColor = new Color(0, 0, 0, Math.min(255, gameOverFadeOutValue));
                window.drawRectangle(new int[]{0, 0, SCREEN_WIDTH, SCREEN_HEIGHT}, fadeColor, true);

                if (gameOverFadeOutValue == 300) {
                    playMenu(window, mode);
                }
            }

            clock.tick(FPS);
            window.update();
        }
    }

    /**
     * Displays the main menu screen
     *
     * @param window the window to draw on
     * @param mode    the game mode
     */
    public static void playMenu(Display window, String mode) {
        if (mode == null) {
            mode = "Classic";
        }
        
        if (music) {
            mixer.playBackgroundMusic(new Mixer.Music("./assets/menu_music.wav"));
        } else {
            mixer.stopAll();
        }

        String setupStatus = "None";
        Time clock = new Time();

        Sprite classicMenu = new Sprite(0, 0, "./assets/classic_menu.png");
        Sprite twoPlayerMenu = new Sprite(0, 0, "./assets/two_player_menu.png");
        Sprite obstacleMenu = new Sprite(0, 0, "./assets/obstacle_menu.png");
        Sprite strategyMenu = new Sprite(0, 0, "./assets/strategy_menu.png");
        Sprite setupMenu = new Sprite(0, 0, "./assets/setup_menu.png");
        Sprite gameModeMenu = new Sprite(0, 0, "./assets/game_mode_menu.png");

        Text musicText = new Text("Music: " + (music ? "On" : "Off"), SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 - 50, FONT, Color.WHITE);
        Text soundText = new Text("Sound Effects: " + (sound_fx ? "On" : "Off"), SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 + 50, FONT, Color.WHITE);
        Text debugText = new Text("Debug: Waiting for input", 10, SCREEN_HEIGHT - 30, FONT, Color.WHITE);
        Text modeText = new Text("Current Mode: " + mode, SCREEN_WIDTH / 2, 30, FONT, Color.WHITE);

        // Define button rectangles
        int[] playRect = new int[]{0, 214, 355, 49};
        int[] setupRect = new int[]{0, 280, 358, 49};
        int[] optionsRect = new int[]{0, 585, 300, 49};
        int[] backRect = new int[]{0, 650, 300, 49};
        int[] gameModeRect = new int[]{0, 290, 358, 30};
        int[] classicRect = new int[]{0, 214, 276, 49};
        int[] twoPlayerRect = new int[]{0, 280, 276, 49};
        int[] obstacleRect = new int[]{0, 348, 278, 49};
        int[] strategyRect = new int[]{0, 420, 278, 49};

        while (true) {
            window.clear();
            window.fill(Color.black);

            ArrayList<Events.Event> events = Events.get();

            if (setupStatus.equals("None")) {
                // Display appropriate menu based on mode
                if (mode.equals("Classic")) {
                    window.addSprite(classicMenu);
                } else if (mode.equals("Two Player")) {
                    window.addSprite(twoPlayerMenu);
                } else if (mode.equals("Obstacle Course")) {
                    window.addSprite(obstacleMenu);
                } else if (mode.equals("Strategy")) {
                    window.addSprite(strategyMenu);
                }

                // Handle menu navigation
                for (Events.Event event : events) {
                    if (event.getType() == Locals.KEYDOWN) {
                        char keyChar = (char) event.getAttribute("char");
                        debugText.setText("Debug: Key pressed - " + keyChar);
                        
                        if (keyChar == '1') {
                            mode = "Classic";
                            modeText.setText("Current Mode: " + mode);
                        } else if (keyChar == '2') {
                            mode = "Two Player";
                            modeText.setText("Current Mode: " + mode);
                        } else if (keyChar == '3') {
                            mode = "Obstacle Course";
                            modeText.setText("Current Mode: " + mode);
                        } else if (keyChar == '4') {
                            mode = "Strategy";
                            modeText.setText("Current Mode: " + mode);
                        } else if (keyChar == 'o') {
                            setupStatus = "Option";
                        } else if (keyChar == ' ') {
                            debugText.setText("Debug: Starting game...");
                            window.drawText(debugText);
                            window.update();

                            break;

                        }
                    } else if (event.getType() == Locals.MOUSEBUTTONDOWN) {
                        int mouseX = (int) event.getAttribute("x");
                        int mouseY = (int) event.getAttribute("y");
                        debugText.setText("Debug: Mouse clicked at " + mouseX + "," + mouseY);
                        
                        if (isPointInRect(mouseX, mouseY, playRect)) {
                            debugText.setText("Debug: Starting game...");
                            window.drawText(debugText);
                            window.update();

                            break;
                            
                        } else if (isPointInRect(mouseX, mouseY, setupRect)) {
                            setupStatus = "Option";
                        }
                    }
                }
            } else if (setupStatus.equals("Option")) {
                window.addSprite(setupMenu);
                
                // Handle option menu
                for (Events.Event event : events) {
                    if (event.getType() == Locals.KEYDOWN) {
                        char keyChar = (char) event.getAttribute("char");
                        debugText.setText("Debug: Key pressed in options - " + keyChar);
                        
                        if (keyChar == 'm') {
                            music = !music;
                            if (!music) {
                                mixer.stopAll();
                            } else {
                                mixer.playBackgroundMusic(new Mixer.Music("./assets/menu_music.wav"));
                            }
                            musicText.setText("Music: " + (music ? "On" : "Off"));
                        } else if (keyChar == 's') {
                            sound_fx = !sound_fx;
                            soundText.setText("Sound Effects: " + (sound_fx ? "On" : "Off"));
                        } else if (keyChar == 'b') {
                            setupStatus = "None";
                        } else if (keyChar == 'g') {
                            setupStatus = "Game Mode";
                        }
                    } else if (event.getType() == Locals.MOUSEBUTTONDOWN) {
                        int mouseX = (int) event.getAttribute("x");
                        int mouseY = (int) event.getAttribute("y");
                        debugText.setText("Debug: Mouse clicked in options at " + mouseX + "," + mouseY);
                        
                        if (isPointInRect(mouseX, mouseY, gameModeRect)) {
                            setupStatus = "Game Mode";
                        } else {
                            setupStatus = "None";
                        }
                    }
                }

                // Display current settings
                window.drawText(musicText);
                window.drawText(soundText);
            } else if (setupStatus.equals("Game Mode")) {
                window.addSprite(gameModeMenu);
                
                for (Events.Event event : events) {
                    if (event.getType() == Locals.MOUSEBUTTONDOWN) {
                        int mouseX = (int) event.getAttribute("x");
                        int mouseY = (int) event.getAttribute("y");
                        debugText.setText("Debug: Mouse clicked in game mode at " + mouseX + "," + mouseY);
                        
                        if (isPointInRect(mouseX, mouseY, classicRect)) {
                            mode = "Classic";
                            modeText.setText("Current Mode: " + mode);
                            setupStatus = "None";
                        } else if (isPointInRect(mouseX, mouseY, twoPlayerRect)) {
                            mode = "Two Player";
                            modeText.setText("Current Mode: " + mode);
                            setupStatus = "None";
                        } else if (isPointInRect(mouseX, mouseY, obstacleRect)) {
                            mode = "Obstacle Course";
                            modeText.setText("Current Mode: " + mode);
                            setupStatus = "None";
                        } else if (isPointInRect(mouseX, mouseY, strategyRect)) {
                            mode = "Strategy";
                            modeText.setText("Current Mode: " + mode);
                            setupStatus = "None";
                        }
                    } else if (event.getType() == Locals.KEYDOWN) {
                        char keyChar = (char) event.getAttribute("char");
                        if (keyChar == 'b') {
                            setupStatus = "None";
                        }
                    }
                }
            }

            game(mode, window);

            window.drawText(modeText);  // Always show current mode
            window.drawText(debugText);  // Always draw debug text
            clock.tick(FPS);
            window.update();
        }
    }

    private static boolean isPointInRect(int x, int y, int[] rect) {
        return x >= rect[0] && x <= rect[0] + rect[2] && 
               y >= rect[1] && y <= rect[1] + rect[3];
    }

    /**
     * Checks if there is a collision between two square Objects
     *
     * @param square1 first square
     * @param square2 second square
     * @return        if collision
     */
    public static boolean squareCollision(Square square1, Square square2) {
        return (square1.getX() == square2.getX() && square1.getY() == square2.getY());
    }

    /**
     * Private inner class for a square object, used for the game
     */
    private static class Square {
        protected int x;
        protected int y;

        protected ArrayList<int[]> queue;  // List containing directions to move in
        protected int[] drawRect;
        protected Color color;

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
            this.queue.add(RIGHT);  // Initialize with default direction
            this.drawRect = new int[] {this.x, this.y, SQUARE_SIZE, SQUARE_SIZE};
            this.color = color;
        }

        /**
         * Logic method for the square object
         *
         * @param number
         * @param player
         * @param mode
         * @param fruits
         * @param squares
         * @param squares2
         * @param window
         */
        public void main(int number, int player, String mode, ArrayList<Fruit> fruits, ArrayList<Square> squares, ArrayList<Square> squares2, Display window) {
            if (this.queue.isEmpty()) {
                return;  // Skip if queue is empty
            }

            // Move
            this.x += SQUARE_SIZE * this.queue.get(0)[0];
            this.y += SQUARE_SIZE * this.queue.get(0)[1];
            this.drawRect = new int[] {this.x, this.y, SQUARE_SIZE, SQUARE_SIZE};

            // Check for collision with fruit
            if (number == 0) {  // Only check head collision
                for (Fruit fruit : fruits) {
                    if (this.x == fruit.getX() && this.y == fruit.getY()) {
                        // Add a new square to the player who found it
                        if (player == 1) {
                            squares.add(new Square(squares.get(squares.size() - 1).getX(), squares.get(squares.size() - 1).getY(), Color.BLUE));
                        } else if (player == 2) {
                            squares2.add(new Square(squares2.get(squares2.size() - 1).getX(), squares2.get(squares2.size() - 1).getY(), Color.BLUE));
                        }

                        // Replace the fruit
                        fruits.clear();
                        fruits.add(new Fruit(Random.randint(1, SCREEN_WIDTH / SQUARE_SIZE - 1) * SQUARE_SIZE, Random.randint(1, SCREEN_HEIGHT / SQUARE_SIZE - 1) * SQUARE_SIZE, Color.GREEN));

                        // Play sound effects
                        if (sound_fx) {
                            increaseSound.play();
                        }

                        if (mode.equals("Classic") || mode.equals("Obstacle Course")) {  // Game mode specific changes
                            score++;
                            squareTickerMax = (int) Math.max(5, squareTickerMax - SPEED_INCREASE);  // Don't let it get too fast
                            timeLeft += TIME_INCREASE;
                        }
                    }
                }
            }

            if (number + 1 != squares.size() && player == 1) {  // not the last square, and belongs to player 1
                squares.get(number + 1).queue.add(this.queue.get(0)); // Add the direction to the next square
            }

            if (number + 1 != squares2.size() && player == 2) {  // Fixed: was checking player 1 instead of 2
                squares2.get(number + 1).queue.add(this.queue.get(0));
            }

            this.queue.remove(0);  // remove the current direction

            this.draw(window);
        }

        /**
         * Draws the square object onto the screen
         *
         * @param window  Display object to draw on
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
    private static class Fruit extends Square {
        private int timer;

        /**
         * Constructor for the fruit object
         *
         * @param x     x-coordinate
         * @param y     y-coordinate
         * @param color color of fruit
         */
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

    /**
     * Class for the obstacle object, also an extension of Square
     */
    private static class Obstacle extends Square {
        /**
         * Constructor for the Obstacle object
         *
         * @param x x-coordinate
         * @param y y-coordinate
         */
        public Obstacle(int x, int y) {
            super(x, y, Color.GRAY);
        }

        /**
         * Main method for the obstacle. Does nothing but draw it
         *
         * @param window Display object to draw on
         */
        public void main(Display window) {
            window.drawRectangle(this.drawRect, color, false);
        }
    }

}
