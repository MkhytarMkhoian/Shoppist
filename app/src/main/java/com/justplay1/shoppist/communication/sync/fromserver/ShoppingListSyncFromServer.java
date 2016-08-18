package com.justplay1.shoppist.communication.sync.fromserver;

import com.justplay1.shoppist.communication.alarm.builders.NotificationBuilder;
import com.justplay1.shoppist.communication.alarm.builders.ShoppingListsNotificationBuilder;
import com.justplay1.shoppist.communication.network.ServerRequests;
import com.justplay1.shoppist.database.BaseTable;
import com.justplay1.shoppist.models.ShoppingList;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Mkhytar on 27.03.2016.
 */
public class ShoppingListSyncFromServer extends SyncFromServer<ShoppingList> {
    @Override
    protected Collection<ShoppingList> getDataFromServer(long refreshTime) throws ParseException {
        List<ShoppingList> result = new ArrayList<>();
        List<ParseObject> objects = ServerRequests.getAllShoppingLists(refreshTime);
        for (ParseObject object : objects) {
            result.add(new ShoppingList(object));
        }
        return result;
    }

    @Override
    protected Map<String, ShoppingList> getCache() {
        return tablesHolder.getShoppingListTable().getAllShoppingLists();
    }

    @Override
    protected BaseTable<ShoppingList> getTable() {
        return tablesHolder.getShoppingListTable();
    }

    @Override
    protected NotificationBuilder<ShoppingList> getNotificationBuilder() {
        return new ShoppingListsNotificationBuilder(tablesHolder.getContext());
    }

    @Override
    protected String getTag() {
        return "Shopping Lists from";
    }
}
