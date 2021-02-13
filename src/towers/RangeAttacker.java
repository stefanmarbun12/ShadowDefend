package towers;

import bagel.util.Rectangle;
import slicers.Enemy;

/**
 * Sprites that can 'target' enemies within its radius
 */
public interface RangeAttacker {
    /**
     * @return Bounding box of sprite's radius
     */
    Rectangle getRadius();

    /**
     * Add a new enemy to potentially attack
     *
     * @param newTarget Enemy that has moved into sprite's radius
     */
    void addNewTarget(Enemy newTarget);

    /**
     * @param target Enemy being checked
     * @return Whether the enemy is in the tower's list of targets
     */
    boolean isTargeting(Enemy target);
}
