package map;

import enums.Controls;
import enums.ObjectsType;

import java.util.ArrayList;

public class Map {

    private ArrayList<ArrayList<Field>> fields;
    private ArrayList<Object> objects;
    private int mapWidth;
    private int mapHeight;
    private int mapNumber;
    private int mapWorld;
    private int movesTaken;
    private ArrayList<Integer> goals;
    private int pointToComplete;
    private int completedPoints;


    public Map(int worldNumber, int mapNumber) {
        loadMap(worldNumber, mapNumber);
    }

    public Map(Map map) {
        this.fields = new ArrayList<>();
        this.mapWidth = map.getMapWidth();
        this.mapHeight = map.getMapHeight();
        this.mapNumber = map.getMapNumber();
        this.pointToComplete = map.getPointToComplete();
        this.completedPoints = map.getCompletedPoints();
        this.movesTaken = map.getMovesTaken();
        this.goals = map.getGoals();
        this.objects = new ArrayList<>();


        for (ArrayList<Field> row : map.getFields()) {
            ArrayList<Field> fieldInRow = new ArrayList<>();
            for (Field field : row) {
                fieldInRow.add(new Field(field));

            }
            this.fields.add(fieldInRow);
        }

        for (Object object : map.getObjects()) {
            this.objects.add(object);
        }
    }

