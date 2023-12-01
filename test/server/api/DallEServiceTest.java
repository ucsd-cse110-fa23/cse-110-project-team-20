package server.api;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import server.ServerConfiguration;

public class DallEServiceTest {
  @Test
  @Disabled
  public void imageGeneration() {
    // this will invoke actual image generation so it is disabled
    DallEService service = new DallEService(new ServerConfiguration());
    File file = service.createImage(() -> "A monkey eating a banana");
    assertTrue(file.exists());
  }
}
