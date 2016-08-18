package com.justplay1.shoppist.models;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mkhitar on 05.02.2015.
 */
@IntDef({ItemType.LIST_ITEM, ItemType.HEADER_ITEM, ItemType.CART_HEADER})
@Retention(RetentionPolicy.SOURCE)
public @interface ItemType {
    int LIST_ITEM = 1;
    int HEADER_ITEM = 2;
    int CART_HEADER = 3;
}
