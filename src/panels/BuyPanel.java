package panels;

import bagel.*;
import bagel.util.*;
import gameState.Player;
import gameState.Renderable;
import gameState.ShadowDefend;
import levelElements.Level;
import towers.Tower;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Top panel showing tower options, key binds, and player's money
 */
public class BuyPanel implements Renderable {
    private static final Font BINDS_FONT = new Font("res/fonts/DejaVuSans-bold.ttf",14);
    private static final Point BINDS_LOCATION = new Point(450,19);
    private static final Font MONEY_FONT = new Font("res/fonts/DejaVuSans-bold.ttf",40);
    private static final Point MONEY_LOCATION = new Point(825,60);
    private static final double REC_HEIGHT = 100;
    private static final Rectangle PANEL_RECT = new Rectangle(0,0,Window.getWidth(),REC_HEIGHT);
    private static final Image PANEL_IMAGE = new Image("res/images/buypanel.png");
    private static final String KEY_BINDS =
            "Key binds:\n\n" + Level.START_WAVE_KEY+ " - Start Wave\n" +
                    ShadowDefend.INCREASE_TIMESCALE + " - Increase Timescale\n" +
                    ShadowDefend.DECREASE_TIMESCALE + " - Decrease Timescale";

    private ArrayList<TowerOption> towerOptions = new ArrayList<TowerOption>();
    private boolean placing = false;
    private ArrayList<Tower> towersPlaced = new ArrayList<Tower>();

    private static TowerOption TANK_OPTION =
            new TowerOption("tank","res/images/tank.png",new Point(60,37),250);
    private static TowerOption SUPERTANK_OPTION =
            new TowerOption("supertank","res/images/supertank.png",
                    new Point(180,37),600);
    private static TowerOption AIRPLANE_OPTION =
            new TowerOption("airplane","res/images/airsupport.png",
                    new Point(300,37),500);
    private static BuyPanel panelInstance;

    /**
     * Create instance of buy panel
     */
    private BuyPanel() {
        towerOptions.add(TANK_OPTION);
        towerOptions.add(SUPERTANK_OPTION);
        towerOptions.add(AIRPLANE_OPTION);
    }

    /**
     * Instantiate the only buy panel instance if needed
     *
     * @return Singleton instance of buy panel
     */
    public static BuyPanel getInstance() {
        if (panelInstance==null) {
            panelInstance = new BuyPanel();
        }
        return panelInstance;
    }

    /**
     * Show panel on-screen
     */
    @Override
    public void render() {
        PANEL_IMAGE.drawFromTopLeft(0,0);
        for (TowerOption tower: towerOptions) {
            tower.render();
            tower.update();
        }
        BINDS_FONT.drawString(KEY_BINDS,BINDS_LOCATION.x,BINDS_LOCATION.y);
        MONEY_FONT.drawString(String.format("$%s",
                NumberFormat.getIntegerInstance().format(Player.getInstance().getMoney())),
                MONEY_LOCATION.x,MONEY_LOCATION.y);
    }

    /**
     * Clear out retired towers so new towers can be placed in their location
     */
    @Override
    public void update() {
        towersPlaced.removeIf(Tower::isRetired);
    }

    /**
     * @return If the player is currently placing a tower
     */
    public boolean isPlacing() {
        return placing;
    }

    /**
     * @param placing Whether a tower is currently being placed
     */
    public void setPlacing(boolean placing) {
        this.placing = placing;
    }

    /**
     * @return The panel's bounding box
     */
    public static Rectangle getPanelRect() {
        return PANEL_RECT;
    }

    /**
     * Add placed tower to the list so no towers can be placed in its position
     * @param newTower Tower that was successfully placed
     */
    public void addTowerPlaced(Tower newTower) { towersPlaced.add(newTower); }

    /**
     * @return List of towers placed on the map
     */
    public ArrayList<Tower> getTowersPlaced() {
        return towersPlaced;
    }
}
