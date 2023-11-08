package client.utils.transitions.javafx;

public interface TransitionWithParameter2<T1, T2> extends TransitionWithParameter {
  public void run(T1 t1, T2 t2);
}
