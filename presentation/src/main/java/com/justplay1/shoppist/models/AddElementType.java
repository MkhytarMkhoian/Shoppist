package com.justplay1.shoppist.models;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mkhytar Mkhoian.
 */
@IntDef({AddElementType.LIST_ITEM, AddElementType.CATEGORY, AddElementType.LIST})
@Retention(RetentionPolicy.SOURCE)
public @interface AddElementType {
    int CATEGORY = 1;
    int LIST = 2;
    int LIST_ITEM = 3;
}
