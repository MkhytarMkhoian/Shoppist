package com.justplay1.shoppist.entity;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mkhytar on 13.11.2015.
 */
@IntDef({NotificationStatus.BOUGHT, NotificationStatus.DELETE, NotificationStatus.NEW,
        NotificationStatus.NOT_BOUGHT, NotificationStatus.UPDATE})
@Retention(RetentionPolicy.SOURCE)
public @interface NotificationStatus {
    int UPDATE = 0;
    int DELETE = 1;
    int NEW = 2;
    int BOUGHT = 3;
    int NOT_BOUGHT = 4;
}
