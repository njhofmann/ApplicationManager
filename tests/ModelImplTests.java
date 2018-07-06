import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import datatransfer.AreaData;
import datatransfer.AreaDataImpl;
import model.ModelImpl;
import model.ModelInterface;

import static org.junit.Assert.assertEquals;

public class ModelImplTests {

  String filePath = "/home/nhofmann/Personal Java Projects/ApplicationManager/tests/XMLTestData.xml";
  ModelInterface model;
  AreaData areaData;

  private String xmlTestDataToString() {

    File file = new File(filePath);
    StringBuilder fileContents = new StringBuilder((int)file.length());
    Scanner scanner = null;
    try {
      scanner = new Scanner(file);
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    String lineSeparator = System.getProperty("line.separator");

    try {
      while(scanner.hasNextLine()) {
        fileContents.append(scanner.nextLine() + lineSeparator);
      }
      return fileContents.toString();
    } finally {
      scanner.close();
    }
  }

  private void resetXMLTestData() {
    try {
      File newXMLTestData = new File(filePath);
      String defaultText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
              "<root>\r\n" +
              "\r\n" +
              "</root>\r\n";
      Files.write(Paths.get(filePath), defaultText.getBytes());
    }
    catch (IOException e) {
      // pass
    }
  }

  @Before
  public void init() {
    model = new ModelImpl(filePath);
  }

  @After
  public void end() {
   resetXMLTestData();
  }

  // Invalid models...
  // ...null file path

  // ...empty file path

  // Invalid model method calls...

  // Adding a new area to a model
  @Test
  public void addNewArea() {
    assertEquals(xmlTestDataToString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<root />\n");

    areaData = new AreaDataImpl(-1, "chemistry", "high school chemistry class");
    model.addArea(areaData);

    assertEquals(xmlTestDataToString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<root>\n" +
            "  <area id=\"1\">\n" +
            "    <name>chemistry</name>\n" +
            "    <description>high school chemistry class</description>\n" +
            "  </area>\n" +
            "</root>\n");
  }
  // Editing an area that has already been added to a model
  @Test
  public void editPrevAddedArea() {
    assertEquals(xmlTestDataToString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<root />\n");

    areaData = new AreaDataImpl(-1, "chemistry", "high school chemistry class");
    model.addArea(areaData);
    assertEquals(xmlTestDataToString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<root>\n" +
            "  <area id=\"1\">\n" +
            "    <name>chemistry</name>\n" +
            "    <description>high school chemistry class</description>\n" +
            "  </area>\n" +
            "</root>\n");

    AreaData newAreaData = new AreaDataImpl(1, "history", "my chemistry class is now my history class");
    model.editArea(newAreaData);
    assertEquals(xmlTestDataToString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<root>\n" +
            "  <area id=\"1\">\n" +
            "    <name>history</name>\n" +
            "    <description>my chemistry class is now my history class</description>\n" +
            "  </area>\n" +
            "</root>\n");
  }
}
