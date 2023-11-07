package server;

import com.sun.net.httpserver.*;

import server.api.ChatGPTService;
import server.api.IOpenAIConfiguration;
import server.api.ITextGenerateService;
import server.api.IVoiceToTextService;
import server.api.LocalTextGenerateService;
import server.api.LocalVoiceToTextService;
import server.api.OpenAIConfiguration;
import server.api.WhisperService;
import server.recipe.IRecipeRepository;
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

        IOpenAIConfiguration configuration = new OpenAIConfiguration();

        ITextGenerateService textGenerateService;
        IVoiceToTextService voiceToTextService;

        if (configuration.apiKey() == null || configuration.apiKey().equals("")) {
            System.out.println(
                    "Coudln't find API KEY in app.properties file. The server will be running with mock data.");
            textGenerateService = new LocalTextGenerateService();
            voiceToTextService = new LocalVoiceToTextService();
        } else {
            System.out.println("Warnning: Server is running with actual API KEY. Be careful on spending credit.");
            textGenerateService = new ChatGPTService(configuration);
            voiceToTextService = new WhisperService(configuration);
        }

        IRecipeRepository recipeRepository = new JSONRecipeRepository("database.json");

        server.createContext("/", new IndexHttpHandler());
        server.createContext("/recipe", new RecipeHttpHandler(recipeRepository));
        server.createContext("/recipe/generate", new GenerateRecipeHttpHandler(textGenerateService, voiceToTextService));

        server.setExecutor(threadPoolExecutor);
        server.start();

        System.out.println("Server started on port " + SERVER_PORT);
    }
}
