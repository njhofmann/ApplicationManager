package datatransfer;

public class AreaDataImpl implements AreaData {

  private final String name;

  private final String desp;

  public AreaDataImpl(String name, String desp) {
    if (name.isEmpty()) {
      throw new IllegalArgumentException("Can't have an empty name!");
    }

    this.name = name;
    this.desp = desp;
  }

  @Override
  public int getAreaId() {
    return -1;
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
