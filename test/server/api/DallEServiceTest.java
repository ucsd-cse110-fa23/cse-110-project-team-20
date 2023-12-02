package server.api;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import server.ServerConfiguration;

public class DallEServiceTest {
  @Test
  @Disabled
  public void imageGeneration() {
    // this will invoke actual image generation so it is disabled
    DallEService service = new DallEService(new ServerConfiguration());
    String imageUrl = service.createImage(() -> "A monkey eating a banana");
    assertTrue(imageUrl.contains("data:image/jpg;base64,"));
    assertTrue(imageUrl.length() > 100, "base64 image should be longer than 100 characters.");
  }
}
