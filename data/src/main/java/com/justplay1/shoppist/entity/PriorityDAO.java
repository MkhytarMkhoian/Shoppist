package com.justplay1.shoppist.entity;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mkhitar on 24.11.2014.
 */
@IntDef({PriorityDAO.NO_PRIORITY, PriorityDAO.LOW, PriorityDAO.MEDIUM, PriorityDAO.HIGH})
@Retention(RetentionPolicy.SOURCE)
public @interface PriorityDAO {
    int NO_PRIORITY = 0;
    int LOW = 1;
    int MEDIUM = 2;
    int HIGH = 3;
}


