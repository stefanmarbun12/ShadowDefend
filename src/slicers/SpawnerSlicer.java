package slicers;

import bagel.Image;
import gameState.ShadowDefend;
import levelElements.Wave;

/**
 * Subclass of enemy with the ability to spawn children upon death
 */
public class SpawnerSlicer extends Enemy {
    private final int childNum;
    private final String childType;

    /**
     * Create new spawner slicer
     *
     * @param image             What to render at the enemy's position
     * @param health            Units of health that can be lost before dying
     * @param speed             How fast enemy can move through polyline
     * @param reward            Cash player gains upon death
     * @param penalty           Lives player lose when end is reached
     * @param childNum          Number of children to spawn upon death
     * @param childType         Enemy type of the spawned children
     */
    public SpawnerSlicer(Image image, int health, double speed, int reward,
                         int penalty, int childNum, String childType) {
        super(image, health, speed, reward, penalty);
        this.childNum = childNum;
        this.childType = childType;
    }

    /**
     * Spawn a number of new enemies at the location it was eliminated
     */
    public void spawnChild() {
        for (int i=0;i<childNum;i++) {
            Enemy newEnemy = Enemy.setEnemyType(childType);
            newEnemy.setLocation(this.getLocation());
            newEnemy.setNextPointIndex(this.getNextPointIndex());
            ShadowDefend.addToQueue(newEnemy);
            Wave.addToEnemyList(newEnemy);
        }
    }
}
