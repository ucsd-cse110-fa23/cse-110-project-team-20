package client.utils.transitions;

public interface IViewTransitioner {
    /**
     * Transition to given class name if it is registered
     *
     * @param c
     */
    public void transitionTo(Class<?> c, Object... params);
}
