package server.request;

import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;

public class HttpRequest implements IHttpRequest {
    private HttpExchange httpExchange;
    Map<String,String> query = new HashMap<String, String>();

    public HttpRequest(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;

        parseQuery(httpExchange);
    }

    private void parseQuery(HttpExchange httpExchange) {
        String queryData = httpExchange.getRequestURI().getRawQuery();
        for (String parameter : queryData.split("&")) {
            String key = parameter.substring(0, parameter.indexOf("="));
            String value = parameter.substring(parameter.indexOf("=") + 1);
            if (key != null && value != null) {
                query.put(key, value);
            }
        }
    }

    public String getQuery(String key) {
        return query.get(key);
    }

    public InputStream getRequestBody() {
        return httpExchange.getRequestBody();
    }

    public String getRequestBodyAsString() {
        StringBuffer sb = new StringBuffer();
        InputStream inStream = getRequestBody();
        Scanner scanner = new Scanner(inStream);

        while(scanner.hasNextLine()) {
            sb.append(scanner.nextLine());
        }

        scanner.close();
        return sb.toString();
    }
}
