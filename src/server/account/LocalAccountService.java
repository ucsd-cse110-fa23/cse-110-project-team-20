package server.account;

/**
 * Local Account Service
 * 
 * It allows whatever accounts the user typed in for testing purpose. It does not check any existance of user account.
 */
public class LocalAccountService implements IAccountService {
    @Override
    public boolean loginOrCreateAccount(String username, String password) throws IncorrectPassword {
        System.out.println(
                String.format("[Login] login/create account with %s. It always passes the login.", username, password));
        return true;
    }
}
