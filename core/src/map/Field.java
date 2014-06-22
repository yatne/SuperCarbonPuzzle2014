package map;

import java.util.ArrayList;

public class Field {

    private FieldsType fieldType;
    private Object object;
    private ArrayList<String> behavior;
    private int x;
    private int y;


    public Field(String fieldEnum, ArrayList<String> fieldBehavior, String objectEnum, ArrayList<String> objectBehavior, int x, int y) {

        this.fieldType = FieldsType.valueOf(fieldEnum);
        this.behavior = fieldBehavior;
        this.x = x;
        this.y = y;

        addObject(objectEnum, objectBehavior);
    }

    public Field(String anEnum, ArrayList<String> behavior, int x, int y) {
        this.fieldType = FieldsType.valueOf(anEnum);
        this.behavior = behavior;
        this.x = x;
        this.y = y;

        addObject("NONE", null);

    }

    public Field(Field field){
        this.fieldType=field.getFieldType();
        this.behavior=field.getBehavior();
        this.x=field.getX();
        this.y=field.getY();
        this.object=new Object(field.getObject().getObjectsType().toString(),field.getObject().getBehavior());
    }

    public void copyField(Field copyField) {
        this.fieldType = copyField.getFieldType();
        this.behavior = copyField.getBehavior();

        if (copyField.hasObject())
            addObject(copyField.getObject().getObjectsType().toString(), copyField.getObject().getBehavior());
        else
            addObject("NONE", null);
    }

    public boolean hasObject() {

        if (object.getObjectsType() == ObjectsType.NONE)
            return false;
        else
            return true;

    }

    private void addObject(String anEnum, ArrayList<String> objectBehavior) {
        this.object = new Object(anEnum, objectBehavior);
    }

    public boolean hasBehavior(String wantedBehavior) {


        if (this.behavior != null)
            for (String beh : this.behavior) {
                if (beh.equals(wantedBehavior))
                    return true;
            }
        return false;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public ArrayList<String> getBehavior() {
        return behavior;
    }

    public void setBehavior(ArrayList<String> behavior) {
        this.behavior = behavior;
    }

    public FieldsType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldsType fieldType) {
        this.fieldType = fieldType;
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
}
