package server.api;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Local text to image service impl
 * 
 * Get tomato.jpg and return as a local service
 */
public class LocalTextToImageService implements ITextToImageService {
  @Override
  public File createImage(IRecipeQuery query) throws TextToImageServiceException {
    try {
      File file = new File("test/resources/tomato.jpg");
      FileInputStream in = new FileInputStream(file);
      String path = "build/tmp/image-" + UUID.randomUUID().toString() + ".jpg";
      Files.copy(in, Paths.get(path));
      file = new File(path);
      return file;
    } catch (Exception e) {
      e.printStackTrace();
      throw new TextToImageServiceException(e);
    }
  }
}
