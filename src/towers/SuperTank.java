package towers;

import bagel.Image;
import bagel.util.Point;

/**
 * Stronger type of active tower
 */
public class SuperTank extends ActiveTower {
    private static final Image IMAGE = new Image("res/images/supertank.png");
    private static final int RANGE = 150;
    private static final int COOLDOWN = 500;
    private static final int DAMAGE = 3;
    private static final String PROJECTILE_IMAGE_FILENAME = "res/images/supertank_projectile.png";

    /**
     * Create new supertank at the location
     *
     * @param startPoint Where supertank is to be placed
     */
    public SuperTank(Point startPoint) {
        super(IMAGE, startPoint,RANGE,COOLDOWN,PROJECTILE_IMAGE_FILENAME,DAMAGE);
    }
}
