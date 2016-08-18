package com.justplay1.shoppist.models;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mkhytar on 08.12.2015.
 */
@IntDef({NotificationType.ALARM, NotificationType.SYNC_CATEGORIES_DATA,
        NotificationType.SYNC_UNITS_DATA, NotificationType.SYNC_CURRENCIES_DATA,
        NotificationType.SYNC_SHOPPING_LIST_DATA, NotificationType.SYNC_SHOPPING_LIST_ITEMS_DATA,
        NotificationType.SYNC_GOODS_DATA})
@Retention(RetentionPolicy.SOURCE)
public @interface NotificationType {
    int ALARM = 1;
    int SYNC_CATEGORIES_DATA = 2;
    int SYNC_UNITS_DATA = 3;
    int SYNC_CURRENCIES_DATA = 4;
    int SYNC_SHOPPING_LIST_DATA = 5;
    int SYNC_SHOPPING_LIST_ITEMS_DATA = 6;
    int SYNC_GOODS_DATA = 7;
}
