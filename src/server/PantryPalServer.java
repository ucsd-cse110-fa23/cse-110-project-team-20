package server;

import com.sun.net.httpserver.*;

import server.account.AccountContext;
import server.account.IAccountContext;
import server.account.IAccountService;
import server.account.LocalAccountService;
import server.account.LocalBasicAuthenticator;
import server.api.ChatGPTService;
import server.api.ITextGenerateService;
import server.api.IVoiceToTextService;
import server.api.LocalTextGenerateService;
import server.api.LocalVoiceToTextService;
import server.api.WhisperService;
import server.mongodb.MongoDBBasicAuthenticator;
import server.mongodb.MongoDBRecipeRepository;
import server.mongodb.MongoDBAccountService;
import server.recipe.IRecipeRepository;
import server.recipe.ISharedRecipeRepository;
import server.recipe.JSONRecipeRepository;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class PantryPalServer {
    // initialize server port and hostname
    private static final int SERVER_PORT = 8100;
    private static final String SERVER_HOSTNAME = "localhost";

    public static void main(String[] args) throws IOException {
        // create a thread pool to handle requests
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        // create a server
        HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), 0);

        ServerConfiguration configuration = new ServerConfiguration();

        IAccountContext accountContext = new AccountContext();

        ITextGenerateService textGenerateService;
        IVoiceToTextService voiceToTextService;

        if (configuration.apiKey() == null || configuration.apiKey().equals("")) {
            System.out.println(
                    "[api:INFO] Coudln't find API KEY in app.properties file. The server will be running with mock data.");
            textGenerateService = new LocalTextGenerateService();
            voiceToTextService = new LocalVoiceToTextService();
        } else {
            System.out.println("[api:INFO] The server is running with actual API KEY. Be careful on spending credit.");
            textGenerateService = new ChatGPTService(configuration);
            voiceToTextService = new WhisperService(configuration);
        }

        IRecipeRepository recipeRepository;
        ISharedRecipeRepository sharedRecipeRepository;
        IAccountService accountService;
        Authenticator authenticator;

        if (configuration.getConnectionString() == null || configuration.getConnectionString().equals("")) {
            System.out.println("[db:INFO] Couldn't find mongodb connection string in app.properties file. The server will use database.json file as storage.");
            JSONRecipeRepository jsonRepository = new JSONRecipeRepository("database.json");
            recipeRepository = jsonRepository;
            sharedRecipeRepository = jsonRepository;

            accountService = new LocalAccountService();
            authenticator = new LocalBasicAuthenticator();
        } else {
            System.out.println("[db:INFO] The server is running with a mongodb instance.");

            MongoDBRecipeRepository mongoRepository = new MongoDBRecipeRepository(configuration, accountContext);
            recipeRepository = mongoRepository;
            sharedRecipeRepository = mongoRepository;

            accountService = new MongoDBAccountService(configuration);
            authenticator = new MongoDBBasicAuthenticator(configuration, accountContext);
        }

        server.createContext("/", new IndexHttpHandler());
        server.createContext("/recipe", new RecipeHttpHandler(recipeRepository))
            .setAuthenticator(authenticator);
        server.createContext("/recipe/generate", new GenerateRecipeHttpHandler(textGenerateService, voiceToTextService))
            .setAuthenticator(authenticator);
        server.createContext("/recipe/share", new RecipeMarkAsShareHttpHandler(sharedRecipeRepository))
            .setAuthenticator(authenticator);
        server.createContext("/account/login-or-create", new AccountHttpHandler(accountService));

        server.createContext("/recipe/shared", new SharedRecipeHttpHandler(sharedRecipeRepository));

        server.setExecutor(threadPoolExecutor);
        server.start();

        System.out.println("Server started on port " + SERVER_PORT);
    }
}
