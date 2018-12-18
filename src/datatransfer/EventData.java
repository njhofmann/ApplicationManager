package datatransfer;

import java.time.LocalDateTime;

/**
 * Represents the data of some event of a "Area of interest" being sent from a view to a model
 * to process into this ApplicationManager's data model, or from a model to a view to be displayed
 * to a user.
 */
public interface EventData {
  /**
   * Returns the unique id of the area that the event this EventData represents is associated with.
   * @return associated event's associated area's unique id
   */
  int getAssociatedAreaId();

  /**
   * Returns the unique id of the event that this EventData represents. A negative id represents
   * that this event is brand new and is being added to its assocaited area for the first time,
   * otherwise it represents data
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
   * Returns the date and time of this EventData's associated event as a Java LocalDateTime object.
   * @return date and time of this associated event
   */
  LocalDateTime getEventDateAndTime();

  /**
   * Returns the "final hour" of this EventData's current hour, 0 to 11 for AM, 12 to 11 for PM
   * @return
   */
  int getFinalHour();

  /**
   * Returns the time convention of this EventData, either AM or PM as represented by a String.
   * @return AM or PM
   */
  String getTimeConvention();
}
