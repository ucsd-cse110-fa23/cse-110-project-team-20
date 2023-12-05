package server.api;

import java.io.File;

public class LocalVoiceToTextService implements IVoiceToTextService {
    @Override
    public String
    transcribe(File file)
    {
        if (file.getName().contains("ingredients")) {
            return "tomato, eggs, broccoli, and bacon";
        } else {
            return "dinner";
        }
    }
}
