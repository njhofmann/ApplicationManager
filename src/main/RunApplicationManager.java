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
    ViewInterface view = new ViewImpl();
    view.setAssociatedModel(model);
    view.start();
  }
}
