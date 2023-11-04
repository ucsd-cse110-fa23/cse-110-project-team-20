package client.recipe.mock;

import com.sun.net.httpserver.*;
import java.util.concurrent.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class MockHttpServer {
    private static final int SERVER_PORT = 8100;
    private static final String SERVER_HOSTNAME = "localhost";

    private String expectedResponse;
    private String path;
    private HttpServer server;

    public MockHttpServer(String path, String expectedResponse) {
        this.path = path;
        this.expectedResponse = expectedResponse;
    }

    public void start() throws IOException {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        // create a server
        server = HttpServer.create(
                new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),
                0);

        server.createContext(path, new MockRequestHandler(expectedResponse));
        server.setExecutor(threadPoolExecutor);
        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}

class MockRequestHandler implements HttpHandler {
    private String response;
    MockRequestHandler(String response) {
        this.response = response;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }
}