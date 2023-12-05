package server.mongodb;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import server.account.IAccountService;
import server.account.IncorrectPassword;

/**
 * AccountService implementation for MongoDB
 *
 * This class will check the provided user information from MongoDB. If the account is new one,
 * create one and allow to log in. If the account does exist, check the password is matched. If the
 * password is not matched, throw the incorrect password exception.
 */
public class MongoDBAccountService implements IAccountService {
    private IMongoDBConfiguration config;

    public MongoDBAccountService(IMongoDBConfiguration config)
    {
        this.config = config;
    }

    @Override
    public boolean
    loginOrCreateAccount(String username, String password) throws IncorrectPassword
    {
        try (MongoClient client = MongoClients.create(config.getConnectionString())) {
            MongoCollection<Document> accountCollection =
                client.getDatabase("pantrypal").getCollection("accounts");

            // check if username is in the collection
            Document user = accountCollection.find(eq("username", username)).first();

            if (user != null) {
                String storedPassword = user.getString("password");

                // if the password is not matched, throw incorrect password error. Otherwise, logged
                // in
                if (!storedPassword.equals(password)) {
                    throw new IncorrectPassword();
                }
            } else {
                // if it is a new account, create new one with given credentials
                Document accountDoc = new Document("_id", new ObjectId())
                                          .append("username", username)
                                          .append("password", password);

                accountCollection.insertOne(accountDoc);
            }
        }

        return true;
    }
}
