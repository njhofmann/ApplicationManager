package datatransfer;

/**
 * Represents the data of some event of some "area of interest" being sent from a view to a model
 * to process into this ApplicationManager's data model.
 */
public interface EventData {
  /**
   * Returns the unique id of the area that the event this EventData represents is associated with.
   * @return associated event's associated area's unique id
   */
  int getAssociatedAreaId();

  /**
   * Returns the unique id of the event that this EventData represents.
   * @return associated event's unique id
   */
  int getEventId();

  /**
   * Returns the name of this EventData's associated event.
   * @return name of this associated event
   */
  String getEventName();

  /**
   * Returns the description of this EventData's associated event. No associated description is
   * represented by an empty String.
   * @return description of this associated event
   */
  String getEventDescription();

  /**
   * Returns the location of this EventData's associated event. No associated location is
   * represented by an empty String.
   * @return location of this associated event
   */
  String getEventLocation();

  /**
   * Returns the date and time of this EventData's associated event as a String array in form of
   * year, month, day, hour, and minute.
   * @return date and time of this associated event
   */
  String[] getEventDateAndTime();
}
