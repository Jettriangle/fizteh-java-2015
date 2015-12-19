package ru.fizteh.fivt.students.Jettriangle.collectionquery.impl;

/**
 * Created by rtriangle on 19.12.15.
 */
import java.lang.reflect.InvocationTargetException;

public interface Query<R> {
    Iterable<R> execute() throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException;
}
