package towers;

import bagel.Image;

/**
 * Contains attributes shared by many projectiles
 */
public class ProjectileType{
    private Image image;
    private int damage;

    /**
     * Create a new projectile type
     *
     * @param imageFileName Name of projectile's image file
     * @param damage        How much damage projectile can inflict
     */
    public ProjectileType(String imageFileName, int damage) {
        image = new Image(imageFileName);
        this.damage = damage;
    }

    /**
     * @return How much damage projectile can inflict
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @return Projectile's image to be rendered
     */
    public Image getImage() {
        return image;
    }
}
