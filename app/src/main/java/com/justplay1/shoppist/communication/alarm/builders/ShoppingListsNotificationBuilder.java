package com.justplay1.shoppist.communication.alarm.builders;

import android.content.Context;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.NotificationTypeConstants;
import com.justplay1.shoppist.models.ShoppingList;

import java.util.List;
import java.util.Map;

/**
 * Created by Mkhytar on 13.11.2015.
 */
public class ShoppingListsNotificationBuilder extends NotificationBuilder<ShoppingList> {

    public ShoppingListsNotificationBuilder(Context context) {
        super(context);
    }

    @Override
    protected int getNotificationType() {
        return NotificationTypeConstants.NOTIFICATION_SYNC_SHOPPING_LIST_DATA_ID;
    }

    @Override
    protected String getTitleForNewObjects(List<ShoppingList> items) {
        return mContext.getResources().getQuantityString(R.plurals.added_shopping_lists, items.size(), items.size());
    }

    @Override
    protected String getTitleForUpdatedObjects(Map<ShoppingList, ShoppingList> items) {
        return mContext.getResources().getQuantityString(R.plurals.update_shopping_lists, items.size(), items.size());
    }

    @Override
    protected String getTitleForDeletedObjects(List<ShoppingList> items) {
        return mContext.getResources().getQuantityString(R.plurals.delete_shopping_lists, items.size(), items.size());
    }

    @Override
    protected String getName(ShoppingList item) {
        return item.getName();
    }
}
