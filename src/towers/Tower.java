package towers;

import bagel.Image;
import bagel.util.Point;
import gameState.Sprite;

/**
 * Entities that can be placed by the player to inflict damage on slicers
 */
public abstract class Tower extends Sprite {
    private boolean retired = false;

    /**
     * Create new tower
     *
     * @param image             What to render on tower's position
     * @param speed             How fast tower moves, if it does
     * @param startPoint        Where tower is first placed
     */
    protected Tower(Image image, int speed, Point startPoint) {
        super(image, speed, startPoint);
    }

    /**
     * @return Whether tower must be cleared from the map
     */
    @Override
    public boolean isRetired() {
        return retired;
    }

    /**
     * Factory-esque method to create an instance of the desired tower type
     *
     * @param towerType     Name of tower type
     * @param location      Where the new tower is to be placed
     * @return New instance of the tower type at the location specified
     */
    public static Tower setTowerType(String towerType, Point location) {
        if (towerType.equals("supertank")) {
            return new SuperTank(location);
        } else if (towerType.equals("airplane")) {
            return new Airplane(location);
        } else { // return default regular slicer
            return new Tank(location);
        }
    }

    /**
     * Used to set as retired when a level is completed
     */
    public void setRetired() {
        retired = true;
    }
}
