package server;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import server.chatgpt.ChatGPTServiceException;
import server.chatgpt.IChatGPTService;
import server.chatgpt.RecipeQueryable;
import server.mock.MockHttpRequest;

public class GenerateRecipeHttpHandlerTest {
    @Test
    public void instance()
    {
        IChatGPTService chatGPTService = new IChatGPTService() {
            @Override
            public String request(RecipeQueryable query) throws ChatGPTServiceException {
                return "";
            }
        };

        GenerateRecipeHttpHandler handler = new GenerateRecipeHttpHandler(chatGPTService);
        assertInstanceOf(GenerateRecipeHttpHandler.class, handler);
    }

    @Test
    @Disabled("Disabled until file handling and Whipser API is implemented")
    public void post()
    {
        IChatGPTService chatGPTService = new IChatGPTService() {
            @Override
            public String request(RecipeQueryable query) throws ChatGPTServiceException {
                return "";
            }
        };

        GenerateRecipeHttpHandler handler = new GenerateRecipeHttpHandler(chatGPTService);
        MockHttpRequest request = new MockHttpRequest();
    }
}
