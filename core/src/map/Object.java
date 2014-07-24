package map;

import enums.ObjectsType;

import java.util.ArrayList;

public class Object {

    private ObjectsType objectsType;
    private ArrayList<String> behavior;
    private int x;
    private int y;

    public Object(String anEnum, ArrayList<String> behavior) {
        this.objectsType = ObjectsType.valueOf(anEnum);
        this.behavior = behavior;
    }

    public Object(String anEnum) {
        this.objectsType = ObjectsType.valueOf(anEnum);
        this.behavior = help.utils.HelpUtils.readBehaviors(help.utils.HelpUtils.getElementByAttributeValue(help.utils.ObjectsReader.getObjectsList(), "enum", anEnum));
    }

    public boolean hasBehavior(String wantedBehavior) {


        if (this.behavior != null)
            for (String beh : this.behavior) {
                if (beh.equals(wantedBehavior))
                    return true;
            }
        return false;
    }

    public ObjectsType getObjectsType() {
        return objectsType;
    }

    public void setObjectsType(ObjectsType objectsType) {
        this.objectsType = objectsType;
        this.behavior = help.utils.HelpUtils.readBehaviors(help.utils.HelpUtils.getElementByAttributeValue(help.utils.ObjectsReader.getObjectsList(), "enum", objectsType.toString()));
    }

    public ArrayList<String> getBehavior() {
        return behavior;
    }

    public void setBehavior(ArrayList<String> behavior) {
        this.behavior = behavior;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Object(ObjectsType objectsType, ArrayList<String> behavior, int x, int y) {
        this.objectsType = objectsType;
        this.behavior = behavior;
        this.x = x;
        this.y = y;
    }
}
