package com.justplay1.shoppist.cursors;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.justplay1.shoppist.App;

/**
 * Created by Mkhytar on 31.01.2016.
 */
public class ProductsCursorLoader extends CursorLoader {

    public static final int ID = 3;

    public ProductsCursorLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        return App.get().getProductsManager().getAllProductsCursor();
    }
}