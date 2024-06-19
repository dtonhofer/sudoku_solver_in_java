package name.heatwarp.sudoku_solver;

import java.util.*;
import java.util.Map.Entry;

import name.heatwarp.sudoku_solver.ValueCellBijectionConstraint.Type;

/**
 * All the constraints that are active for a Sudoku board.
 * <p>
 * - For each row, there is a bijection between the 9 possible values and the 9 cells of the row
 * - For each column, there is a bijection between the 9 possible values and the 9 cells of the column
 * - For each 3x3 block, there is a bijection between the 9 possible values and the 9 cells of the block
 * <p>
 * That's 27 constraints.
 * <p>
 * The 27 constraints have no changing internal state and are created once and put into
 * static lookup maps.
 */

public class Constraints {

    private final Map<Location, List<ValueCellBijectionConstraint>> constraintByMonitoredLocation; // immutable
    private final List<ValueCellBijectionConstraint> allConstraints; // immutable

    /**
     * Construction is a bit expensive, but there will be just a single instance of this class
     */

    public Constraints() {
        Map<Location, List<ValueCellBijectionConstraint>> tmpMap = new HashMap<>();
        List<ValueCellBijectionConstraint> tmpAll = new LinkedList<>();
        for (int col0 = 0; col0 < Board.COUNT; col0++) {
            List<Location> locs = getLocationsOfCol(col0);
            ValueCellBijectionConstraint c = new ValueCellBijectionConstraint(locs, Type.col, col0);
            addConstraintByItsMonitoredLocations(locs, c, tmpMap);
            tmpAll.add(c);
        }
        for (int row0 = 0; row0 < Board.COUNT; row0++) {
            List<Location> locs = getLocationsOfRow(row0);
            ValueCellBijectionConstraint c = new ValueCellBijectionConstraint(locs, Type.row, row0);
            addConstraintByItsMonitoredLocations(locs, c, tmpMap);
            tmpAll.add(c);
        }
        for (int block0 = 0; block0 < Board.COUNT; block0++) {
            List<Location> locs = getLocationsOfBlock(block0);
            ValueCellBijectionConstraint c = new ValueCellBijectionConstraint(locs, Type.block, block0);
            addConstraintByItsMonitoredLocations(locs, c, tmpMap);
            tmpAll.add(c);
        }
        this.constraintByMonitoredLocation = makeImmutable(tmpMap);
        this.allConstraints = Collections.unmodifiableList(tmpAll);
    }

    private static Map<Location, List<ValueCellBijectionConstraint>> makeImmutable(Map<Location, List<ValueCellBijectionConstraint>> map) {
        Map<Location, List<ValueCellBijectionConstraint>> res = new HashMap<>();
        for (Entry<Location, List<ValueCellBijectionConstraint>> e : map.entrySet()) {
            res.put(e.getKey(), Collections.unmodifiableList(e.getValue()));
        }
        return Collections.unmodifiableMap(res);
    }

    private static void addConstraintByLocation(Location loc, ValueCellBijectionConstraint c, Map<Location, List<ValueCellBijectionConstraint>> map) {
        if (!map.containsKey(loc)) {
            map.put(loc, new LinkedList<>());
        }
        map.get(loc).add(c);
    }

    private static void addConstraintByItsMonitoredLocations(List<Location> locs, ValueCellBijectionConstraint c, Map<Location, List<ValueCellBijectionConstraint>> map) {
        for (Location loc : locs) {
            addConstraintByLocation(loc, c, map);
        }
    }

    private static List<Location> getLocationsOfCol(final int col0) {
        List<Location> res = new LinkedList<>();
        for (int row0 = 0; row0 < Board.COUNT; row0++) {
            res.add(Location.obtain0(row0, col0));
        }
        return res;
    }

    private static List<Location> getLocationsOfRow(final int row0) {
        List<Location> res = new LinkedList<>();
        for (int col0 = 0; col0 < Board.COUNT; col0++) {
            res.add(Location.obtain0(row0, col0));
        }
        return res;
    }

    private static List<Location> getLocationsOfBlock(final int block0) {
        List<Location> res = new LinkedList<>();
        int topleft_row0 = (block0 / 3) * 3;
        int topleft_col0 = (block0 % 3) * 3;
        for (int rowdd = 0; rowdd < 3; rowdd++) {
            for (int coldd = 0; coldd < 3; coldd++) {
                res.add(Location.obtain0(topleft_row0 + rowdd, topleft_col0 + coldd));
            }
        }
        return res;
    }

    public final List<ValueCellBijectionConstraint> getConstraintMonitoringThisLocation(Location loc) {
        return constraintByMonitoredLocation.get(loc);
    }

    /*
     * Check that "Board" is a solution according to all constraints.
     * Throws if a constraint is violated or a cell has a domain of cardinality != 1
     */

    public void throwIfNotFulfillsSolutionCriterium(Board board) {
        for (ValueCellBijectionConstraint cos : allConstraints) {
            cos.throwIfNotfulfillsSolutionCriterium(board);
        }
    }
}