package server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.Headers;

import server.api.ITextGenerateService;
import server.api.IRecipeQuery;
import server.api.TextGenerateServiceException;
import server.mock.MockHttpRequest;

public class GenerateRecipeHttpHandlerTest {
    @Test
    public void instance()
    {
        ITextGenerateService chatGPTService = new ITextGenerateService() {
            @Override
            public String request(IRecipeQuery query) throws TextGenerateServiceException {
                return "";
            }
        };

        GenerateRecipeHttpHandler handler = new GenerateRecipeHttpHandler(chatGPTService);
        assertInstanceOf(GenerateRecipeHttpHandler.class, handler);
    }

    @Test
    @Disabled("Disabled until file handling and Whipser API is implemented")
    public void post() throws IOException
    {
        File audioRequestBodyFile = new File("test/resources/audio-request-body-with-voice.txt");
        InputStream inputStream = new FileInputStream(audioRequestBodyFile);

        String audioRequestHeaderText = Files.readString(Path.of("test/resources/audio-request-header-with-voice.json"));
        Headers headers = new Headers();
        JSONObject headersJson = new JSONObject(audioRequestHeaderText);

        String contentType = headersJson.getJSONArray("Content-type").getString(0);
        String contentLength = headersJson.getJSONArray("Content-length").getString(0);

        ArrayList<String> contentTypeArr = new ArrayList<>();
        ArrayList<String> contentLengthArr = new ArrayList<>();
        contentTypeArr.add(contentType);
        contentLengthArr.add(contentLength);

        headers.put("Content-Type", contentTypeArr);
        headers.put("Content-Length", contentLengthArr);

        ITextGenerateService chatGPTService = new ITextGenerateService() {
            @Override
            public String request(IRecipeQuery query) throws TextGenerateServiceException {
                return query.toQueryableString();
            }
        };

        GenerateRecipeHttpHandler handler = new GenerateRecipeHttpHandler(chatGPTService);
        MockHttpRequest request = new MockHttpRequest();

        request.setHeaders(headers);
        request.setRequestBody(inputStream);

        String response = handler.handlePost(request);
        assertEquals("Create a recipe with tomato, eggs, broccoli, and bacon. as Dinner", response);
    }
}
