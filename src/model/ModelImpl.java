package model;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import datatransfer.AreaData;
import datatransfer.EventData;

/**
 * An implementation of the ModelInterface for a ApplicationManager. This implementation is
 * associated with a specific XML file - XMLData.xml - to hold all the model data for this AM. Adds
 * new data, edits old data, deletes old data, etc. by directly interacting with that XML file.
 */
public class ModelImpl implements ModelInterface {

  /**
   * File
   */
  private final String modelDataFilePath;

  /**
   * Latest version of this ModelImpl's model data as a readable document.
   */
  private Document modelData;

  /**
   * Default constructor for this ModelImpl, takes in a Writer to display the results of any method
   * calls on this MI / changes to this MI's model data.
   * @param modelDataFilePath file path of the model data this ModelImpl is suppose to read from and
   *                          change as needed
   * @throws IllegalArgumentException if given input file path is null or empty
   */
  public ModelImpl(String modelDataFilePath) {
    if (modelDataFilePath == null || modelDataFilePath.isEmpty()) {
      throw new IllegalArgumentException("Given input file path can't be null or empty!");
    }

    this.modelDataFilePath = modelDataFilePath;
    updateModelData();
    writeToWriter();
  }

  /**
   * Updates this's ModelImpl's modelData by rereading from the associated inputFilePath.
   */
  private void updateModelData() {
    try{
      // Open associated XML data file for parsing.
      File xmlFile = new File(modelDataFilePath);
      SAXBuilder saxBuilder = new SAXBuilder();
      modelData = saxBuilder.build(xmlFile);
    }
    catch (JDOMException e) {
      throw new IllegalStateException("Failed to read associated XML file!");
    }
    catch (IOException e) {
      throw new IllegalStateException("Failed to read associated XML file!");
    }
  }

  private void writeToWriter() {
    try {
      XMLOutputter xmlOutputter = new XMLOutputter();
      xmlOutputter.setFormat(Format.getPrettyFormat());
      xmlOutputter.output(modelData, new FileWriter(modelDataFilePath));
    }
    catch (IOException e) {
      // pass
    }
  }

  /**
   * Checks if a given AreaData is valid, i.e. it is non-null and its name is not empty.
   * @param data AreaData to check
   */
  private void validAreaData(AreaData data) {
    if (data == null) {
      throw new IllegalArgumentException("Given data can't be null");
    }
    else if (data.getAreaName().isEmpty()){
      throw new IllegalArgumentException("Given data must have a non-empty name!");
    }
  }

  @Override
  public void addArea(AreaData data) throws IllegalArgumentException {

    validAreaData(data);

    if (data.getAreaId() >= 0) {
      throw new IllegalArgumentException("Given data's id must be negative to signal it is a new area!");
    }

    updateModelData();

    // Get the root child of the XML file and all its inner children.
    Element root = modelData.getRootElement();
    List<Element> areas = root.getChildren();


    // Get data from input data class.
    int newAreaID = data.getAreaId();
    String newAreaName = data.getAreaName();
    String newAreaDesp = data.getAreaDescription();

    for (Element area : areas) {
      if (area.getAttributeValue("id").equals(newAreaID)) {
        throw new IllegalArgumentException("An element with this id has already been added to the model!");
      }
    }

    // Create a new area element to add to the model from input data.
    Element newArea = new Element("area");
    newArea.setAttribute("id", Integer.toString(areas.size() + 1));

    Element areaElementName = new Element("name");
    areaElementName.setText(newAreaName);
    newArea.addContent(areaElementName);

    Element areaElementDesp = new Element("description");
    areaElementDesp.setText(newAreaDesp);
    newArea.addContent(areaElementDesp);

    // Add new area element to model data
    root.addContent(newArea);
    writeToWriter();
  }

  @Override
  public void editArea(AreaData data) throws IllegalArgumentException {
    validAreaData(data);

    if (data.getAreaId() < 0) {
      throw new IllegalArgumentException("Can't edit a new area!");
    }

    // Open associated XML data file for parsing.
    updateModelData();

    // Get the root child of the XML file and all its inner children.
    Element root = modelData.getRootElement();
    List<Element> areas = root.getChildren();

    // Get data from input data class.
    int editAreaId = data.getAreaId();
    String editAreaName = data.getAreaName();
    String editAreaDesp = data.getAreaDescription();

    // Variable
    Element areaToEdit = new Element("blank");

    for (Element area : areas) {
      if (area.getAttributeValue("id").equals(Integer.toString(editAreaId))) {
        areaToEdit = area;
        break;
      }
    }

    if (areaToEdit.getName().equals("blank")) {
      throw new IllegalArgumentException("No area with the given id has been added to this model data!");
    }

    // Updates the found area with a its new name and description
    Element areaToEditName = areaToEdit.getChild("name");
    areaToEditName.setText(editAreaName);

    Element areaToEditDesp = areaToEdit.getChild("description");
    areaToEditDesp.setText(editAreaDesp);

    writeToWriter();
  }

  @Override
  public void addEvent(EventData data) throws IllegalArgumentException {

  }

  @Override
  public void editEvent(EventData data) throws IllegalArgumentException {

  }
}
