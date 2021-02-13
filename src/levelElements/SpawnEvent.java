package levelElements;

import gameState.ShadowDefend;
import slicers.Enemy;

/**
 * Spawns a number of enemies at regular intervals
 */
public class SpawnEvent implements WaveEvent {
    private int currEnemyNum = 0;
    private int enemyNum;
    private String enemyType;
    private double spawnDelay;
    private boolean firstSpawn = true;
    private boolean finished = false;
    private double frames = 0;

    /**
     * Create new spawn event
     *
     * @param enemyNum Number of slicers to spawn
     * @param enemyType Type of spawn
     * @param spawnDelay Time to wait between spawns
     */
    public SpawnEvent(int enemyNum, String enemyType, int spawnDelay) {
        this.enemyNum = enemyNum;
        this.enemyType = enemyType;
        this.spawnDelay = spawnDelay;
    }

    /**
     * Spawn new slicers depending on how much time has passed
     */
    @Override
    public void runEvent() {
        frames += ShadowDefend.getTimescale();
        updateSpawn();
    }

    /**
     * Update timer and spawn if delay has passed
     */
    private void updateSpawn() {
        if (firstSpawn) { // spawn first enemy since no delay in the beginning
            spawnEnemy();
            currEnemyNum++;
            firstSpawn = false;
        }
        // check if spawn delay has passed
        double targetSeconds = currEnemyNum * spawnDelay;
        if (frames/FPS*IN_MILISECONDS >= targetSeconds) {
            if (currEnemyNum < enemyNum) {
                spawnEnemy();
                currEnemyNum++;
            } else {
                finished = true;
            }
        }
    }

    /**
     * Add new enemy to the map
     */
    private void spawnEnemy() {
        Enemy newEnemy = Enemy.setEnemyType(enemyType);
        ShadowDefend.addToSprites(newEnemy);
        Wave.addToEnemyList(newEnemy);
    }

    /**
     * @return If all slicers have been spawned
     */
    public boolean isFinished() {
        return finished;
    }
}