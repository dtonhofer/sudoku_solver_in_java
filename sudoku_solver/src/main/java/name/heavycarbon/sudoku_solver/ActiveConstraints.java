package name.heavycarbon.sudoku_solver;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The keeper of "active constraints", i.e. constraints that will have to
 * examine the board and maybe update it. This is an element on the call stack
 * and is recreated and dropped often.
 */

public class ActiveConstraints {

    private final Set<ValueCellBijectionConstraint> activeAsSet = new HashSet<>(); // changes over time
    private final List<ValueCellBijectionConstraint> activeAsQueue = new LinkedList<>(); // changes over time

    public int getCount() {
        return activeAsSet.size();
    }

    public boolean isEmpty() {
        return activeAsSet.isEmpty();
    }

    public ValueCellBijectionConstraint getNext() {
        if (isEmpty()) {
            return null;
        }
        else {
            ValueCellBijectionConstraint res = activeAsQueue.removeFirst();
            activeAsSet.remove(res);
            return res;
        }
    }

    public void activateAllConstraintsMonitoringThisLocation(Location loc, ValueCellBijectionConstraint sender, Constraints constraints) {
        List<ValueCellBijectionConstraint> affected = constraints.getConstraintMonitoringThisLocation(loc);
        for (ValueCellBijectionConstraint c : affected) {
            // The test can handle the case of sender == null
            if (!c.equals(sender) && !activeAsSet.contains(c)) {
                activeAsSet.add(c);
                activeAsQueue.add(c);
            }
        }
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        boolean addComma = false;
        for (ValueCellBijectionConstraint c : activeAsQueue) {
            if (addComma) {
                buf.append(",");
            }
            buf.append(c);
            addComma = true;
        }
        return buf.toString();
    }
}
