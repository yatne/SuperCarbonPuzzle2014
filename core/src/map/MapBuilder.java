package map;

import com.badlogic.gdx.maps.MapObject;
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


    private Element getMapEle(int mapNumber) {

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        Element chosenMap = null;
        try {

            DocumentBuilder dBuilder = builderFactory.newDocumentBuilder();
            Document document = dBuilder.parse(Map.class.getResourceAsStream("/resources/maps.xml"));
            document.normalize();
            NodeList rootNodes = document.getElementsByTagName("maps");
            Node rootNode = rootNodes.item(0);
            Element rootElement = (Element) rootNode;
            NodeList mapList = rootElement.getElementsByTagName("map");
            Node mapNode = help.utils.HelpUtils.getElementByAttributeValue(mapList, "number", Integer.toString(mapNumber));
            chosenMap = (Element) mapNode;


        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return chosenMap;
    }

    public ArrayList<Integer> getGoals(Map map) {
        Element mapEle = getMapEle(map.getMapNumber());
        ArrayList<Integer> goals = new ArrayList<>();
        if (mapEle.hasAttribute("gold"))
            goals.add(Integer.parseInt(mapEle.getAttribute("gold")));
        else
            goals.add(100);
        if (mapEle.hasAttribute("silver"))
            goals.add(Integer.parseInt(mapEle.getAttribute("silver")));
        else
            goals.add(50);
        if (mapEle.hasAttribute("plat"))
            goals.add(Integer.parseInt(mapEle.getAttribute("plat")));
        return goals;
    }

    public ArrayList<ArrayList<Field>> buildMap(int mapNumber) {

        Element mapBlueprint = getMapEle(mapNumber);
        NodeList blocksList = BlocksReader.getBlocksList();
        NodeList objectsList = ObjectsReader.getObjectsList();
        ArrayList<ArrayList<Field>> map = new ArrayList<>();

        NodeList rows = mapBlueprint.getElementsByTagName("row");
        String rowBlueprint = null;

        for (int i = 0; i < rows.getLength(); i++) {

            for (int k = 0; k < rows.getLength(); k++) {
                Element row = (Element) rows.item(k);
                if (Integer.parseInt(row.getAttribute("nr")) == rows.getLength() - i)
                    rowBlueprint = row.getTextContent();
            }

            ArrayList<Field> fieldsInARow = new ArrayList<>();
            for (int j = 0; j < rowBlueprint.length(); j++) {
                String symbol = rowBlueprint.substring(j, j + 1);

                Element block = getElementByAttributeValue(blocksList, "representation", symbol);
                Element object = getElementByAttributeValue(objectsList, "representation", symbol);

                if (block != null) {
                    fieldsInARow.add(new Field(block.getAttribute("enum"), readBehaviors(block), j, i));
                } else {
                    Element underBlock = getElementByAttributeValue(blocksList, "enum", object.getAttribute("placedOn"));
                    fieldsInARow.add(new Field(underBlock.getAttribute("enum"), readBehaviors(underBlock),
                            object.getAttribute("enum"), readBehaviors(object), j, i));

                }


            }
            map.add(fieldsInARow);
        }
        return map;
    }

    public ArrayList<Object> loadObjects(int mapNumber) {

        ArrayList<Object> objects = new ArrayList<>();

        Element mapBlueprint = getMapEle(mapNumber);
        NodeList objectsList = ObjectsReader.getObjectsList();

        NodeList rows = mapBlueprint.getElementsByTagName("row");
        String rowBlueprint = null;

        for (int i = 0; i < rows.getLength(); i++) {

            for (int k = 0; k < rows.getLength(); k++) {
                Element row = (Element) rows.item(k);
                if (Integer.parseInt(row.getAttribute("nr")) == rows.getLength() - i)
                    rowBlueprint = row.getTextContent();
            }

            for (int j = 0; j < rowBlueprint.length(); j++) {
                String symbol = rowBlueprint.substring(j, j + 1);


                Element object = getElementByAttributeValue(objectsList, "representation", symbol);

                if (object != null) {
                    objects.add(new Object(ObjectsType.valueOf(object.getAttribute("enum")), help.utils.HelpUtils.readBehaviors(object), j, i, objects.size()));
                }


            }
        }
        return objects;

    }

}
