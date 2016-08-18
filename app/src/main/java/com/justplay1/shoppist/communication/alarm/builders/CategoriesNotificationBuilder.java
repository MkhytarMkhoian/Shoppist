package com.justplay1.shoppist.communication.alarm.builders;

import android.content.Context;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.models.NotificationTypeConstants;

import java.util.List;
import java.util.Map;

/**
 * Created by Mkhytar on 13.11.2015.
 */
public class CategoriesNotificationBuilder extends NotificationBuilder<Category> {

    public CategoriesNotificationBuilder(Context context) {
        super(context);
    }

    @Override
    protected int getNotificationType() {
        return NotificationTypeConstants.NOTIFICATION_SYNC_CATEGORIES_DATA_ID;
    }

    @Override
    protected String getTitleForNewObjects(List<Category> items) {
        return mContext.getResources().getQuantityString(R.plurals.added_categories, items.size(), items.size());
    }

    @Override
    protected String getTitleForUpdatedObjects(Map<Category, Category> items) {
        return mContext.getResources().getQuantityString(R.plurals.update_categories, items.size(), items.size());
    }

    @Override
    protected String getTitleForDeletedObjects(List<Category> items) {
        return mContext.getResources().getQuantityString(R.plurals.delete_categories, items.size(), items.size());
    }

    @Override
    protected String getName(Category item) {
        return item.getName();
    }
}
