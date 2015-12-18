package ru.fizteh.fivt.students.Jettriangle.miniORM;

/**
 * Created by rtriangle on 18.12.15.
 */

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;


@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String name() default "";
}
