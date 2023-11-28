package server.mongodb;

import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.sun.net.httpserver.*;

import server.account.IAccountContext;

import static com.mongodb.client.model.Filters.eq;

/**
 * MongoDB based BasicAuthenticator for java server
 *
 * Our basic authentication will be handled with this class.
 * ref: https://stackoverflow.com/questions/41407510/java-httpserver-basic-authentication-for-different-request-methods
 */
public class MongoDBBasicAuthenticator extends BasicAuthenticator {
    private IMongoDBConfiguration config;
    private IAccountContext accountContext;

    public MongoDBBasicAuthenticator(IMongoDBConfiguration config, IAccountContext accountContext) {
        super("PantryPal");
        this.config = config;
        this.accountContext = accountContext;
    }

    @Override
    public boolean checkCredentials(String username, String password) {
        accountContext.setUsername(null);

        // check credential by checking mongodb with given username and password
        try (MongoClient client = MongoClients.create(config.getConnectionString())) {
            MongoCollection<Document> accountCollection = client.getDatabase("pantrypal")
                .getCollection("accounts");

            // check if username is in the collection
            Document user = accountCollection.find(eq("username", username)).first();

            if (user != null) {
                String storedPassword = user.getString("password");
                if (storedPassword.equals(password)) {
                    accountContext.setUsername(username);
                    System.out.println(String.format("[AUTH] requested as: %s", username));
                    return true;
                }
            }
        }
        return false;
    }
}
