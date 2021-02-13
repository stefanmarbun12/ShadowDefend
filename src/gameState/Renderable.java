package gameState;

/**
 * Represents entities that can be rendered and updated onto the game window
 */
public interface Renderable {
    /**
     * Show the entity on the window
     */
    void render();

    /**
     * Update the state of the entity
     */
    void update();
}
