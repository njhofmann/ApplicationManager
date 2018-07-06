package main;

import datatransfer.AreaData;
import datatransfer.AreaDataImpl;
import model.ModelImpl;
import model.ModelInterface;

/**
 * Starts up this ApplicationManager.
 */
public class RunApplicationManager {

  /**
   * Starts up this ApplicationManager with the proper configurations (if any).
   * @param args configurations to start this program up with
   */
  public static void main(String[] args) {

    AreaData areaData = new AreaDataImpl(-1, "nate", "stuff to do");
    String filePath = "/home/nhofmann/Personal Java Projects/ApplicationManager/src/model/XMLData.xml";
    ModelInterface model = new ModelImpl(filePath);
    model.addArea(areaData);
  }
}
