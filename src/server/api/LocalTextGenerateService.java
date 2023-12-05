package server.api;

public class LocalTextGenerateService implements ITextGenerateService {
    private static final String RECIPE_FORMAT = "Tomato, Cucumber, and Egg Salad\n\n"
        + "1. Boil the eggs until they are hard-boiled. Let them cool and then peel and chop them.\n"
        + "2. Wash and dice the tomato and cucumber.\n"
        + "3. In a large bowl, combine the chopped eggs, diced tomato, and cucumber.\n"
        + "4. Toss the ingredients together.\n"
        + "5. Season with salt and pepper to taste.\n"
        + "6. Serve as a refreshing and healthy dinner salad.";

    @Override

    public String
    request(IRecipeQuery query) throws TextGenerateServiceException
    {
        return RECIPE_FORMAT;
    }
}
