package map;

import enums.Animations;
import enums.Controls;
import view.Move;

import java.awt.*;
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
    private ArrayList<Point> objectsCords;
    private ArrayList<Move> eventList;

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
        objectsCords = new ArrayList<>();


        for (ArrayList<Field> row : map.getFields()) {
            ArrayList<Field> fieldInRow = new ArrayList<Field>();
            for (Field field : row) {
                fieldInRow.add(new Field(field));

                if (field.hasObject()) {
                    objectsCords.add(new Point(field.getX(), field.getY()));
                }
            }
            this.fields.add(fieldInRow);
        }
    }

    private void loadMap(int mapNumber) {

        MapBuilder mapBuilder = new MapBuilder();

        fields = new ArrayList<>();
        fields = mapBuilder.buildMap(mapNumber);
        pointToComplete = getAllFieldsByBehavior("objective").size();
        completedPoints = 0;
        this.mapNumber = mapNumber;
        movesTaken = 0;
        goals = mapBuilder.getGoals(this);

        mapHeight = fields.size();
        mapWidth = fields.get(1).size();

        objectsCords = new ArrayList<>();

        refreshObjectCords();

    }

    public  ArrayList<Move> makeMove(Controls controls) {

        eventList=new ArrayList<>();

        if (controls != Controls.NONE) {
            movesTaken++;

            if (controls == Controls.LEFT) {

                help.utils.HelpUtils.sortByX(objectsCords);
                for (Point cords : objectsCords) {

                    for (double x = cords.getX(); x > 0; x--) {
                        combine(fields.get((int) cords.getY()).get((int) x), fields.get((int) cords.getY()).get((int) x - 1));
                    }
                }
            } else if (controls == Controls.RIGHT) {

                help.utils.HelpUtils.sortByXReverse(objectsCords);
                for (Point cords : objectsCords) {

                    for (double x = cords.getX(); x < mapWidth - 1; x++) {
                        combine(fields.get((int) cords.getY()).get((int) x), fields.get((int) cords.getY()).get((int) x + 1));

                    }
                }
            } else if (controls == Controls.DOWN) {

                help.utils.HelpUtils.sortByY(objectsCords);
                for (Point cords : objectsCords) {

                    for (double y = cords.getY(); y > 0; y--) {
                        combine(fields.get((int) y).get((int) cords.getX()), fields.get((int) y - 1).get((int) (cords.getX())));
                    }
                }

            } else if (controls == Controls.UP) {

                help.utils.HelpUtils.sortByYReverse(objectsCords);
                for (Point cords : objectsCords) {

                    for (double y = cords.getY(); y < mapHeight - 1; y++) {
                        combine(fields.get((int) y).get((int) cords.getX()), fields.get((int) y + 1).get((int) (cords.getX())));
                    }
                }

            } else if (controls == Controls.RESET) {

                loadMap(mapNumber);

            }

            calculateObjectCords(eventList);
            checkForFinish();


        }

        return eventList;
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



        eventList.add(new Move(fieldA.getX(),fieldA.getY(), Animations.MOVE, fieldB.getX(),fieldB.getY()));


        Field swapField = new Field("NONE", null, fieldB.getX(), fieldB.getY());
        swapField.copyField(fieldB);

        fieldB.copyField(fieldA);
        fieldA.copyField(swapField);


    }

    private void finish(Field fieldA, Field fieldB) {
        fieldA.setObject(new Object("NONE", null));
        fieldB.setObject(new Object("FINISHED"));
        completedPoints++;
        eventList.add(new Move(fieldA.getX(),fieldA.getY(),Animations.GOAL,fieldB.getX(),fieldB.getY()));
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




            loadMap(mapNumber + 1);
        }


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

    private void calculateObjectCords(ArrayList<Move> eventList){
        for (Move move: eventList){
            for (Point point: objectsCords){
                if(move.getFromX()==point.getX() && move.getFromY()==point.getY()){
                    point.setLocation(move.getToX(),move.getToY());
                    break;
                }
            }
        }
    }

    private void refreshObjectCords() {
        objectsCords = new ArrayList<>();
        for (ArrayList<Field> row : fields) {
            ArrayList<Field> fieldInRow = new ArrayList<Field>();
            for (Field field : row) {
                if (field.hasObject()) {
                    objectsCords.add(new Point(field.getX(), field.getY()));
                }
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
}
