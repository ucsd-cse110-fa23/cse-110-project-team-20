package client.utils.transitions;

/**
 * PrintConsoleViewTransitioner
 *
 * Print any requested transition on the console window.
 */
public class PrintConsoleViewTransitioner implements IViewTransitioner {
    private void
    info(String message, Object[] params)
    {
        System.out.println(String.format("[Transition] %s (%d)", message, params.length));
    }

    @Override
    public void
    transitionTo(Class<?> c, Object[] params)
    {
        info(c.toString(), params);
    }
}
