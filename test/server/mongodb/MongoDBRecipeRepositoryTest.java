package server.mongodb;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import client.Recipe;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.account.AccountContext;
import server.account.IAccountContext;
import server.recipe.ISharedRecipeConfiguration;

public class MongoDBRecipeRepositoryTest {
    private MongoCollection<Document> collection;
    private MongoClient client;
    private MongoServer server;

    private IMongoDBConfiguration config;
    private ISharedRecipeConfiguration shareConfig;
    private IAccountContext accountContext;

    private Date oldDate;
    private Date recentDate;

    @BeforeEach
    void
    setUp()
    {
        server = new MongoServer(new MemoryBackend());

        String connectionString = server.bindAndGetConnectionString();

        client = MongoClients.create(connectionString);
        collection = client.getDatabase("pantrypal").getCollection("recipes");

        config = new IMongoDBConfiguration() {
            @Override public String getConnectionString()
            {
                return connectionString;
            }
        };
        shareConfig = () -> "http://localhost/recipe/shared?id=";

        accountContext = new AccountContext();

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2023, 11, 1);
        oldDate = cal.getTime();
        cal.set(2023, 12, 1);
        recentDate = cal.getTime();
    }

    @AfterEach
    void
    tearDown()
    {
        client.close();
        server.shutdown();
    }

    @Test
    public void
    createRecipe()
    {
        accountContext.setUsername("some username");

        MongoDBRecipeRepository repository = new MongoDBRecipeRepository(config, shareConfig, accountContext);
        repository.createRecipe(
            new Recipe("Tomato soup", "Some tomato soup description with steps"));

        Document actual = collection.find().first();
        assertEquals(1, collection.countDocuments());
        assertEquals("some username", actual.getString("username"));
        assertEquals("Tomato soup", actual.getString("title"));
        assertEquals("Some tomato soup description with steps", actual.getString("description"));
    }

    @Test
    public void
    readRecipes()
    {
        collection.insertOne(new Document()
                                 .append("username", "pantrypalguest")
                                 .append("title", "some title 1")
                                 .append("description", "some desc")
                                 .append("ingredients", "some ingredients")
                                 .append("meal_type", "some meal type"));
        collection.insertOne(new Document()
                                 .append("username", "some username")
                                 .append("title", "some title 0")
                                 .append("description", "some desc 0")
                                 .append("ingredients", "some ingredients 0")
                                 .append("meal_type", "some meal type 0")
                                 .append("shared_url", "some-shared-url")
                                 .append("created_at", oldDate));
        collection.insertOne(new Document()
                                 .append("username", "some username")
                                 .append("title", "some title 1")
                                 .append("description", "some desc 1")
                                 .append("ingredients", "some ingredients 1")
                                 .append("meal_type", "some meal type 1")
                                 .append("created_at", recentDate));

        accountContext.setUsername("some username");
        MongoDBRecipeRepository repository = new MongoDBRecipeRepository(config, shareConfig, accountContext);
        List<Recipe> recipes = repository.getRecipes();

        assertEquals(2, recipes.size());
        // desc sort based on created at
        assertEquals("some title 1", recipes.get(0).getTitle());
        assertEquals("some desc 1", recipes.get(0).getDescription());
        assertEquals("some ingredients 1", recipes.get(0).getIngredients());
        assertEquals("some meal type 1", recipes.get(0).getMealType());
        assertNull(recipes.get(0).getSharedUrl());
        assertEquals("some title 0", recipes.get(1).getTitle());
        assertEquals("some desc 0", recipes.get(1).getDescription());
        assertEquals("some ingredients 0", recipes.get(1).getIngredients());
        assertEquals("some meal type 0", recipes.get(1).getMealType());
        assertEquals(shareConfig.sharedUrlBase() + "some-shared-url", recipes.get(1).getSharedUrl());
    }

    @Test
    public void
    readRecipe()
    {
        collection.insertOne(new Document()
                                 .append("username", "pantrypalguest")
                                 .append("title", "some title 2")
                                 .append("description", "some desc")
                                 .append("ingredients", "some ingredients")
                                 .append("meal_type", "some meal type"));
        collection.insertOne(new Document()
                                 .append("username", "some username")
                                 .append("title", "some title 0")
                                 .append("description", "some desc 0")
                                 .append("ingredients", "some ingredients 0")
                                 .append("meal_type", "some meal type 0")
                                 .append("created_at", oldDate));
        collection.insertOne(new Document()
                                 .append("username", "some username")
                                 .append("title", "some title 3")
                                 .append("description", "some desc 1")
                                 .append("ingredients", "some ingredients 1")
                                 .append("meal_type", "some meal type 1")
                                 .append("created_at", recentDate));

        accountContext.setUsername("some username");
        MongoDBRecipeRepository repository = new MongoDBRecipeRepository(config, shareConfig, accountContext);
        Recipe recipe = repository.getRecipe(1);

        assertEquals("some title 0", recipe.getTitle());
        assertEquals("some desc 0", recipe.getDescription());
        assertEquals("some ingredients 0", recipe.getIngredients());
        assertEquals("some meal type 0", recipe.getMealType());
    }

    @Test
    public void
    deleteRecipe()
    {
        collection.insertOne(new Document()
                                 .append("username", "pantrypalguest")
                                 .append("title", "some title 1")
                                 .append("description", "some desc")
                                 .append("ingredients", "some ingredients")
                                 .append("meal_type", "some meal type"));
        collection.insertOne(new Document()
                                 .append("username", "some username")
                                 .append("title", "some title 0")
                                 .append("description", "some desc 0")
                                 .append("ingredients", "some ingredients 0")
                                 .append("meal_type", "some meal type 0")
                                 .append("created_at", oldDate));
        collection.insertOne(new Document()
                                 .append("username", "some username")
                                 .append("title", "some title 1")
                                 .append("description", "some desc 1")
                                 .append("ingredients", "some ingredients 1")
                                 .append("meal_type", "some meal type 1")
                                 .append("created_at", recentDate));

        accountContext.setUsername("some username");
        MongoDBRecipeRepository repository = new MongoDBRecipeRepository(config, shareConfig, accountContext);
        repository.deleteRecipe(0);
        repository.deleteRecipe(0);

        assertEquals(1, collection.countDocuments());
    }

    @Test
    public void
    updateRecipe()
    {
        collection.insertOne(new Document()
                                 .append("username", "pantrypalguest")
                                 .append("title", "some title 1")
                                 .append("description", "some desc")
                                 .append("ingredients", "some ingredients")
                                 .append("meal_type", "some meal type"));
        collection.insertOne(new Document()
                                 .append("username", "some username")
                                 .append("title", "some title 0")
                                 .append("description", "some desc 0")
                                 .append("ingredients", "some ingredients 0")
                                 .append("meal_type", "some meal type 0")
                                 .append("created_at", oldDate));
        collection.insertOne(new Document()
                                 .append("username", "some username")
                                 .append("title", "some title 1")
                                 .append("description", "some desc 1")
                                 .append("ingredients", "some ingredients 1")
                                 .append("meal_type", "some meal type 1")
                                 .append("created_at", recentDate));

        accountContext.setUsername("some username");
        MongoDBRecipeRepository repository = new MongoDBRecipeRepository(config, shareConfig, accountContext);
        repository.updateRecipe(1, new Recipe("Updated title", "Updated description"));

        assertEquals(1, collection.countDocuments(eq("title", "Updated title")));
    }

    @Test
    public void markAsShared() {
        collection
                .insertOne(new Document()
                        .append("username", "some username")
                        .append("title", "some title 0")
                        .append("description", "some desc 0")
                        .append("ingredients", "some ingredients 0")
                        .append("meal_type", "some meal type 0")
                        .append("created_at", new Date()));

        accountContext.setUsername("some username");
        MongoDBRecipeRepository repository = new MongoDBRecipeRepository(config, shareConfig, accountContext);
        repository.markAsShared(0);

        assertEquals(1, collection.countDocuments(exists("shared_url")));
        assertNotNull(collection.find().first().getString("shared_url"));
    }

    @Test
    public void getRecipeBySharedUrl() {
        collection
                .insertOne(new Document()
                        .append("username", "pantrypalguest")
                        .append("title", "some sahred recipe title")
                        .append("description", "some desc")
                        .append("ingredients", "some ingredients")
                        .append("meal_type", "some meal type")
                        .append("shared_url", "some-shared-url"));
        collection
                .insertOne(new Document()
                        .append("username", "some username")
                        .append("title", "some title 0")
                        .append("description", "some desc 0")
                        .append("ingredients", "some ingredients 0")
                        .append("meal_type", "some meal type 0")
                        .append("created_at", oldDate));
        collection
                .insertOne(new Document()
                        .append("username", "some username")
                        .append("title", "some title 1")
                        .append("description", "some desc 1")
                        .append("ingredients", "some ingredients 1")
                        .append("meal_type", "some meal type 1")
                        .append("created_at", recentDate));

        accountContext.setUsername("some username");
        MongoDBRecipeRepository repository = new MongoDBRecipeRepository(config, shareConfig, accountContext);
        Recipe recipe = repository.getRecipeBySharedUrl("some-shared-url");

        assertEquals("some sahred recipe title", recipe.getTitle());
    }

    @Test
    public void getRecipeBySharedUrlNonExist() {
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
                        .append("created_at", oldDate));
        collection
                .insertOne(new Document()
                        .append("username", "some username")
                        .append("title", "some title 1")
                        .append("description", "some desc 1")
                        .append("ingredients", "some ingredients 1")
                        .append("meal_type", "some meal type 1")
                        .append("created_at", recentDate));

        accountContext.setUsername("some username");
        MongoDBRecipeRepository repository = new MongoDBRecipeRepository(config, shareConfig, accountContext);
        Recipe recipe = repository.getRecipeBySharedUrl("some-shared-url");

        assertNull(recipe);
    }
}
