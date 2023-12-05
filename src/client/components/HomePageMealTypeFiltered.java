package client.components;

/**
 * Apply given mealType filter on homepage
 */
public class HomePageMealTypeFiltered {
    public HomePageMealTypeFiltered(HomePage homepage, String mealType)
    {
        homepage.applyMealTypeFilter(mealType);
    }
}
