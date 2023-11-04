package server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

import server.request.HttpRequest;

public class HttpRequestTest {
    @Test
    public void instance()
    {
        MockHttpExchange mock = new MockHttpExchange();
        mock.setURI("http://localhost/?hello=123");

        HttpRequest httpRequest = new HttpRequest(mock);
        assertInstanceOf(HttpRequest.class, httpRequest);
    }

    @Test
    public void queryString()
    {
        String expected1 = "123";
        String expected2 = "456";

        MockHttpExchange mock = new MockHttpExchange();
        mock.setURI("http://localhost/?hello=123&world=456");

        HttpRequest httpRequest = new HttpRequest(mock);
        String actual1 = httpRequest.getQuery("hello");
        String actual2 = httpRequest.getQuery("world");
        String actual3 = httpRequest.getQuery("something-not-exist");

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
        assertNull(actual3);
    }

    @Test
    public void requestBody()
    {
        String expected = "Body Text";

        MockHttpExchange mock = new MockHttpExchange();
        mock.setURI("http://localhost/?hello=123&world=456");
        mock.setRequestBody("Body Text");

        HttpRequest httpRequest = new HttpRequest(mock);
        String actual = httpRequest.getRequestBodyAsString();

        assertEquals(expected, actual);
    }
}

class MockHttpExchange extends HttpExchange {

    private String uri;
    private String requestBody;

    public void setURI(String uri) {
        this.uri = uri;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    @Override
    public URI getRequestURI() {
        try {
            return new URI(this.uri);
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public InputStream getRequestBody() {
        return new ByteArrayInputStream(requestBody.getBytes());
    }

    @Override
    public Headers getRequestHeaders() {
        throw new UnsupportedOperationException("Unimplemented method 'getRequestHeaders'");
    }

    @Override
    public Headers getResponseHeaders() {
        throw new UnsupportedOperationException("Unimplemented method 'getResponseHeaders'");
    }

    @Override
    public String getRequestMethod() {
        throw new UnsupportedOperationException("Unimplemented method 'getRequestMethod'");
    }

    @Override
    public HttpContext getHttpContext() {
        throw new UnsupportedOperationException("Unimplemented method 'getHttpContext'");
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Unimplemented method 'close'");
    }

    @Override
    public OutputStream getResponseBody() {
        throw new UnsupportedOperationException("Unimplemented method 'getResponseBody'");
    }

    @Override
    public void sendResponseHeaders(int rCode, long responseLength) throws IOException {
        throw new UnsupportedOperationException("Unimplemented method 'sendResponseHeaders'");
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        throw new UnsupportedOperationException("Unimplemented method 'getRemoteAddress'");
    }

    @Override
    public int getResponseCode() {
        throw new UnsupportedOperationException("Unimplemented method 'getResponseCode'");
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        throw new UnsupportedOperationException("Unimplemented method 'getLocalAddress'");
    }

    @Override
    public String getProtocol() {
        throw new UnsupportedOperationException("Unimplemented method 'getProtocol'");
    }

    @Override
    public Object getAttribute(String name) {
        throw new UnsupportedOperationException("Unimplemented method 'getAttribute'");
    }

    @Override
    public void setAttribute(String name, Object value) {
        throw new UnsupportedOperationException("Unimplemented method 'setAttribute'");
    }

    @Override
    public void setStreams(InputStream i, OutputStream o) {
        throw new UnsupportedOperationException("Unimplemented method 'setStreams'");
    }

    @Override
    public HttpPrincipal getPrincipal() {
        throw new UnsupportedOperationException("Unimplemented method 'getPrincipal'");
    }}