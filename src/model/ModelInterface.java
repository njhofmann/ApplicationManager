package model;

import java.util.List;

import datatransfer.AreaData;
import datatransfer.EventData;

/**
 * Represents the data of this ApplicationManager, manages any changes that wish to be made to the
 * data as well as outputting the data into a "universal" data class so it can be utilized by other
 * objects.
 */
public interface ModelInterface {

  /**
   * Opens this ApplicationManager's data modek, should only be called before all other relevant
   * methods need to be called (other than closeModelData()).
   */
  void openModelData();

  /**
   * Closes this ApplicationManager's data model, should only be called after all other relevant
   * methods have been called (other than openModelData()).
   */
  void closeModelData();

  /**
   * Adds a new "area of interest" to this ApplicationManager's data model. Data has a unique
   * integer id, a name, and a optional description. ID of AreaData should be 0 to signal it is a
   * new AreaData.
   * @param data area of interest to add
   * @throws IllegalArgumentException if given area's ID isn't zero, if given data is null, or if
   * given name is empty
   */
  void addArea(AreaData data) throws IllegalArgumentException;

  /**
   * Edits an existing "area of interest" in this ApplicationManager's data model by finding an
   * existing area with given area's id, then updates all fields that were changed.
   * @param data data of existing area to update
   * @throws IllegalArgumentException if given data has associated id of a area not added to this
   *         AM's data model, if given data is null, if given name is empty
   */
  void editArea(AreaData data) throws IllegalArgumentException;

  /**
   * Adds a new event to an existing "area of interest" in this ApplicationManger's data model.
   * Has id of its associated area, unique integer id, a name, an optional location, an optional
   * description, and a date & time. Unique integer id is negative to indicate it is a new event.
   * @param data data of event to add to some existing area
   * @throws IllegalArgumentException if given event is associated with a nonexistent area, it
   *         has a nonnegative integer id, has event id of an already existing event in its
   *         associated area, if given data is null, or if given name or date / time is empty
   */
  void addEvent(EventData data) throws IllegalArgumentException;

  /**
   * Edits an existing event in an existing "area of interest" in this AM's data model by finding
   * area with event's associated area's id, then finding event with matching id as the given data,
   * then updating any necessary data.
   * @param data data of an existing event to update with
   * @throws IllegalArgumentException if given data is associated with a area not added to this
   * AM's data model, or if data is associated with a event not added to its associated area, if
   * given data is null, or if given name or date / time is empty
   */
  void editEvent(EventData data) throws IllegalArgumentException;

  /**
   * Removes the Area with the associated ID from the given AreaData from this model. Ignores the
   * name and description parts of the AreaData.
   * @param areaID
   * @throws IllegalArgumentException if given AreaData is null, or if no Area with the given ID
   *         has been added to the model
   */
  void deleteArea(int areaID);

  /**
   * Removes the Event with the associated Event ID from the Area with the matching Area ID, both
   * IDs are given by the input EventData. Ignores the name, description, location, and date & time
   * parts of the EventData.
   * @param areaID
   * @param eventID
   * @throws IllegalArgumentException if given EventData is null, if no Area with the given Area ID
   *         has been added to the model, or if no Event with the given Event ID has been added to
   *         the Area with the given Area ID
   */
  void deleteEvent(int areaID, int eventID);

  /**
   * Outputs a list of AreaDatas representing all Areas stored in this Model.
   * @return list of AreaDatas representing all Areas stored in this Model
   */
  List<AreaData> outputAreas();

  /**
   * Outputs a list of EventDatas representing all the Events of a specific added Area, specified
   * Area is given by the ID of the given AreaData. The name and description of the given AreaData
   * are ignored.
   * @param areaID
   * @return list of EventDatas representing all the events of a specific Area, as given by the ID
   *         of the given AreaData
   * @throws IllegalArgumentException if given AreaData is null
   */
  List<EventData> outputEvents(int areaID);

  /**
   * Outputs the data of this ApplicationManager's data model as a String for other storage,
   * testing, etc.
   * @return data of AM's data model as a String.
   */
  String outputModelDataAsString();
}
