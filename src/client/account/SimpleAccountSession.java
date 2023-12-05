package client.account;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Simple Account Session class
 *
 * This class holds token and allow to use the token in http request
 * We can allow this session can store the token to stay logged in later.
 * Also provides property change listener for observer pattern using
 * PropertyChangeListener when it changes the current token.
 *
 * ref: https://www.baeldung.com/java-observer-pattern
 */
public class SimpleAccountSession implements IAccountSession {
    private String token;
    private PropertyChangeSupport support;

    public SimpleAccountSession()
    {
        support = new PropertyChangeSupport(this);
    }

    public void
    setToken(String token)
    {
        support.firePropertyChange("token", this.token, token);
        this.token = token;
    }

    public String
    getToken()
    {
        return token;
    }

    public void
    addPropertyChangeListener(PropertyChangeListener pcl)
    {
        support.addPropertyChangeListener(pcl);
    }
}
