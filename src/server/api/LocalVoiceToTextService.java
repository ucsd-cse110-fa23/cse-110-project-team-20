package server.api;

import java.io.File;

public class LocalVoiceToTextService implements IVoiceToTextService {
  private int i = 0;
  @Override
  public String transcribe(File file) {
    if (i == 0) {
      i++;
      return "tomato, eggs, cucumber";
    } else {
      i--;
      return "dinner";
    }
  }  
}
