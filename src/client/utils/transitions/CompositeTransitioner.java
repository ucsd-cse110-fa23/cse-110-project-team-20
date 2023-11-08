package client.utils.transitions;

import java.util.ArrayList;

public class CompositeTransitioner implements ITransitioner {
  private ArrayList<ITransitioner> transitioners = new ArrayList<>();

  public CompositeTransitioner add(ITransitioner transitioner) {
    transitioners.add(transitioner);
    return this;
  }

  @Override
  public void transitionTo(Class<?> c) {
    for (ITransitioner transitioner : transitioners) {
      transitioner.transitionTo(c);
    }
  }
  @Override
  public <T1> void transitionTo(Class<?> c, T1 t1) {
    for (ITransitioner transitioner : transitioners) {
      transitioner.transitionTo(c, t1);
    }
  }
  @Override
  public <T1, T2> void transitionTo(Class<?> c, T1 t1, T2 t2) {
    for (ITransitioner transitioner : transitioners) {
      transitioner.transitionTo(c, t1, t2);
    }
  }
}
