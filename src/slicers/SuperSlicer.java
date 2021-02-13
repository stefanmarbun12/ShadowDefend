package slicers;

import bagel.Image;

/**
 * Basic spawner enemy type
 */
public class SuperSlicer extends SpawnerSlicer {
    private static final Image IMAGE = new Image("res/images/superslicer.png");
    public static final int HEALTH = RegularSlicer.HEALTH * 2;
    public static final double SPEED = RegularSlicer.SPEED * 0.75;
    private static final int REWARD = 15;
    private static final int CHILD_NUM = 2;
    private static final String CHILD_TYPE = "slicer";
    public static final int PENALTY = CHILD_NUM * RegularSlicer.PENALTY;

    public SuperSlicer() {
        super(IMAGE, HEALTH, SPEED, REWARD, PENALTY, CHILD_NUM, CHILD_TYPE);
    }
}
