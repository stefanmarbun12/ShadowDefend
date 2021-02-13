package panels;

import bagel.*;
import bagel.util.*;
import gameState.Player;
import gameState.ShadowDefend;
import gameState.Sprite;
import levelElements.Level;
import slicers.Enemy;
import towers.RangeAttacker;
import towers.Tower;

/**
 * Sprites that can be clicked to buy and place towers, shown on the buy panel
 */
public class TowerOption extends Sprite {
    private static final int NO_SPEED = 0;
    private static final Font PRICE_FONT = new Font("res/fonts/DejaVuSans-bold.ttf",20);
    private static final int TEXT_SHIFT_X = -25;
    private static final int TEXT_SHIFT_Y = 47;
    // this tower can be placed on the path since it is passive
    private static final String CAN_PLACE_ON_PATH = "airplane";

    private String towerName;
    private int price;
    private DrawOptions textColour;
    private boolean placing;

    /**
     * Create tower option
     *
     * @param towerName Type of tower represented by the option's image
     * @param imageFileName To get the image of the tower type
     * @param startPoint Where the option is shown on the buy panel
     * @param price Cost of buying the tower
     */
    public TowerOption(String towerName, String imageFileName, Point startPoint, int price) {
        super(imageFileName, NO_SPEED, startPoint);
        this.towerName = towerName;
        this.price = price;
        textColour = new DrawOptions();
        placing = false;
    }

    /**
     * Show the option and the tower's visual indicator if player is placing
     */
    @Override
    public void render() {
        super.render();
        Input mouseClick = ShadowDefend.getInput();
        PRICE_FONT.drawString(String.format("$%d",price),
                getRect().centre().x+TEXT_SHIFT_X,getRect().centre().y+TEXT_SHIFT_Y,textColour);
        // show visual indicator while placing tower
        if (BuyPanel.getInstance().isPlacing() && placing) {
            Point mouseLocation = mouseClick.getMousePosition();
            if (canPlace(mouseLocation)) {
                if (towerName.equals(CAN_PLACE_ON_PATH) || !Level.isBlocked(mouseLocation)) {
                    getFigure().draw(mouseLocation.x,mouseLocation.y);
                    if (mouseClick.isDown(MouseButtons.LEFT)) {
                        addTower(mouseLocation);
                    }
                }
            }
        }
    }

    /**
     * Set the right colour for the price and check whether the option was clicked
     */
    @Override
    public void update() {
        // if player was placing when level ended, set as false
        if (StatusPanel.getInstance().getCurrLevel().isComplete()) {
            placing = false;
            BuyPanel.getInstance().setPlacing(false);
        }

        Input mouseClick = ShadowDefend.getInput();
        textColour.setBlendColour(Player.getInstance().getMoney() >= price ? Colour.GREEN : Colour.RED);

        // check whether the player would like to place the tower
        if (!BuyPanel.getInstance().isPlacing() && !placing && mouseClick.isDown(MouseButtons.LEFT)
                && getRect().intersects(mouseClick.getMousePosition()) && Player.getInstance().getMoney() >= price) {
            placing = true;
            BuyPanel.getInstance().setPlacing(true);
        }
        if (BuyPanel.getInstance().isPlacing() && placing && mouseClick.isDown(MouseButtons.RIGHT)) {
            placing = false;
            BuyPanel.getInstance().setPlacing(false);
        }
    }

    /**
     * This sprite would never be retired
     *
     * @return Not retired
     */
    @Override
    public boolean isRetired() {
        return false;
    }

    /**
     * Add new tower at the valid location
     *
     * @param location Where player left-clicked on the screen
     */
    private void addTower(Point location) {
        Tower newTower = Tower.setTowerType(towerName,location);
        ShadowDefend.addToSprites(newTower);
        BuyPanel.getInstance().addTowerPlaced(newTower);
        if (newTower instanceof RangeAttacker) {
            Enemy.addToRangeList((RangeAttacker) newTower);
        }
        Player.getInstance().spendMoney(price);
        // player is no longer placing a tower
        placing = false;
        BuyPanel.getInstance().setPlacing(false);
    }

    /**
     * Ensure that the tower being placed doesn't leave the window or intersect with panels and other towers
     *
     * @param location Where player's cursor is at
     * @return Whether tower can be placed
     */
    private boolean canPlace(Point location) {
        if (!BuyPanel.getPanelRect().intersects(location) && !StatusPanel.getPanelRect().intersects(location) &&
        location.x >= 0 && location.x <= Window.getWidth() && location.y >= 0 && location.y <= Window.getHeight()) {
            for (Tower tower: BuyPanel.getInstance().getTowersPlaced()) {
                if (tower.getRect().intersects(location)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
