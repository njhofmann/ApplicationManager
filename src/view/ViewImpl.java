package view;

import java.util.ArrayList;
import java.util.List;

import datatransfer.AreaData;
import datatransfer.AreaDataImpl;
import datatransfer.EventData;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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

  /**
   * ScrollPane displaying the data in each EventData in events.
   */
  private ScrollPane eventsDisplay;

  private final int totalWidth = 700;

  private final int totalHeight = 500;

  private final int areaWidth = 200;

  private final int eventWidth = totalWidth - areaWidth;

  public ViewImpl() {
  }

  @Override
  public void init() throws Exception {
    areasDisplay = new ScrollPane();
    eventsDisplay = new ScrollPane();
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

      Label despLabel = new Label("New Area Desp:");
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
    addNewEvent.setOnAction(event -> {
      if (displayID > 0) {

      }
    });
    addNewEvent.setMinWidth(addNewEventWidth);
    addNewEvent.setMinHeight(addNewEventHeight);

    Pane currentAreaInfo = new Pane();
    currentAreaInfo.setMinWidth(currentAreaInfoWidth);
    currentAreaInfo.setMinHeight(currentAreaInfoHeight);

    HBox eventHeader = new HBox();
    eventHeader.getChildren().addAll(currentAreaInfo, addNewEvent);

    eventsDisplay.setMinWidth(eventsDisplayWidth);
    eventsDisplay.setMinHeight(eventsDisplayHeight);

    eventVBox.setSpacing(0);
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

        int backgroundWidth = (int)(areaWidth - areasDisplay.getViewportBounds().getWidth());
        int backgroundHeight = 50;
        Button background = new Button();
        background.setMinHeight(backgroundHeight);
        background.setMinWidth(backgroundWidth);

        background.setOnAction(event -> {
          displayID = area.getAreaId();
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

        int backgroundWidth = (int)(eventWidth);
        int backgroundHeight = 50;
        System.out.print(backgroundWidth);
        Rectangle background = new Rectangle(backgroundWidth, backgroundHeight, Color.AZURE);

        Text name = new Text("Event Name: " + event.getEventName());
        Text desp = new Text("Event Desp: " + event.getEventDescription());
        Text location = new Text("Event Location: " + event.getEventLocation());

        Text dateAndTime = new Text();

        HBox text = new HBox();
        text.getChildren().addAll(name, desp, location);

        StackPane stack = new StackPane();
        stack.getChildren().addAll(background, text);

        internalVBox.getChildren().add(stack);
      }
      eventsDisplay.setContent(internalVBox);
    }
  }

  @Override
  public AreaData outputAreaData() {
    return null;
  }

  @Override
  public EventData outputEventData() {
    return null;
  }

}
