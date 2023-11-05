package server.request;

import java.io.InputStream;

import com.sun.net.httpserver.Headers;

public interface IHttpRequest {
    public String getQuery(String key);
    public String getRequestBodyAsString();
    public InputStream getRequestBody();
    public Headers getHeaders();
}
