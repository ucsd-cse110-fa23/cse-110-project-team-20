package client.helpers;

import client.Controller;
import client.PantryPalsApp;
import javafx.stage.Stage;

public interface AppInspection {
    public void inspect(PantryPalsApp app, Stage stage, Controller controller) throws Exception;
}
