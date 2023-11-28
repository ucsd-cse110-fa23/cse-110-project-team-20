package client.account;

/**
 * Simple Account Session class
 * 
 * This class holds token and allow to use the token in http request
 * We can allow this session can store the token to stay logged in later
 */
public class SimpleAccountSession implements IAccountSession {
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
