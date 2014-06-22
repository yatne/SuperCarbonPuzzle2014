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

public class ObjectsReader {


    public static NodeList getObjectsList() {

        NodeList objectTypes = null;

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = builderFactory.newDocumentBuilder();
            Document document = dBuilder.parse(Map.class.getResourceAsStream("/resources/objects.xml"));
            document.normalize();
            NodeList rootNodes = document.getElementsByTagName("objects");
            Node rootNode = rootNodes.item(0);
            Element rootElement = (Element) rootNode;

            objectTypes = rootElement.getElementsByTagName("object");


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return objectTypes;
    }


}
