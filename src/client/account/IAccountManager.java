package client.account;

/**
 * Account Manager interface
 *
 * This interface will provide login/create account functionality in this app
 */
public interface IAccountManager {
    /**
     * perform login or create account
     *
     * @param username
     * @param password
     * @return token to access API
     * @throws IncorrectPassword
     * @throws LoginFailed
     */
    public String loginOrCreateAccount(String username, String password)
        throws IncorrectPassword, LoginFailed;
}
