package mapSolver;

import help.utils.Controls;
import map.Map;

public class MapSolver {


    public MapSolver(Map map) {

    }

    public void solveMap(String moves, Map map, int iterator, int maxIterations) {
        Map mapL = new Map(map);
        Map mapR = new Map(map);
        Map mapU = new Map(map);
        Map mapD = new Map(map);
        iterator++;
        if (map.getCompletedPoints() >= map.getPointToComplete())
            System.out.println(moves);

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

    public void solveSimpleMap(String moves, Map map, int iterator, int maxIterations) {

        Map mapL = new Map(map);
        Map mapR = new Map(map);
        Map mapU = new Map(map);
        Map mapD = new Map(map);
        iterator++;
        if (map.getCompletedPoints() >= map.getPointToComplete())
            System.out.println(moves);

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
            System.out.println(moves);

        if (iterator <= maxIterations) {
            if (controls == Controls.LEFT) {

                map1.makeMove(Controls.UP);
                solveNext(moves + "U", map1, iterator, maxIterations, Controls.UP);
                map2.makeMove(Controls.DOWN);
                solveNext(moves + "D", map2, iterator, maxIterations, Controls.DOWN);
            }
            if (controls == Controls.RIGHT) {

                map1.makeMove(Controls.UP);
                solveNext(moves + "U", map1, iterator, maxIterations, Controls.UP);
                map2.makeMove(Controls.DOWN);
                solveNext(moves + "D", map2, iterator, maxIterations, Controls.DOWN);
            }
            if (controls == Controls.UP) {

                map1.makeMove(Controls.LEFT);
                solveNext(moves + "L", map1, iterator, maxIterations, Controls.LEFT);
                map2.makeMove(Controls.RIGHT);
                solveNext(moves + "R", map2, iterator, maxIterations, Controls.RIGHT);
            }
            if (controls == Controls.DOWN) {

                map1.makeMove(Controls.LEFT);
                solveNext(moves + "L", map1, iterator, maxIterations, Controls.LEFT);
                map2.makeMove(Controls.RIGHT);
                solveNext(moves + "R", map2, iterator, maxIterations, Controls.RIGHT);
            }
        }


    }


}
