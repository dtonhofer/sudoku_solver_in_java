package sudoku;

import sudoku.Cell.Value;

import java.util.*;
import java.util.Map.Entry;

/**
 * Model a "current state" of the Sudoku 9 x 9 board (i.e. an "order 3" board).
 * A Board instance is immutable. Any modified "current state" is a new
 * Board instance derived from a previous Board instance.
 */

public class Board {

    public final static int COUNT = 9;

    public final Map<Location, Cell> board; // immutable map of 81 Locations

    private static void fillBoardWithFreshCells(Map<Location, Cell> board) {
        for (int row0 = 0; row0 < COUNT; row0++) {
            for (int col0 = 0; col0 < COUNT; col0++) {
                board.put(Location.obtain0(row0, col0), new Cell()); // cell will have full domain of values
            }
        }
    }

    /**
     * Create a Board where there are no constraints on the cells
     * (i.e. all the cells have a full domain)
     */

    public Board() {
        Map<Location, Cell> board = new HashMap<>();
        fillBoardWithFreshCells(board);
        this.board = Collections.unmodifiableMap(board);
    }

    /**
     * Create a new Board from an existing one and a set of Location-Cell pairs
     */

    public Board(Board oldBoard, Map<Location, Cell> overrides) {
        Map<Location, Cell> board = new HashMap<>();
        for (Entry<Location, Cell> e : oldBoard.board.entrySet()) {
            Location loc = e.getKey();
            if (overrides.containsKey(loc)) {
                board.put(loc, overrides.get(loc));
            } else {
                board.put(loc, e.getValue());
            }
        }
        this.board = Collections.unmodifiableMap(board);
    }

    /**
     * Create a new Board with newCell at loc replacing the corresponding cell of oldBoard.
     */

    public Board(Board oldBoard, Cell newCell, Location loc) {
        Map<Location, Cell> board = new HashMap<>();
        board.putAll(oldBoard.board);
        board.put(loc, newCell);
        this.board = Collections.unmodifiableMap(board);
    }

    public static class SetResult {

        public final Board board;
        public final boolean changed;

        public SetResult(Board board, boolean changed) {
            this.board = board;
            this.changed = changed;
        }
    }

    /**
     * Create a new Board from the existing one by forcing
     * the Cell at location "loc" to have domain with the unique element v.
     * If the Cell at location "loc" does not have v as a domain element,
     * an EmptyDomainException is raised.
     * Also return a boolean indicating whether the board has actually changed.
     */

    public SetResult setCell(Location loc, Value v) throws EmptyDomainException {
        Cell oldCell = board.get(loc);
        if (!oldCell.domainContains(v)) {
            throw new EmptyDomainException();
        }
        Cell newCell = new Cell(v); // "just v"
        if (!Cell.isDifferent(oldCell, newCell)) {
            return new SetResult(this, false);
        } else {
            Board newBoard = new Board(this, newCell, loc);
            return new SetResult(newBoard, true);
        }
    }

    /**
     * We are done if every cell has a domain with cardinality 1
     */

    public boolean isDone() {
        for (Cell c : board.values()) {
            if (!c.isUnique()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Select the cells with smallest domain as we want to "guess" going forward.
     * Cells are given by their Locations.
     */

    public List<Location> findCellsWithSmallestDomainThatIsNotOne() {
        List<Location> res = new LinkedList<>();
        int fewestSoFar = Value.values().length + 1;
        for (Entry<Location, Cell> e : board.entrySet()) {
            Cell cell = e.getValue();
            Location loc = e.getKey();
            if (!cell.isUnique()) {
                if (cell.cardinality < fewestSoFar) {
                    res.clear();
                    res.add(loc);
                    fewestSoFar = cell.cardinality;
                } else if (cell.cardinality == fewestSoFar) {
                    res.add(loc);
                } else {
                    // skip this cell
                }
            }
        }
        return res;
    }

    /**
     * Stringification
     */

    public String toString() {
        StringBuilder[] rowStringBuilder = new StringBuilder[COUNT];
        List<Integer> widths = new ArrayList<>(COUNT);
        for (int row0 = 0; row0 < COUNT; row0++) {
            rowStringBuilder[row0] = new StringBuilder();
        }
        for (int col0 = 0; col0 < COUNT; col0++) {
            String[] thisColText = new String[COUNT];
            int width = 0;
            for (int row0 = 0; row0 < COUNT; row0++) {
                thisColText[row0] = board.get(Location.obtain0(row0, col0)).toString();
                width = Math.max(width, thisColText[row0].length());
            }
            widths.add(width);
            for (int row0 = 0; row0 < COUNT; row0++) {
                if (col0 % 3 == 0) {
                    if (col0 > 0) {
                        rowStringBuilder[row0].append(" | ");
                    } else {
                        rowStringBuilder[row0].append("| ");
                    }
                } else {
                    rowStringBuilder[row0].append(" . ");
                }
                rowStringBuilder[row0].append(thisColText[row0]);
                for (int s = 0; s < width - thisColText[row0].length(); s++) {
                    rowStringBuilder[row0].append(" ");
                }
            }
        }
        for (int row0 = 0; row0 < COUNT; row0++) {
            rowStringBuilder[row0].append(" |");
        }
        return makeFullString(widths, rowStringBuilder);
    }

    private static String makeFullString(List<Integer> widths, StringBuilder[] rowStringBuilder) {
        StringBuilder res = new StringBuilder();
        for (int row0 = 0; row0 < COUNT; row0++) {
            if (row0 % 3 == 0) {
                res.append(makeSeparatorLine(widths));
                res.append("\n");
            }
            res.append(rowStringBuilder[row0]);
            res.append("\n");
        }
        res.append(makeSeparatorLine(widths));
        return res.toString();
    }

    private static String makeSeparatorLine(List<Integer> widths) {
        StringBuilder res = new StringBuilder();
        res.append("+");
        for (Integer w : widths) {
            res.append("-");
            for (int s = 0; s < w; s++) {
                res.append("-");
            }
            res.append("-+");
        }
        return res.toString();
    }
}
