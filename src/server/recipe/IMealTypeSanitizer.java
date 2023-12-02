package server.recipe;

/**
 * Sanitizing given value to appropriate meal type value
 */
public interface IMealTypeSanitizer {
    public String apply(String value);
}
