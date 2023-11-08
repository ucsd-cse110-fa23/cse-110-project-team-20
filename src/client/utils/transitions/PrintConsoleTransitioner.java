package client.utils.transitions;

public class PrintConsoleTransitioner implements ITransitioner {

  private void info(String message) {
    System.out.println(String.format("[Transition] %s", message));
  }

  private void info(String message, String param1) {
    info(String.format("%s (%s)", message, param1));
  }

  private void info(String message, String param1, String param2) {
    info(String.format("%s (%s)", message, param1, param2));
  }

  @Override
  public void transitionTo(Class<?> c) {
    info(c.toString());
  }

  @Override
  public <T1> void transitionTo(Class<?> c, T1 t1) {
    info(c.toString(), t1.toString());
  }

  @Override
  public <T1, T2> void transitionTo(Class<?> c, T1 t1, T2 t2) {
    info(c.toString(), t1.toString(), t2.toString());
  }
}
