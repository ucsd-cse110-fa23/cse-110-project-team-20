package server.api;

import java.io.File;

public interface ITextToImageService {
  public File createImage(IRecipeQuery query) throws TextToImageServiceException;
}
