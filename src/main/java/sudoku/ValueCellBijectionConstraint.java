package sudoku;

import sudoku.Cell.Value;

import java.util.*;
import java.util.Map.Entry;

public class ValueCellBijectionConstraint {

    public enum Type {row, col, block}

    private final SortedSet<Location> locations; // locations under this constraint's purview
    private final Type type; // does this check a row, column or block ?
    private final int where0; // and which one (0-based)
    private final int hashCode; // a buffer for the hash code
    private final String name; // a buffer for printing

    public ValueCellBijectionConstraint(List<Location> locations, Type type, int where0) {
        SortedSet<Location> tmp = new TreeSet<>();
        tmp.addAll(locations);
        this.locations = Collections.unmodifiableSortedSet(tmp);
        this.name = type + "(" + (where0 + 1) + ")";
        this.type = type;
        this.where0 = where0;
        this.hashCode = this.type.hashCode() ^ Integer.hashCode(this.where0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ValueCellBijectionConstraint)) {
            return false;
        }
        ValueCellBijectionConstraint other = (ValueCellBijectionConstraint) obj;
        return this.type == other.type && this.where0 == other.where0;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Pull the cells whose locations we are interested in from a Board
     */

    private Map<Location, Cell> pullFromBoard(Board board) {
        Map<Location, Cell> pulled = new HashMap<>();
        for (Location loc : locations) {
            pulled.put(loc, board.board.get(loc));
        }
        return pulled;
    }

    /**
     * Helper: Create the initial structure that will be used to collect
     * the Locations associated to a given value over the constraint's
     * locations of interest. Every value will be encountered, so the
     * Map will have an initial entry for each one.
     */

    private static Map<Value, List<Location>> freshValueToLocationsMap() {
        Map<Value, List<Location>> map = new HashMap<>();
        for (Value v : Value.values()) {
            map.put(v, new LinkedList<>());
        }
        return map;
    }

    private static Map<Value, List<Location>> getDomainTotals(Map<Location, Cell> cells) {
        Map<Value, List<Location>> map = freshValueToLocationsMap();
        for (Entry<Location, Cell> e : cells.entrySet()) {
            Location loc = e.getKey();
            Cell cell = e.getValue();
            cell.updateDomainTotals(map, loc);
        }
        return map;
    }

    /**
     * Update the domains of the cells in "cells" according to the rule that:
     * "the cell->value function is injective" (which is exactly "all different")
     * <p>
     * "For any value that appears as unique member of a cell's domain: it cannot appear anywhere else"
     * <p>
     * Throws an exception if this implies an empty domain on some cell.
     * Returns the set of updated locations, which may be empty.
     * If this changed something, it should be run again by the caller.
     */

    private Set<Location> updateDomains_allDifferent(Map<Location, Cell> cells) throws EmptyDomainException {
        Set<Location> res = new HashSet<>();
        for (Location loc : cells.keySet()) { // should be the same as "locations"
            Cell cell = cells.get(loc);
            if (cell.isUnique()) {
                Value v = cell.getUnique();
                for (Location loc2 : cells.keySet()) {
                    if (!loc2.equals(loc)) {
                        Cell oldCell = cells.get(loc2);
                        if (oldCell.domainContains(v)) {
                            Cell updatedCell = new Cell(oldCell, v);
                            if (updatedCell.isEmpty()) {
                                throw new EmptyDomainException();
                            }
                            cells.put(loc2, updatedCell);
                            res.add(loc2);
                        }
                    }
                }
            }
        }
        return res;
    }

    /**
     * Update the domains of the cells in "cells" according to the rule that
     * "the value->cell mapping is injective and total" ("inverse function is all different")
     * <p>
     * "For any domain that contains a value that appears in only that domain among all cells, the domain
     * can be reduced to that value alone."
     * <p>
     * Throws an exception if this implies an empty domain on some cell.
     * Returns the set of updated locations, which may be empty.
     * If this changed something, it should be run again by the caller.
     */

    private Set<Location> updateDomains_inverseAllDifferent(Map<Location, Cell> cells) throws EmptyDomainException {
        Set<Location> res = new HashSet<>();
        Map<Value, List<Location>> map = getDomainTotals(cells);
        for (Value v : Value.values()) {
            int count = map.get(v).size();
            assert (count > 0);
            if (count == 1) {
                Location loc = map.get(v).get(0);
                Cell cell = cells.get(loc);
                if (!cell.isUnique()) {
                    cells.put(loc, new Cell(v)); // now it is unique!
                    res.add(loc);
                }
            }
        }
        return res;
    }

    public static class UpdateResult {

        public final Board board;
        public final Set<Location> changedLocs;

        public UpdateResult(Board board, Set<Location> changedLocs) {
            this.board = board;
            this.changedLocs = changedLocs;
        }

    }

    public UpdateResult update(Board board) throws EmptyDomainException {
        // Collect the current cells into a modifiable structure that we shall update
        Map<Location, Cell> cellsOfInterest = pullFromBoard(board);
        Set<Location> collectedChanges = new HashSet<>();
        boolean changed1 = false;
        boolean changed2 = false;
        do {
            Set<Location> locs1 = updateDomains_allDifferent(cellsOfInterest);
            Set<Location> locs2 = updateDomains_inverseAllDifferent(cellsOfInterest);
            changed1 = !(locs1.isEmpty());
            changed2 = !(locs2.isEmpty());
            collectedChanges.addAll(locs1);
            collectedChanges.addAll(locs2);
        } while (changed1 || changed2);
        Board newBoard;
        if (collectedChanges.isEmpty()) {
            newBoard = board;
        } else {
            newBoard = new Board(board, cellsOfInterest);
        }
        return new UpdateResult(newBoard, collectedChanges);
    }

    /*
     * Check that "Board" is a solution according to this constraint. Throws if not.
     */

    public boolean fulfillsSolutionCriterium(Board board) {
        Map<Value, List<Location>> map = getDomainTotals(pullFromBoard(board));
        for (Entry<Value, List<Location>> e : map.entrySet()) {
            if (e.getValue().size() != 1) {
                throw new IllegalStateException("Not a solution state because constraint " + this + " finds " + e.getValue().size() + " entries for value " + e.getValue());
            }
        }
        return true;
    }
}