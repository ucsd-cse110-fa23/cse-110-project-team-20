package client.utils.transitions.javafx;

public interface ITransitionWithParameter3<T1, T2, T3> extends ITransitionWithParameter {
  public void run(T1 t1, T2 t2, T3 t3);
}
