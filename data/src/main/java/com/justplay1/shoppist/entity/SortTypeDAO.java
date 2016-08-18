package com.justplay1.shoppist.entity;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mkhytar on 03.06.2016.
 */
@IntDef({SortTypeDAO.SORT_BY_NAME, SortTypeDAO.SORT_BY_CATEGORIES, SortTypeDAO.SORT_BY_PRIORITY, SortTypeDAO.SORT_BY_TIME_CREATED})
@Retention(RetentionPolicy.SOURCE)
public @interface SortTypeDAO {
    int SORT_BY_NAME = 1;
    int SORT_BY_PRIORITY = 2;
    int SORT_BY_CATEGORIES = 3;
    int SORT_BY_TIME_CREATED = 4;
}
