package controller;

import controller.eventhandlers.TimeConventionEventHandler;
import controller.eventhandlers.TimeSpinnerValueFactory;
import datatransfer.AreaData;
import datatransfer.AreaDataImpl;
import datatransfer.EventData;
import datatransfer.EventDataImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ModelInterface;
import view.ViewInterface;

/**
 * Controller to handle the interfacing between a given View and Model.
 */
public class Controller {

  /**
   * The specific View this Controller is given to work with.
   */
  private final ViewInterface view;

  /**
   * The specific Model this Controller is given to work with.
   */
  private final ModelInterface model;

  /**
   * Constant padding value for borders of event handler windows.
   */
  private final int paddingValue;

  /**
   * Constructs this Controller from a given View and Model to work with and provide interface
   * between.
   * @param view view to display data on and receive data from
   * @param model model to draw data from and save data to
   * @throws IllegalArgumentException if either given Model or View are null
   */
  public Controller(ViewInterface view, ModelInterface model) {
    if (view == null) {
      throw new IllegalArgumentException("Given view must be non-null!");
    }
    else if (model == null) {
      throw new IllegalArgumentException("Given model must be non-null!");
    }

    this.model = model;
    this.view = view;
    paddingValue = 5;
    assignMappings();
    view.receiveAreas(model.outputAreas());
  }


  /**
   * Assigns a mapping of String to Events to the {@code View} this Controller is working with.
   */
  private void assignMappings() {
    Map<ButtonEvents, EventHandler<ActionEvent>> mapping = new HashMap<>();
    mapping.put(ButtonEvents.NEW_AREA, new AddNewArea());
    mapping.put(ButtonEvents.NEW_EVENT, new AddNewEvent());
    mapping.put(ButtonEvents.EDIT_AREA, new EditArea());
    mapping.put(ButtonEvents.DELETE_AREA, new DeleteArea());
    mapping.put(ButtonEvents.EDIT_EVENT, new EditEvent());
    mapping.put(ButtonEvents.DELETE_EVENT, new DeleteEvent());
    mapping.put(ButtonEvents.DISPLAY, new DisplayEvent());
    view.assignButtonEvents(mapping);
  }

