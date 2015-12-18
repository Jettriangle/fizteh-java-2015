package ru.fizteh.fivt.students.Jettriangle.miniORM;

/**
 * Created by rtriangle on 18.12.15.
 */

class RightNameResolver {
    static final String REGEX = "[A-Za-z0-9_-]*";
    public static Boolean isGood(String name) {
        return name.matches(REGEX);
    }
}
