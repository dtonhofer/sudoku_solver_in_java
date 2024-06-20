package name.heavycarbon.sudoku_solver;

import java.util.LinkedList;
import java.util.List;

public class CourseraSettings {

    public static List<Setting> getSettings() {
        List<Setting> settings = new LinkedList<>();
        settings.add(Setting.f(2, 1, Value.v7));
        settings.add(Setting.f(2, 2, Value.v8));
        settings.add(Setting.f(2, 5, Value.v1));
        settings.add(Setting.f(3, 5, Value.v2));
        settings.add(Setting.f(3, 8, Value.v3));
        settings.add(Setting.f(4, 4, Value.v3));
        settings.add(Setting.f(4, 5, Value.v4));
        settings.add(Setting.f(5, 2, Value.v6));
        settings.add(Setting.f(5, 5, Value.v5));
        settings.add(Setting.f(5, 8, Value.v1));
        settings.add(Setting.f(6, 5, Value.v6));
        settings.add(Setting.f(7, 5, Value.v7));
        settings.add(Setting.f(8, 1, Value.v5));
        settings.add(Setting.f(8, 2, Value.v4));
        settings.add(Setting.f(8, 5, Value.v8));
        settings.add(Setting.f(8, 6, Value.v6));
        settings.add(Setting.f(8, 7, Value.v9));
        settings.add(Setting.f(8, 8, Value.v7));
        settings.add(Setting.f(9, 5, Value.v9));
        return settings;
    }
}
