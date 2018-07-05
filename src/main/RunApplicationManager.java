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
    ModelInterface model = new ModelImpl();
    AreaData areaData = new AreaDataImpl("nate", "stuff to do");
    model.addArea(areaData);
  }
}