    private void loadMap(int worldNumber, int mapNumber) {


        MapBuilder mapBuilder = new MapBuilder();

        fields = new ArrayList<>();
        fields = mapBuilder.buildMap(worldNumber, mapNumber);
        objects = mapBuilder.loadObjects(worldNumber, mapNumber);
        pointToComplete = getAllObjectsByBehavior("objective").size();
        completedPoints = 0;
        mapWorld = worldNumber;
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
                    object.setGoneThroughTele(false);
                    for (int x = object.getX(); x < mapWidth - 1; x++) {
                        combine(object, object.getX() + 1, object.getY());
                    }
                    if (isOnPortal(object, "portal-right")) {
                        teleport(object, getPortal(object), 1);
                    }
                }

            } else if (control == Controls.LEFT) {

                help.utils.HelpUtils.sortByX(objects);
                for (Object object : objects) {
                    object.setGoneThroughTele(false);
                    for (int x = object.getX(); x > 0; x--) {
                        combine(object, object.getX() - 1, object.getY());
                    }
                    if (isOnPortal(object, "portal-left")) {
                        teleport(object, getPortal(object), 1);
                    }
                }

            } else if (control == Controls.UP) {

                help.utils.HelpUtils.sortByYReverse(objects);
                for (Object object : objects) {
                    object.setGoneThroughTele(false);
                    for (int y = object.getY(); y < mapHeight - 1; y++) {
                        combine(object, object.getX(), object.getY() + 1);
                    }
                    if (isOnPortal(object, "portal-upper")) {
                        teleport(object, getPortal(object), 1);
                    }
                }

            } else if (control == Controls.DOWN) {

                help.utils.HelpUtils.sortByY(objects);
                for (Object object : objects) {
                    object.setGoneThroughTele(false);
                    for (int y = object.getY(); y > 0; y--) {
                        combine(object, object.getX(), object.getY() - 1);
                    }
                    if (isOnPortal(object, "portal-lower")) {
                        teleport(object, getPortal(object), 1);
                    }
                }


            }

        }
    }

    public void combine(Object objectA, int x, int y) {

        Object objectB = findObject(x, y);
        if (objectA.hasBehavior("moving")) {
            if (objectB.getObjectsType() == ObjectsType.NONE) {
                if (fields.get(y).get(x).hasBehavior("empty")) {
                    actionOnLeave(objectA);
                    objectA.setX(x);
                    objectA.setY(y);
                }
            } else if (objectA.hasBehavior("objective") && objectB.hasBehavior("objective-end")) {
                actionOnLeave(objectA);
                finish(objectA, objectB);
            }
            if (objectB.hasBehavior("portal")) {
                actionOnLeave(objectA);
                teleport(objectA, objectB, 0);


            } else if (objectB.hasBehavior("empty") ) {
                actionOnEnter(objectB);
                actionOnLeave(objectA);
                objectA.setX(x);
                objectA.setY(y);

            }
        }

    }

    private void actionOnEnter(Object objectB) {

        if (objectB.hasBehavior("gwb-on-enter")) {
            objectB.setObjectsType(ObjectsType.GWB);
        }
    }

    private void actionOnLeave(Object objectA) {
        for (Object object : objects) {
            if (object.getX() == objectA.getX() && object.getY() == objectA.getY()) {
                if (object.hasBehavior("solid-on-leave"))
                    object.setObjectsType(ObjectsType.CREATED);
            }
        }
    }

    private void teleport(Object objectA, Object objectB, int type) {

        boolean portalFound = false;
        if ((objectA.getX() < objectB.getX() && objectB.hasBehavior("portal-right") && type == 0)
                || (objectB.hasBehavior("portal-right") && type == 1)) {
            objectA.setGoneThroughTele(true);
            for (Object portal : getAllObjectsByBehavior("portal")) {
                if (portal.hasBehavior("portal-left") && portal.getY() == objectB.getY()) {
                    objectA.setX(portal.getX());
                    objectA.setY(portal.getY());
                    portalFound = true;
                }
            }
            if (portalFound)

                for (int newX = objectA.getX(); newX < mapWidth - 1; newX++) {
                    combine(objectA, objectA.getX() + 1, objectA.getY());
                }
        } else if ((objectA.getX() > objectB.getX() && objectB.hasBehavior("portal-left") && type == 0)
                || (objectB.hasBehavior("portal-left") && type == 1)) {
            objectA.setGoneThroughTele(true);
            for (Object portal : getAllObjectsByBehavior("portal")) {
                if (portal.hasBehavior("portal-right") && portal.getY() == objectB.getY()) {
                    objectA.setX(portal.getX());
                    objectA.setY(portal.getY());
                    portalFound = true;
                }
            }
            if (portalFound)
                for (int x = objectA.getX(); x > 0; x--) {
                    combine(objectA, objectA.getX() - 1, objectA.getY());
                }
        } else if ((objectA.getY() < objectB.getY() && objectB.hasBehavior("portal-upper") && type == 0)
                || (objectB.hasBehavior("portal-upper") && type == 1)) {
            objectA.setGoneThroughTele(true);
            for (Object portal : getAllObjectsByBehavior("portal")) {
                if (portal.hasBehavior("portal-lower") && portal.getX() == objectB.getX()) {
                    objectA.setX(portal.getX());
                    objectA.setY(portal.getY());
                    portalFound = true;
                }
            }
            if (portalFound)

                for (int y = objectA.getY(); y < mapHeight - 1; y++) {
                    combine(objectA, objectA.getX(), objectA.getY() + 1);

                }
        } else if ((objectA.getY() > objectB.getY() && objectB.hasBehavior("portal-lower") && type == 0)
                || (objectB.hasBehavior("portal-lower") && type == 1)) {
            objectA.setGoneThroughTele(true);
            for (Object portal : getAllObjectsByBehavior("portal")) {
                if (portal.hasBehavior("portal-upper") && portal.getX() == objectB.getX()) {
                    objectA.setX(portal.getX());
                    objectA.setY(portal.getY());
                    portalFound = true;
                }
            }
            if (portalFound)

                for (int y = objectA.getY(); y > 0; y--) {
                    combine(objectA, objectA.getX(), objectA.getY() - 1);
                }
        }
        if (!portalFound) {
            objectA.setX(objectB.getX());
            objectA.setY(objectB.getY());
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

        objectB.setObjectsType(ObjectsType.NOTHING);
        objectA.setX(objectB.getX());
        objectA.setY(objectB.getY());
        objectA.setObjectsType(ObjectsType.FINISHED);


        completedPoints++;
    }

    public boolean checkForFinish() {

        return (completedPoints >= pointToComplete);
    }

    private boolean isOnPortal(Object object, String portalType) {
        if (!object.hasBehavior("portal"))
            for (Object portal : objects) {
                if (portal.hasBehavior(portalType) && portal.getX() == object.getX() && portal.getY() == object.getY()) {
                    return true;
                }
            }
        return false;
    }

    private Object getPortal(Object object) {
        for (Object portal : objects) {
            if (portal.hasBehavior("portal") && portal.getX() == object.getX() && portal.getY() == object.getY()) {
                return portal;
            }
        }
        return null;
    }

    private ArrayList<Field> getAllFieldsByBehavior(String behavior) {

        ArrayList<Field> fieldsList = new ArrayList<>();

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

    public int getMapWorld() {
        return mapWorld;
    }
}
