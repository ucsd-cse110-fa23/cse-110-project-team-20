package client.utils.transitions;

/**
 * PrintConsoleViewTransitioner
 * 
 * Print any requested transition on the console window.
 */
public class PrintConsoleViewTransitioner implements IViewTransitioner {

  private void info(String message) {
    System.out.println(String.format("[Transition] %s", message));
  }

  private void info(String message, String param1) {
    info(String.format("%s (%s)", message, param1));
  }

  private void info(String message, String param1, String param2) {
    info(String.format("%s (%s)", message, param1, param2));
  }

  private void info(String message, String param1, String param2, String param3) {
    info(String.format("%s (%s)", message, param1, param2, param3));
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

  @Override
  public <T1, T2, T3> void transitionTo(Class<?> c, T1 t1, T2 t2, T3 t3) {
    info(c.toString(), t1.toString(), t2.toString(), t3.toString());
  }
}
