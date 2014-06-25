package help.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class HelpUtils {

  @Nullable
  public static Element getElementByAttributeValue(@NotNull NodeList nodeList, @NotNull String attributeName, @NotNull String attributeValue) {

    Element element = null;

    for (int i = 0; i < nodeList.getLength(); i++) {

      if (((Element) nodeList.item(i)).getAttribute(attributeName).equals(attributeValue))
        element = (Element) nodeList.item(i);

    }

    return element;
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
