package server;
import client.Recipe;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;


public class RequestHandler implements HttpHandler {

    private LinkedList<Recipe> recipes; // currently, data is not written to permanent storage

    public RequestHandler() {
        recipes = new LinkedList<Recipe>();
        recipes.add(new Recipe("c","d"));
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else if (method.equals("POST")) {
                //response = handlePost(httpExchange);
            } else if (method.equals("PUT")) {
                //response = handlePut(httpExchange);
            } else if (method.equals("DELETE")) {
                //response = handleDelete(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }

        //Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    //TODO: This is a stub with very basic functionality, should be developed when we decide on a format
    private String handleGet(HttpExchange httpExchange) throws IOException {
        String response = "Invalid GET request";
//        URI uri = httpExchange.getRequestURI();
//        String query = uri.getRawQuery();
//        if (query != null) {
//            String value = query.substring(query.indexOf("=") + 1);
//        }
        response = recipes.get(0).getTitle() + "|" + recipes.get(0).getDescription();
        return response;
    }

    //TODO: untested stub adapted from lab5
    private String handlePost(HttpExchange httpExchange) throws IOException {
//        InputStream inStream = httpExchange.getRequestBody();
//        Scanner scanner = new Scanner(inStream);
//        String postData = scanner.nextLine();
//        String title = postData.substring(0, postData.indexOf(","));
//        String description = postData.substring(postData.indexOf(",") + 1);
//
//
//        // Store data in hashmap
//        recipes.add(new Recipe(title, description));
//
//        String response = "Posted entry {" + title + ", " + description + "}";
//        System.out.println(response);
//        scanner.close();
//
//        return response;
        return "Unfinished method";
    }

//    private String handlePut(HttpExchange httpExchange) throws IOException {
//        String response = "Invalid PUT request";
//        InputStream inStream = httpExchange.getRequestBody();
//        Scanner scanner = new Scanner(inStream);
//        String postData, language, year, oldYear;
//        postData = scanner.nextLine();
//        language = postData.substring(0, postData.indexOf(","));
//        year = postData.substring(postData.indexOf(",") + 1);
//
//        scanner.close();
//
//        if (data.containsKey(language)) {
//            oldYear = data.get(language);
//            data.put(language, year);
//
//            response = "Posted entry {" + language + ", " + year + "} (Previous year: " + oldYear + ")";
//        } else {
//            data.put(language, year);
//
//            response = "Added entry {" + language + "," + year + "}";
//        }
//
//        System.out.println(response);
//        return response;
//    }
//    private String handleDelete(HttpExchange httpExchange) throws IOException {
//        String response = "Invalid query";
//        URI uri = httpExchange.getRequestURI();
//        String query, value, year;
//        query = uri.getRawQuery();
//        if (query != null) {
//            value = query.substring(query.indexOf("=") + 1);
//            if (data.get(value) != null) {
//                year = data.get(value);
//                data.remove(value);
//                response = "Deleted entry {" + value + "," + year + "}";
//            } else {
//                response = "No data found for " + value;
//            }
//        }
//        return response;
//    }
}