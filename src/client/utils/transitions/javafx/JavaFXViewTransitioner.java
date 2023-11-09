package client.utils.transitions.javafx;

import java.util.HashMap;

import client.utils.transitions.IViewTransitioner;
import client.utils.transitions.ViewTransitionException;
import javafx.application.Platform;

/**
 * ViewTransitioner for JavaFX
 */
public class JavaFXViewTransitioner implements IViewTransitioner {
  private HashMap<String, IViewTransitionWithParameter> transitions = new HashMap<>();

  private String key(Class<?> c, int count) {
    return String.format("%s@%s", c.toString(), count);
  }

  /**
   * Register a class with given runnerable. The transitioner will call the runnerable when
   * transitionTo is called with appropriate class name.
   *
   * @param c
   * @param a
   * @return
   */
  public JavaFXViewTransitioner register(Class<?> c, IViewTransitionWithParameter0 a) {
    transitions.put(key(c, 0), a);
    System.out.println(key(c, 0));
    return this;
  }

  /**
   * Register a class with given runnerable. The transitioner will call the runnerable when
   * transitionTo is called with appropriate class name.
   *
   * @param <T1>
   * @param c
   * @param t1
   * @return
   */
  public <T1> JavaFXViewTransitioner register(Class<?> c, IViewTransitionWithParameter1<T1> t1) {
    transitions.put(key(c, 1), t1);
    System.out.println(key(c, 1));
    return this;
  }

  /**
   * Register a class with given runnerable. The transitioner will call the runnerable when
   * transitionTo is called with appropriate class name.
   *
   * @param <T1>
   * @param <T2>
   * @param c
   * @param t2
   * @return
   */
  public <T1, T2> JavaFXViewTransitioner register(Class<?> c, IViewTransitionWithParameter2<T1, T2> t2) {
    transitions.put(key(c, 2), t2);
    System.out.println(key(c, 2));
    return this;
  }

  /**
   * Register a class with given runnerable. The transitioner will call the runnerable when
   * transitionTo is called with appropriate class name.
   *
   * @param <T1>
   * @param <T2>
   * @param <T3>
   * @param c
   * @param t3
   * @return
   */
  public <T1, T2, T3> JavaFXViewTransitioner register(Class<?> c, IViewTransitionWithParameter3<T1, T2, T3> t3) {
    transitions.put(key(c, 3), t3);
    System.out.println(key(c, 3));
    return this;
  }

  public void transitionTo(Class<?> c) {
    IViewTransitionWithParameter a = transitions.get(key(c, 0));
    if (a instanceof IViewTransitionWithParameter0) {
      IViewTransitionWithParameter0 runner = (IViewTransitionWithParameter0) a;
      Platform.runLater(() -> runner.run());
      return;
    }
    throw new ViewTransitionException(String.format("%s is not registered", key(c, 0)));
  }

  public <T1> void transitionTo(Class<?> c, T1 t1) {
    IViewTransitionWithParameter a = transitions.get(key(c, 1));
    if (a instanceof IViewTransitionWithParameter1) {
      @SuppressWarnings("unchecked")
      IViewTransitionWithParameter1<T1> runner = (IViewTransitionWithParameter1<T1>) a;
      Platform.runLater(() -> runner.run(t1));
      return;
    }
    throw new ViewTransitionException(String.format("%s is not registered", key(c, 1)));
  }

  public <T1, T2> void transitionTo(Class<?> c, T1 t1, T2 t2) {
    IViewTransitionWithParameter a = transitions.get(key(c, 2));
    if (a instanceof IViewTransitionWithParameter2) {
      @SuppressWarnings("unchecked")
      IViewTransitionWithParameter2<T1, T2> runner = (IViewTransitionWithParameter2<T1, T2>) a;
      Platform.runLater(() -> runner.run(t1, t2));
      return;
    }
    throw new ViewTransitionException(String.format("%s is not registered", key(c, 2)));
  }

  public <T1, T2, T3> void transitionTo(Class<?> c, T1 t1, T2 t2, T3 t3) {
    IViewTransitionWithParameter a = transitions.get(key(c, 3));
    if (a instanceof IViewTransitionWithParameter3) {
      @SuppressWarnings("unchecked")
      IViewTransitionWithParameter3<T1, T2, T3> runner = (IViewTransitionWithParameter3<T1, T2, T3>) a;
      Platform.runLater(() -> runner.run(t1, t2, t3));
      return;
    }
    throw new ViewTransitionException(String.format("%s is not registered", key(c, 3)));
  }
}
