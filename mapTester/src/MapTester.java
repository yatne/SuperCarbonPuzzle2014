import map.Map;
import mapSolver.MapSolver;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class MapTester {

    public static void main(String[] arg) {


        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream("C://output2.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //System.setOut(out);

        MapSolver mapSolver = new MapSolver(new Map(3, 1));

        /*System.out.println("0%: 21");
        mapSolver.solveMap(new Map(5, 21), 8);
        System.out.println("0%: 15");
        mapSolver.solveMap(new Map(5, 15), 10);
        System.out.println("0%: 22");
        mapSolver.solveMap(new Map(5, 22), 11);
        System.out.println("0%: 19");
        mapSolver.solveMap(new Map(5, 19), 15);
        System.out.println("0%: 17");
        mapSolver.solveMap(new Map(5, 17), 16);
        System.out.println("0%: 18");
        mapSolver.solveMap(new Map(5, 18), 18);
        System.out.println("0%: 16");

        mapSolver.solveMap(new Map(5, 16), 17); */
      //  System.out.println("0%: 20");
      //  mapSolver.solveMap(new Map(5, 20), 15);
        System.out.println("0%: 14");
        mapSolver.solveMap(new Map(5, 14), 17);
        System.out.println("0%: 23");
        mapSolver.solveMap(new Map(5, 23), 24);
        System.out.println("0%: 24");
        mapSolver.solveMap(new Map(5, 24), 29);

    }
}
