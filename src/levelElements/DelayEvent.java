package levelElements;

import gameState.ShadowDefend;

/**
 * A delay usually in-between spawn events
 */
public class DelayEvent implements WaveEvent {
    private double delayTime;
    private boolean finished;
    private double frames;

    /**
     * Create new delay event
     *
     * @param delayTime Time to wait until the event is complete
     */
    public DelayEvent(int delayTime) {
        this.delayTime = delayTime;
        this.finished = false;
        this.frames = 0;
    }

    /**
     * Keep track of time to check whether delay has passed
     */
    @Override
    public void runEvent() {
        frames += ShadowDefend.getTimescale();
        if (frames/FPS*IN_MILISECONDS >= delayTime) {
            finished = true;
        }
    }

    /**
     * @return If delay has passed
     */
    public boolean isFinished() {
        return finished;
    }
}