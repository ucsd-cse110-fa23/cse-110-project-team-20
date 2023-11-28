package feature.mock;

import client.account.IAccountSession;

/**
 * Mock for account session
 */
public class MockAccountSession implements IAccountSession {
    private String token;
    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getToken() {
        return token;
    }
    
}
