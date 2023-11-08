package client.utils.transitions.javafx;

public interface ITransitionWithParameter2<T1, T2> extends ITransitionWithParameter {
  public void run(T1 t1, T2 t2);
}
