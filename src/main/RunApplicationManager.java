package main;

import java.io.File;

import datatransfer.AreaData;
import datatransfer.AreaDataImpl;
import datatransfer.EventData;
import datatransfer.EventDataImpl;
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
    AreaData areaData = new AreaDataImpl(0, "nate", "stuff to do");
    EventData eventData = new EventDataImpl(1, 0, "code", "", "", new int[]{2017, 7, 23, 11, 55, 0});
    EventData eventData1 = new EventDataImpl(1, 0, "write", "sci-fi", "my house", new int[]{2018, 5, 1, 1, 3, 1});
    String filePath = new File(".").getAbsolutePath() + "/src/model/XMLData.xml";
    ModelInterface model = new ModelImpl(filePath);
    model.openModelData();
    model.addArea(areaData);
    model.addEvent(eventData);
    model.addEvent(eventData1);
    model.closeModelData();
  }
}
