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

    public CompositeViewTransitioner
    add(IViewTransitioner transitioner)
    {
        transitioners.add(transitioner);
        return this;
    }

    public void
    transitionTo(Class<?> c, Object[] params)
    {
        for (IViewTransitioner tran : transitioners) {
            tran.transitionTo(c, params);
        }
    }
}
