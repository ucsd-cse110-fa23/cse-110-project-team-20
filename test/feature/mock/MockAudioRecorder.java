package feature.mock;

import client.audio.IAudioRecorder;

/**
 * Mock behavior of audio recorder
 */
public class MockAudioRecorder implements IAudioRecorder {
  public boolean recordingInProgress = false;
  public String filename;

  @Override
  public void startRecording(String filename) {
    this.filename = filename;
    recordingInProgress = true;
  }

  @Override
  public void stopRecording() {
    recordingInProgress = false;
  }
}
