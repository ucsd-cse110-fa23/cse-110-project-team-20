package client.utils.transitions.javafx;

import java.util.HashMap;

import client.utils.transitions.ITransitioner;
import client.utils.transitions.TransitionException;
import javafx.application.Platform;

public class JavaFXTransitioner implements ITransitioner {
  private HashMap<String, TransitionWithParameter> transitions = new HashMap<>();

  private String key(Class<?> c, int count) {
    return String.format("%s@%s", c.toString(), count);
  }

  public JavaFXTransitioner register(Class<?> c, TransitionWithParameter0 a) {
    transitions.put(key(c, 0), a);
    return this;
  }

  public <T1> JavaFXTransitioner register(Class<?> c, TransitionWithParameter1<T1> t1) {
    transitions.put(key(c, 1), t1);
    return this;
  }

  public <T1, T2> JavaFXTransitioner register(Class<?> c, TransitionWithParameter2<T1, T2> t2) {
    transitions.put(key(c, 2), t2);
    return this;
  }

  public void transitionTo(Class<?> c) {
    TransitionWithParameter a = transitions.get(key(c, 0));
    if (a instanceof TransitionWithParameter0) {
      TransitionWithParameter0 runner = (TransitionWithParameter0) a;
      Platform.runLater(() -> runner.run());
      return;
    }
    throw new TransitionException(String.format("%s is not registered", key(c, 0)));
  }

  public <T1> void transitionTo(Class<?> c, T1 t1) {
    TransitionWithParameter a = transitions.get(key(c, 1));
    if (a instanceof TransitionWithParameter1) {
      @SuppressWarnings("unchecked")
      TransitionWithParameter1<T1> runner = (TransitionWithParameter1<T1>) a;
      Platform.runLater(() -> runner.run(t1));
      return;
    }
    throw new TransitionException(String.format("%s is not registered", key(c, 1)));
  }

  public <T1, T2> void transitionTo(Class<?> c, T1 t1, T2 t2) {
    TransitionWithParameter a = transitions.get(key(c, 2));
    if (a instanceof TransitionWithParameter2) {
      @SuppressWarnings("unchecked")
      TransitionWithParameter2<T1, T2> runner = (TransitionWithParameter2<T1, T2>) a;
      Platform.runLater(() -> runner.run(t1, t2));
      return;
    }
    throw new TransitionException(String.format("%s is not registered", key(c, 2)));
  }
}

