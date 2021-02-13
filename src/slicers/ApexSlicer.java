package slicers;

import bagel.Image;

/**
 * Toughest enemy type
 */
public class ApexSlicer extends SpawnerSlicer {
    private static final Image IMAGE = new Image("res/images/apexslicer.png");
    public static final int HEALTH = RegularSlicer.HEALTH * 25;
    public static final double SPEED = MegaSlicer.SPEED * 0.5;
    private static final int REWARD = 150;
    private static final int CHILD_NUM = 4;
    private static final String CHILD_TYPE = "megaslicer";
    public static final int PENALTY = CHILD_NUM * MegaSlicer.PENALTY;

    public ApexSlicer() {
        super(IMAGE, HEALTH, SPEED, REWARD, PENALTY, CHILD_NUM, CHILD_TYPE);
    }
}
