package server.api;

import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;

/**
 * Local text to image service impl
 * 
 * Get tomato.jpg and return as a local service
 */
public class LocalTextToImageService implements ITextToImageService {
  @Override
  public String createImage(IRecipeQuery query) throws TextToImageServiceException {
    File file = new File("test/resources/tomato.jpg");
    String result = null;
    try {
      FileInputStream in = new FileInputStream(file);
      result = Base64.getEncoder().encodeToString(in.readAllBytes());
    } catch (Exception e) {
      e.printStackTrace();
      throw new TextToImageServiceException(e);
    }
    return "data:image/jpg;base64," + result;
  }
}
