package levelElements;

/**
 * Represents a series of actions which is part of a wave
 */
public interface WaveEvent {
    int FPS = 60;
    int IN_MILISECONDS = 1000;

    /**
     * Execute the actions to be done during the event
     */
    void runEvent();

    /**
     * @return Whether the event has been completed
     */
    boolean isFinished();
}
