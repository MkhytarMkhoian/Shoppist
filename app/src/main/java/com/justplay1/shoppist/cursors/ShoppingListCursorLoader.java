package com.justplay1.shoppist.cursors;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.justplay1.shoppist.App;

/**
 * Created by Mkhytar on 31.01.2016.
 */
public class ShoppingListCursorLoader extends CursorLoader {

    public static final int ID = 6;

    public ShoppingListCursorLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        return App.get().getShoppingListsManager().getShoppingListsCursor();
    }
}
