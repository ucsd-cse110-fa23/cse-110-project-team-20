package server.chatgpt;

public interface IChatGPTService {
    public String request(RecipeQueryable query) throws ChatGPTServiceException;
}
