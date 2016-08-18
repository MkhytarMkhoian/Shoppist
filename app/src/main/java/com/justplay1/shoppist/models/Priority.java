package com.justplay1.shoppist.models;

import android.support.annotation.ColorRes;

import com.justplay1.shoppist.R;

/**
 * Created by Mkhitar on 24.11.2014.
 */
public enum Priority {
    NO_PRIORITY(android.R.color.transparent), LOW(R.color.green_500), MEDIUM(R.color.orange_500), HIGH(R.color.red_color);

    @ColorRes
    public final int mColorRes;

    Priority(int color){
        mColorRes = color;
    }
}
