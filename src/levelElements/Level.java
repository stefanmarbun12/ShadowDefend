package levelElements;

import bagel.*;
import bagel.map.TiledMap;
import bagel.util.Colour;
import bagel.util.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import gameState.Player;
import gameState.Renderable;
import gameState.ShadowDefend;
import slicers.Enemy;
import towers.Airplane;

/**
 * A game level to be played with its own map and waves
 */
public class Level implements Renderable {
    private static final int BASE_AWARD = 150;
    private static final int MULTIPLIER = 100;
    private static final String BLOCKED_PROPERTY = "blocked";
    private static final Font FONT1 = new Font("res/fonts/DejaVuSans-bold.ttf",60);
    private static final Font FONT2 = new Font("res/fonts/DejaVuSans-bold.ttf",30);
    private static final Font FONT3 = new Font("res/fonts/DejaVuSans-bold.ttf",15);
    private static final String FILE_ERROR_MESSAGE = "Wave file not found.";
    private static final String GAME_OVER_TEXT = "GAME OVER";
    private static final String EXIT_TEXT = "Press ESC to exit";
    private static final Point GAMEOV_POS = new Point(330,383);
    private static final Point NO_WON_POS = new Point(335,410);
    private static final Point EXIT_TXT_POS = new Point(420,460);
    public static final Keys START_WAVE_KEY = Keys.S;
    private static final String SEPARATOR = ",";

    private Wave currWave;
    private ArrayList<String[]> waveInfoList = new ArrayList<String[]>();
    private boolean waveInProgress = false;
    private boolean complete = false;
    private int waveNum = 0;
    private int currWaveNum = 0;

    private static TiledMap map;

    /**
     * Create new level
     *
     * @param mapFileName Name of file containing map info
     * @param waveFileName Name of file containing wave info
     */
    public Level(String mapFileName, String waveFileName){
        map = new TiledMap(mapFileName);
        Enemy.setPoints(map.getAllPolylines().get(0));
        loadWaveInfoList(waveFileName);
        Player.getInstance().reset();
        Airplane.reset();
    }

    /**
     * Show map in game window
     */
    public void render() {
        map.draw(0,0,0,0, Window.getWidth(),Window.getWidth());
    }

    /**
     * Update level state and its waves
     */
    public void update() {
        if (!waveInProgress && ShadowDefend.getInput().wasPressed(START_WAVE_KEY)) { // start new wave
            currWaveNum++;
            currWave = new Wave(waveInfoList, currWaveNum);
            waveInProgress = true;
        }

        if (waveInProgress) {
            currWave.runWave();
            if (currWave.isFinished()) {
                Player.getInstance().getReward(BASE_AWARD + currWaveNum * MULTIPLIER);
                currWave = null;
                waveInProgress = false;
                if (currWaveNum == waveNum) {
                    complete = true;
                }
            }
        }
    }

    /**
     * Get lines of wave info from the wave file
     *
     * @param waveFileName Name of wave file
     */
    private void loadWaveInfoList(String waveFileName) {
        try {
            Scanner fileReader = new Scanner(new File(waveFileName));
            while (fileReader.hasNextLine()) { // get info for each wave
                String[] waveLine = fileReader.nextLine().split(SEPARATOR);
                if (Integer.parseInt(waveLine[Wave.WAVE_NUM_INDEX]) == waveNum+1) {
                    waveNum++;
                }
                waveInfoList.add(waveLine);
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println(FILE_ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * @return If the level has completed all its waves
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * @return If the level has a wave in progress
     */
    public boolean isWaveInProgress() {
        return waveInProgress;
    }

    /**
     * @return The number of the wave in progress
     */
    public int getCurrWaveNum() {
        return currWaveNum;
    }

    /**
     * @param point Location on the map
     * @return Whether the point on the map is a blocked tile
     */
    public static boolean isBlocked(Point point) {
        return map.hasProperty((int) point.x, (int) point.y, BLOCKED_PROPERTY);
    }

    /**
     * Show a game over screen
     */
    public static void showGameOver() {
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), Colour.BLACK);
        FONT1.drawString(GAME_OVER_TEXT,GAMEOV_POS.x,GAMEOV_POS.y);
        FONT2.drawString(String.format("%d levels won", Player.getInstance().getLevelsWon()),NO_WON_POS.x,NO_WON_POS.y);
        FONT3.drawString(EXIT_TEXT,EXIT_TXT_POS.x,EXIT_TXT_POS.y);
    }
}
