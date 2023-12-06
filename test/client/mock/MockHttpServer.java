package client.mock;

import com.sun.net.httpserver.*;

import server.request.HttpRequest;
import server.request.IHttpRequest;

import java.util.concurrent.*;

import java.io.*;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.text.Normalizer;

public class MockHttpServer {
    private static final int SERVER_PORT = 8100;
    private static final String SERVER_HOSTNAME = "localhost";

    private String expectedResponse;
    private String path;
    private HttpServer server;
    private MockRequestHandler mockHandler;

    public MockHttpServer(String path, String expectedResponse) {
        this.path = path;
        this.expectedResponse = expectedResponse;
    }

    public void start() throws IOException {
        start(SERVER_HOSTNAME, SERVER_PORT);
    }

    public void start(int port) throws IOException {
        start(SERVER_HOSTNAME, port);
    }

    public void start(String hostName, int port) throws IOException {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        // create a server
        server = HttpServer.create(
                new InetSocketAddress(hostName, port),
                0);

        mockHandler = new MockRequestHandler(expectedResponse);

        server.createContext(path, mockHandler);
        server.setExecutor(threadPoolExecutor);
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    public String getLastHttpRequestQuery(String key) {
        return mockHandler.getLastHttpRequestQuery(key);
    }
    public String getLastHttpRequestBodyAsString() {
        return mockHandler.getLastHttpRequestBodyAsString();
    }
}

class MockRequestHandler implements HttpHandler {
    private String response;
    private IHttpRequest lastRequest;
    private String body;

    MockRequestHandler(String response) {
        this.response = response;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        // // testing purpose
        // BufferedWriter writer = new BufferedWriter(new FileWriter("header.json"));
        // writer.write(new JSONObject(httpExchange.getRequestHeaders()).toString(2));
        // writer.close();
        // File outputFile = new File("body.txt");
        // try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
        //     outputStream.write(httpExchange.getRequestBody().readAllBytes());
        // }
        lastRequest = new HttpRequest(httpExchange);
        body = lastRequest.getRequestBodyAsString();

        response = Normalizer.normalize(response, Normalizer.Form.NFD);
        response = response.replaceAll("\\P{InBasic_Latin}", "");

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    public String getLastHttpRequestQuery(String key) {
        return lastRequest != null ? lastRequest.getQuery(key) : null;
    }

    public String getLastHttpRequestBodyAsString() {
        return body;
    }
}