package client.account;

public class LoginFailed extends RuntimeException {
    public LoginFailed(String message)
    {
        super(message);
    }
}
