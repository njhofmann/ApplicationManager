package datatransfer;

public class AreaDataImpl implements AreaData {

  private final int id;

  private final String name;

  private final String desp;

  public AreaDataImpl(int id, String name, String desp) {
    if (name.isEmpty()) {
      throw new IllegalArgumentException("Can't have an empty name!");
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
