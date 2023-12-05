package server;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.json.JSONObject;

import server.account.IAccountService;
import server.account.IncorrectPassword;
import server.request.IHttpRequest;

public class AccountHttpHandler extends HttpHandlerBase {
    private IAccountService accountService;

    public AccountHttpHandler(IAccountService accountService) {
        this.accountService = accountService;
    }

    protected String handlePost(IHttpRequest request) throws UnsupportedMethodException {
        JSONObject requestObj = new JSONObject(request.getRequestBodyAsString());

        String username = requestObj.has("username") ? requestObj.getString("username") : null;
        String password = requestObj.has("password") ? requestObj.getString("password") : null;

        if (username == null || username.length() == 0) {
            return fail("Username is missing");
        }

        if (password == null || password.length() == 0) {
            return fail("Password is missing");
        }
        
        boolean loggedIn = false;
        
        try {
            loggedIn = accountService.loginOrCreateAccount(username, password);
        } catch (IncorrectPassword e) {
            return fail("Incorrect Password");
        }

        if (! loggedIn) {
            return fail("Login/creating an account is failed");
        }

        return success(username, password);
    }

    private String success(String username, String password) {
        // ref:
        // https://stackoverflow.com/questions/3283234/http-basic-authentication-in-java-using-httpclient
        String token = "";
        try {
            token = Base64.getEncoder().encodeToString((username + ":" + password).getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject response = new JSONObject();
        response.put("token", String.format("Basic %s", token));
        return response.toString(2);
    }
}
