package server.mongodb;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.bson.conversions.Bson;

import org.json.JSONObject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import client.Recipe;
import server.account.IAccountContext;
import server.recipe.IRecipeRepository;
import server.recipe.ISharedRecipeConfiguration;
import server.recipe.ISharedRecipeRepository;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;

/**
 * MongoDB implementation of IRecipeRepository
 * 
 * This implementation will perform create, update, read, and delete
 * operation on recipe in MongoDB
 */
public class MongoDBRecipeRepository implements IRecipeRepository, ISharedRecipeRepository {
    private IMongoDBConfiguration config;
    private ISharedRecipeConfiguration sharedUrlConfig;
    private IAccountContext accountContext;
    private Bson defaultSorting = descending("created_at");

    public MongoDBRecipeRepository(IMongoDBConfiguration config, ISharedRecipeConfiguration sharedUrlConfig, IAccountContext accountContext) {
        this.config = config;
        this.sharedUrlConfig = sharedUrlConfig;
        this.accountContext = accountContext;
    }

    private MongoCollection<Document> getCollection(MongoClient client) {
        return client.getDatabase("pantrypal").getCollection("recipes");
    }

    @Override
    public List<Recipe> getRecipes() {
        ArrayList<Recipe> recipeList = new ArrayList<>();

        try (MongoClient client = MongoClients.create(config.getConnectionString())) {
            MongoCollection<Document> recipeCollection = getCollection(client);

            FindIterable<Document> recipeDocList = recipeCollection
                    .find(eq("username", accountContext.getUsername()))
                    .sort(defaultSorting);

            for (Document recipeDoc : recipeDocList) {
                if (recipeDoc.getString("shared_url") != null) {
                    recipeDoc.put("shared_url", sharedUrlConfig.sharedUrlBase() + recipeDoc.getString("shared_url"));
                }
                Recipe recipe = Recipe.fromJson(recipeDoc.toJson());
                recipeList.add(recipe);
            }
        }

        return recipeList;
    }

    @Override
    public Recipe getRecipe(int id) {
        try (MongoClient client = MongoClients.create(config.getConnectionString())) {
            MongoCollection<Document> recipeCollection = getCollection(client);
            Document recipeDoc = recipeCollection.find(eq("username", accountContext.getUsername()))
                    .sort(defaultSorting)
                    .skip(id)
                    .first();

            return Recipe.fromJson(recipeDoc.toJson());
        }
    }

    @Override
    public void createRecipe(Recipe recipe) {
        try (MongoClient client = MongoClients.create(config.getConnectionString())) {
            MongoCollection<Document> recipeCollection = getCollection(client);

            Document recipeDoc = Document.parse(new JSONObject(recipe).toString());
            recipeDoc.append("_id", new ObjectId());
            recipeDoc.append("username", accountContext.getUsername());
            recipeDoc.append("created_at", new Date());

            recipeCollection.insertOne(recipeDoc);
        }
    }

    @Override
    public void updateRecipe(int id, Recipe recipe) {
        try (MongoClient client = MongoClients.create(config.getConnectionString())) {
            MongoCollection<Document> recipeCollection = getCollection(client);

            Document recipeDoc = recipeCollection.find(eq("username", accountContext.getUsername()))
                    .sort(defaultSorting)
                    .skip(id)
                    .first();

            if (recipe.getTitle() != null) {
                recipeDoc.append("title", recipe.getTitle());
            }
            if (recipe.getDescription() != null) {
                recipeDoc.append("description", recipe.getDescription());
            }
            if (recipe.getIngredients() != null) {
                recipeDoc.append("ingredients", recipe.getIngredients());
            }
            if (recipe.getMealType() != null) {
                recipeDoc.append("meal_type", recipe.getMealType());
            }

            recipeCollection.updateOne(eq("_id", recipeDoc.get("_id")), new Document("$set", recipeDoc));
        }
    }

    @Override
    public void deleteRecipe(int id) {

        try (MongoClient client = MongoClients.create(config.getConnectionString())) {
            MongoCollection<Document> recipeCollection = getCollection(client);

            Document recipeDoc = recipeCollection.find(eq("username", accountContext.getUsername()))
                    .sort(defaultSorting)
                    .skip(id)
                    .first();

            recipeCollection.deleteOne(eq("_id", recipeDoc.get("_id")));
        }
    }

    @Override
    public void markAsShared(int id) {
        try (MongoClient client = MongoClients.create(config.getConnectionString())) {
            MongoCollection<Document> recipeCollection = getCollection(client);

            Document recipeDoc = recipeCollection.find(eq("username", accountContext.getUsername()))
                    .sort(defaultSorting)
                    .skip(id)
                    .first();

            Document updatedRecipe = new Document().append("shared_url", UUID.randomUUID().toString());

            recipeCollection.updateOne(eq("_id", recipeDoc.get("_id")), new Document("$set", updatedRecipe));
        }
    }

    @Override
    public Recipe getRecipeBySharedUrl(String sharedUrl) {
        try (MongoClient client = MongoClients.create(config.getConnectionString())) {
            MongoCollection<Document> recipeCollection = getCollection(client);
            Document recipeDoc = recipeCollection.find(eq("shared_url", sharedUrl)).first();
            return recipeDoc != null ? Recipe.fromJson(recipeDoc.toJson()) : null;
        }
    }
}
