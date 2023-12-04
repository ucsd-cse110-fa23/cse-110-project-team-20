package server.api;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LocalTextToImageServiceTest {
  @Test
  public void result() {
    LocalTextToImageService service = new LocalTextToImageService();
    String res = service.createImage(() -> "some query");
    assertTrue(res.contains("data:image/jpg;base64,"));
  }
}
