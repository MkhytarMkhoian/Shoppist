package com.justplay1.shoppist.entity;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mkhitar on 07.02.2015.
 */
@IntDef({StatusDAO.DONE, StatusDAO.NOT_DONE})
@Retention(RetentionPolicy.SOURCE)
public @interface StatusDAO {
    int NOT_DONE = 0;
    int DONE = 1;
}
