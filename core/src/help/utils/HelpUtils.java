package help.utils;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class HelpUtils {

    public static Element getElementByAttributeValue(NodeList nodeList, String attributeName, String attributeValue) {


        for (int i = 0; i < nodeList.getLength(); i++) {
            Element ele = (Element) nodeList.item(i);

            if (ele.getAttribute(attributeName).equals(attributeValue))
                return ele;

        }

        return null;
    }

    public static ArrayList<String> readBehaviors(Element ele) {

        ArrayList<String> behavior = new ArrayList<String>();
        NodeList behaviorNodeList = ele.getElementsByTagName("behavior");
        for (int i = 0; i < behaviorNodeList.getLength(); i++) {
            Element behaviorElement = (Element) behaviorNodeList.item(i);
            behavior.add(behaviorElement.getTextContent());
        }
        return behavior;
    }

    public static boolean hasBehavior(ArrayList<String> behaviorList, String behavior) {

        for (String beh : behaviorList) {
            if (beh.equals(behavior))
                return true;
        }
        return false;
    }
}
