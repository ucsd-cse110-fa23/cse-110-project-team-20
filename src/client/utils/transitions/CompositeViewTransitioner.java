package client.utils.transitions;

import java.util.ArrayList;

/**
 * CompositeViewTransitioner
 * 
 * Register multiple transitioners and propagate the transitionTo calling to all
 * registered tansitioners.
 */
public class CompositeViewTransitioner implements IViewTransitioner {
  private ArrayList<IViewTransitioner> transitioners = new ArrayList<>();

  public CompositeViewTransitioner add(IViewTransitioner transitioner) {
    transitioners.add(transitioner);
    return this;
  }

  @Override
  public void transitionTo(Class<?> c) {
    for (IViewTransitioner transitioner : transitioners) {
      transitioner.transitionTo(c);
    }
  }
  @Override
  public <T1> void transitionTo(Class<?> c, T1 t1) {
    for (IViewTransitioner transitioner : transitioners) {
      transitioner.transitionTo(c, t1);
    }
  }
  @Override
  public <T1, T2> void transitionTo(Class<?> c, T1 t1, T2 t2) {
    for (IViewTransitioner transitioner : transitioners) {
      transitioner.transitionTo(c, t1, t2);
    }
  }

  @Override
  public <T1, T2, T3> void transitionTo(Class<?> c, T1 t1, T2 t2, T3 t3) {
    for (IViewTransitioner transitioner : transitioners) {
      transitioner.transitionTo(c, t1, t2, t3);
    }
  }

  @Override
  public <T1, T2, T3, T4> void transitionTo(Class<?> c, T1 t1, T2 t2, T3 t3, T4 t4) {
    for (IViewTransitioner transitioner : transitioners) {
      transitioner.transitionTo(c, t1, t2, t3, t4);
    }
  }
}
