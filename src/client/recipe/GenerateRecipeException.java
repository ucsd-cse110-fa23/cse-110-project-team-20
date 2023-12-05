package client.recipe;

// Separate class for when the API fails to generate a recipe
public class GenerateRecipeException extends RuntimeException {
    public GenerateRecipeException(String message)
    {
        super(message);
    }
}
