package server;

import java.io.*;
import com.sun.net.httpserver.*;
import server.request.*;

public abstract class HttpHandlerBase implements HttpHandler {

    public void handle(HttpExchange httpExchange) throws IOException {
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

    protected String handleGet(IHttpRequest request) throws UnsupportedMethodException {
        throw new UnsupportedMethodException();
    }

    protected String handlePost(IHttpRequest request) throws UnsupportedMethodException {
        throw new UnsupportedMethodException();
    }

    protected String handlePut(IHttpRequest request) throws UnsupportedMethodException {
        throw new UnsupportedMethodException();
    }

    protected String handleDelete(IHttpRequest request) throws UnsupportedMethodException {
        throw new UnsupportedMethodException();
    }
}