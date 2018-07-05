package model;

import datatransfer.AreaData;
import datatransfer.EventData;

/**
 * Represents the data of this ApplicationManager, manages any changes that wish to be made to the
 * data as well as outputting the data into a "universal" data class so it can be utilized by other
 * objects.
 */
public interface ModelInterface {

  /**
   * Adds a new "area of interest" to this ApplicationManager's data model. Data has a unique
   * integer id, a name, and a optional description.
   * @param data area of interest to add
   * @throws IllegalArgumentException if given area's id number shares an id number with an already
   *         existing area, if given data is null, or if given name is empty
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
   * description, and a date & time. Unique integer id is -1 to indicate it is a new event.
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
}
