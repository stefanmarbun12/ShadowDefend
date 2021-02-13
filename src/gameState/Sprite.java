package gameState;

import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Rectangle;
import bagel.util.Point;
import bagel.util.Vector2;

import java.lang.Math;

/**
 * Represents renderables that can move around in the game window
 */
public abstract class Sprite implements Renderable {
    protected static final int FPS = 60;
    protected static final int IN_MILISECONDS = 1000;

    private final Image figure;
    private Rectangle rect;
    private Point location;
    private DrawOptions rotation;
    private double speed;

    /**
     * Create a new game entity
     *
     * @param imageFileName To get the image to be rendered at the sprite's location
     * @param speed         How fast the sprite can move
     * @param startPoint    Initial location
     */
    public Sprite(String imageFileName, double speed, Point startPoint) {
        this.figure = new Image(imageFileName);
        this.rotation = new DrawOptions();
        this.rect = figure.getBoundingBoxAt(startPoint);
        this.location = rect.centre();
        this.speed = speed;
    }

    /**
     * Constructor for flyweight pattern
     *
     * @param image         What to show at the sprite's location
     * @param speed         How fast the sprite can move
     * @param startPoint    Initial location
     */
    public Sprite(Image image, double speed, Point startPoint) {
        this.figure = image;
        this.rotation = new DrawOptions();
        this.rect = figure.getBoundingBoxAt(startPoint);
        this.location = rect.centre();
        this.speed = speed;
    }

    /**
     * Show the sprite in the game window
     */
    @Override
    public void render() { figure.draw(rect.centre().x, rect.centre().y, rotation); }

    /**
     * Move the sprite to a new Point position
     *
     * @param nextPoint New location to be reached
     */
    public void move(Point nextPoint) {
        location = rect.centre();
        int timescale = ShadowDefend.getTimescale();
        // set velocity
        double newX = location.x;
        double newY = location.y;
        Vector2 velocity = new Vector2(nextPoint.x - newX,nextPoint.y - newY);
        velocity = velocity.normalised();

        // get new location
        newX += velocity.x * speed * timescale;
        newY += velocity.y * speed * timescale;
        location = new Point(newX, newY);
        rect = figure.getBoundingBoxAt(location);

        // set rotation
        rotation.setRotation(Math.atan2(velocity.y,velocity.x));
    }

    /**
     * Rotate sprite towards another sprite
     *
     * @param sprite Object to look at
     */
    public void faceTowards(Sprite sprite) {
        location = rect.centre();
        // set velocity
        double newX = location.x;
        double newY = location.y;
        Vector2 velocity = new Vector2(sprite.getLocation().x - newX,sprite.getLocation().y - newY);
        velocity = velocity.normalised();

        // set rotation
        rotation.setRotation(Math.atan2(velocity.x,-velocity.y));
    }

    /**
     * @param nextPoint A location that the sprite must move to
     * @return If the point has been reached
     */
    public boolean hasReachedPoint(Point nextPoint) {
        Vector2 target = nextPoint.asVector();
        Vector2 current = location.asVector();
        Vector2 distance = target.sub(current);
        double magnitude = distance.length();
        // check if close to target
        if (magnitude < speed * ShadowDefend.getTimescale()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return If the sprite can no longer be used or rendered
     */
    public abstract boolean isRetired();

    /**
     * Change position of the sprite on the window
     *
     * @param newLocation Where sprite should be shown
     */
    public void setLocation(Point newLocation) {
        location = newLocation;
        rect = figure.getBoundingBoxAt(location);
    }

    /**
     * @return Current position of sprite
     */
    public Point getLocation() {
        return location;
    }

    /**
     * @return Sprite's bounding box
     */
    public Rectangle getRect() {
        return rect;
    }

    /**
     * @return Rotation of sprite's image
     */
    public DrawOptions getRotation() {
        return rotation;
    }

    /**
     * @return Sprite's image
     */
    public Image getFigure() {
        return figure;
    }
}
