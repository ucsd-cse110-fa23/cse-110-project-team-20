package server;

import client.Recipe;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import server.recipe.ISharedRecipeRepository;
import server.request.IHttpRequest;

/**
 * handle shared recipe
 *
 * if we have shared recipe under given shared_url, display the recipe. Otherwise, show not found
 * page.
 */
public class SharedRecipeHttpHandler extends HttpHandlerBase {
    private ISharedRecipeRepository sharedRecipeRepository;

    public SharedRecipeHttpHandler(ISharedRecipeRepository sharedRecipeRepository)
    {
        this.sharedRecipeRepository = sharedRecipeRepository;
    }

    protected String
    handleGet(IHttpRequest request) throws UnsupportedMethodException
    {
        String url = request.getQuery("url");

        // show not found page if:
        //  - given url is empty or null
        //  - there is no recipe with given url

        if (url == null || url.isEmpty()) {
            return notFoundPage();
        }

        Recipe recipe = sharedRecipeRepository.getRecipeBySharedUrl(url);

        if (recipe == null) {
            return notFoundPage();
        }

        // display recipe with recipe page
        return recipePage(recipe);
    }

    private String
    notFoundPage()
    {
        return loadTemplate("notFoundPage.html");
    }

    private String
    recipePage(Recipe recipe)
    {
        String template = loadTemplate("recipePage.html");

        return template.replaceAll("%TITLE%", recipe.getTitle())
            .replaceAll("%MEAL_TYPE%", recipe.getMealType())
            .replaceAll("%IMAGE%", recipe.getImageUrl())
            .replaceAll("%DESCRIPTION%", recipe.getDescription().replaceAll("\n", "<br />"));
    }

    /**
     * Read given filename from resources folder and returns it
     *
     * @param filename
     * @return file content
     */
    private String
    loadTemplate(String filename)
    {
        String fileContent;

        try {
            // ref: https://www.baeldung.com/reading-file-in-java
            fileContent = String.join(
                "", Files.readAllLines(Paths.get(getClass().getResource(filename).getPath())));
        } catch (IOException e) {
            e.printStackTrace();
            fileContent = filename + " is missing";
        }

        return fileContent;
    }
}
