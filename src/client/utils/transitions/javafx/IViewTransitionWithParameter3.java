package client.utils.transitions.javafx;

public interface IViewTransitionWithParameter3<T1, T2, T3> extends IViewTransitionWithParameter {
  public void run(T1 t1, T2 t2, T3 t3);
}
