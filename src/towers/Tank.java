package towers;

import bagel.Image;
import bagel.util.Point;

/**
 * Weaker type of active tower
 */
public class Tank extends ActiveTower {
    private static final Image IMAGE = new Image("res/images/tank.png");
    private static final int RANGE = 100;
    private static final int COOLDOWN = 1000;
    private static final int DAMAGE = 1;
    private static final String PROJECTILE_IMAGE_FILENAME = "res/images/tank_projectile.png";

    /**
     * Create new tank at the location
     *
     * @param startPoint Where tank is to be placed
     */
    public Tank(Point startPoint) {
        super(IMAGE, startPoint,RANGE,COOLDOWN,PROJECTILE_IMAGE_FILENAME,DAMAGE);
    }
}
