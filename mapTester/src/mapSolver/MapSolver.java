package mapSolver;

import enums.Controls;
import map.Map;

public class MapSolver {

    Map map;
    boolean solved;
    int moves;

    public MapSolver(Map map) {
        this.map = new Map(map);
        solved = false;
        moves = 99;
    }

    public void solveMap(Map map, int maxIterations) {
        solve(map, 0, maxIterations, Integer.toString(map.getMapNumber()) + ": ", Controls.PLAY);
    }

    public void solve(Map map, int iterator, int maxIterator, String string, Controls control) {
        if (iterator <= maxIterator) {

            if (map.checkForFinish()) {
                System.out.println(string + " moves: " + iterator);
            }

            iterator++;
            if (control == Controls.PLAY) {
                solveFourWays(map, iterator, maxIterator, string);
            } else if (!map.isMapFailed()) {
                if (map.isSomethingHappened()) {
                    solveThreeWays(map, iterator, maxIterator, control, string);
                } else {
                    solveTwoWays(map, iterator, maxIterator, control, string);
                }
            }
        }
    }

    public void solveTwoWays(Map map, int iterator, int maxIterations, Controls controls, String string) {



        if (controls == Controls.LEFT || controls == Controls.RIGHT) {
            Map map2 = new Map(map);
            map.makeMove(Controls.UP);
            solve(map, iterator, maxIterations, string + "U", Controls.UP);
            map2.makeMove(Controls.DOWN);
            solve(map2, iterator, maxIterations, string + "D", Controls.DOWN);
        } else if (controls == Controls.UP || controls == Controls.DOWN) {
            Map map2 = new Map(map);
            map.makeMove(Controls.LEFT);
            solve(map, iterator, maxIterations, string + "L", Controls.LEFT);
            map2.makeMove(Controls.RIGHT);
            solve(map2, iterator, maxIterations, string + "R", Controls.RIGHT);
        }


    }

    public void solveThreeWays(Map map, int iterator, int maxIterations, Controls controls, String string) {



        Map map1 = new Map(map);
        Map map2 = new Map(map);

        if (controls == Controls.LEFT || controls == Controls.RIGHT) {
            map1.makeMove(Controls.UP);
            solve(map1, iterator, maxIterations, string + "U", Controls.UP);
            map2.makeMove(Controls.DOWN);
            solve(map2, iterator, maxIterations, string + "D", Controls.DOWN);
            if (controls == Controls.LEFT) {
                map.makeMove(Controls.RIGHT);
                solve(map, iterator, maxIterations, string + "R", Controls.RIGHT);
            } else {
                map.makeMove(Controls.LEFT);
                solve(map, iterator, maxIterations, string + "L", Controls.LEFT);
            }
        } else if (controls == Controls.UP || controls == Controls.DOWN) {

            map1.makeMove(Controls.LEFT);
            solve(map1, iterator, maxIterations, string + "L", Controls.LEFT);
            map2.makeMove(Controls.RIGHT);
            solve(map2, iterator, maxIterations, string + "R", Controls.RIGHT);
            if (controls == Controls.UP) {
                map.makeMove(Controls.DOWN);
                solve(map, iterator, maxIterations, string + "D", Controls.DOWN);
            } else {
                map.makeMove(Controls.UP);
                solve(map, iterator, maxIterations, string + "U", Controls.UP);
            }

        }

    }

    public void solveFourWays(Map map, int iterator, int maxIterations, String string) {

        Map map1 = new Map(map);
        Map map2 = new Map(map);
        Map map3 = new Map(map);


        map.makeMove(Controls.RIGHT);
        solve(map, iterator, maxIterations, string + "R", Controls.RIGHT);
        System.out.println("25% - R done");
        map2.makeMove(Controls.DOWN);
        solve(map2, iterator, maxIterations, string + "D", Controls.DOWN);
        System.out.println("50% - D done");
        map1.makeMove(Controls.UP);
        solve(map1, iterator, maxIterations, string + "U", Controls.UP);
        System.out.println("75% - U done");
        map3.makeMove(Controls.LEFT);
        solve(map3, iterator, maxIterations, string + "L", Controls.LEFT);
        System.out.println("100% - L done");

    }

