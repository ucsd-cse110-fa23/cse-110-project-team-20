package client.utils.transitions.javafx;

import java.util.HashMap;

import client.utils.transitions.ITransitioner;
import client.utils.transitions.TransitionException;
import javafx.application.Platform;

/**
 * Transitioner for JavaFX
 */
public class JavaFXTransitioner implements ITransitioner {
  private HashMap<String, ITransitionWithParameter> transitions = new HashMap<>();

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
  public JavaFXTransitioner register(Class<?> c, ITransitionWithParameter0 a) {
    transitions.put(key(c, 0), a);
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
  public <T1> JavaFXTransitioner register(Class<?> c, ITransitionWithParameter1<T1> t1) {
    transitions.put(key(c, 1), t1);
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
  public <T1, T2> JavaFXTransitioner register(Class<?> c, ITransitionWithParameter2<T1, T2> t2) {
    transitions.put(key(c, 2), t2);
    return this;
  }

  public void transitionTo(Class<?> c) {
    ITransitionWithParameter a = transitions.get(key(c, 0));
    if (a instanceof ITransitionWithParameter0) {
      ITransitionWithParameter0 runner = (ITransitionWithParameter0) a;
      Platform.runLater(() -> runner.run());
      return;
    }
    throw new TransitionException(String.format("%s is not registered", key(c, 0)));
  }

  public <T1> void transitionTo(Class<?> c, T1 t1) {
    ITransitionWithParameter a = transitions.get(key(c, 1));
    if (a instanceof ITransitionWithParameter1) {
      @SuppressWarnings("unchecked")
      ITransitionWithParameter1<T1> runner = (ITransitionWithParameter1<T1>) a;
      Platform.runLater(() -> runner.run(t1));
      return;
    }
    throw new TransitionException(String.format("%s is not registered", key(c, 1)));
  }

  public <T1, T2> void transitionTo(Class<?> c, T1 t1, T2 t2) {
    ITransitionWithParameter a = transitions.get(key(c, 2));
    if (a instanceof ITransitionWithParameter2) {
      @SuppressWarnings("unchecked")
      ITransitionWithParameter2<T1, T2> runner = (ITransitionWithParameter2<T1, T2>) a;
      Platform.runLater(() -> runner.run(t1, t2));
      return;
    }
    throw new TransitionException(String.format("%s is not registered", key(c, 2)));
  }
}

