package client.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.PropertyChangeEvent;

public class SimpleAccountSessionTest {
  private boolean called = false;
  private String name = null;
  private String value = null;

  @Test
  public void sessionChange() {
    SimpleAccountSession session = new SimpleAccountSession();
    session.addPropertyChangeListener((PropertyChangeEvent evt) -> {
      name = evt.getPropertyName();
      value = (String) evt.getNewValue();
      called = true;
    });
    session.setToken("new token");

    assertTrue(called);
    assertEquals("token", name);
    assertEquals("new token", value);
  }
}
