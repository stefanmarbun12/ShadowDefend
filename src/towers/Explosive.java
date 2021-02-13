package towers;

import bagel.Image;
import bagel.util.Rectangle;
import bagel.util.Point;
import gameState.Player;
import gameState.ShadowDefend;
import gameState.Sprite;
import slicers.Enemy;
import slicers.SpawnerSlicer;

import java.util.ArrayList;

/**
 * Sprite that remains on a location until it can detonate, damaging all enemies within its radius
 */
public class Explosive extends Sprite implements RangeAttacker {
    private static final int SPEED = 0;

    private int delay;
    private int damage;
    private Rectangle radius;
    private int frames;
    private ArrayList<Enemy> enemiesInRange;

    /**
     * Create new explosive with values specific to tower type
     *
     * @param image         What to render in explosive's position
     * @param startPoint    Where explosive is dropped
     * @param delay         How long until detonation
     * @param damage        How much damage inflicted to enemies within range
     * @param range         How far damage can be inflicted
     */
    public Explosive(Image image, Point startPoint, int delay, int damage, int range) {
        super(image, SPEED, startPoint);
        this.delay = delay;
        this.damage = damage;
        radius = new Rectangle(new Point(startPoint.x-range,startPoint.y-range),range*2,range*2);
        enemiesInRange = new ArrayList<Enemy>();
    }

    /**
     * @return Whether the explosive has detonated or not
     */
    @Override
    public boolean isRetired() {
        return (frames/Sprite.FPS * Sprite.IN_MILISECONDS >= delay);
    }

    /**
     * Update timer and detonate once delay has passed
     */
    @Override
    public void update() {
        frames += ShadowDefend.getTimescale();
        enemiesInRange.removeIf(enemy -> !enemy.getRect().intersects(radius) || enemy.isRetired());
        if (isRetired()) {
            for (Enemy enemy: enemiesInRange) {
                enemy.loseHealth(damage);
                if (enemy.isRetired()) {
                    if (enemy instanceof SpawnerSlicer) {
                        ((SpawnerSlicer) enemy).spawnChild();
                    }
                    Player.getInstance().getReward(enemy.getReward());
                }
            }
        }
    }

    /**
     * @return Bounding box of explosive's radius
     */
    @Override
    public Rectangle getRadius() { return radius; }

    /**
     * Add a new enemy to potentially damage
     *
     * @param newEnemy Enemy that has moved into the explosive's radius
     */
    @Override
    public void addNewTarget(Enemy newEnemy) {
        enemiesInRange.add(newEnemy);
    }

    /**
     * @param target Enemy being checked
     * @return Whether the enemy is in the tower's list of targets
     */
    @Override
    public boolean isTargeting(Enemy target) {
        return enemiesInRange.contains(target);
    }
}
