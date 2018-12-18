package view;

import controller.ButtonEvents;
import datatransfer.AreaData;
import datatransfer.EventData;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Implementation of the ViewInterface, as a JavaFX GUI.
 */
public class ViewImpl implements ViewInterface {

  /**
   * The AreaDatas whose data s currently being displayed. Static variable since when launching an
   * application, a new instance of that application is created, must be static so <i>any</i>
   * ViewImpl instance can access the areas that need to be displayed.
   */
  private List<AreaData> areas;

  /**
   * The ID of the Area whose events are currently being displayed. If 0, indicated no Area is 
   * currently selected
   */
  private int currentAreaID = 0;

  /**
   * The ID of the Event who is currently being displayed. If 0, indicated no Event is
   * currently selected
   */
  private int currentEventID = 0;

  /**
   * The EventDatas whose data is currently being display, are the Events of the AreaData whose ID
   * is equal to currentAreaID.
   */
  private List<EventData> events;

  /**
   * ScrollPane displaying the data in each AreaData in areas.
   */
  private ScrollPane areasDisplay;

  /**
   * ScrollPane displaying the name and description of the Area currently being displayed (as per
   * currentAreaID).
   */
  private ScrollPane currentAreaInfo;

  /**
   * ScrollPane displaying the data in each EventData in events.
   */
  private ScrollPane eventsDisplay;

  /**
   * The total width of this application's window.
   */
  private final int totalWidth;

  /**
   * The total height of this application's window.
   */
  private final int totalHeight;

  /**
   * The total width of area of this application delegated to displaying what Areas a user may
   * select from.
   */
  private final int areaWidth;

  /**
   * The total width of area of this application delegated to displaying what the Events of the
   * currently selected Area.
   */
  private final int eventWidth;

  /**
   * Universal value for any padding in this Display.
   */
  private final int paddingValue;

  /**
   * Button to add a new Area.
   */
  private final Button addNewArea;

  /**
   * Button to add a new Event to a new Area.
   */
  private final Button addNewEvent;

  /**
   * Button to edit the selected Area.
   */
  private final Button editArea;

  /**
   * Button to delete the currently selected Area.
   */
  private final Button deleteArea;

  private EventHandler<ActionEvent> displayEventHandler;

  private EventHandler<ActionEvent> deleteEventHandler;

  private EventHandler<ActionEvent> editEventHandler;

  /**
   * Root frame of this view.
   */
  private final BorderPane root;

  /**
   *
   */
  public ViewImpl() {
    // Set constants
    areas = new ArrayList<>();
    events = new ArrayList<>();
    totalWidth = 700;
    totalHeight = 500;
    areaWidth = 200;
    eventWidth = totalWidth - areaWidth;
    paddingValue = 5;

    areasDisplay = new ScrollPane();
    eventsDisplay = new ScrollPane();
    currentAreaInfo = new ScrollPane();

    // Root pane to display all this application's content. Split into an area to display all Areas,
    // and an area to display all the Events of a selected Area - and the Area's own info.
    root = new BorderPane();

    // Size parameters for buttons, internal containers, etc.
    int labelHeight = 10;

    int areasDisplayWidth = areaWidth;
    int areasDisplayHeight = totalHeight - labelHeight;

    int buttonWidth = 150;
    int buttonHeight = 100;
    int currentAreaInfoWidth = eventWidth - buttonWidth;
    int currentAreaInfoHeight = buttonHeight;

    int eventsDisplayWidth = eventWidth - labelHeight;
    int eventsDisplayHeight = totalHeight - buttonHeight;

    // Part of the root pane to display Areas.

    areasDisplay.setMinWidth(areasDisplayWidth);
    areasDisplay.setMinHeight(areasDisplayHeight);

    Label areasLabel = new Label("Areas");
    areasLabel.setPrefHeight(labelHeight);
    VBox areaVBox = new VBox(areasLabel, areasDisplay);
    areaVBox.setAlignment(Pos.CENTER);
    areaVBox.setMinWidth(areaWidth);
    areaVBox.setPrefHeight(totalHeight);

    // Part of the root pane to display Events.
    // Button to add a new Area to the list of all Areas currently stored in the model.
    addNewArea = new Button("Add New Area");
    addNewArea.setPrefWidth(buttonWidth);
    addNewArea.setPrefHeight(buttonHeight / 4);

    // Button to add a new Event to the currently selected / displayed Area.
    addNewEvent = new Button("Add New Event");
    addNewEvent.setMinWidth(buttonWidth);
    addNewEvent.setPrefHeight(buttonHeight / 4);

    // Button to edit the information of the currently selected / displayed Area.
    editArea = new Button("Edit Area");
    editArea.setMinWidth(buttonWidth);
    editArea.setPrefHeight(buttonHeight / 4);

    // Button to delete the currently selected / displayed Area.
    deleteArea = new Button("Delete Area");
    deleteArea.setMinWidth(buttonWidth);
    deleteArea.setPrefHeight(buttonHeight / 4);

    VBox buttons = new VBox(addNewArea, addNewEvent, deleteArea, editArea);

    Label areaInfo = new Label("Current Area Info");
    VBox currentAreaInfoBox = new VBox(areaInfo, currentAreaInfo);
    currentAreaInfoBox.setAlignment(Pos.CENTER);
    currentAreaInfoBox.setPrefWidth(currentAreaInfoWidth);
    currentAreaInfoBox.setPrefHeight(currentAreaInfoHeight);

    HBox eventHeader = new HBox(currentAreaInfoBox, buttons);

    Label eventsLabel = new Label("Events");
    eventsLabel.setPrefHeight(labelHeight);
    eventsDisplay.setMinWidth(eventsDisplayWidth);
    eventsDisplay.setMinHeight(eventsDisplayHeight);

    VBox eventVBox = new VBox(eventHeader, eventsLabel, eventsDisplay);
    eventVBox.setAlignment(Pos.CENTER);

    // Assign the component displaying all the Areas to the left part of the root pane, and the
    // component displaying all Areas to the right part of the root pane.
    root.setLeft(areaVBox);
    root.setRight(eventVBox);
  }

