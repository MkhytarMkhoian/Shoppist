package com.justplay1.shoppist.models;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mkhytar on 14.02.2016.
 */
@IntDef({SearchItemType.PRODUCT})
@Retention(RetentionPolicy.SOURCE)
public @interface SearchItemType {
    int PRODUCT = 1;
}
