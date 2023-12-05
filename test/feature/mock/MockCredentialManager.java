package feature.mock;

import client.account.ICredentialManager;

/**
 * mock for account manager
 */
public class MockCredentialManager implements ICredentialManager {
    String username = null, password = null;
    public void
    setCredentials(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
    public String
    getUsername()
    {
        return this.username;
    }
    public String
    getPassword()
    {
        return this.password;
    }
    public boolean
    hasCredentials()
    {
        return this.username != null && this.password != null;
    }
}
