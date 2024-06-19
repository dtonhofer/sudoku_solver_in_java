package name.heatwarp.sudoku_solver;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 * A single Sudoku "Cell", i.e. a "logical variable", which also stores its allowed domain:
 * A subset of the values v1...v9.
 *
 * We store the values that are possible for this Cell as an array of booleans.
 * If there is no value left that is possible for this Cell, we have reached a dead end and need to backtrack.
 * If there is a single value that is possible for this Cell, it is completely determined, at least for the
 * current search tree.
 * The Cell is immutable.
 */

public class Cell implements Iterable<Value> {

    private final static Value[] byIndex = new Value[]{Value.v1,Value.v2,Value.v3,Value.v4,Value.v5,Value.v6,Value.v7,Value.v8,Value.v9};

    private static Value valueFromIndex(int index) {
        return byIndex[index];
    }

    /**
     * Array representation of the domain: an array of booleans, one for each possible value.
     * If every entry in the domain is false, the domain is empty, which means that the
     * solution that is based on this Cell is not acceptable.
     */

    private final boolean[] domain = new boolean[Value.values().length];

    /**
     * Domain cardinality, corresponds to the number of "true" in domain[]
     */

    public final int cardinality;

    /**
     * Helpers
     */

    private void setAll(boolean x) {
        Arrays.fill(domain, x);
    }

    private void copyAll(boolean[] otherDomain) {
        System.arraycopy(otherDomain, 0, domain, 0, domain.length);
    }

    private int determineCardinality() {
        int c = 0;
        for (boolean b : domain) {
            if (b) {
                c++;
            }
        }
        return c;
    }

    /**
     * Create a Cell for which "all the values are acceptable"
     */

    public Cell() {
        setAll(true);
        cardinality = domain.length;
    }

    /**
     * Create a Cell which is completely determined, i.e. "exactly one value is acceptable"
     */

    public Cell(final Value v) {
        setAll(false);
        domain[v.index] = true;
        cardinality = 1;
    }

    /**
     * Create a Cell with the domain of "other" and with additionally the value "impossible"
     * removed. That value may already no longer be in the domain of "other". This may result
     * in the "empty domain".
     */

    public Cell(final Cell other, final Value impossible) {
        copyAll(other.domain);
        domain[impossible.index] = false;
        cardinality = determineCardinality();
    }

    /**
     * This Cell is fully determined (the domain has cardinality 1)
     */

    public boolean isUnique() {
        return cardinality == 1;
    }

    /**
     * This Cell has no possibilities left (the domain is empty)
     */

    public boolean isEmpty() {
        return cardinality == 0;
    }

    /**
     * Check whether a value is still possible, i.e. still in the domain
     */

    public boolean domainContains(final Value v) {
        return domain[v.index];
    }

    /**
     * Update an array of counters, one for each value
     */

    public void updateDomainTotals(Map<Value, List<Location>> valueToLocation, Location yourLocation) {
        for (Value v : Value.values()) {
            if (domain[v.index]) {
                valueToLocation.get(v).add(yourLocation);
            }
        }
    }

    /**
     * Determine whether there is a difference between two cells
     */

    public static boolean isDifferent(Cell a, Cell b) {
        if (a.cardinality != b.cardinality) {
            return true;
        }
        for (Value v : Value.values()) {
           if (a.domain[v.index] != b.domain[v.index])  {
               return true;
           }
        }
        return false;
    }

    /**
     * Assuming the domain is of size 1, return the corresponding value.
     */

    public Value getUnique() {
        if (!isUnique()) {
            throw new IllegalStateException("Cell is not 'unique'");
        }
        for (Value v : Value.values()) {
            if (domain[v.index]) {
                return v;
            }
        }
        throw new IllegalStateException("Can't be here");
    }

    /**
     * Check whether 'v' is the single entry in the domain'
     */

    public boolean isUnique(final Value v) {
        return isUnique() && domainContains(v);
    }

    /**
     * Print the domain as a comma-separated list of variables
     */

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        boolean addComma = false;
        for (Value v : Value.values()) {
            if (domain[v.index]) {
                if (addComma) {
                    b.append(",");
                }
                b.append(v);
                addComma = true;
            }
        }
        return b.toString();
    }

    public class DomainIterator implements Iterator<Value> {

        private int pos; // always points to the next valid entry or past end

        public DomainIterator() {
            pos = 0;
            searchForward();
        }

        private void searchForward() {
            while (pos < domain.length && !domain[pos]) {
                pos++;
            }
        }

        @Override
        public boolean hasNext() {
            return (pos < domain.length);
        }

        @Override
        public Value next() {
            if (!hasNext()) {
                throw new IllegalStateException("Past end");
            }
            else {
                int myPos = pos;
                pos++;
                searchForward();
                return valueFromIndex(myPos);
            }
        }
    }

    @Override
    public Iterator<Value> iterator() {
        return new DomainIterator();
    }

}
