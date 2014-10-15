package map;

import enums.ObjectsType;
import help.utils.BlocksReader;
import help.utils.ObjectsReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

import static help.utils.HelpUtils.getElementByAttributeValue;
import static help.utils.HelpUtils.readBehaviors;

public class MapBuilder {

    DocumentBuilder dBuilder;
    ArrayList<Integer> goals;
    ArrayList<ArrayList<Field>> map;

    public MapBuilder() {

        map = new ArrayList<>();
        goals = new ArrayList<>();
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

        try {
            dBuilder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private Element getMapEle(int mapWorld, int mapNumber) {

        Document document = null;
        try {
            document = dBuilder.parse(Map.class.getResourceAsStream("/resources/maps" + mapWorld + ".xml"));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        document.normalize();
        NodeList rootNodes = document.getElementsByTagName("maps");
        Node rootNode = rootNodes.item(0);
        Element rootElement = (Element) rootNode;
        NodeList mapList = rootElement.getElementsByTagName("map");
        Node mapNode = help.utils.HelpUtils.getElementByAttributeValue(mapList, "number", Integer.toString(mapNumber));

        return (Element) mapNode;
    }

    public ArrayList<Integer> getGoals(Map map) {
        Element mapEle = getMapEle(map.getMapWorld(), map.getMapNumber());
        goals.clear();
        if (mapEle.hasAttribute("silver"))
            goals.add(Integer.parseInt(mapEle.getAttribute("silver")));

        if (mapEle.hasAttribute("gold"))
            goals.add(Integer.parseInt(mapEle.getAttribute("gold")));

        if (mapEle.hasAttribute("plat"))
            goals.add(Integer.parseInt(mapEle.getAttribute("plat")));
        return goals;
    }

    public ArrayList<ArrayList<Field>> buildMap(int mapWorld, int mapNumber) {

        Element mapBlueprint = getMapEle(mapWorld, mapNumber);
        NodeList blocksList = BlocksReader.getBlocksList();
        NodeList objectsList = ObjectsReader.getObjectsList();
        map.clear();

        NodeList rows = mapBlueprint.getElementsByTagName("row");
        String rowBlueprint = null;

        for (int i = 0; i < rows.getLength(); i++) {

            for (int k = 0; k < rows.getLength(); k++) {
                Element row = (Element) rows.item(k);
                if (Integer.parseInt(row.getAttribute("nr")) == rows.getLength() - i)
                    rowBlueprint = row.getTextContent();
            }

            ArrayList<Field> fieldsInARow = new ArrayList<>();
            int fieldX = 0;
            for (int j = 0; j < rowBlueprint.length(); j++) {
                String symbol = rowBlueprint.substring(j, j + 1);

                Element block = getElementByAttributeValue(blocksList, "representation", symbol);
                Element object = getElementByAttributeValue(objectsList, "representation", symbol);

                if (block != null) {
                    fieldsInARow.add(new Field(block.getAttribute("enum"), readBehaviors(block), fieldX, i));
                } else if (symbol.equals("[")) {
                    Element underBlock = getElementByAttributeValue(blocksList, "enum", "EMPTY");
                    fieldsInARow.add(new Field("EMPTY", readBehaviors(underBlock), fieldX, i));
                    while (!symbol.equals("]")) {
                        j++;
                        symbol = rowBlueprint.substring(j, j + 1);

                    }
                } else {
                    Element underBlock = getElementByAttributeValue(blocksList, "enum", object.getAttribute("placedOn"));
                    fieldsInARow.add(new Field(underBlock.getAttribute("enum"), readBehaviors(underBlock),
                            object.getAttribute("enum"), readBehaviors(object), fieldX, i));

                }
                fieldX++;

            }
            map.add(fieldsInARow);
        }
        return map;
    }

    public ArrayList<Object> loadObjects(int mapWorld, int mapNumber) {

        ArrayList<Object> objects = new ArrayList<>();

        Element mapBlueprint = getMapEle(mapWorld, mapNumber);
        NodeList objectsList = ObjectsReader.getObjectsList();

        NodeList rows = mapBlueprint.getElementsByTagName("row");
        String rowBlueprint = null;

        for (int i = 0; i < rows.getLength(); i++) {

            for (int k = 0; k < rows.getLength(); k++) {
                Element row = (Element) rows.item(k);
                if (Integer.parseInt(row.getAttribute("nr")) == rows.getLength() - i)
                    rowBlueprint = row.getTextContent();
            }

            int objX = 0;
            for (int j = 0; j < rowBlueprint.length(); j++) {
                String symbol = rowBlueprint.substring(j, j + 1);

                if (symbol.equals("[")) {
                    while (!symbol.equals("]")) {
                        j++;
                        symbol = rowBlueprint.substring(j, j + 1);

                        Element object = getElementByAttributeValue(objectsList, "representation", symbol);

                        if (object != null) {
                            Object objToAdd = new Object(ObjectsType.valueOf(object.getAttribute("enum")), objX, i, objects.size());
                            objects.add(objToAdd);
                            if (objToAdd.hasBehavior("has-ball")) {
                                objects.add(new Object(ObjectsType.BALL, j, i, objects.size()));
                            }

                        }

                    }
                }

                Element object = getElementByAttributeValue(objectsList, "representation", symbol);

                if (object != null) {
                    Object objToAdd = new Object(ObjectsType.valueOf(object.getAttribute("enum")), objX, i, objects.size());
                    objects.add(objToAdd);
                    if (objToAdd.hasBehavior("has-ball")) {
                        objects.add(new Object(ObjectsType.BALL, j, i, objects.size()));
                    }

                }
                objX++;

            }
        }
        return objects;

    }

}
