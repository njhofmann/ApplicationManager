import org.junit.Before;
import org.junit.Test;

import java.io.File;

import datatransfer.AreaData;
import datatransfer.AreaDataImpl;
import model.ModelImpl;
import model.ModelInterface;

import static org.junit.Assert.assertEquals;

public class ModelImplTests {

  String filePath = new File(".").getAbsolutePath() + "/tests/XMLTestData.xml";
  ModelInterface model;
  AreaData areaData = new AreaDataImpl(0, "test", "testing");

  @Before
  public void init() {
    model = new ModelImpl(filePath);
  }

  // Invalid models...
  // ...null filepath
  @Test(expected = IllegalArgumentException.class)
  public void nullFilePath() {
    model = new ModelImpl(null);
  }

  // ...empty filepath
  @Test(expected = IllegalArgumentException.class)
  public void emptyFilePath() {
    model = new ModelImpl("");
  }

  // ...invalid filepath
  @Test(expected = IllegalArgumentException.class)
  public void invalidFilePath() {
    model = new ModelImpl("src/controller/XMLData.xml");
  }

  // Invalid adding AreaDatas operations...
  // ...adding null AreaData
  @Test(expected = IllegalArgumentException.class)
  public void addingNullAreaData() {
    model.addArea(null);
  }

  // ...adding AreaData with negative ID
  // ...adding AreaData with positive ID
  @Test(expected = IllegalArgumentException.class)
  public void addingAreaDataWithPostitiveID() {
    areaData = new AreaDataImpl(1, "test", "testing");
    model.addArea(areaData);
  }

  // Valid adding AreaData operations...
  // ...adding one AreaData with no description
  @Test
  public void addingAreaDataNoDesp() {
    assertEquals(model.outputModelDataAsString(),
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root />\r\n");
    areaData = new AreaDataImpl(0, "test", "");
    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description></description></area></root>\r\n");
  }

  // ...adding one AreaData with a description
  @Test
  public void addingAreaDataWithDesp() {
    assertEquals(model.outputModelDataAsString(),
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                    "<root />\r\n");

    areaData = new AreaDataImpl(0, "test", "testing");
    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");
  }

  // ...adding same AreaData multiple times
  @Test
  public void addingSameAreaData() {
    assertEquals(model.outputModelDataAsString(),
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                    "<root />\r\n");

    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");

    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "<area id=\"2\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");
  }

