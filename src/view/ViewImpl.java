package view;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import datatransfer.AreaData;
import datatransfer.EventData;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 */
public class ViewImpl extends Application implements ViewInterface {

  /**
   * The AreaDatas whose data s currently being displayed. Static variable since when launching an
   * application, a new instance of that application is created, must be static so <i>any</i>
   * ViewImpl instance can access the areas that need to be displayed.
   */
  private static List<AreaData> areas = new ArrayList<>();

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
    
    // Width and height values
    int totalWidth = 800;
    int totalHeight = 500;
    
    int areaWidth = 300;
    int eventWidth = totalWidth - areaWidth;

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

    areasDisplay.setMinWidth(areasDisplayWidth);
    areasDisplay.setMinHeight(areasDisplayHeight);
    receiveAreas(areas);

    VBox internalVBox = new VBox();
    internalVBox.setSpacing(0);
    internalVBox.setMinWidth(300);

    areaVBox.setSpacing(0);
    areaVBox.getChildren().addAll(addNewArea, areasDisplay);
    areaVBox.setMinWidth(areaWidth);
    areaVBox.setMinHeight(totalHeight);

    // Event part of the root
    VBox eventVBox = new VBox();
    Button addNewEvent = new Button("Add New Event");
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
  public void start() {
    launch();
  }

  @Override
  public void end() {

  }

  @Override
  public void update() {

  }

  @Override
  public void addActionListener(ActionListener actionListener) {

  }

  @Override
  public void receiveAreas(List<AreaData> areas) {
    if (areas == null) {
      throw new IllegalArgumentException("Given list of AreaDatas can't be null!");
    }

    this.areas = areas;

    if (areasDisplay != null) {
      VBox internalVBox = new VBox();
      internalVBox.setSpacing(10);
      internalVBox.setMinWidth(300);

      for (AreaData area : this.areas) {

        Text areaName = new Text(area.getAreaName());
        Rectangle rectangle = new Rectangle(300, 100, Color.GHOSTWHITE);
        StackPane toAdd = new StackPane();
        toAdd.getChildren().addAll(rectangle, areaName);
        toAdd.setMinWidth(300);
        toAdd.setMinHeight(100);

        internalVBox.getChildren().add(toAdd);
        System.out.print(area.getAreaName());
      }
      areasDisplay.setContent(internalVBox);
    }
  }

  @Override
  public void receiveEvents(List<EventData> events) {

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
