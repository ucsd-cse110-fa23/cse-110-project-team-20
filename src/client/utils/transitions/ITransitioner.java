package client.utils.transitions;

public interface ITransitioner {
  /**
   * Transition to given class name if it is registered
   *
   * @param c
   */
  public void transitionTo(Class<?> c);

  /**
   * Transition to given class name if it is registered with one parameter
   *
   * @param <T1>
   * @param c
   * @param t1
   */
  public <T1> void transitionTo(Class<?> c, T1 t1);

  /**
   * Transition to given class name if it is registered with two parameters
   *
   * @param <T1>
   * @param <T2>
   * @param c
   * @param t1
   * @param t2
   */
  public <T1, T2> void transitionTo(Class<?> c, T1 t1, T2 t2);
  
  /**
   * Transition to given class name if it is registered with three parameters
   *
   * @param <T1>
   * @param <T2>
   * @param <T3>
   * @param c
   * @param t1
   * @param t2
   * @param t3
   */
  public <T1, T2, T3> void transitionTo(Class<?> c, T1 t1, T2 t2, T3 t3);
}
