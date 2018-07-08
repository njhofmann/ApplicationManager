package datatransfer;

/**
 * Represents the data of some "Area of interest" being sent from a view to a model to process into
 * this ApplicationManager's data model, or from a model to view to be displayed to a user.
 */
public interface AreaData {
  /**
   * Returns this AreaData's associated id number. If zero then the area this AreaData
   * represents is brand new and is being added to the data model for the first time, otherwise
   * it is a unique id representing some specific area already added to the data model - this
   * AreaData simply contains data to update the associated area with.
   * @return associated area's unique id
   */
  int getAreaId();

  /**
   * Returns the latest name of this AreaData's associated area.
   * @return associated area's latest name
   */
  String getAreaName();

  /**
   * Returns the latest description of this AreaData's associated area.
   * @return associated area's latest description
   */
  String getAreaDescription();
}
