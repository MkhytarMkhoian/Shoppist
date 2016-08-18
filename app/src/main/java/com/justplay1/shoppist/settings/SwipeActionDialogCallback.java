package com.justplay1.shoppist.settings;

import android.widget.ArrayAdapter;

/**
 * Created by Mkhytar on 19.12.2015.
 */
public interface SwipeActionDialogCallback {

    void onClickPositiveBtn(final String swipeId, final int[] selectedItem, final ArrayAdapter<String>adapter);
}
