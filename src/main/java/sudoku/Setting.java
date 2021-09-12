package sudoku;

import sudoku.Cell.Value;

public class Setting {

    public final int row1; // 1- based row
    public final int col1; // 1-based col
    public final Value v;

    public Setting(int row1, int col1, Value v) {
        this.row1 = row1;
        this.col1 = col1;
        this.v = v;
    }

    public static Setting f(int row1, int col1, Value v) {
        return new Setting(row1, col1, v);
    }

    @Override
    public String toString() {
        return "(" + row1 + "," + col1 + ")=" + v;
    }
    
}
