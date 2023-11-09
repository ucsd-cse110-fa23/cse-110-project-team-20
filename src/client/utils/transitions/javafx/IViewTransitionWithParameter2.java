package client.utils.transitions.javafx;

public interface IViewTransitionWithParameter2<T1, T2> extends IViewTransitionWithParameter {
  public void run(T1 t1, T2 t2);
}
