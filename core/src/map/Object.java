package map;

import enums.ObjectsType;

import java.util.ArrayList;

public class Object {

    private ObjectsType objectsType;
    private ArrayList<String> behavior;
    private boolean goneThroughTele;
    private int id;
    private int x;
    private int y;

    public Object(ObjectsType objectsType, int x, int y, int id) {
        this.objectsType = objectsType;
        this.behavior = help.utils.HelpUtils.readBehaviors(help.utils.HelpUtils.getElementByAttributeValue(help.utils.ObjectsReader.getObjectsList(), "enum", objectsType.toString()));
        this.x = x;
        this.y = y;
        this.id = id;
        goneThroughTele = false;
    }

    public Object(String anEnum, ArrayList<String> behavior, int id) {
        this.objectsType = ObjectsType.valueOf(anEnum);
        this.behavior = behavior;
        this.id = id;
    }

    public Object(Object object) {
        this.objectsType = object.getObjectsType();
        this.behavior = new ArrayList<>();
        for (String behavior : object.getBehavior()) {
            this.behavior.add(behavior);
        }
        this.goneThroughTele = object.goneThroughTele;
        this.id = object.getId();
        this.x = object.getX();
        this.y = object.getY();

    }

    public boolean hasBehavior(String wantedBehavior) {

        if (this.behavior != null)
            for (String beh : this.behavior) {
                if (beh.equals(wantedBehavior))
                    return true;
            }
        return false;
    }

    public boolean equals(Object object) {
        boolean areTheSame = true;


        if (this.objectsType != object.getObjectsType() || this.id != object.getId()
                || this.x != object.getX() || this.y != object.getY())

            areTheSame = false;
        return areTheSame;

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

    public int getId() {
        return id;
    }

    public boolean isGoneThroughTele() {
        return goneThroughTele;
    }

    public void setGoneThroughTele(boolean goneThroughTele) {
        this.goneThroughTele = goneThroughTele;
    }
}
