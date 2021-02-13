package slicers;

import bagel.Image;

/**
 * Mid-tier spawner enemy type
 */
public class MegaSlicer extends SpawnerSlicer {
    private static final Image IMAGE = new Image("res/images/megaslicer.png");
    public static final int HEALTH = SuperSlicer.HEALTH * 2;
    public static final double SPEED = SuperSlicer.SPEED;
    private static final int REWARD = 10;
    private static final int CHILD_NUM = 2;
    private static final String CHILD_TYPE = "superslicer";
    public static final int PENALTY = CHILD_NUM * SuperSlicer.PENALTY;

    public MegaSlicer() {
        super(IMAGE, HEALTH, SPEED, REWARD, PENALTY, CHILD_NUM, CHILD_TYPE);
    }
}
