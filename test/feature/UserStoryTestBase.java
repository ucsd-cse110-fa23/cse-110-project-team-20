package feature;

import static org.junit.jupiter.api.Assertions.fail;

import client.Controller;
import client.PantryPalsApp;
import javafx.application.Platform;
import javafx.stage.Stage;

abstract public class UserStoryTestBase {
  // setTimeout helper
  // ref:
  // https://stackoverflow.com/questions/26311470/what-is-the-equivalent-of-javascript-settimeout-in-java
  public static void setTimeout(Runnable runnable, int delay) {
    new Thread(() -> {
      try {
        Thread.sleep(delay);
        Platform.runLater(runnable);
      } catch (Exception e) {
      }
    }).start();
  }

  public static void closeApp() {
    Platform.setImplicitExit(false);
    Platform.exit();
  }

  protected PantryPalsApp app;
  protected Stage primaryStage;
  protected Controller controller;

  public void condition(Runnable runnable) {
    // minimum 1s to avoid switching delay
    condition(runnable, 2000);
  }

  public void condition(Runnable runnable, long utillMillis) {
    Thread inspectionThread = PantryPalsApp.inspect((PantryPalsApp app, Stage primaryStage, Controller controller) -> {
      this.app = app;
      this.primaryStage = primaryStage;
      this.controller = controller;

      runnable.run();
    });
    inspectionThread.start();

    try {
      Thread.sleep(utillMillis);
    } catch (InterruptedException e) {
      fail(e.getMessage());
    }
  }
}
