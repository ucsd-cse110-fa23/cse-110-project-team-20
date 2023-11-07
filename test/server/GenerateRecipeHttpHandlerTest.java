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
import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.Headers;

import server.api.ITextGenerateService;
import server.api.IVoiceToTextService;
import server.api.IRecipeQuery;
import server.api.TextGenerateServiceException;
import server.mock.MockHttpRequest;

public class GenerateRecipeHttpHandlerTest {
    @Test
    public void instance()
    {
        ITextGenerateService textGenerateService = new ITextGenerateService() {
            @Override
            public String request(IRecipeQuery query) throws TextGenerateServiceException {
                return "";
            }
        };

        IVoiceToTextService voiceToTextService = new IVoiceToTextService() {
            @Override
            public String transcribe(File file) {
                return "";
            }
        };

        GenerateRecipeHttpHandler handler = new GenerateRecipeHttpHandler(textGenerateService, voiceToTextService);
        assertInstanceOf(GenerateRecipeHttpHandler.class, handler);
    }

    @Test
    public void integrationTestWithMock() throws IOException
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

        ITextGenerateService textGenerateService = new ITextGenerateService() {
            @Override
            public String request(IRecipeQuery query) throws TextGenerateServiceException {
                return String.format("Generated recipe based on: %s", query.toQueryableString());
            }
        };

        IVoiceToTextService voiceToTextService = new IVoiceToTextService() {
            @Override
            public String transcribe(File file) {
                if (file.getName().contains("ingredients")) {
                    return "tomato, eggs, broccoli, and bacon";
                } else {
                    return "dinner";
                }
            }
        };

        GenerateRecipeHttpHandler handler = new GenerateRecipeHttpHandler(textGenerateService, voiceToTextService);
        MockHttpRequest request = new MockHttpRequest();

        request.setHeaders(headers);
        request.setRequestBody(inputStream);

        String response = handler.handlePost(request);
        assertEquals("Generated recipe based on: Create a recipe with tomato, eggs, broccoli, and bacon as dinner", response);
    }
}
