package server.mongodb;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import server.account.IAccountContext;

public class MongoDBBasicAuthenticatorTest {
    private MongoCollection<Document> collection;
    private MongoClient client;
    private MongoServer server;

    private IMongoDBConfiguration config;

    @BeforeEach
    void setUp() {
        server = new MongoServer(new MemoryBackend());

        String connectionString = server.bindAndGetConnectionString();

        client = MongoClients.create(connectionString);
        collection = client.getDatabase("pantrypal")
                .getCollection("accounts");

        config = new IMongoDBConfiguration() {
            @Override
            public String getConnectionString() {
                return connectionString;
            }
        };
    }

    @AfterEach
    void tearDown() {
        client.close();
        server.shutdown();
    }

    @Test
    public void checkCredentials() {
        collection.insertOne(new Document().append("username", "pantrypalguest").append("password", "pass1234"));

        IAccountContext accountContext = new IAccountContext() {
            private String username;
            @Override
            public void setUsername(String username) {
                this.username = username;
            }

            @Override
            public String getUsername() {
                return username;
            }
        };

        MongoDBBasicAuthenticator authenticator = new MongoDBBasicAuthenticator(config, accountContext);

        assertFalse(authenticator.checkCredentials("username", "password"));
        assertFalse(authenticator.checkCredentials("pantrypalguest", "password"));
        assertTrue(authenticator.checkCredentials("pantrypalguest", "pass1234"));
    }
}
