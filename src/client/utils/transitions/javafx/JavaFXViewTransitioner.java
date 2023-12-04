package client.utils.transitions.javafx;

import client.utils.transitions.IViewTransitioner;
import client.utils.transitions.ViewTransitionException;
import java.util.HashMap;
import javafx.application.Platform;

/**
 * ViewTransitioner for JavaFX
 */
public class JavaFXViewTransitioner implements IViewTransitioner {
    private HashMap<String, IViewTransitionWithParameter> transitions = new HashMap<>();

    private String
    key(Class<?> c, int count)
    {
        return String.format("%s@%s", c.toString(), count);
    }

    /**
     * Register a class with given runnable. The transitioner will call the runnable when
     * transitionTo is called with appropriate class name.
     *
     * @param c
     * @param a
     * @return
     */
    public JavaFXViewTransitioner register(Class<?> c, IViewTransitionWithParameter a)
    {
        transitions.put(key(c, 0), a);
        System.out.println(key(c, 0));
        return this;
    }

    public void
    transitionTo(Class<?> c, Object... params)
    {
        IViewTransitionWithParameter a = transitions.get(key(c, 0));
        Platform.runLater(() -> a.run(params));
    }
}
