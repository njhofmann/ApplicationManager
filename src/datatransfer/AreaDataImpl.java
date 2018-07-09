package datatransfer;

/**
 * An implementation of the AreaData interface, simply stores the ID, name, and description of
 * some "Area of interest".
 */
public class AreaDataImpl implements AreaData {

  /**
   * This ID of the Area this AreaDataImpl represents.
   */
  private final int id;

  /**
   * This name of the Area this AreaDataImpl represents.
   */
  private final String name;

  /**
   * This desp of the Area this AreaDataImpl represents.
   */
  private final String desp;

  /**
   * Default constructor for this AreaDataImpl, takes in the ID, name, and description of some
   * Area to represent. Name must be non-null and non-empty, given ID must be natural number, and
   * given desp can't be null.
   * @param id ID of represented Area
   * @param name name of represented Area
   * @param desp description of represented Area
   * @throws IllegalArgumentException if given ID is not a natural number, if given name is null, or
   *         if given description is null.
   */
  public AreaDataImpl(int id, String name, String desp) {
    if (id < 0) {
      throw new IllegalArgumentException("Given ID must be a natural number!");
    }
    else if (name == null) {
      throw new IllegalArgumentException("Given name can't be null!");
    }
    else if (desp == null) {
      throw new IllegalArgumentException("Given description can't be null!");
    }

    this.id = id;
    this.name = name;
    this.desp = desp;
  }

  @Override
  public int getAreaId() {
    return id;
  }

  @Override
  public String getAreaName() {
    return name;
  }

  @Override
  public String getAreaDescription() {
    return desp;
  }
}
