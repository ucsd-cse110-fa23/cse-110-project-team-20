package server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import server.account.IAccountService;
import server.account.IncorrectPassword;
import server.mock.MockHttpRequest;

public class AccountHttpHandlerTest {
    private IAccountService accountServiceMockThatAlwaysTrue = new IAccountService() {
        @Override
        public boolean loginOrCreateAccount(String username, String password) throws IncorrectPassword {
            return true;
        }
    };
    private IAccountService accountServiceMockThatAlwaysFalse = new IAccountService() {
        @Override
        public boolean loginOrCreateAccount(String username, String password) throws IncorrectPassword {
            return false;
        }
    };
    private IAccountService accountServiceMockThatAlwaysThrowIncorrectPassword = new IAccountService() {
        @Override
        public boolean loginOrCreateAccount(String username, String password) throws IncorrectPassword {
            throw new IncorrectPassword();
        }
    };

    @Test
    public void loginSuccessfully() {
        AccountHttpHandler handler = new AccountHttpHandler(accountServiceMockThatAlwaysTrue);
        MockHttpRequest request = new MockHttpRequest();
        request.setRequestBodyAsString(new JSONObject("{\"username\":\"admin\",\"password\":\"scret\"}").toString());
        String response = handler.handlePost(request);

        JSONObject responseObj = new JSONObject(response);
        assertEquals("Basic YWRtaW46c2NyZXQ=", responseObj.getString("token"));
    }

    @Test
    public void missingUsername() {
        AccountHttpHandler handler = new AccountHttpHandler(accountServiceMockThatAlwaysTrue);
        MockHttpRequest request = new MockHttpRequest();
        request.setRequestBodyAsString(new JSONObject("{\"password\":\"scret\"}").toString());
        String response = handler.handlePost(request);

        JSONObject responseObj = new JSONObject(response);
        assertEquals("Username is missing", responseObj.getString("error"));
    }

    @Test
    public void missingPassword() {
        AccountHttpHandler handler = new AccountHttpHandler(accountServiceMockThatAlwaysTrue);
        MockHttpRequest request = new MockHttpRequest();
        request.setRequestBodyAsString(new JSONObject("{\"username\":\"some-name\"}").toString());
        String response = handler.handlePost(request);

        JSONObject responseObj = new JSONObject(response);
        assertEquals("Password is missing", responseObj.getString("error"));
    }
    @Test
    public void incorrectPassword() {
        AccountHttpHandler handler = new AccountHttpHandler(accountServiceMockThatAlwaysThrowIncorrectPassword);
        MockHttpRequest request = new MockHttpRequest();
        request.setRequestBodyAsString(new JSONObject("{\"username\":\"admin\",\"password\":\"scret\"}").toString());
        String response = handler.handlePost(request);

        JSONObject responseObj = new JSONObject(response);
        assertEquals("Incorrect Password", responseObj.getString("error"));
    }
    @Test
    public void loginFailed() {
        AccountHttpHandler handler = new AccountHttpHandler(accountServiceMockThatAlwaysFalse);
        MockHttpRequest request = new MockHttpRequest();
        request.setRequestBodyAsString(new JSONObject("{\"username\":\"admin\",\"password\":\"scret\"}").toString());
        String response = handler.handlePost(request);

        JSONObject responseObj = new JSONObject(response);
        assertEquals("Login/creating an account is failed", responseObj.getString("error"));
    }
}
