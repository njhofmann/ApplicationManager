package main;

import controller.Controller;
import controller.ControllerInterface;
import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.ModelImpl;
import model.ModelInterface;
import view.ViewImpl;
import view.ViewInterface;

/**
 * Starts up this ApplicationManager.
 */
public class RunApplicationManager extends Application {

  ModelInterface model;

  ViewInterface view;

  /**
   * Starts up this ApplicationManager with the proper configurations (if any).
   * @param args configurations to start this program up with
   */
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    String filePath = new File(".").getAbsolutePath() + "/src/model/XMLData.xml";
    model = new ModelImpl(filePath);

    // Open associated model for editing
    model.openModelData();

    view = new ViewImpl();

    ControllerInterface controller = new Controller(view, model);

    Scene scene = new Scene(view.asParent(), view.getWidth(), view.getHeight());
    primaryStage.setTitle("Application Manager");
    primaryStage.setResizable(false);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * After use has closed the View window, save changes made in the model, so long as the model is
   * assigned.
   * @throws IllegalArgumentException if the model has not yet been properly assigned
   */
  @Override
  public void stop() {
    if (model == null) {
      throw new IllegalArgumentException("Model must be non null to save info!");
    }
    model.closeModelData();
  }
}
