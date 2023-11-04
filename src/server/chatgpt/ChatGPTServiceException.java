package server.chatgpt;

public class ChatGPTServiceException extends RuntimeException {
    public ChatGPTServiceException(String message) {
        super(message);
    }

    public ChatGPTServiceException(Throwable cause) {
        super(cause);
    }
}
