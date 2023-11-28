package server.account;

import com.sun.net.httpserver.BasicAuthenticator;

/**
 * Local Basic Authenticator
 *
 * Allows any auth login without validation
 */
public class LocalBasicAuthenticator extends BasicAuthenticator {
    public LocalBasicAuthenticator() {
        super("PantryPal");
    }

    @Override
    public boolean checkCredentials(String username, String password) {
        return true;
    }
    
}
