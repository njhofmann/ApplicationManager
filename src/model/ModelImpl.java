package model;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import datatransfer.AreaData;
import datatransfer.AreaDataImpl;
import datatransfer.EventData;
import datatransfer.EventDataImpl;

/**
 * An implementation of the ModelInterface for a ApplicationManager. This implementation is
 * associated with a specific XML file - XMLData.xml - to hold all the model data for this AM. Adds
 * new data, edits old data, deletes old data, etc. by directly interacting with that XML file.
 */
public class ModelImpl implements ModelInterface {

  /**
   * The file path to this ModelImpl's associated XML model data file.
   */
  private final String modelDataFilePath;

  /**
   * Latest version of this ModelImpl's model data as a readable document.
   */
  private Document modelData = new Document();

  /**
   * Default constructor for this ModelImpl, takes in a file path to read data from and make changes
   * to as  the results of any method calls on this MI / changes to this MI's model data.
   * @param modelDataFilePath file path of the model data this ModelImpl is suppose to read from and
   *                          change as needed
   * @throws IllegalArgumentException if given input file path is null, empty, or doesn't exist
   */
  public ModelImpl(String modelDataFilePath) {
    if (modelDataFilePath == null) {
      throw new IllegalArgumentException("Given input file path can't be null!");
    }
    else if (modelDataFilePath.isEmpty()) {
      throw new IllegalArgumentException("Given file path can't be empty!");
    }
    else if (Files.notExists(Paths.get(modelDataFilePath))) {
      throw new IllegalArgumentException("Given file path doesn't exist!");
    }

    this.modelDataFilePath = modelDataFilePath;
  }

