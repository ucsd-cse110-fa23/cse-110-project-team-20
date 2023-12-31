package client.models;

import client.Recipe;
import client.account.IAccountSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 * Server recipe model
 *
 * Describes how recipe data is saved on the server primarily by overriding IRecipeModel.
 * Also contains methods that interact with the server via performRequest, all related to recipe
 * data manipulation.
 */
public class ServerRecipeModel implements IRecipeModel {
    private String path = "/recipe";
    private String URL;
    private IAccountSession session;
    private IRecipeModelErrorHandler errorHandler = null;

    public ServerRecipeModel(IAccountSession session)
    {
        this(session, "http://localhost:8100");
    }

    public ServerRecipeModel(IAccountSession session, String baseUrl)
    {
        URL = String.format("%s%s", baseUrl, path);
        this.session = session;
    }

    public void
    setErrorHandler(IRecipeModelErrorHandler handler)
    {
        this.errorHandler = handler;
    }

    @Override
    public List<Recipe>
    getRecipes()
    {
        JSONObject response = performRequest(RequestMethod.GET);
        JSONArray recipesJson = response.getJSONArray("recipes");

        ArrayList<Recipe> recipes = new ArrayList<>();

        for (int i = 0; i < recipesJson.length(); i++) {
            JSONObject recipe = recipesJson.getJSONObject(i);
            Recipe r = Recipe.fromJson(recipe.toString());
            recipes.add(r);
        }

        return recipes;
    }

    @Override
    public Recipe
    getRecipe(int id)
    {
        JSONObject response = performRequest(RequestMethod.GET, id);
        return Recipe.fromJson(response.toString());
    }

    @Override
    public void
    createRecipe(Recipe recipe)
    {
        String body = new JSONObject(recipe).toString();
        performRequest(RequestMethod.POST, null, body);
    }

    @Override
    public void
    updateRecipe(int id, Recipe recipe)
    {
        String body = new JSONObject(recipe).toString();
        performRequest(RequestMethod.PUT, id, body);
    }

    @Override
    public void
    deleteRecipe(int id)
    {
        performRequest(RequestMethod.DELETE, id);
    }

    @Override
    public void
    shareRecipe(int id)
    {
        performRequest("/share", RequestMethod.POST, id, null);
    }

    @Override
    public void
    shareRecipe(int id, Runnable onComplete)
    {
        shareRecipe(id);
    }

    private JSONObject
    performRequest(RequestMethod method)
    {
        return performRequest(method, null, null);
    }

    private JSONObject
    performRequest(RequestMethod method, Integer id)
    {
        return performRequest(method, id, null);
    }

    private JSONObject
    performRequest(RequestMethod method, Integer id, String body)
    {
        return performRequest("", method, id, body);
    }

    /**
     * Perform HTTP request to the given URL with method, id, and body.
     *
     * @param method
     * @param id
     * @param body
     * @return
     */
    private JSONObject
    performRequest(String resourceUrl, RequestMethod method, Integer id, String body)
    {
        String urlString = URL;

        if (resourceUrl != null) {
            urlString += resourceUrl;
        }

        String response = "{\"error\": \"Could not perform request\"}";

        try {
            if (id != null) {
                urlString += String.format("?id=%s", id);
            }

            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", session.getToken());
            conn.setRequestMethod(method.getMethodName());
            conn.setDoOutput(true);

            if ((method == RequestMethod.POST || method == RequestMethod.PUT) && body != null) {
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(body);
                out.flush();
                out.close();
            }

            // @TODO we need to handle response code from server and client
            // int responseCode = conn.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder sb = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();

            response = sb.toString();
        } catch (Exception e) {
            if (this.errorHandler != null) {
                errorHandler.onError(e);
            }
            e.printStackTrace();
        }

        return new JSONObject(response.toString().trim());
    }
}

/**
 * Typed Request Method
 *
 * RequestMethod enum helps us not miss the name of request method
 */
enum RequestMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private String methodName;

    RequestMethod(String methodName)
    {
        this.methodName = methodName;
    }

    public String
    getMethodName()
    {
        return methodName;
    }
}
