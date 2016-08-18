package com.justplay1.shoppist.models;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mkhytar on 08.12.2015.
 */

@IntDef(flag = true, value = {
        NotificationTypeConstants.NOTIFICATION_ALARM_ID,
        NotificationTypeConstants.NOTIFICATION_SYNC_CATEGORIES_DATA_ID,
        NotificationTypeConstants.NOTIFICATION_SYNC_CURRENCIES_DATA_ID,
        NotificationTypeConstants.NOTIFICATION_SYNC_GOODS_DATA_ID,
        NotificationTypeConstants.NOTIFICATION_SYNC_SHOPPING_LIST_DATA_ID,
        NotificationTypeConstants.NOTIFICATION_SYNC_SHOPPING_LIST_ITEMS_DATA_ID,
        NotificationTypeConstants.NOTIFICATION_SYNC_UNITS_DATA_ID,

})
@Retention(RetentionPolicy.SOURCE)
public @interface NotificationType {
}
