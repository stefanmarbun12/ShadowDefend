package panels;

import bagel.*;
import bagel.util.Colour;
import bagel.util.Rectangle;
import gameState.Player;
import gameState.Renderable;
import gameState.ShadowDefend;
import levelElements.Level;

/**
 * Bottom panel showing level and player status
 */
public class StatusPanel implements Renderable {
    private static final Font FONT = new Font("res/fonts/DejaVuSans-bold.ttf",17);
    private static final double STRING_Y_POS = Window.getHeight()-8;
    private static final int WAVE_NUM_POS = 25;
    private static final int TIMESCALE_POS = 200;
    private static final int STATUS_POS = 425;
    private static final int LIVES_POS = 925;
    private static final double REC_HEIGHT = 25;
    private static final Rectangle PANEL_RECT = new Rectangle(0,
            Window.getHeight()-REC_HEIGHT,Window.getWidth(),REC_HEIGHT);
    private static final Image PANEL_IMAGE = new Image("res/images/statuspanel.png");
    private static final String START_STRING = "Status: Awaiting Start";
    private static final String PROGRESS_STRING = "Status: Wave In Progress";
    private static final String WINNER_STRING = "Status: Winner!";
    private static final String PLACING_STRING = "Status: Placing";

    private String statusString;
    private Level currLevel;
    private DrawOptions timescaleColour = new DrawOptions().setBlendColour(Colour.WHITE);

    private static StatusPanel panelInstance;

    /**
     * Create instance of status panel
     */
    private StatusPanel() {
        statusString = START_STRING;
    }

    /**
     * Instantiate the only status panel instance if needed
     *
     * @return Singleton instance of status panel
     */
    public static StatusPanel getInstance() {
        if (panelInstance==null) {
            panelInstance = new StatusPanel();
        }
        return panelInstance;
    }

    /**
     * Show panel on-screen
     */
    @Override
    public void render() {
        PANEL_IMAGE.drawFromTopLeft(0,Window.getHeight()-PANEL_IMAGE.getHeight());
        FONT.drawString(String.format("Wave: %d",currLevel.getCurrWaveNum()),WAVE_NUM_POS,STRING_Y_POS);
        FONT.drawString(String.format("Time Scale: %.1f",(double) ShadowDefend.getTimescale()),
                TIMESCALE_POS,STRING_Y_POS,timescaleColour);
        FONT.drawString(statusString,STATUS_POS,STRING_Y_POS);
        FONT.drawString(String.format("Lives: %d", Player.getInstance().getLives()),LIVES_POS,STRING_Y_POS);
    }

    /**
     * Get latest status and timescale colour
     */
    @Override
    public void update() {
        if (currLevel!=null) {
            if (currLevel.isComplete()) {
                statusString = WINNER_STRING;
            } else if (BuyPanel.getInstance().isPlacing()) {
                statusString = PLACING_STRING;
            } else if (currLevel.isWaveInProgress()) {
                statusString = PROGRESS_STRING;
            } else {
                statusString = START_STRING;
            }
        }
        timescaleColour.setBlendColour(ShadowDefend.getTimescale()>ShadowDefend.MIN_TIMESCALE ?
                Colour.GREEN : Colour.WHITE);
    }

    /**
     * Assign the current level as an attribute to access its values
     *
     * @param currLevel Level currently being played
     */
    public void setCurrLevel(Level currLevel) {
        this.currLevel = currLevel;
    }

    /**
     * @return Level currently being played
     */
    public Level getCurrLevel() {
        return currLevel;
    }

    /**
     * @return The panel's bounding box
     */
    public static Rectangle getPanelRect() {
        return PANEL_RECT;
    }
}