  /**
   * Action event handling the adding of a new Area to the Model.
   */
  public class AddNewArea implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent actionEvent) {
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
        view.receiveAreas(model.outputAreas());
      });

      cancel.setOnAction(event1 -> addAreaWindow.close());

      addAreaWindow.show();
    }
  }

  /**
   * Action event handling the adding of a new Event to the Model, to an Area selected in the View.
   */
  class AddNewEvent implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent actionEvent) {
      // Don't add a new Event unless an Area is actually being displayed.
      if (view.getSelectedAreaID() > 0) {
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
          int hour = (int)hours.getValue();
          int minute = (int)mins.getValue();

          if (timeConvention.getText().equals("PM")) {
            hour += 12;
          }

          LocalDateTime dateTime = dateInfo.atTime(hour, minute);

          EventData toSend = new EventDataImpl(view.getSelectedAreaID(), 0, nameField.getText(),
              despField.getText(), locationField.getText(), dateTime);
          model.addEvent(toSend);
          view.receiveEvents(model.outputEvents(view.getSelectedAreaID()));
        });

        cancel.setOnAction(event1 -> {
          addEventWindow.close();
        });

        // Display the window.
        addEventWindow.show();
      }
    }
  }

  /**
   * Action event handling the editing of an existing Event in the model, as per selected in
   * the View.
   */
  class EditArea implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent actionEvent) {
      // Can't edit an Area unless an Area is actually being displayed / is selected.
      if (view.getSelectedAreaID() > 0) {
        AreaData toEdit = model.getAreaData(view.getSelectedAreaID());

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
          view.resetAreaDisplay();
          view.receiveAreas(model.outputAreas());
        });

        cancel.setOnAction(event1 -> {
          // Closes the window.
          addAreaWindow.close();
        });

        // Displays the window.
        addAreaWindow.show();
      }
    }
  }

  /**
   * Action event handling the deletion of an Area from the Model, as per selected in the View.
   */
  class DeleteArea implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent actionEvent) {
      // Don't delete the current Area unless an Area is actually being displayed.
      if (view.getSelectedAreaID() > 0) {
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
          model.deleteArea(view.getSelectedAreaID());
          view.resetAreaDisplay();
          view.receiveAreas(model.outputAreas());
        });

        cancel.setOnAction(event1 -> {
          window.close();
        });

        // Display the window.
        window.show();
      }
    }
  }

  /**
   * Action event handling the editing of an Event in the Model, as per selected in the View.
   */
  class EditEvent implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent actionEvent) {
      EventData toEdit = model.getEventData(view.getSelectedAreaID(), view.getSelectedEventID());

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
      LocalDateTime dateTimeInfo = toEdit.getEventDateAndTime();
      DatePicker datePicker = new DatePicker(dateTimeInfo.toLocalDate());
      windowRoot.add(datePicker, 1, 3);

      Label editTimeLabel = new Label("New Event Time: ");
      windowRoot.add(editTimeLabel, 0, 4, 2, 1);
      editTimeLabel.setPadding(new Insets(paddingValue, 0, paddingValue, 0));
      GridPane.setHalignment(editTimeLabel, HPos.CENTER);

      Label hoursLabel = new Label("Hour: ");
      windowRoot.add(hoursLabel, 0, 5);
      GridPane.setHalignment(hoursLabel, HPos.RIGHT);

      Spinner hours = new Spinner();
      windowRoot.add(hours, 1, 5);

      if (toEdit.getTimeConvention().equals("AM")) {
        hours.setValueFactory(TimeSpinnerValueFactory.getAMSpinnerValues());
      }
      else {
        hours.setValueFactory(TimeSpinnerValueFactory.getPMSpinnerValues());
      }

      Label minutesLabel = new Label("Minutes: ");
      windowRoot.add(minutesLabel, 0, 6);
      GridPane.setHalignment(minutesLabel, HPos.RIGHT);

      Spinner mins = new Spinner(TimeSpinnerValueFactory.getMinuteSpinnerValues());
      mins.getValueFactory().setValue(dateTimeInfo.getMinute());
      windowRoot.add(mins, 1, 6);

      Label timeConventionLabel = new Label("Time Convention: ");
      windowRoot.add(timeConventionLabel, 0, 7);
      GridPane.setHalignment(timeConventionLabel, HPos.RIGHT);

      Button editTimeConvention = new Button(toEdit.getTimeConvention());
      windowRoot.add(editTimeConvention, 1, 7);

      TimeConventionEventHandler timeConventionEventHandler =
          new TimeConventionEventHandler(editTimeConvention, hours);
      hours.getValueFactory().setValue(toEdit.getFinalHour());
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
        int newHour = (int)hours.getValue();
        int newMinute = (int)mins.getValue();

        if (toEdit.getTimeConvention().equals("PM") && 1 <= newHour && newHour <= 11) {
          newHour += 12;
        }

        LocalDateTime dateTime = dateInfo.atTime(newHour, newMinute);

        EventData toSend = new EventDataImpl(view.getSelectedAreaID(), toEdit.getEventId(),
            nameField.getText(), despField.getText(), locationField.getText(), dateTime);
        model.editEvent(toSend);
        view.receiveEvents(model.outputEvents(view.getSelectedAreaID()));
      });

      cancel.setOnAction(event13 -> {
        window.close();
      });

      window.show();
    }
  }

  /**
   * Action event handling the deletion of an Event in the Model, as per selected in the View.
   */
  class DeleteEvent implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent actionEvent) {
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
        model.deleteEvent(view.getSelectedAreaID(), view.getSelectedEventID());
        view.receiveEvents(model.outputEvents(view.getSelectedAreaID()));
        window.close();
      });

      cancel.setOnAction(event3 -> {
        window.close();
      });

      window.setTitle("Delete Event");
      window.initModality(Modality.APPLICATION_MODAL);
      window.show();
    }
  }

  /**
   * Action event handling the displaying of Events associated with a selected Area, as per selected
   * in the View.
   */
  class DisplayEvent implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent actionEvent) {
      int currentAreaID = view.getSelectedAreaID();

      // Send a request to the model for Event's associated with the Area that was just clicked.
      List<EventData> eventsToDisplay = model.outputEvents(currentAreaID);

      // Display the received events.
      view.receiveEvents(eventsToDisplay);
    }
  }
}
