package server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

import server.mock.MockHttpRequest;
import server.request.IHttpRequest;

public class IndexHttpHandlerTest {
  @Test
  public void instance() {
    IndexHttpHandler handler = new IndexHttpHandler();
    assertInstanceOf(IndexHttpHandler.class, handler);
  }

  @Test
  public void get() {
    IndexHttpHandler handler = new IndexHttpHandler();
    IHttpRequest request = new MockHttpRequest();

    String expected = "<html><body><h1>PantryPal Server</h1><p>The server is up and running.</p></body></html>";
    String response = handler.handleGet(request);
    assertEquals(expected, response);
  }
}
