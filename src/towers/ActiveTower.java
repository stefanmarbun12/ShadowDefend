package towers;

import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import gameState.Renderable;
import gameState.ShadowDefend;
import slicers.Enemy;

import java.util.ArrayList;

/**
 * Towers that stay at a fixed position and shoot projectiles at enemies within its radius
 */
public abstract class ActiveTower extends Tower implements RangeAttacker {
    // since the sprite would never move
    private static final int NO_SPEED = 0;

    private Rectangle radius;
    private ArrayList<Enemy> enemiesInRange;
    private boolean canShoot;
    private double frames;
    private int cooldown;
    private Enemy currentTarget;
    private String projectileFileName;
    private int projectileDamage;

    /**
     * Create new active tower
     *
     * @param image                 What to render on tower's position
     * @param startPoint            Where tower is first placed
     * @param range                 How far tower can shoot
     * @param cooldown              Time inbetween projectiles shot
     * @param projectileFileName    Name of projectile's image file
     * @param projectileDamage      How much damage projectile can inflict on enemy
     */
    public ActiveTower(Image image, Point startPoint,
                       int range, int cooldown, String projectileFileName, int projectileDamage) {
        super(image, NO_SPEED, startPoint);
        radius = new Rectangle(new Point(startPoint.x-range,startPoint.y-range),range*2,range*2);
        this.cooldown = cooldown;
        enemiesInRange = new ArrayList<Enemy>();
        canShoot = true;
        frames = 0;
        this.projectileFileName = projectileFileName;
        this.projectileDamage = projectileDamage;
    }

    /**
     * Find the closest target and shoot at it if cooldown has passed
     */
    @Override
    public void update() {
        enemiesInRange.removeIf(enemy -> !enemy.getRect().intersects(radius) || enemy.isRetired());
        if (enemiesInRange.size()>0) { // aim at closest target (first in the list)
            if (currentTarget==null || !currentTarget.getRect().intersects(radius) || currentTarget.isRetired()) {
                currentTarget = enemiesInRange.get(0);
            }
            faceTowards(currentTarget);
            if (canShoot) {
                shootProjectileAt(currentTarget);
                canShoot = false;
            }
        }
        if (!canShoot) { // update timer
            frames += ShadowDefend.getTimescale();
            if (frames/FPS*IN_MILISECONDS >= cooldown) {
                canShoot = true;
                frames = 0;
            }
        }
    }

    /**
     * @return Bounding box of tower's radius
     */
    @Override
    public Rectangle getRadius() {
        return radius;
    }

    /**
     * Add the enemy as a potential target
     *
     * @param newEnemy Enemy within the tower's radius
     */
    @Override
    public void addNewTarget(Enemy newEnemy) {
        enemiesInRange.add(newEnemy);
    }

    /**
     * Add a new projectile aimed at the target as an entity to be rendered
     *
     * @param target Enemy for the projectile to move towards
     */
    private void shootProjectileAt(Enemy target) {
        ProjectileType type = ProjectileFactory.getProjectileType(projectileFileName, projectileDamage);
        Projectile newProjectile = new Projectile(type,getLocation(),target);
        ShadowDefend.addToQueue(newProjectile);
    }

    /**
     * @param target Enemy being checked
     * @return Whether the enemy is in the tower's list of targets
     */
    @Override
    public boolean isTargeting(Enemy target) {
        return enemiesInRange.contains(target);
    }
}

