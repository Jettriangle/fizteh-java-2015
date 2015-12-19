package ru.fizteh.fivt.students.Jettriangle.collectionquery;

/**
 * Created by rtriangle on 19.12.15.
 */

import java.util.function.Function;
import java.util.function.Predicate;

public class Conditions<T> {
    public static <T> Predicate<T> rlike(Function<T, String> expression, String regexp) {
        return element -> expression.apply(element).matches(regexp);
    }
}
