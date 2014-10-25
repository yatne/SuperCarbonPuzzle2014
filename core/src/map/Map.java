package map;

import enums.Controls;
import enums.ObjectsType;

import java.util.ArrayList;

public class Map {

    MapBuilder mapBuilder;
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
    private boolean somethingHappened;


    public Map(int worldNumber, int mapNumber) {
        fields = new ArrayList<>();
        mapBuilder = new MapBuilder();
        loadMap(worldNumber, mapNumber);
        somethingHappened = false;

    }

    public Map(Map map) {
        this.fields = new ArrayList<>(map.getFields());
        this.mapWidth = map.getMapWidth();
        this.mapHeight = map.getMapHeight();
        this.mapNumber = map.getMapNumber();
        this.pointToComplete = map.getPointToComplete();
        this.completedPoints = map.getCompletedPoints();
        this.movesTaken = map.getMovesTaken();
        this.goals = map.getGoals();
        this.objects = new ArrayList<>();

        for (Object object : map.getObjects()) {
            this.objects.add(new Object(object));
        }
        somethingHappened = false;
    }

    public void loadMap(int worldNumber, int mapNumber) {


        fields.clear();
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

        boolean portalWasUsed = false;
        somethingHappened = false;

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
                        teleportHorizontal(object, getPortal(object), 1);
                        portalWasUsed = true;
                    }
                }
                if (portalWasUsed) {
                    for (Object object : objects) {
                        for (int x = object.getX(); x < mapWidth - 1; x++) {
                            combine(object, object.getX() + 1, object.getY());
                        }
                        if (isOnPortal(object, "portal-right")) {
                            teleportHorizontal(object, getPortal(object), 1);
                        }
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
                        teleportHorizontal(object, getPortal(object), 1);
                        portalWasUsed = true;
                    }
                }
                if (portalWasUsed) {
                    for (Object object : objects) {

                        for (int x = object.getX(); x > 0; x--) {
                            combine(object, object.getX() - 1, object.getY());
                        }
                        if (isOnPortal(object, "portal-left")) {
                            teleportHorizontal(object, getPortal(object), 1);

                        }
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
                        teleportVertical(object, getPortal(object), 1);
                        portalWasUsed = true;
                    }
                }

                if (portalWasUsed)
                    for (Object object : objects) {

                        for (int y = object.getY(); y < mapHeight - 1; y++) {
                            combine(object, object.getX(), object.getY() + 1);
                        }
                        if (isOnPortal(object, "portal-upper")) {
                            teleportVertical(object, getPortal(object), 1);
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
                        teleportVertical(object, getPortal(object), 1);
                        portalWasUsed = true;
                    }
                }
                if (portalWasUsed)
                    for (Object object : objects) {

                        for (int y = object.getY(); y > 0; y--) {
                            combine(object, object.getX(), object.getY() - 1);
                        }
                        if (isOnPortal(object, "portal-lower")) {
                            teleportVertical(object, getPortal(object), 1);
                        }
                    }
            }
        }

    }

    public void combine(Object objectA, int x, int y) {

        Object objectB = findObject(x, y);
        if (objectA.hasBehavior("moving") || (isRailed(objectA, objectB))) {
            if (objectB.getObjectsType() == ObjectsType.NONE) {
                if (fields.get(y).get(x).hasBehavior("empty")) {
                    actionOnLeave(objectA);
                    objectA.setX(x);
                    objectA.setY(y);
                }
            } else if (objectA.hasBehavior("objective") && objectB.hasBehavior("objective-end")) {
                actionOnLeave(objectA);
                if (objectA.hasBehavior("obj1") && objectB.hasBehavior("obj1")) {
                    somethingHappened = true;
                    finish(objectA, objectB, 1);
                } else if (objectA.hasBehavior("obj2") && objectB.hasBehavior("obj2")) {
                    somethingHappened = true;
                    finish(objectA, objectB, 2);
                }
            } else if (objectA.hasBehavior("destroy") && objectB.hasBehavior("todestroy")) {
                somethingHappened = true;
                objectB.setObjectsType(ObjectsType.DESTROYED);
            }
            if (objectB.hasBehavior("portal") && !isThereSolidBlock(x, y)) {
                actionOnLeave(objectA);
                if (objectB.getX() == objectA.getX())
                    teleportVertical(objectA, objectB, 0);
                else if (objectA.getY() == objectB.getY())
                    teleportHorizontal(objectA, objectB, 0);

            } else if (objectB.hasBehavior("empty") && !isThereSolidBlock(x, y)) {
                actionOnEnter(objectB);
                actionOnLeave(objectA);
                actionOnCombine(objectA, objectB);
                objectA.setX(x);
                objectA.setY(y);

            }
        }

    }

    private void actionOnCombine(Object objectA, Object objectB) {

        if (objectB.hasBehavior("trap")) {
            somethingHappened = true;
            objectB.setObjectsType(ObjectsType.NOTHING);
            objectA.setObjectsType(ObjectsType.TRAPA);
        }

    }

    private void actionOnEnter(Object objectB) {


        if (objectB.hasBehavior("gwb-on-enter")) {
            somethingHappened = true;
            objectB.setObjectsType(ObjectsType.GWB);
        }

    }

    private void actionOnLeave(Object objectA) {

        for (Object object : objects) {
            if (object.getX() == objectA.getX() && object.getY() == objectA.getY()) {
                if (object.hasBehavior("solid-on-leave")) {
                    somethingHappened = true;
                    object.setObjectsType(ObjectsType.CREATED);
                }
            }
        }
    }

    private void teleportHorizontal(Object objectA, Object objectB, int type) {

        boolean portalFound = false;
        if ((objectA.getX() < objectB.getX() && objectB.hasBehavior("portal-right") && type == 0)
                || (objectB.hasBehavior("portal-right") && type == 1)) {
            for (Object portal : getAllObjectsByBehavior("portal")) {
                if (portal.hasBehavior("portal-left") && portal.getY() == objectB.getY() && !isThereSolidBlock(portal.getX(), portal.getY())) {
                    objectA.setGoneThroughTele(true);
                    objectA.setX(portal.getX());
                    objectA.setY(portal.getY());
                    portalFound = true;
                }
            }
            if (portalFound)

                for (int newX = objectA.getX(); newX < mapWidth - 1; newX++) {
                    combine(objectA, objectA.getX() + 1, objectA.getY());
                }
            if (!portalFound) {
                objectA.setX(objectB.getX());
                objectA.setY(objectB.getY());
            }
        } else if ((objectA.getX() > objectB.getX() && objectB.hasBehavior("portal-left") && type == 0)
                || (objectB.hasBehavior("portal-left") && type == 1)) {

            for (Object portal : getAllObjectsByBehavior("portal")) {
                if (portal.hasBehavior("portal-right") && portal.getY() == objectB.getY() && !isThereSolidBlock(portal.getX(), portal.getY())) {
                    objectA.setGoneThroughTele(true);
                    objectA.setX(portal.getX());
                    objectA.setY(portal.getY());
                    portalFound = true;
                }
            }
            if (portalFound)
                for (int x = objectA.getX(); x > 0; x--) {
                    combine(objectA, objectA.getX() - 1, objectA.getY());
                }
            if (!portalFound) {
                objectA.setX(objectB.getX());
                objectA.setY(objectB.getY());
            }


        } else if ((objectB.hasBehavior("portal-upper") || objectB.hasBehavior("portal-lower")) && !isThereSolidBlock(objectB.getX(), objectB.getY())) {
            objectA.setX(objectB.getX());
            objectA.setY(objectB.getY());
        }
    }

    private void teleportVertical(Object objectA, Object objectB, int type) {
        boolean portalFound = false;
        if ((objectA.getY() < objectB.getY() && objectB.hasBehavior("portal-upper") && type == 0)
                || (objectB.hasBehavior("portal-upper") && type == 1)) {

            for (Object portal : getAllObjectsByBehavior("portal")) {
                if (portal.hasBehavior("portal-lower") && portal.getX() == objectB.getX() && !isThereSolidBlock(portal.getX(), portal.getY())) {
                    objectA.setGoneThroughTele(true);
                    objectA.setX(portal.getX());
                    objectA.setY(portal.getY());
                    portalFound = true;
                }
            }
            if (portalFound)

                for (int y = objectA.getY(); y < mapHeight - 1; y++) {
                    combine(objectA, objectA.getX(), objectA.getY() + 1);

                }
            if (!portalFound) {
                objectA.setX(objectB.getX());
                objectA.setY(objectB.getY());
            }
        } else if ((objectA.getY() > objectB.getY() && objectB.hasBehavior("portal-lower") && type == 0)
                || (objectB.hasBehavior("portal-lower") && type == 1)) {

            for (Object portal : getAllObjectsByBehavior("portal")) {
                if (portal.hasBehavior("portal-upper") && portal.getX() == objectB.getX() && !isThereSolidBlock(portal.getX(), portal.getY())) {
                    objectA.setGoneThroughTele(true);
                    objectA.setX(portal.getX());
                    objectA.setY(portal.getY());
                    portalFound = true;
                }
            }
            if (portalFound)

                for (int y = objectA.getY(); y > 0; y--) {
                    combine(objectA, objectA.getX(), objectA.getY() - 1);
                }

            if (!portalFound) {
                objectA.setX(objectB.getX());
                objectA.setY(objectB.getY());
            }
        } else if ((objectB.hasBehavior("portal-left") || objectB.hasBehavior("portal-right")) && !isThereSolidBlock(objectB.getX(), objectB.getY())) {
            objectA.setX(objectB.getX());
            objectA.setY(objectB.getY());
        }

    }

    private void destroyObjectsInPlaceOf(Object destroyObj) {
        for (Object object : objects) {
            if (object.getX() == destroyObj.getX() && object.getY() == destroyObj.getY()) {
                if (!object.equals(destroyObj))
                    object.setObjectsType(ObjectsType.NOTHING);
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

    private boolean isThereSolidBlock(int x, int y) {
        boolean isThereSolid = false;
        for (Object object : objects) {
            if (object.getX() == x && object.getY() == y) {
                if (object.hasBehavior("solid")) {
                    isThereSolid = true;
                }
            }
        }
        return isThereSolid;
    }

    private void finish(Object objectA, Object objectB, int ballNr) {

        objectB.setObjectsType(ObjectsType.NOTHING);
        objectA.setX(objectB.getX());
        objectA.setY(objectB.getY());
        if (ballNr == 1)
            objectA.setObjectsType(ObjectsType.FINISHED);
        else if (ballNr == 2)
            objectA.setObjectsType(ObjectsType.FINISHED2);

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

    private boolean isRailed(Object objectA, Object objectB) {
        if (!(objectA.hasBehavior("railed") && objectB.hasBehavior("rail")))
            return false;
        else {
            if (objectB.hasBehavior("rail-l") && objectA.getX() > objectB.getX())
                return true;
            if (objectB.hasBehavior("rail-r") && objectA.getX() < objectB.getX())
                return true;
            if (objectB.hasBehavior("rail-u") && objectA.getY() < objectB.getY())
                return true;
            if (objectB.hasBehavior("rail-d") && objectA.getY() > objectB.getY())
                return true;
        }
        return false;
    }

    public int getObtainedStars() {
        int newStars = 0;
        if (goals.size() == 3) {
            if (movesTaken <= goals.get(2))
                newStars = 4;
            else if (movesTaken <= goals.get(1))
                newStars = 3;
            else if (movesTaken <= goals.get(0))
                newStars = 2;
            else newStars = 1;
        } else if (goals.size() == 2) {
            if (movesTaken <= goals.get(1))
                newStars = 3;
            else if (movesTaken <= goals.get(0))
                newStars = 2;
            else newStars = 1;
        } else if (goals.size() == 1) {
            if (movesTaken <= goals.get(0))
                newStars = 2;
            else newStars = 1;
        } else if (goals.size() == 0) {
            newStars = 1;
        }
        return newStars;
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

    public boolean isSomethingHappened() {
        return somethingHappened;
    }
}
