package gameState;

import bagel.*;
import levelElements.Level;
import panels.BuyPanel;
import panels.StatusPanel;
import towers.Tower;

import java.io.File;
import java.util.ArrayList;

/**
 * Shadow Defend, a tower defense game
 */
public class ShadowDefend extends AbstractGame {
    private static final int DEFAULT_TIMESCALE = 1;
    public static final int MIN_TIMESCALE = 1;
    private static final int MAX_TIMESCALE = 5;
    private static final int CONTROL_FACTOR = 1;
    private static final int TOTAL_LEVELS = 2;
    private static final int INIT_LEVEL_NUM = 1;
    private static final int LEVEL_FILES_NUM = 2;
    private static final String LEVEL_FILES_DIRECTORY = "res/levels/";
    private static final String WAVE_FILE_DIRECTORY = "res/levels/waves.txt";
    private static final String LEVEL_FILE_TYPE= ".tmx";
    public static final Keys INCREASE_TIMESCALE = Keys.L;
    public static final Keys DECREASE_TIMESCALE = Keys.K;

    private boolean levelInProgress = false;
    private ArrayList<String[]> levelFilesList = new ArrayList<String[]>();;
    private Level currLevel;
    private int currLevelNum = 0;
    private Renderable[] panelsToRender = {StatusPanel.getInstance(),BuyPanel.getInstance()};

    private static Input input;
    private static ArrayList<Sprite> sprites = new ArrayList<Sprite>();
    private static ArrayList<Sprite> spriteQueue = new ArrayList<Sprite>();
    private static int timescale = DEFAULT_TIMESCALE;

    /**
     * Entry point for the game
     *
     * @param args Optional command-line arguments
     */
    public static void main(String[] args) {
        // create new instance of game and run it
        new ShadowDefend().run();
    }

    /**
     * Create new instance of the game
     */
    public ShadowDefend(){
        loadLevels(new File(LEVEL_FILES_DIRECTORY).listFiles());
    }

    /**
     * Update state of the game
     *
     * @param input Current state from keyboard or mouse
     */
    @Override
    protected void update(Input input) {
        ShadowDefend.input = input;
        if (input.isDown(Keys.ESCAPE)) {
            System.exit(0);
        }

        updateTimescale(input);

        if (!levelInProgress) {
            startNewLevel();
        }
        updateLevel();

        // render panels
        for (Renderable panel: panelsToRender) {
            panel.render();
            panel.update();
        }
        renderAllSprites();

        if (Player.getInstance().getLives() <= 0) { // game is over
            Level.showGameOver();
        }
    }

    /**
     * @return Timescale value
     */
    public static int getTimescale() {
        return timescale;
    }

    /**
     * Add a new element to be rendered in the window
     *
     * @param newSprite New element to render
     */
    public static void addToSprites(Sprite newSprite) {
        sprites.add(newSprite);
    }

    /**
     * Enqueue a new element to be added to the list of sprites
     *
     * @param newSprite New element to render
     */
    public static void addToQueue(Sprite newSprite) {
        spriteQueue.add(newSprite);
    }

    /**
     * @return Mouse/keyboard input
     */
    public static Input getInput() {
        return input;
    }

    /**
     * Change timescale according to input
     * @param input Key pressed by player
     */
    private void updateTimescale(Input input) {
        if (input.wasPressed(INCREASE_TIMESCALE) && (timescale + CONTROL_FACTOR <= MAX_TIMESCALE)) {
            timescale += CONTROL_FACTOR;
        }
        if (input.wasPressed(DECREASE_TIMESCALE) && (timescale - CONTROL_FACTOR >= MIN_TIMESCALE)) {
            timescale -= CONTROL_FACTOR;
        }
    }

    /**
     * Get level filenames from all files in directory
     *
     * @param files List of files from level folder
     */
    private void loadLevels(File[] files) {
        boolean addingFiles = true;
        int levelNum = INIT_LEVEL_NUM;
        String[] levelFileNames;
        while (addingFiles) {
            levelFileNames = new String[LEVEL_FILES_NUM];
            levelFileNames[1] = WAVE_FILE_DIRECTORY;
            boolean fileFound = false;
            for (File file: files) {
                if (file.getName().equals(levelNum + LEVEL_FILE_TYPE)) {
                    levelFileNames[0] = LEVEL_FILES_DIRECTORY+file.getName();
                    fileFound = true;
                    break;
                }
            }
            if (fileFound) {
                levelFilesList.add(levelFileNames);
                levelNum++;
            } else {
                addingFiles = false;
            }
        }
    }

    /**
     * Clear the game state and start a new level
     */
    private void startNewLevel() {
        sprites.stream()
                .filter(toRender -> toRender instanceof Tower)
                .forEach(toRender->((Tower) toRender).setRetired());
        sprites.removeIf(Sprite::isRetired);
        String[] levelFileNames = levelFilesList.get(currLevelNum);
        currLevel = new Level(levelFileNames[0],levelFileNames[1]);
        levelInProgress = true;
        StatusPanel.getInstance().setCurrLevel(currLevel);
    }

    /**
     * Update level state and finish level if complete
     */
    private void updateLevel() {
        if (levelInProgress && !currLevel.isComplete()) {
        currLevel.render();
        currLevel.update();
        } else if (currLevel.isComplete()) {
            Player.getInstance().winLevel();
            if (currLevelNum < TOTAL_LEVELS-1) {
                levelInProgress = false;
                currLevel = null;
                currLevelNum++;
            } else {
                currLevel.render();
            }
        }
    }

    /**
     * Render all existing sprites and update list
     */
    private void renderAllSprites() {
        for (Renderable toRender: sprites) {
            toRender.render();
            toRender.update();
        }
        sprites.removeIf(Sprite::isRetired);
        sprites.addAll(spriteQueue);
        spriteQueue.clear();
    }
}