  /**
   * Retursn true if this ModelImpl's data model contains an Area Element that has the input id
   * as its own id.
   * @param id unique id to Area Elements for
   * @return if this data model contains an Area Element whose own id is the input id
   */
  private boolean containsAreaElement(int id) {
    // Get the root child of the XML file and all its inner children.
    Element root = modelData.getRootElement();
    List<Element> areas = root.getChildren();

    for (Element area : areas) {
      if (area.getAttributeValue("id").equals(Integer.toString(id))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the Area Element with the associated input id.
   * @param id id of the Area Element to return
   * @return Area element that has input id as its own id
   * @throws IllegalArgumentException if there is no Area Element in this model data that has the
   *                                  associated id
   */
  private Element getAreaElement(int id) {
    if (id == 0) {
      throw new IllegalArgumentException("Can't retrieve an AreaData with an ID of 0!");
    }
    // Get the root child of the XML file and all its inner children.
    Element root = modelData.getRootElement();
    List<Element> areas = root.getChildren();

    for (Element area : areas) {
      if (area.getAttributeValue("id").equals(Integer.toString(id))) {
        return area;
      }
    }
    throw new IllegalArgumentException("No area with the given id has been added to this model data!");
  }

  /**
   * Returns the Event whose ID is the given event ID, whose associated Area has an ID matching
   * given Area ID.
   * @param areaID id of the desired event's associated area
   * @param eventID id the desired event
   * @return Event whose id matches given Event ID, and whose associated Area has an ID matching
   *         given Area ID
   * @throws IllegalArgumentException if there is no Area in this model data with an id matching
   *         given AreaID, or if the matching Area has no Event with an ID matching the given
   *         Event ID
   */
  private Element getEventElement(int areaID, int eventID) {
    Element associatedArea = getAreaElement(areaID);
    List<Element> events = associatedArea.getChildren("event");

    for (Element event : events) {
      if (event.getAttributeValue("id").equals(Integer.toString(eventID))) {
        return event;
      }
    }
    throw new IllegalArgumentException("Area associated with given Area ID doesn't have an " +
            "Event with the given Event ID!");
  }

  /**
   * Checks if a given AreaData is valid, i.e. it is non-null, its name is not empty, and its id
   * is a natural number.
   * @param data AreaData to check
   * @throws IllegalArgumentException if the given Area data is null or has an empty name
   */
  private void validAreaData(AreaData data) {
    if (data == null) {
      throw new IllegalArgumentException("Given Area data can't be null");
    }
    else if (data.getAreaId() < 0) {
      throw new IllegalArgumentException("Given Area data must have a natural number as an ID!");
    }
  }

  /**
   * Checks if a given EventData is valid - i.e. it is non-null, its name is not empty, its id is a
   * natural number, and its associated Area has been added to this model data - else throws an
   * IllegalArgumentException.
   * @param data EventData to check is valid
   * @throws IllegalArgumentException if given EventData is non-null, its name is not empty, and
   * its associated Area has been added to this model data
   */
  private void validEventData(EventData data) {
    if (data == null) {
      throw new IllegalArgumentException("Given data can't be null");
    }
    else if (data.getEventId() < 0) {
      throw new IllegalArgumentException("Given Event data must have a natural number as an ID!");
    }
    else if (!containsAreaElement(data.getAssociatedAreaId())) {
      throw new IllegalArgumentException("The area this event is associated with has not been" +
              "added to this model data!");
    }
  }

  /**
   * Checks if a given integer represents the ID of a possible valid existing Area. Only checks if
   * given ID is >= 0, else throws a specified error.
   * @param areaID possible ID to check
   * @throws IllegalArgumentException if given ID is non-positive
   */
  private void validExistingAreaID(int areaID) {
    if (areaID == 0) {
      throw new IllegalArgumentException("The ID of any Area that is apart of this model can't" +
              " have the ID dedicated to creating new Areas - i.e. no Area can have ID of zero!");
    }
    else if (areaID < 0) {
      throw new IllegalArgumentException("An existing Area's ID must be greater than 0!");
    }
  }

  @Override
  public void openModelData() {
    try{
      File xmlFile = new File(modelDataFilePath);
      SAXBuilder saxBuilder = new SAXBuilder();
      modelData = saxBuilder.build(xmlFile);
    }
    catch (JDOMException e) {
      throw new IllegalStateException("Failed to read associated XML file!");
    }
    catch (IOException e) {
      throw new IllegalStateException("Failed to read associated XML file!");
    }
  }

  @Override
  public void closeModelData() {
    try {
      XMLOutputter xmlOutputter = new XMLOutputter();
      xmlOutputter.setFormat(Format.getPrettyFormat());
      xmlOutputter.output(modelData, new FileWriter(modelDataFilePath));
    }
    catch (IOException e) {
      // pass
    }
  }

  @Override
  public void addArea(AreaData data) throws IllegalArgumentException {
    validAreaData(data);

    if (data.getAreaId() != 0) {
      throw new IllegalArgumentException("Given AreaData's ID must be zero to signal it " +
              "represents an Area not yet added to this model!");
    }

    // Get the root child of the XML file and all its inner children.
    Element root = modelData.getRootElement();
    List<Element> areas = root.getChildren();

    // Get data from input data class.
    int newAreaID = data.getAreaId();
    String newAreaName = data.getAreaName();
    String newAreaDesp = data.getAreaDescription();

    // Create a new area element to add to the model from input data.
    Element newArea = new Element("area");
    newArea.setAttribute("id", Integer.toString(areas.size() + 1));

    Element areaElementName = new Element("name");
    areaElementName.setText(newAreaName);
    newArea.addContent(areaElementName);

    Element areaElementDesp = new Element("description");
    areaElementDesp.setText(newAreaDesp);
    newArea.addContent(areaElementDesp);

    // Add new area element to model data
    root.addContent(newArea);
  }

  @Override
  public void editArea(AreaData data) throws IllegalArgumentException {
    validAreaData(data);

    if (data.getAreaId() == 0) {
      throw new IllegalArgumentException("Can't edit a new area!");
    }

    // Get data from input data class.
    int editAreaId = data.getAreaId();
    String editAreaName = data.getAreaName();
    String editAreaDesp = data.getAreaDescription();

    // Grab area element to edit
    Element areaToEdit = getAreaElement(editAreaId);

    // Updates the found area with a its new name and description
    Element areaToEditName = areaToEdit.getChild("name");
    areaToEditName.setText(editAreaName);

    Element areaToEditDesp = areaToEdit.getChild("description");
    areaToEditDesp.setText(editAreaDesp);
  }

  /**
   * Helper method for converting a date and time array from an EventData into a properly
   * formatted String to be stored in the XML file.
   * @param dateTime integer array containing info for some date and time of an event
   * @return String representation of the given date and time info
   * @throws IllegalArgumentException if date and time array is null or not of length 6, or if
   *         any of the info stored inside the array doesn't fall within the appropriate range:
   *         -year - natural number
   *         -month - [1, 12]
   *         -day - [0, 31]
   *         -hour - [0, 12]
   *         -minute - [0, 59]
   *         -time convention - 0 or 1
   */
  private String dateAndTimeArrayToString(LocalDateTime dateTime) {
    if (dateTime == null) {
      throw new IllegalArgumentException("Given date and time array can't be null!");
    }

    int year = dateTime.getYear();
    int month = dateTime.getMonthValue();
    int day = dateTime.getDayOfMonth();
    int hour = dateTime.getHour();
    int minute = dateTime.getMinute();

    if (year < 0) {
      throw new IllegalArgumentException("Can't have a negative year!");
    }
    else if (!(1 <= month && month <= 12)) {
      throw new IllegalArgumentException("A month must be in range [1, 12]!");
    }
    else if (!(1 <= day && day <= 31)) {
      throw new IllegalArgumentException("A day must be in the range [1, 31]!");
    }
    else if (!(0 <= hour && hour <= 23)) {
      throw new IllegalArgumentException("A hour must be in the range [0, 23]");
    }
    else if (!(0 <= minute && minute <= 59)) {
      throw new IllegalArgumentException("A minute must be in the range [0, 59]");
    }

    return String.format("%02d-%02d-%02dT%02d:%02d:00", year, month, day, hour, minute);
  }

  @Override
  public void addEvent(EventData data) throws IllegalArgumentException {
    validEventData(data);

    // Pull out event data
    int areaID = data.getAssociatedAreaId();
    int eventID = data.getEventId();
    String eventName = data.getEventName();
    String eventDesp = data.getEventDescription();
    String eventLocation = data.getEventLocation();
    LocalDateTime eventDateAndTime = data.getEventDateAndTime();

    if (eventID > 0) {
      throw new IllegalArgumentException("Given event data doesn't represent a new event!");
    }

    Element associatedArea = getAreaElement(areaID);

    Element newEvent = new Element("event");
    newEvent.setAttribute("id", Integer.toString(associatedArea.getChildren("event").size() + 1));

    Element eventNameElement = new Element("name").setText(eventName);
    newEvent.addContent(eventNameElement);

    Element eventDespElement = new Element("description").setText(eventDesp);
    newEvent.addContent(eventDespElement);

    String result = dateAndTimeArrayToString(eventDateAndTime);
    Element eventDateAndTimeElement = new Element("date-time").addContent(result);
    newEvent.addContent(eventDateAndTimeElement);

    Element eventLocationElement = new Element("location").setText(eventLocation);
    newEvent.addContent(eventLocationElement);

    associatedArea.addContent(newEvent);
  }

  @Override
  public void editEvent(EventData data) throws IllegalArgumentException {
    validEventData(data);

    int areaID = data.getAssociatedAreaId();
    int eventID = data.getEventId();
    String editEventName = data.getEventName();
    String editEventDesp = data.getEventDescription();
    String editEventLocation = data.getEventLocation();
    LocalDateTime editEventDateAndTime = data.getEventDateAndTime();

    if (eventID == 0) {
      throw new IllegalArgumentException("Given event data represents a new event that isn't" +
              "already apart of this data model!");
    }

    Element eventToEdit = getEventElement(areaID, eventID);

    eventToEdit.getChild("name").setText(editEventName);

    eventToEdit.getChild("description").setText(editEventDesp);

    String result = dateAndTimeArrayToString(editEventDateAndTime);
    eventToEdit.getChild("date-time").setText(result);

    eventToEdit.getChild("location").setText(editEventLocation);
  }

  @Override
  public void deleteArea(int areaID) {
    validExistingAreaID(areaID);

    Element areaToRemove = getAreaElement(areaID);
    modelData.getRootElement().removeContent(areaToRemove);

    List<Element> remainingAreas = modelData.getRootElement().getChildren();
    for (Element area : remainingAreas) {
      int curAreaID = Integer.parseInt(area.getAttributeValue("id"));
      if (curAreaID > areaID) {
        String newCurAreaID = Integer.toString(curAreaID - 1);
        area.setAttribute("id", newCurAreaID);
      }
    }
  }

  @Override
  public void deleteEvent(int areaID, int eventID) {
    validExistingAreaID(areaID);

    if (eventID == 0) {
      throw new IllegalArgumentException("The ID of any Event that is apart of this model can't " +
              "have the ID dedicated to creating new Events - i.e. no Event can have ID of zero!");
    }
    else if (eventID < 0) {
      throw new IllegalArgumentException("An existing Event's ID must be greater than 0!");
    }

    Element associatedArea = getAreaElement(areaID);
    Element eventToDelete = getEventElement(areaID, eventID);
    associatedArea.removeContent(eventToDelete);

    List<Element> remainingEvents = associatedArea.getChildren("event");
    for (Element event : remainingEvents) {
      int curEventID = Integer.parseInt(event.getAttributeValue("id"));
      if (curEventID > eventID) {
        String newCurEventID = Integer.toString(curEventID - 1);
        event.setAttribute("id", newCurEventID);
      }
    }
  }

  @Override
  public List<AreaData> outputAreas() {
    List<AreaData> toReturn = new ArrayList<>();

    List<Element> areas = modelData.getRootElement().getChildren();

    for (Element area : areas) {
      int id = Integer.parseInt(area.getAttributeValue("id"));
      String name = area.getChild("name").getText();
      String desp = area.getChild("description").getText();
      AreaData toAdd = new AreaDataImpl(id, name, desp);
      toReturn.add(toAdd);
    }

    return toReturn;
  }

  @Override
  public List<EventData> outputEvents(int areaID) {
    validExistingAreaID(areaID);

    Element area = getAreaElement(areaID);

    List<EventData> toReturn = new ArrayList<>();

    List<Element> events = area.getChildren("event");
    for (Element event : events) {
      int id = Integer.parseInt(event.getAttributeValue("id"));
      String name = event.getChild("name").getText();
      String desp = event.getChild("description").getText();
      String location = event.getChild("location").getText();

      String dateAndTimeString = event.getChild("date-time").getText();
      int year = Integer.parseInt(dateAndTimeString.substring(0, 4));
      int month = Integer.parseInt(dateAndTimeString.substring(5, 7));
      int day = Integer.parseInt(dateAndTimeString.substring(8, 10));
      int hour = Integer.parseInt(dateAndTimeString.substring(11, 13));
      int minute = Integer.parseInt(dateAndTimeString.substring(14, 16));

      LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute);

      EventData toAdd = new EventDataImpl(areaID, id, name, desp, location, dateTime);
      toReturn.add(toAdd);
    }

    return toReturn;
  }

  @Override
  public String outputModelDataAsString() {
    return new XMLOutputter().outputString(modelData);
  }
}
