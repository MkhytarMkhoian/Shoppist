package com.justplay1.shoppist.communication.sync.toserver;

import com.justplay1.shoppist.communication.network.ServerRequests;
import com.justplay1.shoppist.database.BaseTable;
import com.justplay1.shoppist.database.ShoppingListContact;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.ShoppingListItem;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;
import java.util.Set;

/**
 * Created by Mkhytar on 27.03.2016.
 */
public class ShoppingListItemsSyncToServer extends SyncToServer<ShoppingListItem> {
    @Override
    protected void addItemsToServer(Set<ShoppingListItem> toAdd) throws ParseException {
        if (toAdd.size() > 0) {
            List<ParseObject> object = ServerRequests.addShoppingListItems(toAdd);
            for (ParseObject item : object) {
                for (ShoppingListItem list : toAdd) {
                    if (list.getId().equals(item.getString(ShoppingListContact.ShoppingListItems.LIST_ITEM_ID))) {
                        list.setServerId(item.getObjectId());
                        list.setTimestamp(item.getLong(ServerRequests.TIMESTAMP));
                        list.setDirty(false);
                    }
                }
            }
        }
    }

    @Override
    protected void updateItemsToServer(Set<ShoppingListItem> toUpdate) throws ParseException {
        ServerRequests.updateShoppingListItems(toUpdate);
    }

    @Override
    protected BaseTable<ShoppingListItem> getTable() {
        return App.get().getTablesHolder().getShoppingListItemTable();
    }

    @Override
    protected String getTag() {
        return "ShoppingListItems to";
    }
}
