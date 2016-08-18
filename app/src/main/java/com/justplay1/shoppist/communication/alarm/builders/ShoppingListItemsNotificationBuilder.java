package com.justplay1.shoppist.communication.alarm.builders;

import android.content.Context;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.NotificationTypeConstants;
import com.justplay1.shoppist.models.ShoppingList;
import com.justplay1.shoppist.models.ShoppingListItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mkhytar on 13.11.2015.
 */
public class ShoppingListItemsNotificationBuilder extends NotificationBuilderListItems<ShoppingListItem> {

    private Map<String, ShoppingList> mShoppingLists;

    public ShoppingListItemsNotificationBuilder(Context context, Collection<ShoppingList> shoppingLists) {
        super(context);
        mShoppingLists = new HashMap<>();
        for (ShoppingList list : shoppingLists) {
            mShoppingLists.put(list.getId(), list);
        }
    }

    @Override
    protected int getNotificationType() {
        return NotificationTypeConstants.NOTIFICATION_SYNC_SHOPPING_LIST_ITEMS_DATA_ID;
    }

    @Override
    protected String getName(ShoppingListItem item) {
        return item.getName() + " (" + mShoppingLists.get(item.getParentListId()).getName() + ")";
    }

    @Override
    protected String getTitleForStatusDoneObjects(Map<ShoppingListItem, ShoppingListItem> items) {
        return mContext.getResources().getQuantityString(R.plurals.bought_goods, items.size(), items.size());
    }

    @Override
    protected String getTitleForStatusNotDoneObjects(Map<ShoppingListItem, ShoppingListItem> items) {
        return mContext.getResources().getQuantityString(R.plurals.not_bought_goods, items.size(), items.size());
    }

    @Override
    protected String getTitleForUpdatedObjects(Map<ShoppingListItem, ShoppingListItem> items) {
        return mContext.getResources().getQuantityString(R.plurals.update_shopping_list_items, items.size(), items.size());
    }

    @Override
    protected String getTitleForDeletedObjects(List<ShoppingListItem> items) {
        return mContext.getResources().getQuantityString(R.plurals.delete_shopping_list_items, items.size(), items.size());
    }

    @Override
    protected String getTitleForNewObjects(List<ShoppingListItem> items) {
        return mContext.getResources().getQuantityString(R.plurals.added_shopping_list_items, items.size(), items.size());
    }
}
