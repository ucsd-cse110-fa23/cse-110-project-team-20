package server.api;

public class TextToImageServiceException extends RuntimeException {
  public TextToImageServiceException(String message) {
      super(message);
  }
  public TextToImageServiceException(Throwable cause) {
      super(cause);
  }
}
