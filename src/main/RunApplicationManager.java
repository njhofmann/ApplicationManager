package main;

import java.io.File;

import model.ModelImpl;
import model.ModelInterface;
import view.ViewImpl;
import view.ViewInterface;

/**
 * Starts up this ApplicationManager.
 */
public class RunApplicationManager {

  /**
   * Starts up this ApplicationManager with the proper configurations (if any).
   * @param args configurations to start this program up with
   */
  public static void main(String[] args) {
    String filePath = new File(".").getAbsolutePath() + "/src/model/XMLData.xml";
    ModelInterface model = new ModelImpl(filePath);

    // Open associated model for editing
    model.openModelData();

    ViewInterface view = new ViewImpl();
    view.setAssociatedModel(model);
    view.start();

    // After use has closed the View window, save changes made in the model
    model.closeModelData();
  }
}