  @Override
  public void receiveAreas(List<AreaData> areas) {
    if (areas == null) {
      throw new IllegalArgumentException("Given list of AreaDatas can't be null!");
    }

    this.areas = areas;

    // Don't attempt to display any Areas unless the View is actually up and running.
    if (areasDisplay != null) {
      // Internal VBox to display each Area.
      VBox internalVBox = new VBox();

      // Visualize each Area as a button with its name on the button.
      for (AreaData area : this.areas) {
        Text areaName = new Text(area.getAreaName());

        int backgroundWidth = areaWidth;
        int backgroundHeight = 50;
        Button background = new Button();
        background.setMinHeight(backgroundHeight);
        background.setMinWidth(backgroundWidth);

        // When Area button is clicked, display the Area's associated Events and own info.
        background.setOnAction(event -> {
          currentAreaID = area.getAreaId();
          // Display the Area's own name and description.
          Text name = new Text("Name: " + area.getAreaName());
          Text desp = new Text("Description: " + area.getAreaDescription());
          VBox text = new VBox(name, desp);
          currentAreaInfo.setContent(text);
          background.setOnAction(displayEventHandler);
          background.fire();
        });

        StackPane toAdd = new StackPane();
        toAdd.getChildren().addAll(background, areaName);

        internalVBox.getChildren().add(toAdd);
      }
      // Assign the internal VBox with all the visualized Areas to the areasDisplay to actually be
      // displayed to the user.
      areasDisplay.setContent(internalVBox);
    }
  }

  @Override
  public void receiveEvents(List<EventData> events) {
    if (events == null) {
      throw new IllegalArgumentException("Given list of EventDatas can't be null!");
    }

    this.events = events;

    // Don't attempt to display any Events unless this View is up and running.
    if (eventsDisplay != null) {
      // VBox where events are to be displayed
      VBox internalVBox = new VBox();
      internalVBox.setSpacing(5);

      // Check that the Events to be displayed are of the currently selected Area (as per
      // currentAreaID).
      for (EventData event : this.events) {
        if (event.getAssociatedAreaId() != currentAreaID) {
          throw new IllegalArgumentException("Can't display Events not associated with the " +
                  "currently selected Area!");
        }

        // Get info out from current Event and prep it for use.
        Text name = new Text("Event Name: " + event.getEventName());
        Text desp = new Text("Event Desp: " + event.getEventDescription());
        Text location = new Text("Event Location: " + event.getEventLocation());

        LocalDateTime dateTimeInfo = event.getEventDateAndTime();
        int year = dateTimeInfo.getYear();
        int month = dateTimeInfo.getMonthValue();
        int day = dateTimeInfo.getDayOfMonth();
        int hour = dateTimeInfo.getHour();
        int minute = dateTimeInfo.getMinute();
        int finalHour = event.getFinalHour();
        String timeConventionString = event.getTimeConvention();

        Text date = new Text(String.format("Date: %d/%d/%d", month, day, year));
        Text time = new Text(String.format("Time: %d:%02d %s", finalHour, minute, timeConventionString));

        // Button to edit this Event.
        Button editEvent = new Button("Edit");
        editEvent.setOnAction(event1 -> {
          currentEventID = event.getEventId();
          editEvent.setOnAction(editEventHandler);
          editEvent.fire();
        });

        // Button to delete this Event.
        Button deleteEvent = new Button("Delete");
        deleteEvent.setOnAction(event1 -> {
          currentEventID = event.getEventId();
          deleteEvent.setOnAction(deleteEventHandler);
          deleteEvent.fire();
        });

        // More containers to hold all the info that needs to be displayed.
        HBox buttons = new HBox(editEvent, deleteEvent);
        VBox items = new VBox(name, desp, location, date, time, buttons);

        // Background to display info and buttons over.
        int backgroundWidth = eventWidth;
        int backgroundHeight = 110;
        Rectangle background = new Rectangle(backgroundWidth, backgroundHeight, Color.LIGHTGRAY);

        StackPane stack = new StackPane();
        stack.getChildren().addAll(background, items);
        stack.setAlignment(Pos.CENTER_LEFT);

        internalVBox.getChildren().add(stack);
      }
      // Add the internal VBox with all the generated Event views to the eventsDisplay to actually
      // be displayed to the user.
      eventsDisplay.setContent(internalVBox);
    }
  }

