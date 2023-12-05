package server.api;

// Called when the GPT service fails
public class TextGenerateServiceException extends RuntimeException {
    public TextGenerateServiceException(String message)
    {
        super(message);
    }

    public TextGenerateServiceException(Throwable cause)
    {
        super(cause);
    }
}
