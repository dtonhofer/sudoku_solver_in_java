package name.heavycarbon.sudoku_solver;

/**
 * An immutable (row,col) tuple that can be used as key in maps.
 */

public class Location implements Comparable<Location> {

    final public int row0; // 0-based row number
    final public int col0; // 0-based column number

    private final static int COUNT = 9; // we have 9x9 locations

    private final static Location[][] storage = new Location[COUNT][COUNT]; // storage for all instances we will ever need

    static {
        for (int row0 = 0; row0 < COUNT; row0++) {
            for (int col0 = 0; col0 < COUNT; col0++) {
                storage[row0][col0] = new Location(row0, col0);
            }
        }
    }

    /**
     * Factory call. Obtain a Location with 0-based values
     */

    public static Location obtain0(int row0, int col0) {
        return storage[row0][col0];
    }

    /**
     * Factory call. Obtain a Location with 1-based values
     */

    public static Location obtain1(int row1, int col1) {
        return obtain0(row1 - 1, col1 - 1);
    }

    /**
     * Private constructor. Only obtain Locations through the factory call!
     */

    private Location(int row0, int col0) {
        this.row0 = row0;
        this.col0 = col0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Location other)) {
            return false;
        } else {
            return this.row0 == other.row0 && this.col0 == other.col0;
        }
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(row0) ^ Integer.hashCode(col0);
    }

    @Override
    public String toString() {
        return "(0-based:" + row0 + "," + col0 + ")";
    }

    public String toStringOneBased() {
        return "(1-based:" + (row0 + 1) + "," + (col0 + 1) + ")";
    }

    @Override
    public int compareTo(Location other) {
        if (other.row0 == this.row0) {
            return other.col0 - this.col0;
        } else {
            return other.row0 - this.row0;
        }
    }
}
