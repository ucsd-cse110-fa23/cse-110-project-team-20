package server.account;

/**
 * AccountContext
 * 
 * This class will hold username so that we can use user information in other context in the server
 * ref: https://www.baeldung.com/java-threadlocal
 */
public class AccountContext implements IAccountContext {
    private ThreadLocal<String> username = new ThreadLocal<>();

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getUsername() {
        return this.username.get();
    }
}
