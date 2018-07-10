package controller;

import model.ModelInterface;
import view.ViewInterface;

public class ControllerImpl implements ControllerInterface {

  /**
   * The Model this Controller is associated with, sends its data to the Controller's corresponding
   * View to be displayed
   */
  private final ModelInterface model;

  /**
   * The View this Controller is associated with, displays all the data sent to it from the
   * Controller's associated Model.
   */
  private final ViewInterface view;

  public ControllerImpl(ModelInterface model, ViewInterface view) {
    if (model == null || view == null) {
      throw new IllegalArgumentException("Input model and view to the controller can't be null!");
    }
    this.model = model;
    this.view = view;
  }

  @Override
  public void run() {
    view.start();
  }
}
