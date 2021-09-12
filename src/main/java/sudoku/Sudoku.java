package sudoku;

import sudoku.Cell.Value;
import sudoku.ValueCellBijectionConstraint.UpdateResult;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/*

 +-----> 2nd coordinate "col" going from 1..9
 |
 |
 V
 1st coordinate "row" going from 1..9

 */

public class Sudoku {

    /*
     * Apply a Setting to a Board, generating a new Board (which is returned) while
     * registering the location of the changed cell in "changedLocs".
     */

    private static Board setupOne(final Board board, final Setting s, final Set<Location> changedLocs) throws EmptyDomainException {
        Location loc = Location.obtain1(s.row1, s.col1);
        Board.SetResult sres = board.setCell(loc, s.v);
        if (sres.changed) {
            changedLocs.add(loc);
        }
        return sres.board; // the new board
    }

    /**
     * Apply multiple settings in turn.
     */

    private static Board applySettings(final Board board, final List<Setting> settings, final Set<Location> changedLocs) throws EmptyDomainException {
        Board curBoard = board;
        for (Setting s : settings) {
            curBoard = setupOne(curBoard, s, changedLocs);
        }
        return curBoard;
    }

    private static Board initialBoardSetupAndConstraintActivation(final List<Setting> settings, final Constraints cos, final ActiveConstraints acos) throws EmptyDomainException {
        Set<Location> changedLocs = new HashSet<>();
        Board board = applySettings(new Board(), settings, changedLocs);
        Printing.initialChangedLocations(changedLocs);
        for (Location loc : changedLocs) {
            acos.activateAllConstraintsMonitoringThisLocation(loc, null, cos);
        }
        Printing.initialActiveConstraints(acos);
        return board;
    }


    private static Board propagate(int depth, final Constraints cos, final ActiveConstraints acos, final Board board) throws EmptyDomainException {
        Board curBoard = board;
        Printing.atPropagateStart(depth, acos);
        // loop until quiescence
        int counter = 0;
        while (!acos.isEmpty()) {
            ValueCellBijectionConstraint curCos = acos.getNext();
            UpdateResult ur = curCos.update(curBoard);
            for (Location loc : ur.changedLocs) {
                acos.activateAllConstraintsMonitoringThisLocation(loc, curCos, cos);
            }
            Printing.inPropagate(depth, counter, curCos, acos, ur.changedLocs);
            counter++;
            curBoard = ur.board;
        }
        return curBoard;
    }

    private static Board find(int depth, final Constraints cos, final ActiveConstraints acos, final Board board) throws EmptyDomainException {
        Board newBoard = propagate(depth, cos, acos, board);
        Printing.afterPropagationQuiescence(depth, newBoard);
        if (newBoard.isDone()) {
            return newBoard; // solution found
        } else {
            Printing.needToFindByTrial(depth);
            return findByTrial(depth, cos, newBoard); // returns solution or throws
        }
    }

    private static Board findByTrial(int depth, final Constraints cos, final Board board) throws EmptyDomainException {
        List<Location> smallest = board.findCellsWithSmallestDomainThatIsNotOne();
        Location pivotLoc = smallest.get(0);
        Cell cell = board.board.get(pivotLoc);
        assert (cell.cardinality > 1);
        Printing.findByTrialStart(depth, pivotLoc, cell);
        for (Value v : cell) {
            try {
                Printing.findByTrialInLoop(depth, v);
                Board trialBoard = new Board(board, new Cell(v), pivotLoc);
                ActiveConstraints trialAcos = new ActiveConstraints();
                trialAcos.activateAllConstraintsMonitoringThisLocation(pivotLoc, null, cos);
                return find(depth + 1, cos, trialAcos, trialBoard); // returns normally on success
            } catch (EmptyDomainException ex) {
                // failure - try again with the next value
                Printing.findByTrialFailed(depth, v, pivotLoc);
            }
        }
        // no solution on this branch!
        Printing.findByTrialFailedCompletely(depth, pivotLoc);
        throw new EmptyDomainException();
    }

    public static Board solve(List<Setting> settings) throws EmptyDomainException {
        Constraints cos = new Constraints(); // just need 1 instance for the whole program
        ActiveConstraints acos = new ActiveConstraints(); // valid only in this stack frame
        Board initBoard = initialBoardSetupAndConstraintActivation(settings, cos, acos);
        Printing.initialBoard(initBoard);
        Board solBoard = find(0, cos, acos, initBoard);
        if (!solBoard.isDone()) {
            throw new IllegalStateException("Board is not done!");
        }
        cos.fulfillsSolutionCriterium(solBoard); // throws if a constraint is violated or a cell has a domain of cardinality != 1
        Printing.solutionBoard(solBoard);
        return solBoard;
    }

    public static void main(String[] argv) throws EmptyDomainException {
        try {
            solve(CourseraSettings.getSettings());
        }
        catch (Exception exe) {
            System.out.println(exe);
        }
    }

}
