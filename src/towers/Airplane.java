package towers;

import bagel.Image;
import bagel.util.Point;

/**
 * Type of passive tower that alternates its path orientation with each tower placed
 */
public class Airplane extends PassiveTower {
    private static final Image IMAGE = new Image("res/images/airsupport.png");
    private static final int SPEED = 3;
    private static final int MIN_DROPTIME = 1;
    private static final int MAX_DROPTIME = 2;
    private static final int EXPLOSIVE_DAMAGE = 500;
    private static final int EXPLOSIVE_RANGE = 200;
    private static final int DELAY = 2;
    private static final Image EXPLOSIVE_IMAGE = new Image("res/images/explosive.png");

    private static boolean horizontal = true;

    /**
     * Create new airplane that flies from the x/y coordinate of point specified
     *
     * @param startPoint To get the plane's starting x/y coordinate
     */
    public Airplane(Point startPoint) {
        super(IMAGE, SPEED,startPoint,MIN_DROPTIME,MAX_DROPTIME,
                EXPLOSIVE_DAMAGE,EXPLOSIVE_RANGE,EXPLOSIVE_IMAGE,DELAY,horizontal);
        horizontal = !horizontal;
    }

    /**
     * Return orientation to horizontal in case the last plane placed before the level was completed flew horizontally
     */
    public static void reset() {
        horizontal = true;
    }
}
