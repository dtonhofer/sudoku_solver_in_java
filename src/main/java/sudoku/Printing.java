package sudoku;

import sudoku.Cell.Value;

import java.util.List;
import java.util.Set;

public class Printing {

    private final static String getHeader(int depth, int counter) {
        return getSpaces(depth) + depth + ": " + counter + ": ";
    }

    private final static String getHeader(int depth) {
        return getSpaces(depth) + depth + ": ";
    }

    private final static String getSpaces(int x) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < x; i++) {
            buf.append(" ");
        }
        return buf.toString();
    }

    public static void atPropagateStart(int depth, ActiveConstraints acos) {
        System.out.println(getSpaces(depth) + depth + ": Active constraints at propagation start: " + acos);
    }

    public static void inPropagate(int depth, int counter, ValueCellBijectionConstraint cos, ActiveConstraints acos, Set<Location> changedLocs) {
        String header = getHeader(depth, counter);
        System.out.println(header + "Board updated via constraint " + cos);
        System.out.println(header + "Locations that changed through the update: " + changedLocs);
        System.out.println(header + "Updated active constraints: " + acos);
    }

    public static void afterPropagationQuiescence(int depth, Board board) {
        String header = getHeader(depth);
        System.out.println(header + "Board after propagation quiescence");
        System.out.println(board);
    }

    public static void needToFindByTrial(int depth) {
        String header = getHeader(depth);
        System.out.println(header + "Need to 'find by trial'");
    }

    public static void initialBoard(Board board) {
        System.out.println("Initial board");
        System.out.println("=============");
        System.out.println(board);
    }

    public static void solutionBoard(Board board) {
        System.out.println("Found a solution!");
        System.out.println("=================");
        System.out.println(board);
    }

    public static void findByTrialStart(int depth, Location pivotLoc, Cell cell) {
        String header = getHeader(depth);
        System.out.println(header + "Selected a pivot location " + pivotLoc.toStringOneBased() + " with domain [" + cell + "]");
    }

    public static void findByTrialInLoop(int depth, Value v) {
        String header = getHeader(depth);
        System.out.println(header + "Trying value " + v);
    }

    public static void findByTrialFailed(int depth, Value v, Location pivotLoc) {
        String header = getHeader(depth);
        System.out.println(header + "Value " + v + " at pivot location " + pivotLoc.toStringOneBased() + " failed");
    }

    public static void findByTrialFailedCompletely(int depth, Location pivotLoc) {
        String header = getHeader(depth);
        System.out.println(header + "Everything at pivot location " + pivotLoc.toStringOneBased() + " failed");
    }

    public static void initialChangedLocations(Set<Location> locs) {
        System.out.println("Initial changed locations:\n" + locs);
    }

    public static void initialActiveConstraints(ActiveConstraints acos) {
        System.out.println("Initial active constraints:\n" + acos);
    }

}
