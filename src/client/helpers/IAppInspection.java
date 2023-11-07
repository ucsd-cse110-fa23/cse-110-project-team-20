package client.helpers;

import client.Controller;
import client.PantryPalApp;
import javafx.stage.Stage;

public interface IAppInspection {
    public void inspect(PantryPalApp app, Stage stage, Controller controller) throws Exception;
}
