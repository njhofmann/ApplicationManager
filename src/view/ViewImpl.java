package view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import datatransfer.AreaData;
import datatransfer.AreaDataImpl;
import datatransfer.EventData;
import datatransfer.EventDataImpl;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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

  /**
   * Universal value for any padding in this Display.
   */
  private final int paddingValue = 5;

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
    // Button to add a new Area to the list of all Areas currently stored in the model.
    areasDisplay.setMinWidth(areasDisplayWidth);
    areasDisplay.setMinHeight(areasDisplayHeight);
    receiveAreas(model.outputAreas());

    Label areasLabel = new Label("Areas");
    areasLabel.setPrefHeight(labelHeight);
    VBox areaVBox = new VBox(areasLabel, areasDisplay);
    areaVBox.setAlignment(Pos.CENTER);
    areaVBox.setMinWidth(areaWidth);
    areaVBox.setPrefHeight(totalHeight);

    // Part of the root pane to display Events.

    // Button to add a new Area to the model as a whole.
    Button addNewArea = new Button("Add New Area");
    addNewArea.setPrefWidth(buttonWidth);
    addNewArea.setPrefHeight(buttonHeight / 4);
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

    // Button to add a new Event to the currently selected / displayed Area.
    Button addNewEvent = new Button("Add New Event");
    addNewEvent.setMinWidth(buttonWidth);
    addNewEvent.setPrefHeight(buttonHeight / 4);
    addNewEvent.setOnAction(event -> {
      // Don't add a new Event unless an Area is actually being displayed.
      if (displayID > 0) {
        // Creates a new window where
        GridPane windowRoot = new GridPane();

        // Field to enter the name of the new Event.
        Label nameLabel = new Label("New Event Name: ");
        windowRoot.add(nameLabel, 0, 0);
        GridPane.setHalignment(nameLabel, HPos.CENTER);

        TextField nameField = new TextField();
        windowRoot.add(nameField, 1, 0);

        // Field to enter a description of the new Event.
        Label despLabel = new Label("New Event Description: ");
        windowRoot.add(despLabel, 0, 1);
        GridPane.setHalignment(despLabel, HPos.CENTER);

        TextField despField = new TextField();
        windowRoot.add(despField, 1, 1);

        // Field to enter the location of the new Event.
        Label locationLabel = new Label("New Event Location: ");
        windowRoot.add(locationLabel, 0, 2);
        GridPane.setHalignment(locationLabel, HPos.CENTER);

        TextField locationField = new TextField();
        windowRoot.add(locationField, 1, 2);

        Label selectDateLabel = new Label("New Event Date: ");
        windowRoot.add(selectDateLabel, 0, 3);
        GridPane.setHalignment(selectDateLabel, HPos.CENTER);

        DatePicker datePicker = new DatePicker(LocalDate.now());
        windowRoot.add(datePicker, 1, 3);

        Label enterTimeLabel = new Label("New Event Time: ");
        windowRoot.add(enterTimeLabel, 0, 4, 2, 1);
        enterTimeLabel.setPadding(new Insets(paddingValue, 0, paddingValue, 0));
        GridPane.setHalignment(enterTimeLabel, HPos.CENTER);

        Label hoursLabel = new Label("Hour: ");
        windowRoot.add(hoursLabel, 0, 5);
        GridPane.setHalignment(hoursLabel, HPos.RIGHT);

        Spinner hours = new Spinner(TimeSpinnerValueFactory.getAMSpinnerValues());
        windowRoot.add(hours, 1, 5);

        Label minutesLabel = new Label("Minutes: ");
        windowRoot.add(minutesLabel, 0, 6);
        GridPane.setHalignment(minutesLabel, HPos.RIGHT);

        Spinner mins = new Spinner(TimeSpinnerValueFactory.getMinuteSpinnerValues());
        windowRoot.add(mins, 1, 6);

        Label timeConventionLabel = new Label("Time Convention: ");
        windowRoot.add(timeConventionLabel, 0, 7);
        GridPane.setHalignment(timeConventionLabel, HPos.RIGHT);

        Button timeConvention = new Button("AM");
        windowRoot.add(timeConvention, 1, 7);

        TimeConventionEventHandler timeConventionEventHandler =
                new TimeConventionEventHandler(timeConvention, hours);
        timeConvention.setOnAction(timeConventionEventHandler);

        // Button for when the user wishes to create a new Event from the information they have
        // currently entered.
        Button submit = new Button("Submit");

        // Button for when the user wishes to cancel creating a new Event.
        Button cancel = new Button("Cancel");

        HBox buttons = new HBox(submit, cancel);
        windowRoot.add(buttons, 0, 8, 2, 1);

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

          LocalDate dateInfo = datePicker.getValue();
          int year = dateInfo.getYear();
          int month = dateInfo.getMonthValue();
          int day = dateInfo.getDayOfMonth();
          int hour = (int)hours.getValue();
          int minute = (int)mins.getValue();

          int timeConventionValue;
          if (timeConvention.getText().equals("AM")) {
            timeConventionValue = 0;
          }
          else {
            timeConventionValue = 1;
          }

          EventData toSend = new EventDataImpl(displayID, 0, nameField.getText(),
                  despField.getText(), locationField.getText(), new int[]{year, month, day, hour,
                  minute, timeConventionValue});
          model.addEvent(toSend);
          receiveEvents(model.outputEvents(displayID));
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
    editArea.setMinWidth(buttonWidth);
    editArea.setPrefHeight(buttonHeight / 4);
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
    deleteArea.setMinWidth(buttonWidth);
    deleteArea.setPrefHeight(buttonHeight / 4);
    deleteArea.setOnAction(event -> {
      // Don't delete the current Area unless an Area is actually being displayed.
      if (displayID > 0) {
        Label check = new Label("Are you sure you want to delete the current Area?");
        check.setPadding(new Insets(paddingValue, paddingValue, paddingValue, paddingValue));

        Button delete = new Button("Delete");
        Button cancel = new Button("Cancel");
        HBox buttons = new HBox(delete, cancel);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(0, 0, paddingValue, 0));

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
          displayID = 0;
          model.deleteArea(displayID);
          receiveAreas(model.outputAreas());
        });

        cancel.setOnAction(event1 -> {
          window.close();
        });

        // Display the window.
        window.show();
      }
    });

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

    // Assign the root pane to a scene, and the scene to the primaryStage, then display the
    // primaryStage.
    Scene scene = new Scene(root, totalWidth, totalHeight);
    primaryStage.setTitle("Application Manager");
    primaryStage.setResizable(false);
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
          List<EventData> eventsToDisplay = model.outputEvents(displayID);

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

        Text date = new Text(String.format("Date: %d/%d/%d", month, day, year));
        Text time = new Text(String.format("Time: %d:%02d %s", hour, minute, timeConventionString));

        // Button to edit this Event.
        Button editEvent = new Button("Edit");
        editEvent.setOnAction(event1 -> {
          EventData toEdit = events.get(event.getEventId() - 1);

          GridPane windowRoot = new GridPane();

          Label nameLabel = new Label("Edit Event Name:");
          GridPane.setHalignment(nameLabel, HPos.CENTER);
          windowRoot.add(nameLabel, 0, 0);

          TextField nameField = new TextField(toEdit.getEventName());
          windowRoot.add(nameField, 1, 0);

          Label despLabel = new Label("Edit Event Description:");
          GridPane.setHalignment(despLabel, HPos.CENTER);
          windowRoot.add(despLabel, 0, 1);

          TextField despField = new TextField(toEdit.getEventDescription());
          windowRoot.add(despField, 1, 1);

          Label locationLabel = new Label("Edit Event Location");
          GridPane.setHalignment(locationLabel, HPos.CENTER);
          windowRoot.add(locationLabel, 0, 2);

          TextField locationField = new TextField(toEdit.getEventLocation());
          windowRoot.add(locationField, 1, 2);

          Label editDateLabel = new Label("New Event Date: ");
          windowRoot.add(editDateLabel, 0, 3);
          GridPane.setHalignment(editDateLabel, HPos.CENTER);

          // Need to set with local date time
          DatePicker datePicker = new DatePicker();
          windowRoot.add(datePicker, 1, 3);

          Label editTimeLabel = new Label("New Event Time: ");
          windowRoot.add(editTimeLabel, 0, 4, 2, 1);
          editTimeLabel.setPadding(new Insets(paddingValue, 0, paddingValue, 0));
          GridPane.setHalignment(editTimeLabel, HPos.CENTER);

          Label hoursLabel = new Label("Hour: ");
          windowRoot.add(hoursLabel, 0, 5);
          GridPane.setHalignment(hoursLabel, HPos.RIGHT);

          Spinner hours = new Spinner(TimeSpinnerValueFactory.getAMSpinnerValues());
          windowRoot.add(hours, 1, 5);

          Label minutesLabel = new Label("Minutes: ");
          windowRoot.add(minutesLabel, 0, 6);
          GridPane.setHalignment(minutesLabel, HPos.RIGHT);

          Spinner mins = new Spinner(TimeSpinnerValueFactory.getMinuteSpinnerValues());
          mins.getValueFactory().setValue(minute);
          windowRoot.add(mins, 1, 6);

          Label timeConventionLabel = new Label("Time Convention: ");
          windowRoot.add(timeConventionLabel, 0, 7);
          GridPane.setHalignment(timeConventionLabel, HPos.RIGHT);

          Button editTimeConvention = new Button(timeConventionString);
          windowRoot.add(editTimeConvention, 1, 7);

          TimeConventionEventHandler timeConventionEventHandler =
                  new TimeConventionEventHandler(editTimeConvention, hours);
          hours.getValueFactory().setValue(hour);
          editTimeConvention.setOnAction(timeConventionEventHandler);

          Button submit = new Button("Submit");

          Button cancel = new Button("Cancel");
          HBox buttons = new HBox(submit, cancel);
          windowRoot.add(buttons, 0, 8, 2, 1);

          Scene windowScene = new Scene(windowRoot);
          Stage window = new Stage();
          window.setScene(windowScene);
          window.setTitle("Edit Current Event");
          window.initModality(Modality.APPLICATION_MODAL);

          submit.setOnAction(event12 -> {
            window.close();

            LocalDate dateInfo = datePicker.getValue();
            int newYear = dateInfo.getYear();
            int newMonth = dateInfo.getMonthValue();
            int newDay = dateInfo.getDayOfMonth();
            int newHour = (int)hours.getValue();
            int newMinute = (int)mins.getValue();

            int newTimeConventionValue;
            if (editTimeConvention.getText().equals("AM")) {
              newTimeConventionValue = 0;
            }
            else {
              newTimeConventionValue = 1;
            }

            EventData toSend = new EventDataImpl(displayID, toEdit.getEventId(), nameField.getText(),
                    despField.getText(), locationField.getText(), new int[]{newYear, newMonth,
                    newDay, newHour, newMinute, newTimeConventionValue});
            model.editEvent(toSend);
            receiveEvents(model.outputEvents(displayID));
          });

          cancel.setOnAction(event13 -> {
            window.close();
          });

          window.show();
        });

        // Button to delete this Event.
        Button deleteEvent = new Button("Delete");
        deleteEvent.setOnAction(event1 -> {
          Label check = new Label("Are you sure you want to delete this Event?");
          check.setPadding(new Insets(paddingValue, paddingValue, paddingValue, paddingValue));

          Button delete = new Button("Delete");
          Button cancel = new Button("Cancel");
          HBox buttons = new HBox(delete, cancel);
          buttons.setPadding(new Insets(0, 0, paddingValue, 0));
          buttons.setAlignment(Pos.CENTER);

          VBox windowRoot = new VBox(check, buttons);
          Scene windowScene = new Scene(windowRoot);
          Stage window = new Stage();
          window.setScene(windowScene);

          delete.setOnAction(event2 -> {
            model.deleteEvent(displayID, event.getEventId());
            receiveEvents(model.outputEvents(displayID));
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

  /**
   *
   */
  private class TimeConventionEventHandler implements EventHandler<ActionEvent> {

    private final Button button;
    private final Spinner<Integer> hours;

    public TimeConventionEventHandler(Button button, Spinner<Integer> hours) {
      this.button = button;
      this.hours = hours;
    }

    @Override
    public void handle(ActionEvent event) {
      if (button.getText().equals("AM")) {
        button.setText("PM");
        hours.setValueFactory(TimeSpinnerValueFactory.getPMSpinnerValues());
      }
      else {
        button.setText("AM");
        hours.setValueFactory(TimeSpinnerValueFactory.getAMSpinnerValues());
      }
    }
  }


  private static class TimeSpinnerValueFactory {
    public static SpinnerValueFactory<Integer> getAMSpinnerValues() {
      SpinnerValueFactory<Integer> toReturn = new SpinnerValueFactory<>() {
        @Override
        public void decrement(int steps) {
          if (getValue().equals(11)) {
            setValue(0);
          } else {
            setValue(getValue() + 1);
          }
        }

        @Override
        public void increment(int steps) {
          if (getValue().equals(0)) {
            setValue(11);
          } else {
            setValue(getValue() - 1);
          }
        }
      };
      toReturn.setValue(0);
      return toReturn;
    }

    public static SpinnerValueFactory<Integer> getPMSpinnerValues() {
      SpinnerValueFactory<Integer> toReturn = new SpinnerValueFactory<>() {
        @Override
        public void decrement(int steps) {
          if (getValue().equals(12)) {
            setValue(1);
          } else {
            setValue(getValue() + 1);
          }
        }

        @Override
        public void increment(int steps) {
          if (getValue().equals(1)) {
            setValue(12);
          } else {
            setValue(getValue() - 1);
          }
        }
      };
      toReturn.setValue(12);
      return toReturn;
    }

    public static SpinnerValueFactory<Integer> getMinuteSpinnerValues() {
      SpinnerValueFactory<Integer> toReturn = new SpinnerValueFactory<Integer>() {
        @Override
        public void decrement(int steps) {
          if (getValue().equals(59)) {
            setValue(0);
          }
          else {
            setValue(getValue() + 1);
          }
        }

        @Override
        public void increment(int steps) {
          if (getValue().equals(0)) {
            setValue(59);
          }
          else {
            setValue(getValue() - 1);
          }
        }
      };
      toReturn.setValue(0);
      return toReturn;
    }
  }
}