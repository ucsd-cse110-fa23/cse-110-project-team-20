package server.mongodb;

/**
 * mongodb configuration interface
 */
public interface IMongoDBConfiguration {
    /**
     * Provides mongodb connection string
     *
     * @return connection string
     */
    public String getConnectionString();
}
