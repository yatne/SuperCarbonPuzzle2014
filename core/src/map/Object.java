package map;

import enums.ObjectsType;

import java.util.ArrayList;

public class Object {

    private ObjectsType objectsType;
    private ArrayList<String> behavior;

    public Object(String anEnum, ArrayList<String> behavior) {
        this.objectsType = ObjectsType.valueOf(anEnum);
        this.behavior = behavior;
    }

    public Object(String anEnum){
        this.objectsType = ObjectsType.valueOf(anEnum);
        this.behavior = help.utils.HelpUtils.readBehaviors(help.utils.HelpUtils.getElementByAttributeValue(help.utils.ObjectsReader.getObjectsList(),"enum",anEnum));
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
    }

    public ArrayList<String> getBehavior() {
        return behavior;
    }

    public void setBehavior(ArrayList<String> behavior) {
        this.behavior = behavior;
    }
}
