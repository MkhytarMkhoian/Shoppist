package com.justplay1.shoppist.models;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import com.justplay1.shoppist.R;

/**
 * Created by Mkhytar on 13.11.2015.
 */
public enum NotificationStatus {

    UPDATE(R.color.bg_swipe_item_left, R.string.status_updated, R.drawable.ic_edit_white),
    DELETE(R.color.bg_swipe_item_delete_red, R.string.status_deleted, R.drawable.ic_delete_white),
    NEW(R.color.green_500, R.string.status_new, R.drawable.ic_add_white_24dp),
    BOUGHT(R.color.green_500, R.string.status_bought, R.drawable.ic_add_shopping_cart),
    NOT_BOUGHT(R.color.bg_swipe_item_left, R.string.status_not_bought, R.drawable.ic_delete_from_shopping_cart);

    @ColorRes
    public final int mColorRes;
    public final int mNameRes;

    @DrawableRes
    public final int mImageRes;

    NotificationStatus(int color, int nameRes, int imageRes) {
        mColorRes = color;
        mNameRes = nameRes;
        mImageRes = imageRes;
    }
}
