package server.api;

import java.io.File;

public interface IVoiceToTextService {
  public String transcribe(File file);
}
