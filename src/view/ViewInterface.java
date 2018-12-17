package view;

import java.util.List;

import datatransfer.AreaData;
import datatransfer.EventData;
import javafx.scene.Parent;
import model.ModelInterface;

/**
 * Displays the data of this ApplicationManager so that a user may visually see it, as well as
 * outputting whatever changes the user may want to make to the data itself (doesn't actually
 * change this AM's data, only what changes the user wishes to make.
 */
public interface ViewInterface {
  /*
   * Receives a list of AreaDatas representing all the Areas stored in the corresponding model to
   * display in this View.
   * @param areas list representing all Areas stored
   * @throws IllegalArgumentException if given list is null
   */
  void receiveAreas(List<AreaData> areas);

  /**
   * Receives a list of EventDatas representing all Events <b>of a single Area</b> stored in the
   * corresponding model to display in this View.
   * @param events list of EventDatas representing all Events of a single Area
   * @throws IllegalArgumentException if given list is null or if all the EventDatas in the list
   *         aren't from the same Area (i.e. all don't have the same AreaID)
   */
  void receiveEvents(List<EventData> events);

  /**
   * Returns this View as a JavaFX parent object if being used as the header of a JavaFX
   * environment.
   * @return this View as a JavaFX parent object
   */
  Parent asParent();

  /**
   * Retrieves the current width of this View, given that it is displayed as a window.
   * @return current width of view
   */
  int getWidth();

  /**
   * Retrieves the current height of this View, given that it is displayed as a window.
   * @return current height of view
   */
  int getHeight();
}
