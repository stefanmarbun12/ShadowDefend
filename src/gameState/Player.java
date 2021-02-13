package gameState;

/**
 * A singleton class representing the game's player
 */
public class Player {
    private static int STARTING_LIVES = 25;
    private static int STARTING_MONEY = 500;

    private static Player playerInstance;
    private int lives;
    private int money;
    private int levelsWon;

    /**
     * Create new instance of player with starting values
     */
    private Player() {
        lives = STARTING_LIVES;
        money = STARTING_MONEY;
        levelsWon = 0;
    }

    /**
     * @return Instance of Player
     */
    public static Player getInstance() {
        if (playerInstance==null) {
            playerInstance = new Player();
        }
        return playerInstance;
    }

    /**
     * @return Number of lives left
     */
    public int getLives() {
        return lives;
    }

    /**
     * @return Amount of money left
     */
    public int getMoney() {
        return money;
    }

    /**
     * @return Number of levels player has won
     */
    public int getLevelsWon() {
        return levelsWon;
    }

    /**
     * Get penalised when a slicer reaches end of map
     *
     * @param penalty Number of lives taken by slicer
     */
    public void loseLives(int penalty) {
        lives-= penalty;
    }

    /**
     * Lose money from buying towers
     *
     * @param price Cost of tower
     */
    public void spendMoney(int price) {
        money -= price;
    }

    /**
     * Receive money for killing slicers or finishing a wave
     *
     * @param reward Amount of money gained
     */
    public void getReward(int reward) {
        money += reward;
    }

    /**
     * Update number of levels player has won
     */
    public void winLevel() { levelsWon++; }

    /**
     * Reset to starting values when a new level starts
     */
    public void reset() {
        money = STARTING_MONEY;
        lives = STARTING_LIVES;
    }
}
