package server.mongodb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import server.account.IncorrectPassword;

public class MongoDbAccountServiceTest {
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
    void testCreatingAccount() {
        MongoDBAccountService accountService = new MongoDBAccountService(config);
        boolean result = accountService.loginOrCreateAccount("pantrypaladmin", "some-long-secret");

        assertTrue(result);
        assertEquals(1, collection.countDocuments());
        assertEquals("pantrypaladmin", collection.find().first().getString("username"));
        assertEquals("some-long-secret", collection.find().first().getString("password"));
    }

    @Test
    void testLogin() {
        // set testing account
        collection
                .insertOne(new Document().append("username", "pantrypalguest").append("password", "some-short-secret"));

        MongoDBAccountService accountService = new MongoDBAccountService(config);
        boolean result = accountService.loginOrCreateAccount("pantrypalguest", "some-short-secret");

        assertTrue(result);
    }

    @Test
    void testLoginWithWrongPassword() {
        // set testing account
        collection
                .insertOne(new Document().append("username", "pantrypalguest").append("password", "some-short-secret"));

        MongoDBAccountService accountService = new MongoDBAccountService(config);

        assertThrows(IncorrectPassword.class, () -> {
            accountService.loginOrCreateAccount("pantrypalguest", "some-wrong-wrong-secret");
        });
    }

}
