package com.justplay1.shoppist.models;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mkhytar on 12.10.2015.
 */
@IntDef({SwipeActionType.MOVE_ITEM, SwipeActionType.EDIT_ITEM, SwipeActionType.DELETE_ITEM})
@Retention(RetentionPolicy.SOURCE)
public @interface SwipeActionType {
    int MOVE_ITEM = 0;
    int DELETE_ITEM = 1;
    int EDIT_ITEM = 2;
}
