package client.components;

public class RecordingPageCallbacks {
  Runnable onRecordingStarted;
  Runnable onRecrodingCompleted;
  public RecordingPageCallbacks(Runnable onRecordingStarted, Runnable onRecrodingCompleted) {
    this.onRecordingStarted = onRecordingStarted;
    this.onRecrodingCompleted = onRecrodingCompleted;
  }

  public Runnable getOnRecordingStarted() {
    return this.onRecordingStarted;
  }

  public Runnable getOnRecordingCompleted() {
    return this.onRecrodingCompleted;
  }
}
