package server;

public class UnsupportedMethodException extends RuntimeException {
    public UnsupportedMethodException() {
    }

    public UnsupportedMethodException(String message) {
        super(message);
    }
}