  // ...adding different AreaData
  @Test
  public void addingDifferentAreaDatas() {
    assertEquals(model.outputModelDataAsString(),
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                    "<root />\r\n");

    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(0, "banana", "fruit");
    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "<area id=\"2\"><name>banana</name><description>fruit</description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(0, "pen", "");
    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "<area id=\"2\"><name>banana</name><description>fruit</description></area>" +
            "<area id=\"3\"><name>pen</name><description></description></area>" +
            "</root>\r\n");
  }


  // Invalid editing AreaData operations...
  // ...trying to edit with a null AreaData
  @Test(expected = IllegalArgumentException.class)
  public void editingWithNullAreaData() {
    model.editArea(null);
  }

  // ...trying to edit with a AreaData whose ID is zero
  @Test(expected = IllegalArgumentException.class)
  public void editingWithAreaDataWithPosID() {
    areaData = new AreaDataImpl(0, "test", "testing");
    model.editArea(areaData);
  }

  // ...trying to edit an AreaData who hasn't been added to the model
  @Test(expected = IllegalArgumentException.class)
  public void editingUnaddedAreaData() {
    areaData = new AreaDataImpl(3, "test", "testing");
    model.editArea(areaData);
  }

  // Valid editing AreaData operations...
  // ...editing one AreaData
  @Test
  public void editingOneAreaData() {
    assertEquals(model.outputModelDataAsString(),
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                    "<root />\r\n");

    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(1, "changed", "changed");
    model.editArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                    "<root><area id=\"1\"><name>changed</name><description>changed" +
            "</description></area></root>\r\n");
  }

  // ...editing same AreaDatas
  @Test
  public void editingSameAreaDatas() {
    assertEquals(model.outputModelDataAsString(),
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                    "<root />\r\n");

    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");

    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "<area id=\"2\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(1, "pen", "");
    model.editArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>pen</name><description></description></area>" +
            "<area id=\"2\"><name>test</name><description>testing</description></area></root>\r\n");

    areaData = new AreaDataImpl(2, "die", "hippie");
    model.editArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>pen</name><description></description></area>" +
            "<area id=\"2\"><name>die</name><description>hippie</description></area></root>\r\n");
  }

  // ...editing different AreaDatas
  @Test
  public void editingMultipleDiffAreaDatas() {
    assertEquals(model.outputModelDataAsString(),
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                    "<root />\r\n");

    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(0, "banana", "fruit");
    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "<area id=\"2\"><name>banana</name><description>fruit</description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(0, "pen", "");
    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "<area id=\"2\"><name>banana</name><description>fruit</description></area>" +
            "<area id=\"3\"><name>pen</name><description></description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(1, "first", "");
    model.editArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>first</name><description></description></area>" +
            "<area id=\"2\"><name>banana</name><description>fruit</description></area>" +
            "<area id=\"3\"><name>pen</name><description></description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(2, "second", "");
    model.editArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>first</name><description></description></area>" +
            "<area id=\"2\"><name>second</name><description></description></area>" +
            "<area id=\"3\"><name>pen</name><description></description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(3, "third", "");
    model.editArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>first</name><description></description></area>" +
            "<area id=\"2\"><name>second</name><description></description></area>" +
            "<area id=\"3\"><name>third</name><description></description></area>" +
            "</root>\r\n");

  }

  // ...editing to induce no changes
  @Test
  public void noChangesFromEdit() {
    assertEquals(model.outputModelDataAsString(),
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                    "<root />\r\n");

    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(1, "changed", "changed");
    model.editArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>changed</name><description>changed" +
            "</description></area></root>\r\n");

    model.editArea(areaData);
    model.editArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>changed</name><description>changed" +
            "</description></area></root>\r\n");
  }

  // ...multiple edits on same Area
  @Test
  public void multipleEdits() {
    assertEquals(model.outputModelDataAsString(),
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                    "<root />\r\n");

    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(1, "changed", "changed");
    model.editArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>changed</name><description>changed" +
            "</description></area></root>\r\n");

    areaData = new AreaDataImpl(1, "again", "");
    model.editArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>again</name><description>" +
            "</description></area></root>\r\n");

    areaData = new AreaDataImpl(1, "finally", "over");
    model.editArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>finally</name><description>over" +
            "</description></area></root>\r\n");
  }

  // Invalid removing Area operations...
  // ...trying to remove an Area with a null AreaData
  @Test(expected = IllegalArgumentException.class)
  public void removeAreaWithNullAreaData() {
    model.deleteArea(null);
  }

  // ...trying to remove an Area that isn't apart of the model (ID of AreaData isn't in the model)
  @Test(expected = IllegalArgumentException.class)
  public void removeAreaWithUnaddedID() {
    model.addArea(areaData);
    areaData = new AreaDataImpl(2, "", "");
    model.deleteArea(areaData);
  }

  // ...trying to remove an Area with an AreaData whose ID is the dedicated ID for a new Area, zero
  @Test(expected = IllegalArgumentException.class)
  public void removeAreaWithAreaDataWithZeroID() {
    model.addArea(areaData);
    areaData = new AreaDataImpl(0, "", "");
    model.deleteArea(areaData);
  }

  // Valid removing Area operations...
  // ...removing one Area
  @Test
  public void removingOneArea() {
    assertEquals(model.outputModelDataAsString(),
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<root />\r\n");

    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(1, "", "");
    model.deleteArea(areaData);
    assertEquals(model.outputModelDataAsString(),
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<root />\r\n");
  }

  // ...removing one of same added Areas
  @Test
  public void removingOneOfSameAreas() {
    assertEquals(model.outputModelDataAsString(),
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<root />\r\n");

    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");

    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "<area id=\"2\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");

    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "<area id=\"2\"><name>test</name><description>testing</description></area>" +
            "<area id=\"3\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(2, "", "");
    model.deleteArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "<area id=\"2\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");
  }

  // ...removing one of several different added Areas
  // ...removing one of same added Areas
  @Test
  public void removingAllOfSameAreas() {
    assertEquals(model.outputModelDataAsString(),
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<root />\r\n");

    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");

    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "<area id=\"2\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");

    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "<area id=\"2\"><name>test</name><description>testing</description></area>" +
            "<area id=\"3\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(2, "", "");
    model.deleteArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "<area id=\"2\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(2, "", "");
    model.deleteArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(1, "", "");
    model.deleteArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root />\r\n");
  }

  // ...removing all of several different added Areas
  @Test
  public void removingMultipleDiffAreas() {
    assertEquals(model.outputModelDataAsString(),
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                    "<root />\r\n");

    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(0, "banana", "fruit");
    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "<area id=\"2\"><name>banana</name><description>fruit</description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(0, "pen", "");
    model.addArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "<area id=\"2\"><name>banana</name><description>fruit</description></area>" +
            "<area id=\"3\"><name>pen</name><description></description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(2, "", "");
    model.deleteArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>test</name><description>testing</description></area>" +
            "<area id=\"2\"><name>pen</name><description></description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(1, "", "");
    model.deleteArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root><area id=\"1\"><name>pen</name><description></description></area>" +
            "</root>\r\n");

    areaData = new AreaDataImpl(1, "", "");
    model.deleteArea(areaData);
    assertEquals(model.outputModelDataAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<root />\r\n");
  }

  // Invalid adding Event operations...
  // ...adding a null EventData
  // ...adding an EventData with a positive ID
  // ...adding an EventData whose AreaData has an ID of 0
  // ...adding a EventData to an AreaData that hasn't been added to this model
  //

}
