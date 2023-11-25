package server.account;

/**
 * Account Service Interface
 */
public interface IAccountService {
    /**
     * Perform login or create account with given username and password.
     * If username does exist, check the password with the associated username.
     * If username does not exist, create a new account and log in into the account.
     *
     * @param username
     * @param password
     * @return true if the account is logged in or is created successfully
     * @throws IncorrectPassword
     */
    public boolean loginOrCreateAccount(String username, String password) throws IncorrectPassword;
}
