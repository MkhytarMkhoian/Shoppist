package com.justplay1.shoppist.di;

import android.content.Context;

/**
 * Created by Mkhytar on 29.06.2016.
 */
public class Data {

    private static Data instance;

    private Data(Context context) {

    }

    public synchronized static Data getInstance(Context context) {
        if (instance == null) {
            instance = new Data(context);
        }
        return instance;
    }
}
