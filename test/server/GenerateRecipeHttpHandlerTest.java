package server;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import server.api.ITextToImageService;
import server.api.IVoiceToTextService;
import server.api.IRecipeQuery;
import server.api.TextGenerateServiceException;
import server.mock.MockHttpRequest;
import server.recipe.IRecipeImageUrlConfiguration;

public class GenerateRecipeHttpHandlerTest {
    @Test
    public void instance() {
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

        ITextToImageService textToImageService = (IRecipeQuery query) -> new File("test/resources/tomato.jpg");
        IRecipeImageUrlConfiguration imageUrlConfig = () -> "http://localhost/recipe/image/?file=";

        GenerateRecipeHttpHandler handler = new GenerateRecipeHttpHandler(textGenerateService, voiceToTextService, textToImageService, imageUrlConfig);
        assertInstanceOf(GenerateRecipeHttpHandler.class, handler);
    }

    @Test
    public void integrationTestWithMock() throws IOException {
        File audioRequestBodyFile = new File("test/resources/audio-request-body-with-voice.txt");
        InputStream inputStream = new FileInputStream(audioRequestBodyFile);

        String audioRequestHeaderText = Files
                .readString(Path.of("test/resources/audio-request-header-with-voice.json"));
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
                return String.format("Title of recipe\n\nGenerated recipe based on: %s", query.toQueryableString());
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
        ITextToImageService textToImageService = (IRecipeQuery query) -> new File("test/resources/tomato.jpg");
        IRecipeImageUrlConfiguration imageUrlConfig = () -> "http://localhost/recipe/image/?file=";

        GenerateRecipeHttpHandler handler = new GenerateRecipeHttpHandler(textGenerateService, voiceToTextService, textToImageService, imageUrlConfig);
        MockHttpRequest request = new MockHttpRequest();

        request.setHeaders(headers);
        request.setRequestBody(inputStream);

        String response = handler.handlePost(request);
        JSONObject responseJson = new JSONObject(response);
        assertTrue(responseJson.getString("description").contains("Generated recipe based on:"));
        assertTrue(responseJson.getString("image_url").contains("localhost/recipe/image/?file"));
    }
}
