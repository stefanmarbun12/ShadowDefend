package towers;

import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import gameState.ShadowDefend;
import slicers.Enemy;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Tower that moves through the map once, dropping explosives at arbitrary intervals
 */
public abstract class PassiveTower extends Tower {
    private static final double HORIZONTAL_ANGLE = 22.0/7/2;
    private static final double VERTICAL_ANGLE = 22.0/7;

    private final int minDropTime;
    private final int maxDropTime;
    private double dropTime;
    private final int explosiveDamage;
    private final int explosiveRange;
    private final Image explosiveImage;
    private final int delay;
    private final Point direction;
    private boolean orientation;
    private double frames = 0;

    /**
     * Create new passive tower
     *
     * @param image             What to render on tower's position
     * @param speed             How fast tower moves through the map
     * @param startPoint        Where tower is first placed
     * @param minDropTime       Least possible delay between drops
     * @param maxDropTime       Longest possible delay between drops
     * @param explosiveDamage   How much damage inflicted to enemies within explosive's radius
     * @param explosiveRange    How far the explosive can inflict damage
     * @param explosiveImage    Name of explosive's image file
     * @param delay             How long until detonation after explosive is dropped
     * @param horizontal        If the tower is to move horizontal or not
     */
    public PassiveTower(Image image, int speed, Point startPoint, int minDropTime, int maxDropTime,
                        int explosiveDamage, int explosiveRange, Image explosiveImage, int delay, boolean horizontal) {
        super(image, speed, startPoint);
        // set its starting location outside of the map, depending on its orientation
        if (horizontal) {
            direction = new Point(Window.getWidth(),startPoint.y);
            this.setLocation(new Point(0,startPoint.y));
        } else {
            direction = new Point(startPoint.x,Window.getHeight());
            this.setLocation(new Point(startPoint.x,0));
        }
        this.orientation = horizontal;
        this.minDropTime = minDropTime;
        this.maxDropTime = maxDropTime;
        this.dropTime = getDropTime();
        this.explosiveDamage = explosiveDamage;
        this.explosiveRange = explosiveRange;
        this.explosiveImage = explosiveImage;
        this.delay = delay;
    }

    /**
     * Update location and drop explosive once drop time has passed
     */
    @Override
    public void update() {
        move(direction);
        getRotation().setRotation(orientation ? HORIZONTAL_ANGLE : VERTICAL_ANGLE);
        frames += ShadowDefend.getTimescale();
        if (frames/FPS >= dropTime) {
            dropExplosive();
            dropTime = getDropTime();
            frames = 0;
        }
    }

    /**
     * @return Whether tower has reached other side of the map
     */
    @Override
    public boolean isRetired() {
        return hasReachedPoint(direction);
    }

    /**
     * @return Randomly generated value between the range of drop times
     */
    private double getDropTime() {
        return ThreadLocalRandom.current().nextDouble(minDropTime, maxDropTime);
    }

    /**
     * Place new explosive at the tower's current location
     */
    private void dropExplosive() {
        Explosive newExplosive = new Explosive(explosiveImage,getLocation(),delay,explosiveDamage,explosiveRange);
        ShadowDefend.addToQueue(newExplosive);
        Enemy.addToRangeList(newExplosive);
    }
}
