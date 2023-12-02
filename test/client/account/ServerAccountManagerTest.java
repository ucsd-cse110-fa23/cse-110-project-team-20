package client.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import client.mock.MockHttpServer;

public class ServerAccountManagerTest {
    private MockHttpServer server;
  
    @AfterEach
    public void tearDownServer() {
      if (server != null) {
        server.stop();
        server = null;
      }
    }

    @Test
    public void loginSuccess() throws IOException {
        server = new MockHttpServer("/account/login-or-create", "{\"token\": \"Some Token Response\"}");
        ServerAccountManager manager = new ServerAccountManager("http://localhost:5101");

        server.start(5101);
    
        manager.loginOrCreateAccount("some username", "some password");

        String body = server.getLastHttpRequestBodyAsString();
        JSONObject bodyObj = new JSONObject(body);
        assertEquals("some username", bodyObj.getString("username"));
        assertEquals("some password", bodyObj.getString("password"));
    }

    @Test
    public void loginFailedWithIncorrectPassword() throws IOException {
      server = new MockHttpServer("/account/login-or-create", "{\"error\": \"Incorrect Password\"}");
      ServerAccountManager manager = new ServerAccountManager("http://localhost:5102");

      server.start(5102);

      assertThrows(IncorrectPassword.class, () -> {
        manager.loginOrCreateAccount("some username", "some password");
      });
    }

    @Test
    public void loginFailedWithGeneralIssue() throws IOException {
      server = new MockHttpServer("/account/login-or-create", "{\"error\": \"Some errors occured\"}");
      ServerAccountManager manager = new ServerAccountManager("http://localhost:5103");

      server.start(5103);

      assertThrows(LoginFailed.class, () -> {
        manager.loginOrCreateAccount("some username", "some password");
      });
    }

    @Test
    public void loginFailedFromServerIssue() throws IOException {
        server = new MockHttpServer("/account/login-or-create", "Some Database Error: connection refused");
        ServerAccountManager manager = new ServerAccountManager("http://localhost:5104");

        server.start(5104);
    
        LoginFailed thrown = assertThrows(LoginFailed.class, () -> {
          manager.loginOrCreateAccount("some username", "some password");
        });
        assertTrue(thrown.getMessage().contains("Some Database Error: connection refused"));
    }
}
