package slicers;

import bagel.Image;
import bagel.util.Point;
import gameState.Player;
import gameState.Sprite;
import towers.RangeAttacker;

import java.util.List;
import java.util.ArrayList;

/**
 * Entities that traverse the level's polyline, must be destroyed by player's towers
 */
public abstract class Enemy extends Sprite {
    // assumes that there will always be at least 2 points from polyline point list
    protected static final int INIT_NEXT_INDEX = 1;

    private int health;
    private final int reward;
    private final int penalty;
    private int nextPointIndex;
    private boolean reachedEnd;

    private static List<Point> points;
    private static ArrayList<RangeAttacker> rangeAttackers = new ArrayList<RangeAttacker>();

    /**
     * Create new enemy
     *
     * @param image             What to render at the enemy's position
     * @param health            Units of health that can be lost before dying
     * @param speed             How fast enemy can move through polyline
     * @param reward            Cash player gains upon death
     * @param penalty           Lives player lose when end is reached
     */
    protected Enemy(Image image, int health, double speed, int reward, int penalty) {
        super(image, speed, points.get(0));
        this.health = health;
        this.reward = reward;
        this.penalty = penalty;
        this.nextPointIndex = INIT_NEXT_INDEX;
        this.reachedEnd = false;
    }

    /**
     * Factory-esque method to create an instance of the desired enemy type
     *
     * @param enemyType Name of enemy type
     * @return New instance of enemy type
     */
    public static Enemy setEnemyType(String enemyType) {
        if (enemyType.equals("superslicer")) {
            return new SuperSlicer();
        } else if (enemyType.equals("megaslicer")) {
            return new MegaSlicer();
        } else if (enemyType.equals("apexslicer")) {
            return new ApexSlicer();
        } else { // return default regular slicer
            return new RegularSlicer();
        }
    }

    /**
     * Move enemy and check for possible attackers
     */
    @Override
    public void update() {
        // set new location
        Point nextPoint = points.get(nextPointIndex);
        this.move(nextPoint);

        // check if next point has been reached
        if (this.hasReachedPoint(nextPoint)) {
            if (nextPointIndex + 1 < points.size()) { // move on to the next point
                nextPointIndex = nextPointIndex + 1;
            } else {
                Player.getInstance().loseLives(penalty);
                reachedEnd = true;
            }
        }

        // check if in range of an attacker with radius
        for (RangeAttacker rangeAttacker: rangeAttackers) {
            if (getRect().intersects(rangeAttacker.getRadius()) && !rangeAttacker.isTargeting(this)) {
                rangeAttacker.addNewTarget(this);
            }
        }
        rangeAttackers.removeIf(sprite -> ((Sprite) sprite).isRetired());
    }

    /**
     * @return Whether the enemy is dead or has reached the end
     */
    public boolean isRetired() {
        return (health<=0 || reachedEnd);
    }

    /**
     * Change the polyline to be traversed
     *
     * @param newPoints Points which make up the new polyline
     */
    public static void setPoints(List<Point> newPoints) {
        points = newPoints;
    }

    /**
     * Change the index in list of points of the next point to reach
     *
     * @param nextPointIndex Index of new point destination
     */
    public void setNextPointIndex(int nextPointIndex) {
        this.nextPointIndex = nextPointIndex;
    }

    /**
     * Add to the list of all attackers with a radius
     *
     * @param newRadius New sprite with radius
     */
    public static void addToRangeList(RangeAttacker newRadius) {
        rangeAttackers.add(newRadius);
    }

    /**
     * Receive damage from projectile/explosive
     *
     * @param damage Amount of health lost
     */
    public void loseHealth(int damage) {
        health -= damage;
    }

    /**
     * @return Enemy's current health
     */
    public int getHealth() {
        return health;
    }

    /**
     * @return Amount of cash received upon death
     */
    public int getReward() {
        return reward;
    }

    /**
     * @return Index in points list of next location to reach
     */
    public int getNextPointIndex() {
        return nextPointIndex;
    }
}