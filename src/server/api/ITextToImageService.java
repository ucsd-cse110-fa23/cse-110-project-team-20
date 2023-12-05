package server.api;

public interface ITextToImageService {
    public String createImage(IRecipeQuery query) throws TextToImageServiceException;
}
