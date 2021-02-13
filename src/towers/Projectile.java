package towers;

import bagel.util.Point;
import gameState.Player;
import gameState.ShadowDefend;
import gameState.Sprite;
import slicers.Enemy;
import slicers.SpawnerSlicer;

/**
 * Sprite that moves toward a target and inflicts damage once it is reached
 */
public class Projectile extends Sprite {
    private static final int SPEED = 10;
    private static final int ERROR_FACTOR = 6;

    private Enemy target;
    private boolean reachedTarget;
    private ProjectileType type;

    /**
     * Create new projectile with values specific to tower type
     *
     * @param type          Type returned from projectile factory
     * @param startPoint    Location of tower that shot the projectile
     * @param target        Enemy to move towards
     */
    public Projectile(ProjectileType type, Point startPoint, Enemy target) {
        super(type.getImage(), SPEED, startPoint);
        this.type = type;
        this.target = target;
        reachedTarget = false;
    }

    /**
     * Move toward target and inflict damage once it is close enough
     */
    @Override
    public void update() {
        move(target.getLocation());
        if (getLocation().distanceTo(target.getLocation()) <= ShadowDefend.getTimescale()*ERROR_FACTOR &&
                !target.isRetired()) {
            target.loseHealth(type.getDamage());
            if (target.getHealth() <= 0) {
                Player.getInstance().getReward(target.getReward());
                if (target instanceof SpawnerSlicer) {
                    ((SpawnerSlicer) target).spawnChild();
                }
            }
            reachedTarget = true;
        }
    }

    /**
     * @return Whether target has been reached or the target has been killed by something else first
     */
    @Override
    public boolean isRetired() {
        return (reachedTarget || target.isRetired());
    }
}