import org.junit.jupiter.api.Test;
import sudoku.*;
import sudoku.Cell.Value;

import java.util.LinkedList;
import java.util.List;

public class TestSudoku {

    @Test
    public void test1() throws EmptyDomainException {
        List<Setting> settings = CourseraSettings.getSettings();
        Sudoku.solve(settings);
    }

    @Test
    public void test2() throws EmptyDomainException {
        List<Setting> settings = new LinkedList<>();

        settings.add(Setting.f(1, 4, Value.v5));
        settings.add(Setting.f(1, 9, Value.v8));


        settings.add(Setting.f(2, 1, Value.v2));
        settings.add(Setting.f(2, 4, Value.v9));
        settings.add(Setting.f(2, 9, Value.v4));

        settings.add(Setting.f(3, 1, Value.v5));
        settings.add(Setting.f(3, 3, Value.v9));
        settings.add(Setting.f(3, 4, Value.v3));
        settings.add(Setting.f(3, 8, Value.v7));

        settings.add(Setting.f(4, 2, Value.v1));
        settings.add(Setting.f(4, 5, Value.v2));
        settings.add(Setting.f(4, 8, Value.v8));

        settings.add(Setting.f(5, 2, Value.v5));
        settings.add(Setting.f(5, 3, Value.v8));

        settings.add(Setting.f(6, 1, Value.v7));
        settings.add(Setting.f(6, 5, Value.v6));
        settings.add(Setting.f(6, 9, Value.v1));

        settings.add(Setting.f(8, 1, Value.v9));
        settings.add(Setting.f(8, 5, Value.v4));
        settings.add(Setting.f(8, 9, Value.v7));

        settings.add(Setting.f(9, 1, Value.v1));
        settings.add(Setting.f(9, 4, Value.v2));
        settings.add(Setting.f(9, 7, Value.v3));

        Board board = Sudoku.solve(settings);
    }

}
