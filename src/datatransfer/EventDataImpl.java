package datatransfer;

/**
 * An implementation of the EventData interface, holds all the data for some Event of some "Area
 * of interest" - stores its ID, associated Area's ID, name, location, description, and data and
 * time.
 */
public class EventDataImpl implements EventData {

  /**
   * The ID of the Area the Event this EventData represent's is associated with.
   */
  private final int areaID;

  /**
   * The ID of the Event this EventData represents.
   */
  private final int id;

  /**
   * The name of the Event this EventData represents.
   */
  private final String name;

  /**
   * The description of the Event this EventData represents.
   */
  private final String desp;

  /**
   * The location of the Event this EventData represents.
   */
  private final String location;

  /**
   * The date and time of the Event this EventData represents.
   */
  private final int[] dateAndTime;

  /**
   * Default constructor of this EventDataImpl, takes in some information about some Event to
   * hold and represent.
   * @param areaID the id of the area that this event is associated with
   * @param id the unique id of this event
   * @param name the name of this event
   * @param desp details about this event
   * @param location where this event is to occur
   * @param dateAndTime the date and time this even is to occur
   * @throws IllegalArgumentException if given areaID is non-positive, if given eventID is negative,
   *                                  if any given parameter is null, if given name is empty, or if
   *                                  given dateAndTime is not of length 6 (year, month, day, hour,
   *                                  minute, and AM / PM), or if given dateAndTime do not have a
   *                                  proper time convention (AM or PM).
   */
  public EventDataImpl(int areaID, int id, String name, String desp, String location,
                       int[] dateAndTime) {
    if (areaID <= 0) {
      throw new IllegalArgumentException("The Area this Event is associated with must actually" +
              "exist, can't have a non-positive (including zero) ID!");
    }
    else if (id < 0) {
      throw new IllegalArgumentException("The ID of this Event must be a natural number!");
    }
    else if (name == null || desp == null || location == null || dateAndTime == null) {
      throw new IllegalArgumentException("Given name, description, location, and / or " +
              "date and time can't be null!");
    }
    else if (name.isEmpty()) {
      throw new IllegalArgumentException("Given Event name can't be empty!");
    }
    else if (dateAndTime.length != 6) {
      throw new IllegalArgumentException("Given date and time array must contain 5 items - " +
              "a year, a month, a day, a hour, a minute, and AM or PM!");
    }
    else if (dateAndTime[5] != 0 && dateAndTime[5] != 1) {
      throw new IllegalArgumentException("Time convention of the given date and time must be" +
              "either 0 for AM or 1 or PM!");
    }

    this.areaID = areaID;
    this.id = id;
    this.name = name;
    this.desp = desp;
    this.location = location;
    this.dateAndTime = dateAndTime;
  }

  @Override
  public int getAssociatedAreaId() {
    return areaID;
  }

  @Override
  public int getEventId() {
    return id;
  }

  @Override
  public String getEventName() {
    return name;
  }

  @Override
  public String getEventDescription() {
    return desp;
  }

  @Override
  public String getEventLocation() {
    return location;
  }

  @Override
  public int[] getEventDateAndTime() {
    return dateAndTime;
  }
}
