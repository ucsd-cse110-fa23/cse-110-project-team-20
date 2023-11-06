package server;

import server.request.IHttpRequest;

public class IndexHttpHandler extends HttpHandlerBase {
  protected String handleGet(IHttpRequest request) throws UnsupportedMethodException {
    // ref: from lab 5
    StringBuilder htmlBuilder = new StringBuilder();
    htmlBuilder
        .append("<html>")
        .append("<body>")
        .append("<h1>PantryPals Server</h1>")
        .append("<p>The server is up and running.</p>")
        .append("</body>")
        .append("</html>");

    // encode HTML content
    return htmlBuilder.toString();
  }
}
