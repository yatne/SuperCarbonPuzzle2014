package mapSolver;

import enums.Controls;
import map.Map;

public class MapSolver {

    Map map;


    public MapSolver(Map map) {
        this.map = new Map(map);
    }

    public void solveMap(int maxIterations) {

        String moves = "start: ";
        int iterator = 0;

        Map mapL = new Map(map);
        Map mapR = new Map(map);
        Map mapU = new Map(map);
        Map mapD = new Map(map);

        if (map.getCompletedPoints() >= map.getPointToComplete())
            System.out.println(moves + " moves: " + map.getMovesTaken());

        if (iterator <= maxIterations) {
            mapL.makeMove(Controls.LEFT);
            solveMap(moves + "L", mapL, 0, maxIterations);
            mapR.makeMove(Controls.RIGHT);
            solveMap(moves + "R", mapR, 0, maxIterations);
            mapU.makeMove(Controls.UP);
            solveMap(moves + "U", mapU, 0, maxIterations);
            mapD.makeMove(Controls.DOWN);
            solveMap(moves + "D", mapD, 0, maxIterations);

        }


    }

    public void solveMap(String moves, Map map, int iterator, int maxIterations) {

        Map mapL = new Map(map);
        Map mapR = new Map(map);
        Map mapU = new Map(map);
        Map mapD = new Map(map);

        iterator++;
        if (map.getCompletedPoints() >= map.getPointToComplete())
            System.out.println(moves + " moves: " + map.getMovesTaken());

        if (iterator <= maxIterations) {
            mapL.makeMove(Controls.LEFT);
            solveMap(moves + "L", mapL, iterator, maxIterations);
            mapR.makeMove(Controls.RIGHT);
            solveMap(moves + "R", mapR, iterator, maxIterations);
            mapU.makeMove(Controls.UP);
            solveMap(moves + "U", mapU, iterator, maxIterations);
            mapD.makeMove(Controls.DOWN);
            solveMap(moves + "D", mapD, iterator, maxIterations);

        }

    }

    public void tryMap(String moves, Map map, int iterator, int maxIterations) {

        iterator++;
        if (map.getCompletedPoints() >= map.getPointToComplete())
            System.out.println(moves + " moves: " + map.getMovesTaken());

        if (iterator <= maxIterations) {

            tryMapL(moves + "L", new Map(map), iterator, maxIterations);
            tryMapR(moves + "R", new Map(map), iterator, maxIterations);
            tryMapU(moves + "U", new Map(map), iterator, maxIterations);
            tryMapD(moves + "D", new Map(map), iterator, maxIterations);

        }

    }

    public void tryMapL(String moves, Map map, int iterator, int maxIterations) {
        map.makeMove(Controls.LEFT);

        iterator++;
        if (map.getCompletedPoints() >= map.getPointToComplete())
            System.out.println(moves + " moves: " + map.getMovesTaken());

        if (iterator <= maxIterations) {

            tryMapR(moves + "R", new Map(map), iterator, maxIterations);
            tryMapU(moves + "U", new Map(map), iterator, maxIterations);
            tryMapD(moves + "D", new Map(map), iterator, maxIterations);

        }

    }

    public void tryMapR(String moves, Map map, int iterator, int maxIterations) {

        map.makeMove(Controls.RIGHT);

        iterator++;
        if (map.getCompletedPoints() >= map.getPointToComplete())
            System.out.println(moves + " moves: " + map.getMovesTaken());

        if (iterator <= maxIterations) {

            tryMapL(moves + "L", new Map(map), iterator, maxIterations);
            tryMapU(moves + "U", new Map(map), iterator, maxIterations);
            tryMapD(moves + "D", new Map(map), iterator, maxIterations);

        }

    }

    public void tryMapU(String moves, Map map, int iterator, int maxIterations) {

        map.makeMove(Controls.UP);

        iterator++;
        if (map.getCompletedPoints() >= map.getPointToComplete())
            System.out.println(moves + " moves: " + map.getMovesTaken());

        if (iterator <= maxIterations) {

            tryMapL(moves + "L", new Map(map), iterator, maxIterations);
            tryMapR(moves + "R", new Map(map), iterator, maxIterations);
            tryMapD(moves + "D", new Map(map), iterator, maxIterations);

        }

    }

    public void tryMapD(String moves, Map map, int iterator, int maxIterations) {

        map.makeMove(Controls.DOWN);

        iterator++;
        if (map.getCompletedPoints() >= map.getPointToComplete())
            System.out.println(moves + " moves: " + map.getMovesTaken());

        if (iterator <= maxIterations) {

            tryMapL(moves + "L", new Map(map), iterator, maxIterations);
            tryMapR(moves + "R", new Map(map), iterator, maxIterations);
            tryMapU(moves + "U", new Map(map), iterator, maxIterations);


        }

    }

    public void solveSimpleMap(String moves, Map map, int iterator, int maxIterations) {

        Map mapL = new Map(map);
        Map mapR = new Map(map);
        Map mapU = new Map(map);
        Map mapD = new Map(map);
        iterator++;
        if (map.getCompletedPoints() >= map.getPointToComplete())
            System.out.println(moves + " moves: " + map.getMovesTaken());

        if (iterator <= maxIterations) {
            mapL.makeMove(Controls.LEFT);
            solveNext(moves + "L", mapL, iterator, maxIterations, Controls.LEFT);
            mapR.makeMove(Controls.RIGHT);
            solveNext(moves + "R", mapR, iterator, maxIterations, Controls.RIGHT);
            mapU.makeMove(Controls.UP);
            solveNext(moves + "U", mapU, iterator, maxIterations, Controls.UP);
            mapD.makeMove(Controls.DOWN);
            solveNext(moves + "D", mapD, iterator, maxIterations, Controls.DOWN);
        }

    }

    public void solveNext(String moves, Map map, int iterator, int maxIterations, Controls controls) {

        Map map1 = new Map(map);
        Map map2 = new Map(map);
        iterator++;
        if (map.getCompletedPoints() >= map.getPointToComplete())
            System.out.println(moves + " moves: " + map.getMovesTaken());

        if (iterator <= maxIterations)
            if (controls == Controls.LEFT) {

                map1.makeMove(Controls.UP);
                solveNext(moves + "U", map1, iterator, maxIterations, Controls.UP);
                map2.makeMove(Controls.DOWN);
                solveNext(moves + "D", map2, iterator, maxIterations, Controls.DOWN);
            } else if (controls == Controls.RIGHT) {

                map1.makeMove(Controls.UP);
                solveNext(moves + "U", map1, iterator, maxIterations, Controls.UP);
                map2.makeMove(Controls.DOWN);
                solveNext(moves + "D", map2, iterator, maxIterations, Controls.DOWN);
            } else if (controls == Controls.UP) {

                map1.makeMove(Controls.LEFT);
                solveNext(moves + "L", map1, iterator, maxIterations, Controls.LEFT);
                map2.makeMove(Controls.RIGHT);
                solveNext(moves + "R", map2, iterator, maxIterations, Controls.RIGHT);
            } else if (controls == Controls.DOWN) {

                map1.makeMove(Controls.LEFT);
                solveNext(moves + "L", map1, iterator, maxIterations, Controls.LEFT);
                map2.makeMove(Controls.RIGHT);
                solveNext(moves + "R", map2, iterator, maxIterations, Controls.RIGHT);
            }

    }

}
