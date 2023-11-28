package server.mongodb;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Date;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;

import client.Recipe;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import server.account.AccountContext;
import server.account.IAccountContext;

public class MongoDBRecipeRepositoryTest {
    private MongoCollection<Document> collection;
    private MongoClient client;
    private MongoServer server;

    private IMongoDBConfiguration config;
    private IAccountContext accountContext;

    @BeforeEach
    void setUp() {
        server = new MongoServer(new MemoryBackend());

        String connectionString = server.bindAndGetConnectionString();

        client = MongoClients.create(connectionString);
        collection = client.getDatabase("pantrypal")
                .getCollection("recipes");

        config = new IMongoDBConfiguration() {
            @Override
            public String getConnectionString() {
                return connectionString;
            }
        };

        accountContext = new AccountContext();
    }

    @AfterEach
    void tearDown() {
        client.close();
        server.shutdown();
    }

    @Test
    public void createRecipe() {
        accountContext.setUsername("some username");
        MongoDBRecipeRepository repository = new MongoDBRecipeRepository(config, accountContext);
        repository.createRecipe(new Recipe("Tomato soup", "Some tomato soup description with steps"));

        Document actual = collection.find().first();
        assertEquals(1, collection.countDocuments());
        assertEquals("some username", actual.getString("username"));
        assertEquals("Tomato soup", actual.getString("title"));
        assertEquals("Some tomato soup description with steps", actual.getString("description"));
    }

    @Test
    public void readRecipes() {
        collection
                .insertOne(new Document()
                        .append("username", "pantrypalguest")
                        .append("title", "some title 1")
                        .append("description", "some desc")
                        .append("ingredients", "some ingredients")
                        .append("meal_type", "some meal type"));
        collection
                .insertOne(new Document()
                        .append("username", "some username")
                        .append("title", "some title 0")
                        .append("description", "some desc 0")
                        .append("ingredients", "some ingredients 0")
                        .append("meal_type", "some meal type 0")
                        .append("created_at", new Date()));
        collection
                .insertOne(new Document()
                        .append("username", "some username")
                        .append("title", "some title 1")
                        .append("description", "some desc 1")
                        .append("ingredients", "some ingredients 1")
                        .append("meal_type", "some meal type 1")
                        .append("created_at", new Date()));

        accountContext.setUsername("some username");
        MongoDBRecipeRepository repository = new MongoDBRecipeRepository(config, accountContext);
        List<Recipe> recipes = repository.getRecipes();

        assertEquals(2, recipes.size());
        // desc sort based on created at
        assertEquals("some title 1", recipes.get(0).getTitle());
        assertEquals("some desc 1", recipes.get(0).getDescription());
        assertEquals("some ingredients 1", recipes.get(0).getIngredients());
        assertEquals("some meal type 1", recipes.get(0).getMealType());
        assertEquals("some title 0", recipes.get(1).getTitle());
        assertEquals("some desc 0", recipes.get(1).getDescription());
        assertEquals("some ingredients 0", recipes.get(1).getIngredients());
        assertEquals("some meal type 0", recipes.get(1).getMealType());
    }

    @Test
    public void readRecipe() {
        collection
                .insertOne(new Document()
                        .append("username", "pantrypalguest")
                        .append("title", "some title 1")
                        .append("description", "some desc")
                        .append("ingredients", "some ingredients")
                        .append("meal_type", "some meal type"));
        collection
                .insertOne(new Document()
                        .append("username", "some username")
                        .append("title", "some title 0")
                        .append("description", "some desc 0")
                        .append("ingredients", "some ingredients 0")
                        .append("meal_type", "some meal type 0")
                        .append("created_at", new Date()));
        collection
                .insertOne(new Document()
                        .append("username", "some username")
                        .append("title", "some title 1")
                        .append("description", "some desc 1")
                        .append("ingredients", "some ingredients 1")
                        .append("meal_type", "some meal type 1")
                        .append("created_at", new Date()));

        accountContext.setUsername("some username");
        MongoDBRecipeRepository repository = new MongoDBRecipeRepository(config, accountContext);
        Recipe recipe = repository.getRecipe(1);

        assertEquals("some title 0", recipe.getTitle());
        assertEquals("some desc 0", recipe.getDescription());
        assertEquals("some ingredients 0", recipe.getIngredients());
        assertEquals("some meal type 0", recipe.getMealType());
    }

    @Test
    public void deleteRecipe() {
        collection
                .insertOne(new Document()
                        .append("username", "pantrypalguest")
                        .append("title", "some title 1")
                        .append("description", "some desc")
                        .append("ingredients", "some ingredients")
                        .append("meal_type", "some meal type"));
        collection
                .insertOne(new Document()
                        .append("username", "some username")
                        .append("title", "some title 0")
                        .append("description", "some desc 0")
                        .append("ingredients", "some ingredients 0")
                        .append("meal_type", "some meal type 0")
                        .append("created_at", new Date()));
        collection
                .insertOne(new Document()
                        .append("username", "some username")
                        .append("title", "some title 1")
                        .append("description", "some desc 1")
                        .append("ingredients", "some ingredients 1")
                        .append("meal_type", "some meal type 1")
                        .append("created_at", new Date()));

        accountContext.setUsername("some username");
        MongoDBRecipeRepository repository = new MongoDBRecipeRepository(config, accountContext);
        repository.deleteRecipe(0);
        repository.deleteRecipe(0);

        assertEquals(1, collection.countDocuments());
    }

    @Test
    public void updateRecipe() {
        collection
                .insertOne(new Document()
                        .append("username", "pantrypalguest")
                        .append("title", "some title 1")
                        .append("description", "some desc")
                        .append("ingredients", "some ingredients")
                        .append("meal_type", "some meal type"));
        collection
                .insertOne(new Document()
                        .append("username", "some username")
                        .append("title", "some title 0")
                        .append("description", "some desc 0")
                        .append("ingredients", "some ingredients 0")
                        .append("meal_type", "some meal type 0")
                        .append("created_at", new Date()));
        collection
                .insertOne(new Document()
                        .append("username", "some username")
                        .append("title", "some title 1")
                        .append("description", "some desc 1")
                        .append("ingredients", "some ingredients 1")
                        .append("meal_type", "some meal type 1")
                        .append("created_at", new Date()));

        accountContext.setUsername("some username");
        MongoDBRecipeRepository repository = new MongoDBRecipeRepository(config, accountContext);
        repository.updateRecipe(1, new Recipe("Updated title", "Updated description"));

        assertEquals(1, collection.countDocuments(eq("title", "Updated title")));
    }
}