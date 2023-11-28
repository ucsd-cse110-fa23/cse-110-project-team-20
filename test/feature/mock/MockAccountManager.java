package feature.mock;

import client.account.IAccountManager;
import client.account.IncorrectPassword;
import client.account.LoginFailed;

/**
 * mock for account manager
 */
public class MockAccountManager implements IAccountManager {
    private String incorrectPassword = null;
    private String loginFailed = null;
    private String token = null;

    public void setToken(String token) {
        this.token = token;
    }

    public void expectToThrowsIncorrectPassword() {
        incorrectPassword = "Incorrect Password";
    }

    public void expectToThrowsLoginFailed(String message) {
        loginFailed = message;
    }

    @Override
    public String loginOrCreateAccount(String username, String password)
            throws IncorrectPassword, LoginFailed {
        if (incorrectPassword != null) {
            throw new IncorrectPassword(incorrectPassword);
        }
        if (loginFailed != null) {
            throw new LoginFailed(loginFailed);
        }
        return token;
    }
}
