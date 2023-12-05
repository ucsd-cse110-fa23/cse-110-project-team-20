package server.api;

public interface IRecipeQuery {
    /**
     * Convert IRecipeQuery as a string that queryable in some other system
     *
     * @return queryable string
     */
    public String toQueryableString();
}
