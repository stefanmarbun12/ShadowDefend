package towers;

import java.util.HashMap;

/**
 * To implement flyweight pattern and reduce duplicate data (since there can be hundreds of projectiles on-screen)
 */
public class ProjectileFactory {
    private static HashMap<String,ProjectileType> projectileTypes = new HashMap<>();

    /**
     * Return cached projectile attributes instead of creating new ones
     *
     * @param imageFileName Name of projectile's image file
     * @param damage        How much damage projectile can inflict
     * @return              Flyweight projectile object
     */
    public static ProjectileType getProjectileType(String imageFileName, int damage) {
        ProjectileType type = projectileTypes.get(imageFileName);
        if (type == null) {
            type = new ProjectileType(imageFileName, damage);
            projectileTypes.put(imageFileName, type);
        }
        return type;
    }
}
