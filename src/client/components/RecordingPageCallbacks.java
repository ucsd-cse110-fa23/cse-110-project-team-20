package client.components;

/**
 * RecordingPageCallbacks
 *
 * This callback class is to carry over recording page callbacks
 * as a single object.
 */
public class RecordingPageCallbacks {
    Runnable onRecordingStarted;
    Runnable onRecrodingCompleted;
    public RecordingPageCallbacks(Runnable onRecordingStarted, Runnable onRecrodingCompleted)
    {
        this.onRecordingStarted = onRecordingStarted;
        this.onRecrodingCompleted = onRecrodingCompleted;
    }

    public Runnable
    getOnRecordingStarted()
    {
        return this.onRecordingStarted;
    }

    public Runnable
    getOnRecordingCompleted()
    {
        return this.onRecrodingCompleted;
    }
}
