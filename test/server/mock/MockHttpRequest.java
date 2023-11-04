package server.mock;

import java.io.InputStream;
import java.util.HashMap;

import server.request.IHttpRequest;

public class MockHttpRequest implements IHttpRequest {
    private HashMap<String, String> mockQuery;
    private String mockRequestBodyAsString;
    private InputStream mockRequestBody;

    public void setQuery(HashMap<String, String> query) {
        mockQuery = query;
    }

    public void setRequestBodyAsString(String str) {
        mockRequestBodyAsString = str;
    }

    public void setRequestBody(InputStream requestBody) {
        mockRequestBody = requestBody;
    }

    @Override
    public String getQuery(String key) {
        return mockQuery.get(key);
    }

    @Override
    public String getRequestBodyAsString() {
        return mockRequestBodyAsString;
    }

    @Override
    public InputStream getRequestBody() {
        return mockRequestBody;
    }
}
