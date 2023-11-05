package server;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;


public class WhisperHandler implements HttpHandler {
    public void handle(HttpExchange httpExchange) throws IOException
    {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }
        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }
    private String handleGet(HttpExchange httpExchange) throws IOException, URISyntaxException
    {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query == "ingredients") {
            response = Whisper.getIngredientTranscription();
        }
        else if (query == "meal_type"){
            response = Whisper.getMealTypeTranscription();
        }
        return response;
    }
}

