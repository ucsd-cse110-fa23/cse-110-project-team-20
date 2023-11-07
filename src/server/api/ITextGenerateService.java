package server.api;

public interface ITextGenerateService {
    public String request(IRecipeQuery query) throws TextGenerateServiceException;
}
