package controller.eventhandlers;

import javafx.scene.control.SpinnerValueFactory;

/**
 * Custom spinner to deal with moving between different hours of AM and PM, and minutes in an
 * hour.
 */
public class TimeSpinnerValueFactory {
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

  /**
   * Custom spinner to deal with moving between the hours of 1 to 12 for PM times.
   */
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

  /**
   * Custom spinner to deal with moving between the minutes of 0 to 59.
   */
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