package view;

import java.awt.event.ActionListener;
import java.util.List;

import datatransfer.AreaData;
import datatransfer.EventData;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 */
public class ViewImpl extends Application implements ViewInterface {

  public ViewImpl() { }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Group root = new Group();
    Scene scene = new Scene(root, 500, 500);
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
