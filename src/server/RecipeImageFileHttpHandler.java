package server;

import com.sun.net.httpserver.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;

import server.request.IHttpRequest;

public class RecipeImageFileHttpHandler implements HttpHandler {
  // ref:
  // https://stackoverflow.com/questions/15902662/how-to-serve-static-content-using-suns-simple-httpserver
  public void handle(HttpExchange httpExchange) throws IOException {
    String method = httpExchange.getRequestMethod();
    try {
      if (!method.equals("GET")) {
        throw new Exception("Not Valid Request Method");
      }
    } catch (Exception e) {
      System.out.println("An erroneous request");
      e.printStackTrace();
      response(httpExchange, e.toString(), 500);
      return;
    }

    IHttpRequest httpRequest = new server.request.HttpRequest(httpExchange);
    String filename = httpRequest.getQuery("file");

    if (filename.isEmpty()) {
      response(httpExchange, "filename is missing.", 404);
      return;
    }

    filename = new File(filename).getName();

    File imageFile = new File("build/tmp", filename);

    if (!imageFile.exists()) {
      response(httpExchange, filename + " does not found.", 404);
      return;
    }

    imageFile = imageFile.getCanonicalFile();
    FileInputStream fileInputStream = new FileInputStream(imageFile);
    httpExchange.getResponseHeaders().set("Content-Type", "image/jpeg");
    httpExchange.sendResponseHeaders(200, imageFile.length());
    OutputStream os = httpExchange.getResponseBody();
    fileInputStream.transferTo(os);
    os.close();
    fileInputStream.close();
  }

  private void response(HttpExchange httpExchange, String responseText, int responseCode) throws IOException {
    // Sending back response to the client
    httpExchange.sendResponseHeaders(responseCode, responseText.length());
    OutputStream outStream = httpExchange.getResponseBody();
    outStream.write(responseText.getBytes());
    outStream.close();
  }
}
