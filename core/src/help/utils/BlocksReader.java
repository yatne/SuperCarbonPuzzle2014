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

public class BlocksReader {

    public static NodeList getBlocksList() {

        NodeList blockTypes = null;

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = builderFactory.newDocumentBuilder();

            Document document = dBuilder.parse(Map.class.getResourceAsStream("/resources/blocks.xml"));
            document.normalize();
            NodeList rootNodes = document.getElementsByTagName("blocks");
            Node rootNode = rootNodes.item(0);
            Element rootElement = (Element) rootNode;

            blockTypes = rootElement.getElementsByTagName("block");


        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return blockTypes;
    }


}
