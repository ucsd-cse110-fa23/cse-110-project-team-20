package server;

import com.sun.net.httpserver.*;
import java.io.*;
import org.json.JSONObject;
import server.request.*;

/*
 * HTTP handler base class
 *
 * Calls the appropriate methods for 4 of the base HTTP requests.
 *
 */
public abstract class HttpHandlerBase implements HttpHandler {
    public void
    handle(HttpExchange httpExchange) throws IOException
    {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        IHttpRequest httpRequest = new HttpRequest(httpExchange);

        try {
            if (method.equals("GET")) {
                response = handleGet(httpRequest);
            } else if (method.equals("POST")) {
                response = handlePost(httpRequest);
            } else if (method.equals("PUT")) {
                response = handlePut(httpRequest);
            } else if (method.equals("DELETE")) {
                response = handleDelete(httpRequest);
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

    protected String
    handleGet(IHttpRequest request) throws UnsupportedMethodException
    {
        throw new UnsupportedMethodException();
    }

    protected String
    handlePost(IHttpRequest request) throws UnsupportedMethodException
    {
        throw new UnsupportedMethodException();
    }

    protected String
    handlePut(IHttpRequest request) throws UnsupportedMethodException
    {
        throw new UnsupportedMethodException();
    }

    protected String
    handleDelete(IHttpRequest request) throws UnsupportedMethodException
    {
        throw new UnsupportedMethodException();
    }

    protected String
    success()
    {
        JSONObject response = new JSONObject();
        response.put("success", true);
        return response.toString(2);
    }

    protected String
    fail(String message)
    {
        JSONObject response = new JSONObject();
        response.put("error", message);
        return response.toString(2);
    }
}
