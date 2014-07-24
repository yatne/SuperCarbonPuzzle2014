package map;

import enums.FieldsType;

import java.util.ArrayList;

public class Field {

    private FieldsType fieldType;
    private ArrayList<String> behavior;
    private int x;
    private int y;


    public Field(String fieldEnum, ArrayList<String> fieldBehavior, String objectEnum, ArrayList<String> objectBehavior, int x, int y) {

        this.fieldType = FieldsType.valueOf(fieldEnum);
        this.behavior = fieldBehavior;
        this.x = x;
        this.y = y;

    }

    public Field(String anEnum, ArrayList<String> behavior, int x, int y) {
        this.fieldType = FieldsType.valueOf(anEnum);
        this.behavior = behavior;
        this.x = x;
        this.y = y;


    }

    public Field(Field field) {
        this.fieldType = field.getFieldType();
        this.behavior = field.getBehavior();
        this.x = field.getX();
        this.y = field.getY();
    }

    public void copyField(Field copyField) {
        this.fieldType = copyField.getFieldType();
        this.behavior = copyField.getBehavior();
    }

    public boolean hasBehavior(String wantedBehavior) {


        if (this.behavior != null)
            for (String beh : this.behavior) {
                if (beh.equals(wantedBehavior))
                    return true;
            }
        return false;
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

    public int getY() {
        return y;
    }

}
