package client.utils.transitions;

// Class that is called when a transition fails and/or is unexpected
public class ViewTransitionException extends RuntimeException {
  public ViewTransitionException() {
    super();
  }
  public ViewTransitionException(String message) {
    super(message);
  }
}
