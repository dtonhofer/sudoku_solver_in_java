package name.heavycarbon.sudoku_solver;

public enum Value {

    v1(0), v2(1), v3(2), v4(3), v5(4), v6(5), v7(6), v8(7), v9(8);

    // 0-based index corresponding to the value;
    // e.g. for v1, the index is 0; the index is used to access "domain[]"

    final int index;

    Value(int index) {
        this.index = index;
    }

}