    public void solveSimpleMap(String moves, Map map, int iterator, int maxIterations) {

        Map mapL = new Map(map);
        Map mapR = new Map(map);
        Map mapD = new Map(map);
        iterator++;
        if (map.getCompletedPoints() >= map.getPointToComplete())
            System.out.println(moves + " moves: " + map.getMovesTaken());

        if (iterator <= maxIterations && !map.isMapFailed()) {

            mapD.makeMove(Controls.DOWN);
            solveNext(moves + "D", mapD, iterator, maxIterations, Controls.DOWN);
            mapL.makeMove(Controls.LEFT);
            solveNext(moves + "L", mapL, iterator, maxIterations, Controls.LEFT);
            mapR.makeMove(Controls.RIGHT);
            solveNext(moves + "R", mapR, iterator, maxIterations, Controls.RIGHT);
            map.makeMove(Controls.UP);
            solveNext(moves + "U", map, iterator, maxIterations, Controls.UP);
        }

    }

    public void solveNext(String moves, Map map, int iterator, int maxIterations, Controls controls) {

        Map map1 = new Map(map);

        if (map.getCompletedPoints() >= map.getPointToComplete())
            System.out.println(moves + " moves: " + map.getMovesTaken());
        else if (iterator <= maxIterations)
            if (map.isSomethingHappened()) {
                solveSimpleMap(moves, map, iterator, maxIterations);
            } else if (controls == Controls.LEFT) {
                iterator++;
                map1.makeMove(Controls.UP);
                solveNext(moves + "U", map1, iterator, maxIterations, Controls.UP);
                map.makeMove(Controls.DOWN);
                solveNext(moves + "D", map, iterator, maxIterations, Controls.DOWN);
            } else if (controls == Controls.RIGHT) {
                iterator++;
                map1.makeMove(Controls.UP);
                solveNext(moves + "U", map1, iterator, maxIterations, Controls.UP);
                map.makeMove(Controls.DOWN);
                solveNext(moves + "D", map, iterator, maxIterations, Controls.DOWN);
            } else if (controls == Controls.UP) {
                iterator++;
                map1.makeMove(Controls.LEFT);
                solveNext(moves + "L", map1, iterator, maxIterations, Controls.LEFT);
                map.makeMove(Controls.RIGHT);
                solveNext(moves + "R", map, iterator, maxIterations, Controls.RIGHT);
            } else if (controls == Controls.DOWN) {
                iterator++;
                map.makeMove(Controls.RIGHT);
                solveNext(moves + "R", map, iterator, maxIterations, Controls.RIGHT);
                map1.makeMove(Controls.LEFT);
                solveNext(moves + "L", map1, iterator, maxIterations, Controls.RIGHT);
            }

    }

    public void solveNextThreeWays(String moves, Map map, int iterator, int maxIterations, Controls controls) {

        Map map1 = new Map(map);
        Map map2 = new Map(map);
        iterator++;
        if (iterator <= maxIterations) {
            if (map.getCompletedPoints() >= map.getPointToComplete())
                System.out.println(moves + " moves: " + map.getMovesTaken());
            if (controls == Controls.LEFT) {
                map1.makeMove(Controls.UP);
                solveNext(moves + "U", map1, iterator, maxIterations, Controls.UP);
                map.makeMove(Controls.DOWN);
                solveNext(moves + "D", map, iterator, maxIterations, Controls.DOWN);
                map2.makeMove(Controls.RIGHT);
                solveNext(moves + "R", map2, iterator, maxIterations, Controls.RIGHT);

            } else if (controls == Controls.RIGHT) {
                map1.makeMove(Controls.UP);
                solveNext(moves + "U", map1, iterator, maxIterations, Controls.UP);
                map.makeMove(Controls.DOWN);
                solveNext(moves + "D", map, iterator, maxIterations, Controls.DOWN);
                map2.makeMove(Controls.LEFT);
                solveNext(moves + "L", map2, iterator, maxIterations, Controls.LEFT);

            } else if (controls == Controls.UP) {
                map1.makeMove(Controls.LEFT);
                solveNext(moves + "L", map1, iterator, maxIterations, Controls.LEFT);
                map.makeMove(Controls.DOWN);
                solveNext(moves + "D", map, iterator, maxIterations, Controls.DOWN);
                map2.makeMove(Controls.RIGHT);
                solveNext(moves + "R", map2, iterator, maxIterations, Controls.RIGHT);

            } else if (controls == Controls.DOWN) {
                map1.makeMove(Controls.UP);
                solveNext(moves + "U", map1, iterator, maxIterations, Controls.UP);
                map.makeMove(Controls.LEFT);
                solveNext(moves + "L", map, iterator, maxIterations, Controls.LEFT);
                map2.makeMove(Controls.RIGHT);
                solveNext(moves + "R", map2, iterator, maxIterations, Controls.RIGHT);
            }
        }
    }

}
