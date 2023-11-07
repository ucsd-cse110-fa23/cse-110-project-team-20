package server.api;

public class TextGenerateServiceException extends RuntimeException {
    public TextGenerateServiceException(String message) {
        super(message);
    }

    public TextGenerateServiceException(Throwable cause) {
        super(cause);
    }
}
