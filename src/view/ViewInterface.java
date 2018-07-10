package view;

import java.awt.event.ActionListener;
import java.util.List;

import datatransfer.AreaData;
import datatransfer.EventData;

/**
 * Displays the data of this ApplicationManager so that a user may visually see it, as well as
 * outputting whatever changes the user may want to make to the data itself (doesn't actually
 * change this AM's data, only what changes the user wishes to make.
 */
public interface ViewInterface {

  /**
   * Starts displaying this View.
   */
  void start();

  /**
   * Ends this View.
   */
  void end();

  /**
   * After new data has been received by this View, updates what the View is displaying to 
   * correspond more closely with the data that has been received.
   */
  void update();

  /**
   * Adds an ActionListener to this View for the purposes of supporting user interaction and
   * properly interfacing the dynamic changes a user may make to corresponding with the displayed
   * data in the View to the actual data model within the corresponding Model.
   * @param actionListener actionListener to add to this View
   */
  void addActionListener(ActionListener actionListener);

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
