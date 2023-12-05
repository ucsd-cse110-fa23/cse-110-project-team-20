package client.account;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import org.json.JSONException;
import org.json.JSONObject;

/* Class that maintains a JSON file that (possibly) contains previously saved
 * credentials.
 *
 * If the file doesn't exist or is invalid JSON, is created/reset when CredentialManager
 * is constructed.
 *
 * If username or password are null, then the user did not select "Stay Logged in" .
 * Otherwise, they contain a VALID username/password pair that can be used to log in.
 *
 * Every time credentials are updated in this class, the file is also updated.
 *
 * Probably not thread safe.
 */
public class CredentialManager implements ICredentialManager {
    private static final String DEFAULT_JSON = "{\"username\": null, \"password\": null}";

    private Path jsonPath;
    private JSONObject credentials;

    public CredentialManager(String filePath)
    {
        this.jsonPath = Paths.get(filePath);
        loadFromFile();
    }

    public void
    setCredentials(String username, String password)
    {
        credentials.put("username", username);
        credentials.put("password", password);
        saveToFile();
    }

    public String
    getUsername()
    {
        return credentials.optString("username", null);
    }

    public String
    getPassword()
    {
        return credentials.optString("password", null);
    }

    public boolean
    hasCredentials()
    {
        return getUsername() != null && getPassword() != null;
    }

    private void
    loadFromFile()
    {
        try {
            String jsonContent = new String(Files.readAllBytes(jsonPath));
            credentials = new JSONObject(jsonContent);
        } catch (IOException e) {
            // If there is an issue reading or parsing JSON, reset to default values
            reset();
        } catch (JSONException e) {
            reset();
        }
    }

    private void
    reset()
    {
        try {
            Files.write(jsonPath, DEFAULT_JSON.getBytes());
            credentials = new JSONObject(DEFAULT_JSON);
        } catch (IOException e) {
            e.printStackTrace(); // Handle this exception as needed
        }
    }

    private void
    saveToFile()
    {
        try {
            Files.write(jsonPath, credentials.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace(); // Handle this exception as needed
        }
    }
}