  @Override
  public void assignButtonEvents(Map<ButtonEvents, EventHandler<ActionEvent>> buttonEvents)
      throws IllegalArgumentException {
    if (buttonEvents == null) {
      throw new IllegalArgumentException("Given mapping can't null!");
    }

    if (buttonEvents.containsKey(ButtonEvents.NEW_AREA)) {
      addNewArea.setOnAction(buttonEvents.get(ButtonEvents.NEW_AREA));
    }
    else {
      throw new IllegalArgumentException("Given mapping must contain EventHandler for NEW_AREA "
          + "ButtonEvent!");
    }

    if (buttonEvents.containsKey(ButtonEvents.DELETE_AREA)) {
      deleteArea.setOnAction(buttonEvents.get(ButtonEvents.DELETE_AREA));
    }
    else {
      throw new IllegalArgumentException("Given mapping must contain EventHandler for DELETE_AREA "
          + "ButtonEvent!");
    }

    if (buttonEvents.containsKey(ButtonEvents.NEW_EVENT)) {
      addNewEvent.setOnAction(buttonEvents.get(ButtonEvents.NEW_EVENT));
    }
    else {
      throw new IllegalArgumentException("Given mapping must contain EventHandler for NEW_EVENT "
          + "ButtonEvent!");
    }

    if (buttonEvents.containsKey(ButtonEvents.EDIT_AREA)) {
      editArea.setOnAction(buttonEvents.get(ButtonEvents.EDIT_AREA));
    }
    else {
      throw new IllegalArgumentException("Given mapping must contain EventHandler for EDIT_AREA "
          + "ButtonEvent!");
    }

    if (buttonEvents.containsKey(ButtonEvents.DELETE_EVENT)) {
      deleteEventHandler = buttonEvents.get(ButtonEvents.DELETE_EVENT);
    }
    else {
      throw new IllegalArgumentException("Given mapping must contain EventHandler for DELETE_EVENT "
          + "ButtonEvent!");
    }

    if (buttonEvents.containsKey(ButtonEvents.EDIT_EVENT)) {
      editEventHandler = buttonEvents.get(ButtonEvents.EDIT_EVENT);
    }
    else {
      throw new IllegalArgumentException("Given mapping must contain EventHandler for EDIT_EVENT "
          + "ButtonEvent!");
    }

    if (buttonEvents.containsKey(ButtonEvents.DISPLAY)) {
      displayEventHandler = buttonEvents.get(ButtonEvents.DISPLAY);
    }
    else {
      throw new IllegalArgumentException("Given mapping must contain EventHandler for DISPLAY "
          + "ButtonEvent!");
    }
  }

  @Override
  public Parent asParent() {
    return root;
  }

  @Override
  public int getWidth() {
    return totalWidth;
  }

  @Override
  public int getHeight() {
    return totalHeight;
  }

  @Override
  public int getSelectedAreaID() {
    return currentAreaID;
  }

  @Override
  public void resetAreaDisplay() {
    currentAreaID = 0;
    currentAreaInfo.setContent(new Text());
  }

  @Override
  public int getSelectedEventID() {
    return currentEventID;
  }

  @Override
  public void resetEventDisplay() {
    currentEventID = 0;
    eventsDisplay.setContent(new Text());
  }
}