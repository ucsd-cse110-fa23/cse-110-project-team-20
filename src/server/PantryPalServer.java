package server;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.*;
import server.account.AccountContext;
import server.account.IAccountContext;
import server.account.IAccountService;
import server.account.LocalAccountService;
import server.account.LocalBasicAuthenticator;
import server.api.ChatGPTService;
import server.api.DallEService;
import server.api.ITextGenerateService;
import server.api.ITextToImageService;
import server.api.IVoiceToTextService;
import server.api.LocalTextGenerateService;
import server.api.LocalTextToImageService;
import server.api.LocalVoiceToTextService;
import server.api.WhisperService;
import server.mongodb.MongoDBAccountService;
import server.mongodb.MongoDBBasicAuthenticator;
import server.mongodb.MongoDBRecipeRepository;
import server.recipe.DistanceBasedMealTypeSanitizer;
import server.recipe.IMealTypeSanitizer;
import server.recipe.IRecipeRepository;
import server.recipe.ISharedRecipeConfiguration;
import server.recipe.ISharedRecipeRepository;
import server.recipe.JSONRecipeRepository;

public class PantryPalServer {
    // initialize server port and hostname
    private static int SERVER_PORT = 8100;
    private static String SERVER_HOSTNAME = "0";

    public static void
    main(String[] args) throws IOException
    {
        // helper for identify the current server
        if (args.length < 2) {
            printArgsRequired();
            return;
        }

        try {
            SERVER_HOSTNAME = args[0]; // expected to be server IP
            SERVER_PORT = Integer.valueOf(args[1]); // expected to be server port
        } catch (Exception e) {
            printArgsParseError();
            return;
        }

        // create a thread pool to handle requests
        ThreadPoolExecutor threadPoolExecutor =
            (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        // create a server
        HttpServer server =
            HttpServer.create(new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), 0);

        ServerConfiguration configuration = new ServerConfiguration();

        IAccountContext accountContext = new AccountContext();

        ITextGenerateService textGenerateService;
        IVoiceToTextService voiceToTextService;
        ITextToImageService textToImageService;
        IMealTypeSanitizer mealTypeSanitizer =
            new DistanceBasedMealTypeSanitizer(Arrays.asList("breakfast", "lunch", "dinner"));

        if (configuration.apiKey() == null || configuration.apiKey().equals("")) {
            System.out.println(
                "[api:INFO] Coudln't find API KEY in server.properties file. The server will be running with mock data.");
            textGenerateService = new LocalTextGenerateService();
            voiceToTextService = new LocalVoiceToTextService();
            textToImageService = new LocalTextToImageService();
        } else {
            System.out.println(
                "[api:INFO] The server is running with actual API KEY. Be careful on spending credit.");
            textGenerateService = new ChatGPTService(configuration);
            voiceToTextService = new WhisperService(configuration);
            textToImageService = new DallEService(configuration);
        }

        IRecipeRepository recipeRepository;
        ISharedRecipeRepository sharedRecipeRepository;
        IAccountService accountService;
        Authenticator authenticator;

        if (configuration.getConnectionString() == null
            || configuration.getConnectionString().equals("")) {
            System.out.println(
                "[db:INFO] Couldn't find mongodb connection string in server.properties file. The server will use database.json file as storage.");
            JSONRecipeRepository jsonRepository = new JSONRecipeRepository("database.json");
            recipeRepository = jsonRepository;
            sharedRecipeRepository = jsonRepository;

            accountService = new LocalAccountService();
            authenticator = new LocalBasicAuthenticator();
        } else {
            System.out.println("[db:INFO] The server is running with a mongodb instance.");

            ISharedRecipeConfiguration shareConfig = ()
                -> String.format("http://%s:%s/recipe/shared/?url=", SERVER_HOSTNAME, SERVER_PORT);
            MongoDBRecipeRepository mongoRepository =
                new MongoDBRecipeRepository(configuration, shareConfig, accountContext);
            recipeRepository = mongoRepository;
            sharedRecipeRepository = mongoRepository;

            accountService = new MongoDBAccountService(configuration);
            authenticator = new MongoDBBasicAuthenticator(configuration, accountContext);
        }

        server.createContext("/", new IndexHttpHandler());
        server.createContext("/recipe", new RecipeHttpHandler(recipeRepository))
            .setAuthenticator(authenticator);
        server
            .createContext("/recipe/generate",
                new GenerateRecipeHttpHandler(
                    textGenerateService, voiceToTextService, textToImageService, mealTypeSanitizer))
            .setAuthenticator(authenticator);
        server
            .createContext(
                "/recipe/share", new RecipeMarkAsShareHttpHandler(sharedRecipeRepository))
            .setAuthenticator(authenticator);
        server.createContext("/account/login-or-create", new AccountHttpHandler(accountService));

        server.createContext("/recipe/shared", new SharedRecipeHttpHandler(sharedRecipeRepository));

        server.setExecutor(threadPoolExecutor);
        server.start();

        System.out.println("[INFO] Server started on " + SERVER_HOSTNAME + " under port " + SERVER_PORT);
    }

    private static void
    printArgsRequired()
    {
        System.out.println("[INFO] The server is not running because of missing IP address.");
        System.out.println("[INFO] Please choose one of IP address from above and pass it as an argument.");
        System.out.println("");

        printCurrentServerAddress();

        System.out.println("");
        System.out.println("[INFO] Then, start the server with argument. e.g. ./gradlew runServer --args=\"<ip address> <port>\"");
        System.out.println("");
    }

    private static void
    printArgsParseError()
    {
        System.err.println("[ERROR] Failed to parse arguments.");
        System.err.println("[INFO] Please use this format: ./gradlew runServer --args=\"<ip address> <port>\"");
    }

    private static void
    printCurrentServerAddress()
    {
        // collect all host addresses
        // ref: https://stackoverflow.com/questions/20353450
        try {
            for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress address : Collections.list(ni.getInetAddresses())) {
                    if (address instanceof Inet4Address) {
                        System.out.println("\t" + address.getHostAddress());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
