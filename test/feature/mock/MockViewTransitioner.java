package feature.mock;

import client.utils.transitions.IViewTransitioner;

/**
 * Mock view transitioner
 */
public class MockViewTransitioner implements IViewTransitioner {
    public Class<?> currentPageClass;
    public Object[] params;

    @Override
    public void
    transitionTo(Class<?> c, Object[] params)
    {
        currentPageClass = c;
        this.params = params;
    }
}
