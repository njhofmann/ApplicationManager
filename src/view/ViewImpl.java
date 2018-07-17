package view;

import java.util.ArrayList;
import java.util.List;

import datatransfer.AreaData;
import datatransfer.AreaDataImpl;
import datatransfer.EventData;
import datatransfer.EventDataImpl;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ModelInterface;

/**
 *
 */
public class ViewImpl extends Application implements ViewInterface {

  /**
   * The model this View is associated with.
   */
  private static ModelInterface model;

  /**
   * The AreaDatas whose data s currently being displayed. Static variable since when launching an
   * application, a new instance of that application is created, must be static so <i>any</i>
   * ViewImpl instance can access the areas that need to be displayed.
   */
  private List<AreaData> areas = new ArrayList<>();

  /**
   * The ID of the Area whose events are currently being displayed. If 0, indicated no Area is 
   * currently selected
   */
  private int displayID = 0;

  /**
   * The EventDatas whose data is currently being display, are the Events of the AreaData whose ID
   * is equal to displayID.
   */
  private List<EventData> events = new ArrayList<>();

  /**
   * ScrollPane displaying the data in each AreaData in areas.
   */
  private ScrollPane areasDisplay;

  /**
   * ScrollPane displaying the name and description of the Area currently being displayed (as per
   * displayID).
   */
  private ScrollPane currentAreaInfo;

  /**
   * ScrollPane displaying the data in each EventData in events.
   */
  private ScrollPane eventsDisplay;

  /**
   * The total width of this application's window.
   */
  private final int totalWidth = 700;

  /**
   * The total height of this application's window.
   */
  private final int totalHeight = 500;

  /**
   * The total width of area of this application delegated to displaying what Areas a user may
   * select from.
   */
  private final int areaWidth = 200;

  /**
   * The total width of area of this application delegated to displaying what the Events of the
   * currently selected Area.
   */
  private final int eventWidth = totalWidth - areaWidth;

  @Override
  public void init() {
    areasDisplay = new ScrollPane();
    eventsDisplay = new ScrollPane();
    currentAreaInfo = new ScrollPane();
  }

