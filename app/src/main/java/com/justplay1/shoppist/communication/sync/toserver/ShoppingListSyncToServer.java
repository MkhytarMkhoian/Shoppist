package com.justplay1.shoppist.communication.sync.toserver;

import com.justplay1.shoppist.communication.network.ServerRequests;
import com.justplay1.shoppist.database.BaseTable;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.ShoppingList;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;
import java.util.Set;

/**
 * Created by Mkhytar on 27.03.2016.
 */
public class ShoppingListSyncToServer extends SyncToServer<ShoppingList> {
    @Override
    protected void addItemsToServer(Set<ShoppingList> toAdd) throws ParseException {
        if (toAdd.size() > 0) {
            List<ParseObject> object = ServerRequests.addShoppingLists(toAdd);
            toAdd.clear();
            for (ParseObject item : object) {
                ShoppingList list = new ShoppingList(item);
                toAdd.add(list);
            }
        }
    }

    @Override
    protected void updateItemsToServer(Set<ShoppingList> toUpdate) throws ParseException {
        ServerRequests.updateShoppingLists(toUpdate);
    }

    @Override
    protected BaseTable<ShoppingList> getTable() {
        return App.get().getTablesHolder().getShoppingListTable();
    }

    @Override
    protected String getTag() {
        return "Shopping Lists to";
    }
}
