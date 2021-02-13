package slicers;

import bagel.Image;

/**
 * Basic enemy type
 */
public class RegularSlicer extends Enemy {
    private static final Image IMAGE = new Image("res/images/slicer.png");
    public static final int HEALTH = 1;
    public static final int SPEED = 1;
    private static final int REWARD = 2;
    public static final int PENALTY = 1;

    public RegularSlicer() {
        super(IMAGE, HEALTH, SPEED, REWARD, PENALTY);
    }
}
