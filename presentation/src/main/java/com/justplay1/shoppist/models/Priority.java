package com.justplay1.shoppist.models;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mkhitar on 24.11.2014.
 */
@IntDef({Priority.NO_PRIORITY, Priority.LOW, Priority.MEDIUM, Priority.HIGH})
@Retention(RetentionPolicy.SOURCE)
public @interface Priority {
    int NO_PRIORITY = 0;
    int LOW = 1;
    int MEDIUM = 2;
    int HIGH = 3;
}


