package client.account;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import org.json.JSONObject;

/**
 * Server Account Manager
 *
 * request to the server with given username and password. Process the response to take user login
 * token. Any error during the login process will throw appropriate exception message such as
 * IncorrectPassword.
 */
public class ServerAccountManager implements IAccountManager {
    private String path = "/account/login-or-create";
    private String URL;

    public ServerAccountManager()
    {
        this("http://localhost:8100");
    }

    public ServerAccountManager(String baseUrl)
    {
        URL = String.format("%s%s", baseUrl, path);
    }

    @Override
    public String
    loginOrCreateAccount(String username, String password) throws IncorrectPassword, LoginFailed
    {
        // request to the server with username nad password
        JSONObject resObj = performRequest(username, password);

        // if there is any error, do proper error handling
        if (resObj.has("error")) {
            String error = resObj.getString("error");
            if (error.equals("Incorrect Password")) {
                throw new IncorrectPassword(error);
            } else {
                throw new LoginFailed(error);
            }
        }

        // take token and return from the response
        return resObj.has("token") ? resObj.getString("token") : null;
    }

    private JSONObject
    performRequest(String username, String password)
    {
        String urlString = URL;
        String response = "{\"error\": \"Could not perform request\"}";
        JSONObject reqObj = new JSONObject();
        reqObj.put("username", username);
        reqObj.put("password", password);

        try {
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(reqObj.toString());
            out.flush();
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder sb = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();

            response = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject responseJson;

        try {
            responseJson = new JSONObject(response.toString().trim());
        } catch (Exception e) {
            responseJson = new JSONObject();
            responseJson.put("error", response.toString());
        }

        return responseJson;
    }
}
