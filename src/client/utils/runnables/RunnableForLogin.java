package client.utils.runnables;

public interface RunnableForLogin {
  public void run(String username, String password, boolean stayLoggedIn, Runnable onLoaded);
}
