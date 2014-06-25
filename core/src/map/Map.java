package map;

import enums.Controls;

import java.util.ArrayList;

public class Map {

    private ArrayList<ArrayList<Field>> fields;
    private int mapWidth;
    private int mapHeight;
    private int mapNumber;
    private int movesTaken;
    private ArrayList<Integer> goals;
    private int pointToComplete;
    private int completedPoints;

    public Map(int mapNumber) {


        MapBuilder mapBuilder = new MapBuilder();
        fields = mapBuilder.buildMap(mapNumber);
        pointToComplete = getAllFieldsByBehavior("objective").size();
        completedPoints = 0;
        this.mapNumber = mapNumber;
        movesTaken = 0;
        goals = mapBuilder.getGoals(this);
        mapHeight = fields.size();
        mapWidth = fields.get(1).size();

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

    public void makeMove(Controls controls) {
        if (controls != Controls.NONE) {
            movesTaken++;

            if (controls == Controls.RIGHT) {
                for (int n = 0; n < mapWidth; n++) {
                    for (ArrayList<Field> row : fields) {
                        for (int i = n - 1; i >= 0; i--) {
                            combine(row.get(mapWidth - i - 2), row.get(mapWidth - i - 1));
                        }
                    }
                }
            }
            if (controls == Controls.LEFT) {

                for (int n = 0; n < mapWidth; n++) {
                    for (ArrayList<Field> row : fields) {
                        for (int i = n - 1; i >= 0; i--) {
                            combine(row.get(i + 1), row.get(i));
                        }
                    }
                }
            }
            if (controls == Controls.UP) {
                for (int n = 0; n < mapHeight; n++) {
                    for (int i = n - 1; i >= 0; i--) {
                        for (int j = 0; j < mapWidth; j++) {
                            combine(fields.get(mapHeight - i - 2).get(j), fields.get(mapHeight - i - 1).get(j));
                        }
                    }
                }
            }
            if (controls == Controls.DOWN) {
                for (int n = 0; n < mapHeight; n++) {
                    for (int i = n - 1; i >= 0; i--) {
                        for (int j = 0; j < mapWidth; j++) {
                            combine(fields.get(i + 1).get(j), fields.get(i).get(j));
                        }
                    }
                }
            }
            if (controls == Controls.RESET) {

                MapBuilder mapBuilder = new MapBuilder();
                fields = mapBuilder.buildMap(mapNumber);
                completedPoints = 0;
                mapHeight = fields.size();
                mapWidth = fields.get(0).size();
                movesTaken = 0;
            }

            checkForFinish();
        }
    }

    public void combine(Field fieldA, Field fieldB) {

        if (fieldA.hasObject()) {
            if (fieldA.getObject().hasBehavior("moving")) {

                if (fieldB.hasBehavior("empty")) {
                    if (fieldB.hasObject()) {
                        if (fieldA.getObject().hasBehavior("objective") && fieldB.getObject().hasBehavior("objective-end")) {
                            finish(fieldA, fieldB);
                        }
                    } else {
                        swap(fieldA, fieldB);
                    }
                }

            }
        }
    }

    private void swap(Field fieldA, Field fieldB) {

        Field swapField = new Field("NONE", null, fieldB.getX(), fieldB.getY());
        swapField.copyField(fieldB);

        fieldB.copyField(fieldA);
        fieldA.copyField(swapField);

    }

    private void finish(Field fieldA, Field fieldB) {
        fieldA.setObject(new Object("NONE", null));
        fieldB.setObject(new Object("FINISHED"));
        completedPoints++;
    }

    private void checkForFinish() {
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
            loadNextMap();
        }


    }

    private void loadNextMap() {


        MapBuilder mapBuilder = new MapBuilder();

        fields = mapBuilder.buildMap(mapNumber + 1);
        pointToComplete = getAllFieldsByBehavior("objective").size();
        completedPoints = 0;
        mapNumber = mapNumber + 1;
        movesTaken = 0;
        goals = mapBuilder.getGoals(this);

        mapHeight = fields.size();
        mapWidth = fields.get(1).size();

    }

    private ArrayList<Field> getAllFieldsByBehavior(String behavior) {

        ArrayList<Field> fieldsList = new ArrayList<Field>();

        for (ArrayList<Field> row : fields) {
            for (Field field : row) {
                if (field.hasBehavior(behavior)) {
                    fieldsList.add(field);
                }
                if (field.hasObject()) {
                    if (field.getObject().hasBehavior(behavior)) {
                        fieldsList.add(field);
                    }
                }

            }
        }
        return fieldsList;

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
}
