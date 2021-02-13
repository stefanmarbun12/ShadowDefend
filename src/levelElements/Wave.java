package levelElements;

import slicers.Enemy;

import java.util.ArrayList;

/**
 * Comprised of wave events involving enemy spawning and time delays
 */
public class Wave {
    public static final int WAVE_NUM_INDEX = 0;
    private static final int EVENT_INDEX = 1;
    private static final int SLICER_NUM_INDEX = 2;
    private static final int TYPE_INDEX = 3;
    private static final int DELAY_INDEX = 4;
    private static final String SPAWN_EVENT_NAME = "spawn";
    private static final String DELAY_EVENT_NAME = "delay";

    private static ArrayList<Enemy> enemies = new ArrayList<Enemy>();;
    private ArrayList<WaveEvent> waveEvents = new ArrayList<WaveEvent>();
    private int currWaveEventNum = 0;
    private boolean finished = false;

    /**
     * Create a wave with its list of wave events loaded
     *
     * @param waveInfoList Lines of info about each wave
     * @param waveNum Number of latest wave to be executed
     */
    public Wave(ArrayList<String[]> waveInfoList, int waveNum) {
        for (String[] waveLine: waveInfoList) {
            if (Integer.parseInt(waveLine[WAVE_NUM_INDEX]) == waveNum) {
                loadWaveEvent(waveLine);
            }
        }
    }

    /**
     * Run the current wave event and get the next once finished
     */
    public void runWave() {
        waveEvents.get(currWaveEventNum).runEvent();
        if (waveEvents.get(currWaveEventNum).isFinished()) {
            if (currWaveEventNum < waveEvents.size()-1) { // go to next event
                currWaveEventNum++;
            } else { // check if there are no more enemies on the map
                finished = true;
                for (Enemy enemy: enemies) {
                    if (!enemy.isRetired()) {
                        finished = false;
                    }
                }
            }
        }
        enemies.removeIf(Enemy::isRetired);
    }

    /**
     * Add the wave event described by the strings to the list
     *
     * @param waveLine Array of strings containing wave event info
     */
    private void loadWaveEvent(String[] waveLine) {
        WaveEvent newWaveEvent;
        if (waveLine[EVENT_INDEX].equals(SPAWN_EVENT_NAME)) {
            newWaveEvent = new SpawnEvent(Integer.parseInt(waveLine[SLICER_NUM_INDEX]),
                                            waveLine[TYPE_INDEX],Integer.parseInt(waveLine[DELAY_INDEX]));
        } else if (waveLine[EVENT_INDEX].equals(DELAY_EVENT_NAME)) {
            newWaveEvent = new DelayEvent(Integer.parseInt(waveLine[SLICER_NUM_INDEX]));
        } else {
            newWaveEvent = null;
        }
        waveEvents.add(newWaveEvent);
    }

    /**
     * Add to the wave's list of enemies on the map
     *
     * @param newEnemy Enemy to be added
     */
    public static void addToEnemyList(Enemy newEnemy) {
        enemies.add(newEnemy);
    }

    /**
     * @return Whether the wave has completed all its events
     */
    public boolean isFinished() {
        return finished;
    }
}
