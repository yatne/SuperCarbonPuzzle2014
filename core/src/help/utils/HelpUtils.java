package help.utils;

import map.Object;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import view.Move;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    public static void sortByX(ArrayList<map.Object> objects) {

        Collections.sort(objects, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1.getX() < o2.getX())
                    return -1;
                else if (o1.getX() == o2.getX())
                    return 0;
                else
                    return 1;
            }
        }
        );
    }


    public static void sortByXReverse(ArrayList<map.Object> objects) {

        Collections.sort(objects, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1.getX() < o2.getX())
                    return 1;
                else if (o1.getX() == o2.getX())
                    return 0;
                else
                    return -1;
            }
        }
        );
    }


    public static void sortByY(ArrayList<map.Object> objects) {

        Collections.sort(objects, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1.getY() < o2.getY())
                    return -1;
                else if (o1.getX() == o2.getX())
                    return 0;
                else
                    return 1;
            }
        }
        );
    }

    public static void sortByYReverse(ArrayList<map.Object> objects) {

        Collections.sort(objects, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1.getY() < o2.getY())
                    return 1;
                else if (o1.getY() == o2.getY())
                    return 0;
                else
                    return -1;
            }
        }
        );
    }


    public static ArrayList<Move> combineMoves(ArrayList<Move> moves) {

        ArrayList<Move> newMoves = new ArrayList<>();

        if (moves.size() <= 1) {
            newMoves = moves;
        } else {

            if (moves.get(0).getFromX() != moves.get(0).getToX()) { //lewo - prawo

                int startX = -1;
                for (int i = 0; i < moves.size(); i++) {
                    if (startX == -1) {
                        startX = moves.get(0).getFromX();
                    }
                    if (i + 1 < moves.size()) {
                        if ((moves.get(i).getToX() != moves.get(i + 1).getFromX()) && (moves.get(i).getFromY() != moves.get(i + 1).getFromY())) {
                            newMoves.add(new Move(startX, moves.get(i).getFromY(), moves.get(i).getAnimationType(), moves.get(i).getToX(), moves.get(i).getToY()));
                            startX = -1;
                        }
                    } else {
                        newMoves.add(new Move(startX, moves.get(i).getFromY(), moves.get(i).getAnimationType(), moves.get(i).getToX(), moves.get(i).getToY()));
                        startX = -1;
                    }
                }

            } else if (moves.get(0).getFromY() != moves.get(0).getToY()) { //góra - dół

                int startY = -1;
                for (int i = 0; i < moves.size(); i++) {
                    if (startY == -1) {
                        startY = moves.get(0).getFromY();
                    }
                    if (i + 1 < moves.size()) {
                        if ((moves.get(i).getToY() != moves.get(i + 1).getFromY()) && (moves.get(i).getFromX() != moves.get(i + 1).getFromX())) {
                            newMoves.add(new Move(moves.get(i).getFromX(), startY, moves.get(i).getAnimationType(), moves.get(i).getToX(), moves.get(i).getToY()));
                            startY = -1;
                        }
                    } else {
                        newMoves.add(new Move(moves.get(i).getFromX(), startY, moves.get(i).getAnimationType(), moves.get(i).getToX(), moves.get(i).getToY()));
                        startY = -1;
                    }
                }


            } else {
                newMoves = moves;
            }
        }

        return newMoves;
    }

}
