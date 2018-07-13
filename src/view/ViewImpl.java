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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
   * The ID of the Area whose events are currently being displayed.
   */
  private int displayID = 0;

  /**
   * The EventDatas whose data is currently being display, are the Events of the AreaData whose ID
   * is equal to the display ID.
   */
  private List<EventData> events = new ArrayList<>();

  /**
   * ScrollPane displaying the data in each AreaData in areas.
   */
  private ScrollPane areasDisplay;

  private ScrollPane currentAreaInfo;

  /**
   * ScrollPane displaying the data in each EventData in events.
   */
  private ScrollPane eventsDisplay;

  private final int totalWidth = 700;

  private final int totalHeight = 500;

  private final int areaWidth = 200;

  private final int eventWidth = totalWidth - areaWidth;

  @Override
  public void init() throws Exception {
    areasDisplay = new ScrollPane();
    eventsDisplay = new ScrollPane();
    currentAreaInfo = new ScrollPane();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    BorderPane root = new BorderPane();

    int newAreaButtonWidth = areaWidth;
    int newAreaButtonHeight = 40;
    int areasDisplayWidth = areaWidth;
    int areasDisplayHeight = totalHeight - newAreaButtonHeight;

    int addNewEventWidth = 150;
    int addNewEventHeight = 100;
    int currentAreaInfoWidth = eventWidth - addNewEventWidth;
    int currentAreaInfoHeight = addNewEventHeight;

    int eventsDisplayWidth = eventWidth;
    int eventsDisplayHeight = totalHeight - addNewEventHeight;

    // Area part of the root
    VBox areaVBox = new VBox();

    Button addNewArea = new Button("Add New Area");
    addNewArea.setMinWidth(newAreaButtonWidth);
    addNewArea.setMinHeight(newAreaButtonHeight);

    addNewArea.setOnAction(event -> {
      GridPane newAreaWindowRoot = new GridPane();

      Label nameLabel = new Label("New Area Name:");
      newAreaWindowRoot.add(nameLabel, 0, 0);

      Label despLabel = new Label("New Area Description:");
      newAreaWindowRoot.add(despLabel, 0, 1);

      TextField nameField = new TextField();
      newAreaWindowRoot.add(nameField, 1, 0);

      TextField despField = new TextField();
      newAreaWindowRoot.add(despField, 1, 1);

      Button submit = new Button("Submit");
      newAreaWindowRoot.add(submit, 2, 0);

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

      cancel.setOnAction(event1 -> {
        addAreaWindow.close();
      });

      addAreaWindow.show();
    });

    areasDisplay.setMinWidth(areasDisplayWidth);
    areasDisplay.setMinHeight(areasDisplayHeight);
    receiveAreas(model.outputAreas());

    areaVBox.getChildren().addAll(addNewArea, areasDisplay);
    areaVBox.setMinWidth(areaWidth);
    areaVBox.setMinHeight(totalHeight);

    // Event part of the root
    VBox eventVBox = new VBox();

    Button addNewEvent = new Button("Add New Event");
    addNewEvent.setMinWidth(addNewEventWidth);

    addNewEvent.setOnAction(event -> {
      if (displayID > 0) {
        GridPane windowRoot = new GridPane();

        Label nameLabel = new Label("New Event Name:");
        windowRoot.add(nameLabel, 0, 0);

        Label despLabel = new Label("New Event Description:");
        windowRoot.add(despLabel, 0, 1);

        Label locationLabel = new Label("New Event Location:");
        windowRoot.add(locationLabel, 0, 2);

        TextField nameField = new TextField();
        windowRoot.add(nameField, 1, 0);

        TextField despField = new TextField();
        windowRoot.add(despField, 1, 1);

        TextField locationField = new TextField();
        windowRoot.add(locationField, 1, 2);

        Button submit = new Button("Submit");
        windowRoot.add(submit, 2, 0);

        Button cancel = new Button("Cancel");
        windowRoot.add(cancel, 2, 1);

        Scene windowScene = new Scene(windowRoot);
        Stage addEventWindow = new Stage();
        addEventWindow.setScene(windowScene);
        addEventWindow.setTitle("Add New Event");
        addEventWindow.initModality(Modality.APPLICATION_MODAL);

        submit.setOnAction(event12 -> {
          addEventWindow.close();
          EventData toSend = new EventDataImpl(displayID, 0, nameField.getText(), despField.getText(), locationField.getText(), new int[]{2017, 5, 23, 5, 5, 0});
          model.addEvent(toSend);
          receiveEvents(model.outputEvents(new AreaDataImpl(displayID, "", "")));
        });

        cancel.setOnAction(event1 -> {
          addEventWindow.close();
        });

        addEventWindow.show();
      }
    });

    Button editArea = new Button("Edit Area");
    editArea.setMinWidth(addNewEventWidth);

    editArea.setOnAction(event -> {
      if (displayID > 0) {
        AreaData toEdit = areas.get(displayID - 1);

        GridPane newAreaWindowRoot = new GridPane();

        Label nameLabel = new Label("Edit Area Name:");
        newAreaWindowRoot.add(nameLabel, 0, 0);

        Label despLabel = new Label("Edit Area Description:");
        newAreaWindowRoot.add(despLabel, 0, 1);

        TextField nameField = new TextField(toEdit.getAreaName());
        newAreaWindowRoot.add(nameField, 1, 0);

        TextField despField = new TextField(toEdit.getAreaDescription());
        newAreaWindowRoot.add(despField, 1, 1);

        Button submit = new Button("Submit");
        newAreaWindowRoot.add(submit, 2, 0);

        Button cancel = new Button("Cancel");
        newAreaWindowRoot.add(cancel, 2, 1);

        Scene newAreaWindowScene = new Scene(newAreaWindowRoot);
        Stage addAreaWindow = new Stage();
        addAreaWindow.setScene(newAreaWindowScene);
        addAreaWindow.setTitle("Edit Currnet Area");
        addAreaWindow.initModality(Modality.APPLICATION_MODAL);

        submit.setOnAction(event12 -> {
          addAreaWindow.close();
          AreaData toSend = new AreaDataImpl(toEdit.getAreaId(), nameField.getText(), despField.getText());
          model.editArea(toSend);
          receiveAreas(model.outputAreas());
        });

        cancel.setOnAction(event1 -> {
          addAreaWindow.close();
        });

        addAreaWindow.show();
      }
    });

    Button deleteArea = new Button("Delete Area");
    deleteArea.setMinWidth(addNewEventWidth);

    deleteArea.setOnAction(event -> {
      if (displayID > 0) {
        Label check = new Label("Are you sure you want to delete the current Area?");
        Button delete = new Button("Delete");
        Button cancel = new Button("Cancel");

        HBox buttons = new HBox(delete, cancel);
        VBox windowRoot = new VBox(check, buttons);

        Scene windowScene = new Scene(windowRoot);
        Stage window = new Stage();
        window.setScene(windowScene);

        delete.setOnAction(event1 -> {
          AreaData toSend = new AreaDataImpl(displayID, "", "");
          displayID = 0;
          model.deleteArea(toSend);
          receiveAreas(model.outputAreas());
          window.close();
        });

        cancel.setOnAction(event1 -> {
          window.close();
        });

        window.setTitle("Delete Area");
        window.initModality(Modality.APPLICATION_MODAL);
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

    root.setLeft(areaVBox);
    root.setRight(eventVBox);

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

    if (areasDisplay != null) {
      VBox internalVBox = new VBox();
      internalVBox.setSpacing(5);

      for (AreaData area : this.areas) {
        Text areaName = new Text(area.getAreaName());

        int backgroundWidth = areaWidth;
        int backgroundHeight = 50;
        Button background = new Button();
        background.setMinHeight(backgroundHeight);
        background.setMinWidth(backgroundWidth);

        background.setOnAction(event -> {
          displayID = area.getAreaId();

          Text name = new Text("Name: " + area.getAreaName());
          Text desp = new Text("Description: " + area.getAreaDescription());
          VBox text = new VBox(name, desp);
          currentAreaInfo.setContent(text);

          AreaData areaID = new AreaDataImpl(displayID, "", "");
          List<EventData> eventsToDisplay = model.outputEvents(areaID);
          receiveEvents(eventsToDisplay);
        });

        StackPane toAdd = new StackPane();
        toAdd.getChildren().addAll(background, areaName);

        internalVBox.getChildren().add(toAdd);
      }
      areasDisplay.setContent(internalVBox);
    }
  }

  @Override
  public void receiveEvents(List<EventData> events) {
    if (events == null) {
      throw new IllegalArgumentException("Given list of EventDatas can't be null!");
    }

    this.events = events;

    if (events != null) {
      VBox internalVBox = new VBox();
      internalVBox.setSpacing(5);

      for (EventData event : this.events) {
        if (event.getAssociatedAreaId() != displayID) {
          throw new IllegalArgumentException("Can't display Events not associated with the currently selected Area!");
        }

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

        HBox buttons = new HBox(editEvent, deleteEvent);
        VBox items = new VBox(name, desp, location, date, time, buttons);

        int backgroundWidth = eventWidth;
        int backgroundHeight = 110;
        Rectangle background = new Rectangle(backgroundWidth, backgroundHeight, Color.GOLD);

        StackPane stack = new StackPane();
        stack.getChildren().addAll(background, items);
        stack.setAlignment(Pos.CENTER_LEFT);

        internalVBox.getChildren().add(stack);
      }
      eventsDisplay.setContent(internalVBox);
    }
  }
}