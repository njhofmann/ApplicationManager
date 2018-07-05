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
  @Override
  public void addArea(AreaData data) throws IllegalArgumentException {
    if (data == null) {
      throw new IllegalArgumentException("Given data can't be null");
    }
    else if (data.getAreaName().isEmpty()){
      throw new IllegalArgumentException("Given data must have a non-empty name!");
    }
    else if (data.getAreaId() >= 0) {
      throw new IllegalArgumentException("Given data's id must be negative to signal it is a new area!");
    }

    // Get data from input data class.
    int newAreaID = data.getAreaId();
    String newAreaName = data.getAreaName();
    String newAreaDesp = data.getAreaDescription();

    try{
      // Open associated XML data file for parsing.
      File xmlFile = new File("/home/nhofmann/Personal Java Projects/src/model/XMLData.xml");
      SAXBuilder saxBuilder = new SAXBuilder();
      Document xmlDoc = saxBuilder.build(xmlFile);

      // Get the root child of the XML file and all its inner children.
      Element root = xmlDoc.getRootElement();
      List<Element> areas = root.getChildren();

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

      XMLOutputter xmlOutputter = new XMLOutputter();
      xmlOutputter.setFormat(Format.getPrettyFormat());
      xmlOutputter.output(xmlDoc, new FileWriter("/home/nhofmann/Personal Java Projects/src/model/XMLData.xml"));
    }
    catch (JDOMException e) {
      throw new IllegalStateException("Failed to read associated XML file!");
    }
    catch (IOException e) {
      throw new IllegalStateException("Failed to read associated XML file!");
    }
  }

  @Override
  public void editArea(AreaData data) throws IllegalArgumentException {

  }

  @Override
  public void addEvent(EventData data) throws IllegalArgumentException {

  }

  @Override
  public void editEvent(EventData data) throws IllegalArgumentException {

  }
}
