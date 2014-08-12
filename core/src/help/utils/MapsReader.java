package help.utils;

import map.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class MapsReader {

    public static NodeList getMapsList(String xmlPath) {

        NodeList maps = null;

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = builderFactory.newDocumentBuilder();

            Document document = dBuilder.parse(Map.class.getResourceAsStream(xmlPath));
            document.normalize();
            NodeList rootNodes = document.getElementsByTagName("maps");
            Node rootNode = rootNodes.item(0);
            Element rootElement = (Element) rootNode;

            maps = rootElement.getElementsByTagName("map");


        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return maps;
    }

    public static String getMapName(Map map) {

        NodeList mapsList = MapsReader.getMapsList("/resources/maps" + map.getMapWorld() + ".xml");
        for (int i = 0; i < mapsList.getLength(); i++) {
            Element mapEle = (Element) mapsList.item(i);
            if (mapEle.getAttribute("number").equals(Integer.toString(map.getMapNumber()))) {
                return mapEle.getAttribute("name");
            }
        }
        return null;
    }

    public static int starsToUnlock(int world, int level) {

        NodeList mapsList = MapsReader.getMapsList("/resources/maps" + world + ".xml");
        for (int i = 0; i < mapsList.getLength(); i++) {
            Element mapEle = (Element) mapsList.item(i);
            if (mapEle.getAttribute("number").equals(Integer.toString(level))) {
                try{
                return Integer.parseInt(mapEle.getAttribute("unlocked"));
                }
                catch (NumberFormatException e) {
                    System.out.println("mapa nie ma pola unlocked");
                }

            }
        }
        return 0;
    }

}
