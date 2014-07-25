package map;

import enums.Controls;
import enums.ObjectsType;
import view.Move;

import java.util.ArrayList;

public class Map {

    private ArrayList<ArrayList<Field>> fields;
    private ArrayList<Object> objects;
    private int mapWidth;
    private int mapHeight;
    private int mapNumber;
    private int movesTaken;
    private ArrayList<Integer> goals;
    private int pointToComplete;
    private int completedPoints;


    public Map(int mapNumber) {
        loadMap(mapNumber);
    }

    public Map(Map map) {
        this.fields = new ArrayList<ArrayList<Field>>();
        this.mapWidth = map.getMapWidth();
        this.mapHeight = map.getMapHeight();
        this.mapNumber = map.getMapNumber();
        this.pointToComplete = map.getPointToComplete();
        this.completedPoints = map.getCompletedPoints();
        this.movesTaken = map.getMovesTaken();
        this.goals = map.getGoals();


        for (ArrayList<Field> row : map.getFields()) {
            ArrayList<Field> fieldInRow = new ArrayList<Field>();
            for (Field field : row) {
                fieldInRow.add(new Field(field));

            }
            this.fields.add(fieldInRow);
        }
    }

    private void loadMap(int mapNumber) {

        MapBuilder mapBuilder = new MapBuilder();

        fields = new ArrayList<>();
        fields = mapBuilder.buildMap(mapNumber);
        objects = mapBuilder.loadObjects(mapNumber);
        pointToComplete = getAllObjectsByBehavior("objective").size();
        completedPoints = 0;
        this.mapNumber = mapNumber;
        movesTaken = 0;
        goals = mapBuilder.getGoals(this);

        mapHeight = fields.size();
        mapWidth = fields.get(1).size();

    }

    public void makeMove(Controls control) {



        if (control != Controls.NONE) {
            movesTaken++;

            if (control == Controls.RIGHT) {

                help.utils.HelpUtils.sortByXReverse(objects);
                for (Object object : objects) {
                    for (int x = object.getX(); x < mapWidth - 1; x++) {
                        combine(object, object.getX() + 1, object.getY());
                    }
                }

            } else if (control == Controls.LEFT) {

                help.utils.HelpUtils.sortByX(objects);
                for (Object object : objects) {
                    for (int x = object.getX(); x > 0; x--) {
                        combine(object, object.getX() - 1, object.getY());
                    }
                }

            } else if (control == Controls.UP) {

                help.utils.HelpUtils.sortByYReverse(objects);
                for (Object object : objects) {
                    for (int y = object.getY(); y < mapHeight - 1; y++) {
                        combine(object, object.getX(), object.getY() + 1);
                    }
                }

            } else if (control == Controls.DOWN) {

                help.utils.HelpUtils.sortByY(objects);
                for (Object object : objects) {
                    for (int y = object.getY(); y > 0; y--) {
                        combine(object, object.getX(), object.getY() - 1);
                    }
                }

            } else if (control == Controls.NEXT) {

                loadMap(mapNumber + 1);

            } else if (control == Controls.PREVIOUS && mapNumber != 1) {

                loadMap(mapNumber - 1);

            }

            checkForFinish();

        }

    }

    public void combine(Object objectA, int x, int y) {

        Object objectB = findObject(x, y);
        if (objectA.hasBehavior("moving")) {
            if (objectB.getObjectsType() == ObjectsType.NONE) {
                if (fields.get(y).get(x).hasBehavior("empty")) {
                    objectA.setX(x);
                    objectA.setY(y);
                }
            } else if (objectA.hasBehavior("objective") && objectB.hasBehavior("objective-end")) {
                finish(objectA, objectB);
            }

        }
    }

    private Object findObject(int x, int y) {

        for (Object object : objects) {
            if (object.getX() == x && object.getY() == y) {
                return object;
            }
        }

        Object object = new Object("NONE", null, -1);
        return object;
    }

    private void finish(Object objectA, Object objectB) {

        objectB.setObjectsType(ObjectsType.FINISHED);
        objectA.setX(objectB.getX());
        objectA.setY(objectB.getY());
        objectA.setObjectsType(ObjectsType.FINISHED);


        completedPoints++;
    }

    public boolean checkForFinish() {
        if (completedPoints >= pointToComplete) {

            if (goals.size() == 3) {
                if (movesTaken <= goals.get(2))
                    System.out.println("super duper platyna!");
                else if (movesTaken <= goals.get(0))
                    System.out.println("złotko!");
                else if (movesTaken <= goals.get(1))
                    System.out.println("sreberro!");
                else System.out.println("brąz");

            } else {
                if (movesTaken <= goals.get(0))
                    System.out.println("złotko!");
                else if (movesTaken <= goals.get(1))
                    System.out.println("sreberro!");
                else System.out.println("brąz");
            }
        }

        return (completedPoints >= pointToComplete);
    }

    private ArrayList<Field> getAllFieldsByBehavior(String behavior) {

        ArrayList<Field> fieldsList = new ArrayList<Field>();

        for (ArrayList<Field> row : fields) {
            for (Field field : row) {
                if (field.hasBehavior(behavior)) {
                    fieldsList.add(field);
                }
            }
        }
        return fieldsList;

    }

    private ArrayList<Object> getAllObjectsByBehavior(String behavior) {
        ArrayList<Object> objectsList = new ArrayList<>();

        for (Object object : objects) {
            if (object.hasBehavior(behavior)) {
                objectsList.add(object);
            }
        }
        return objectsList;
    }

    public void deleteObject(int x, int y) {
        for (int i = 0; i < objects.size() - 1; i++) {
            if (objects.get(i).getX() == x && objects.get(i).getY() == y) {
                objects.remove(i);
            }
        }
    }

    public ArrayList<ArrayList<Field>> getFields() {
        return fields;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapNumber() {
        return mapNumber;
    }

    public int getPointToComplete() {
        return pointToComplete;
    }

    public int getCompletedPoints() {
        return completedPoints;
    }

    public int getMovesTaken() {
        return movesTaken;
    }

    public ArrayList<Integer> getGoals() {
        return goals;
    }

    public ArrayList<Object> getObjects() {
        return objects;
    }

}
