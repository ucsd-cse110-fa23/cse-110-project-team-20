package client.account;

public interface ICredentialManager {
    public void setCredentials(String username, String password);
    public String getUsername();
    public String getPassword();
    public boolean hasCredentials();
}
