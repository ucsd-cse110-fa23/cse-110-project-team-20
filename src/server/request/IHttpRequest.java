package server.request;

import com.sun.net.httpserver.Headers;
import java.io.InputStream;

public interface IHttpRequest {
    public String getQuery(String key);
    public String getRequestBodyAsString();
    public InputStream getRequestBody();
    public Headers getHeaders();
}