  @Override
  public void start(Stage primaryStage) {
    // Root pane to display all this application's content. Split into an area to display all Areas,
    // and an area to display all the Events of a selected Area - and the Area's own info.
    BorderPane root = new BorderPane();

    // Size parameters for buttons, internal containers, etc.
    int addNewAreaWidth = areaWidth;
    int addNewAreaHeight = 40;
    int areasDisplayWidth = areaWidth;
    int areasDisplayHeight = totalHeight - addNewAreaHeight;

    int addNewEventWidth = 150;
    int addNewEventHeight = 100;
    int currentAreaInfoWidth = eventWidth - addNewEventWidth;
    int currentAreaInfoHeight = addNewEventHeight;

    int eventsDisplayWidth = eventWidth;
    int eventsDisplayHeight = totalHeight - addNewEventHeight;

    // Part of the root pane to display Areas.
    VBox areaVBox = new VBox();

    // Button to add a new Area to the list of all Areas currently stored in the model.
    Button addNewArea = new Button("Add New Area");
    addNewArea.setMinWidth(addNewAreaWidth);
    addNewArea.setMinHeight(addNewAreaHeight);
    addNewArea.setOnAction(event -> {
      GridPane newAreaWindowRoot = new GridPane();

      Label nameLabel = new Label("New Area Name:");
      newAreaWindowRoot.add(nameLabel, 0, 0);
      TextField nameField = new TextField();
      newAreaWindowRoot.add(nameField, 1, 0);

      Label despLabel = new Label("New Area Description:");
      newAreaWindowRoot.add(despLabel, 0, 1);
      TextField despField = new TextField();
      newAreaWindowRoot.add(despField, 1, 1);

      // Button for if the user wishes to create a new Area from the info they have entered so far.
      Button submit = new Button("Submit");
      newAreaWindowRoot.add(submit, 2, 0);

      // Button for if the user wishes to cancel creating a new Area.
      Button cancel = new Button("Cancel");
      newAreaWindowRoot.add(cancel, 2, 1);

      Scene newAreaWindowScene = new Scene(newAreaWindowRoot);
      Stage addAreaWindow = new Stage();
      addAreaWindow.setScene(newAreaWindowScene);
      addAreaWindow.setTitle("Add New Area");
      addAreaWindow.initModality(Modality.APPLICATION_MODAL);

      submit.setOnAction(event12 -> {
        addAreaWindow.close();
        AreaData toSend = new AreaDataImpl(0, nameField.getText(), despField.getText());
        model.addArea(toSend);
        receiveAreas(model.outputAreas());
      });

      cancel.setOnAction(event1 -> addAreaWindow.close());

      addAreaWindow.show();
    });

    areasDisplay.setMinWidth(areasDisplayWidth);
    areasDisplay.setMinHeight(areasDisplayHeight);
    receiveAreas(model.outputAreas());

    areaVBox.getChildren().addAll(addNewArea, areasDisplay);
    areaVBox.setMinWidth(areaWidth);
    areaVBox.setMinHeight(totalHeight);

    // Part of the root pane to display Events.
    VBox eventVBox = new VBox();

    // Button to add a new Event to the currently selected / displayed Area.
    Button addNewEvent = new Button("Add New Event");
    addNewEvent.setMinWidth(addNewEventWidth);
    addNewEvent.setOnAction(event -> {
      // Don't add a new Event unless an Area is actually being displayed.
      if (displayID > 0) {
        // Creates a new window where
        GridPane windowRoot = new GridPane();
        GridPane.setFillHeight(windowRoot, true);

        // Field to enter the name of the new Event.
        Label nameLabel = new Label("New Event Name: ");
        windowRoot.add(nameLabel, 0, 0);
        TextField nameField = new TextField();
        windowRoot.add(nameField, 1, 0);

        // Field to enter a description of the new Event.
        Label despLabel = new Label("New Event Description: ");
        windowRoot.add(despLabel, 0, 1);
        TextField despField = new TextField();
        windowRoot.add(despField, 1, 1);


        // Field to enter the location of the new Event.
        Label locationLabel = new Label("New Event Location: ");
        windowRoot.add(locationLabel, 0, 2);
        TextField locationField = new TextField();
        windowRoot.add(locationField, 1, 2);

        Label selectDateLabel = new Label("New Event Date: ");
        windowRoot.add(selectDateLabel, 0, 3);
        DatePicker datePicker = new DatePicker();
        windowRoot.add(datePicker, 1, 3);

        Label enterTimeLabel = new Label("New Event Time: ");
        windowRoot.add(enterTimeLabel, 0, 4);

        Label hoursLabel = new Label("Hour: ");
        windowRoot.add(hoursLabel, 0, 5);
        hoursLabel.setAlignment(Pos.CENTER_RIGHT);
        Label minutesLabel = new Label("Minutes: ");
        windowRoot.add(minutesLabel, 0, 6);
        minutesLabel.setAlignment(Pos.CENTER_RIGHT);

        Spinner hours = new Spinner<Integer>(0, 59, 0);
        windowRoot.add(hours, 1, 4);
        //Spinner mins = new Spinner(0, )

        // Button for when the user wishes to create a new Event from the information they have
        // currently entered.
        Button submit = new Button("Submit");

        // Button for when the user wishes to cancel creating a new Event.
        Button cancel = new Button("Cancel");

        HBox buttons = new HBox(submit, cancel);
        windowRoot.add(buttons, 0, 5, 2, 1);

        // Creates a new window to display the texts fields, makes it so the parent window can't
        // be interacted with as long as this window is open.
        Scene windowScene = new Scene(windowRoot);
        Stage addEventWindow = new Stage();
        addEventWindow.setResizable(false);
        addEventWindow.setScene(windowScene);
        addEventWindow.setTitle("Add New Event");
        addEventWindow.initModality(Modality.APPLICATION_MODAL);

        submit.setOnAction(event12 -> {
          // Closes the window, sends the information the user entered to the model as a new
          // EventData, then resends a request to the model for the Events associated with the
          // currently selected / displayed Area.
          addEventWindow.close();
          EventData toSend = new EventDataImpl(displayID, 0, nameField.getText(),
                  despField.getText(), locationField.getText(), new int[]{2017, 5, 23, 5, 5, 0});
          model.addEvent(toSend);
          receiveEvents(model.outputEvents(new AreaDataImpl(displayID, "", "")));
        });

        cancel.setOnAction(event1 -> {
          addEventWindow.close();
        });

        // Display the window.
        addEventWindow.show();
      }
    });

    // Button to edit the information of the currently selected / displayed Area.
    Button editArea = new Button("Edit Area");
    editArea.setMinWidth(addNewEventWidth);
    editArea.setOnAction(event -> {
      // Can't edit an Area unless an Area is actually being displayed / is selected.
      if (displayID > 0) {
        AreaData toEdit = areas.get(displayID - 1);

        GridPane newAreaWindowRoot = new GridPane();

        // Displays the name field of the current Area for the user to make changes to.
        Label nameLabel = new Label("Edit Area Name:");
        newAreaWindowRoot.add(nameLabel, 0, 0);
        TextField nameField = new TextField(toEdit.getAreaName());
        newAreaWindowRoot.add(nameField, 1, 0);

        // Displays the description field of the current Area for the user to make changes to.
        Label despLabel = new Label("Edit Area Description:");
        newAreaWindowRoot.add(despLabel, 0, 1);
        TextField despField = new TextField(toEdit.getAreaDescription());
        newAreaWindowRoot.add(despField, 1, 1);

        // Button for if the user wishes to finalize the edits they made.
        Button submit = new Button("Submit");
        newAreaWindowRoot.add(submit, 2, 0);

        //Button for if the users to cancel making any changes.
        Button cancel = new Button("Cancel");
        newAreaWindowRoot.add(cancel, 2, 1);

        Scene newAreaWindowScene = new Scene(newAreaWindowRoot);
        Stage addAreaWindow = new Stage();
        addAreaWindow.setScene(newAreaWindowScene);
        addAreaWindow.setTitle("Edit Current Area");
        addAreaWindow.initModality(Modality.APPLICATION_MODAL);

        submit.setOnAction(event12 -> {
          // Closes the window, grabs the text the user entered into the fields and sends those
          // changes to the model, then sends a request again to the model to display all its Areas.
          // Any changes made should be displayed in the new batch of Areas.
          addAreaWindow.close();
          AreaData toSend = new AreaDataImpl(toEdit.getAreaId(), nameField.getText(),
                  despField.getText());
          model.editArea(toSend);
          receiveAreas(model.outputAreas());
        });

        cancel.setOnAction(event1 -> {
          // Closes the window.
          addAreaWindow.close();
        });

        // Displays the window.
        addAreaWindow.show();
      }
    });

    // Button to delete the currently selected / displayed Area.
    Button deleteArea = new Button("Delete Area");
    deleteArea.setMinWidth(addNewEventWidth);
    deleteArea.setOnAction(event -> {
      // Don't delete the current Area unless an Area is actually being displayed.
      if (displayID > 0) {
        Label check = new Label("Are you sure you want to delete the current Area?");
        Button delete = new Button("Delete");
        Button cancel = new Button("Cancel");

        HBox buttons = new HBox(delete, cancel);
        VBox windowRoot = new VBox(check, buttons);

        Scene windowScene = new Scene(windowRoot);
        Stage window = new Stage();
        window.setScene(windowScene);
        window.setTitle("Delete Area");
        window.initModality(Modality.APPLICATION_MODAL);

        delete.setOnAction(event1 -> {
          // Closes the window, sends a request to the model to delete the currently selected /
          // displayed Area, resets the displayID to 0, then sends another request to the model to
          // display all its Areas. The just deleted Area should no longer be included in the new
          // batch of Areas.
          window.close();
          AreaData toSend = new AreaDataImpl(displayID, "", "");
          displayID = 0;
          model.deleteArea(toSend);
          receiveAreas(model.outputAreas());
        });

        cancel.setOnAction(event1 -> {
          window.close();
        });

        // Display the window.
        window.show();
      }
    });

    VBox buttons = new VBox();
    buttons.getChildren().addAll(addNewEvent, deleteArea, editArea);
    buttons.setMinHeight(addNewEventHeight);

    currentAreaInfo.setMinWidth(currentAreaInfoWidth);
    currentAreaInfo.setMinHeight(currentAreaInfoHeight);

    HBox eventHeader = new HBox();
    eventHeader.getChildren().addAll(currentAreaInfo, buttons);

    eventsDisplay.setMinWidth(eventsDisplayWidth);
    eventsDisplay.setMinHeight(eventsDisplayHeight);

    eventVBox.getChildren().addAll(eventHeader, eventsDisplay);

    // Assign the component displaying all the Areas to the left part of the root pane, and the
    // component displaying all Areas to the right part of the root pane.
    root.setLeft(areaVBox);
    root.setRight(eventVBox);

    // Assign the root pane to a scene, and the scene to the primaryStage, then display the
    // primaryStage.
    Scene scene = new Scene(root, totalWidth, totalHeight);
    primaryStage.setTitle("Application Manager");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @Override
  public void setAssociatedModel(ModelInterface model) {
    ViewImpl.model = model;
  }

  @Override
  public void start() {
    launch();
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
          displayID = area.getAreaId();

          // Display the Area's own name and description.
          Text name = new Text("Name: " + area.getAreaName());
          Text desp = new Text("Description: " + area.getAreaDescription());
          VBox text = new VBox(name, desp);
          currentAreaInfo.setContent(text);

          // Send a request to the model for Event's associated with the Area that was just clicked.
          AreaData areaID = new AreaDataImpl(displayID, "", "");
          List<EventData> eventsToDisplay = model.outputEvents(areaID);

          // Display the received events.
          receiveEvents(eventsToDisplay);
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
      // displayID).
      for (EventData event : this.events) {
        if (event.getAssociatedAreaId() != displayID) {
          throw new IllegalArgumentException("Can't display Events not associated with the " +
                  "currently selected Area!");
        }

        // Get info out from current Event and prep it for use.
        Text name = new Text("Event Name: " + event.getEventName());
        Text desp = new Text("Event Desp: " + event.getEventDescription());
        Text location = new Text("Event Location: " + event.getEventLocation());

        int[] dateAndTimeInfo = event.getEventDateAndTime();
        int year = dateAndTimeInfo[0];
        int month = dateAndTimeInfo[1];
        int day = dateAndTimeInfo[2];
        int hour = dateAndTimeInfo[3];
        int minute = dateAndTimeInfo[4];
        int timeConvention = dateAndTimeInfo[5];

        String timeConventionString;
        if (timeConvention == 0) {
          timeConventionString = "AM";
        }
        else {
          timeConventionString = "PM";
        }

        Text date = new Text(String.format("Date: %d/%d/%d", day, month, year));
        Text time = new Text(String.format("Time: %d:%02d %s", hour, minute, timeConventionString));

        // Button to edit this Event.
        Button editEvent = new Button("Edit");
        editEvent.setOnAction(event1 -> {
          EventData toEdit = events.get(event.getEventId() - 1);

          GridPane windowRoot = new GridPane();

          Label nameLabel = new Label("Edit Event Name:");
          windowRoot.add(nameLabel, 0, 0);

          Label despLabel = new Label("Edit Event Description:");
          windowRoot.add(despLabel, 0, 1);

          Label locationLabel = new Label("Edit Event Location");
          windowRoot.add(locationLabel, 0, 2);

          TextField nameField = new TextField(toEdit.getEventName());
          windowRoot.add(nameField, 1, 0);

          TextField despField = new TextField(toEdit.getEventDescription());
          windowRoot.add(despField, 1, 1);

          TextField locationField = new TextField(toEdit.getEventLocation());
          windowRoot.add(locationField, 1, 2);

          Button submit = new Button("Submit");
          windowRoot.add(submit, 2, 0);

          Button cancel = new Button("Cancel");
          windowRoot.add(cancel, 2, 1);

          Scene windowScene = new Scene(windowRoot);
          Stage window = new Stage();
          window.setScene(windowScene);
          window.setTitle("Edit Current Event");
          window.initModality(Modality.APPLICATION_MODAL);

          submit.setOnAction(event12 -> {
            window.close();
            EventData toSend = new EventDataImpl(displayID, toEdit.getEventId(), nameField.getText(),
                    despField.getText(), locationField.getText(), toEdit.getEventDateAndTime());
            model.editEvent(toSend);
            receiveEvents(model.outputEvents(new AreaDataImpl(displayID, "", "")));
          });

          cancel.setOnAction(event13 -> {
            window.close();
          });

          window.show();
        });

        // Button to delete this Event.
        Button deleteEvent = new Button("Delete");
        deleteEvent.setOnAction(event1 -> {
          Label check = new Label("Are you sure you want to delete the current Event?");
          Button delete = new Button("Delete");
          Button cancel = new Button("Cancel");

          HBox buttons = new HBox(delete, cancel);
          VBox windowRoot = new VBox(check, buttons);

          Scene windowScene = new Scene(windowRoot);
          Stage window = new Stage();
          window.setScene(windowScene);

          delete.setOnAction(event2 -> {
            EventData toSend = new EventDataImpl(displayID, event.getEventId(), "", "", "", new int[]{1, 1, 1, 0, 0, 0});
            model.deleteEvent(toSend);
            receiveEvents(model.outputEvents(new AreaDataImpl(displayID, "", "")));
            window.close();
          });

          cancel.setOnAction(event3 -> {
            window.close();
          });

          window.setTitle("Delete Event");
          window.initModality(Modality.APPLICATION_MODAL);
          window.show();
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
}