package controller.eventhandlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;

/**
 * Custom event handler to deal with time conventions on a spinner for AM and PM times.
 */
public class TimeConventionEventHandler implements EventHandler<ActionEvent> {

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
