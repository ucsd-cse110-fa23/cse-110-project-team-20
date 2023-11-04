package server.request;

import java.io.InputStream;

public interface IHttpRequest {
    public String getQuery(String key);
    public String getRequestBodyAsString();
    public InputStream getRequestBody();
}
