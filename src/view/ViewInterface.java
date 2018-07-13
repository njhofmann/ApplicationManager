package view;

import java.util.List;

import datatransfer.AreaData;
import datatransfer.EventData;
import model.ModelInterface;

/**
 * Displays the data of this ApplicationManager so that a user may visually see it, as well as
 * outputting whatever changes the user may want to make to the data itself (doesn't actually
 * change this AM's data, only what changes the user wishes to make.
 */
public interface ViewInterface {

  /**
   * Sets the Model this View is associated with, Model gives the data for the View to display.
   * @param model
   */
  void setAssociatedModel(ModelInterface model);

  /**
   * Starts displaying this View's contents.
   */
  void start();

  /**
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
   * Outputs an AreaData to send to a corresponding model for the purposes of creating a new Area,
   * editing a preexisting Area, or deleting a preexisting Area inside this ApplicationManager's
   * data model.
   * @return AreaData containing information about making some change to this AM's data model
   */
  AreaData outputAreaData();

  /**
   * Outputs an EventData to send to a corresponding model for the purposes of creating a new Event,
   * editing a preexisting Event, or deleting a preexisting Event inside this ApplicationManager's
   * data model.
   * @return EventData containing information about making some change to this AM's data model
   */
  EventData outputEventData();
}
