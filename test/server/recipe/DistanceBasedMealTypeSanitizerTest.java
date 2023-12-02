package server.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class DistanceBasedMealTypeSanitizerTest {
    @Test
    public void checkDistance() {
        DistanceBasedMealTypeSanitizer sanitizer = new DistanceBasedMealTypeSanitizer(Arrays.asList("breakfast", "lunch", "dinner"));
        String actual1 = sanitizer.apply("dinner");
        String actual2 = sanitizer.apply("BreakFast");
        String actual3 = sanitizer.apply("Lunch");
        String actual4 = sanitizer.apply("Luanch.");
        String actual5 = sanitizer.apply("LUNCHES");
        String actual6 = sanitizer.apply("I want to have a lunch");
        String actual7 = sanitizer.apply("I want to make a meal for breakfast");
        String actual8 = sanitizer.apply("DINNER!");

        assertEquals("dinner", actual1);
        assertEquals("breakfast", actual2);
        assertEquals("lunch", actual3);
        assertEquals("lunch", actual4);
        assertEquals("lunch", actual5);
        assertEquals("lunch", actual6);
        assertEquals("breakfast", actual7);
        assertEquals("dinner", actual8);
    }
}
