package com.justplay1.shoppist.communication.managers;

import android.database.Cursor;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.communication.ThreadExecutor;
import com.justplay1.shoppist.database.TablesHolder;
import com.justplay1.shoppist.models.ShoppingList;
import com.justplay1.shoppist.widget.ShoppistWidgetProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Mkhitar on 20.01.2015.
 */
public class ShoppingListsManager {

    private TablesHolder mTablesHolder;

    public ShoppingListsManager(TablesHolder cacheHolder) {
        this.mTablesHolder = cacheHolder;
    }

    public Collection<ShoppingList> getShoppingLists() {
        return mTablesHolder.getShoppingListTable().getAllShoppingLists().values();
    }

    public ShoppingList getShoppingList(String id) {
        return mTablesHolder.getShoppingListTable().getShoppingList(id);
    }

    public Collection<ShoppingList> getShoppingLists(long timestamp) {
        return mTablesHolder.getShoppingListTable().getAllShoppingLists(timestamp).values();
    }

    public Collection<ShoppingList> getDirtyShoppingLists() {
        return mTablesHolder.getShoppingListTable().getDirtyShoppingLists().values();
    }

    public Cursor getShoppingListsCursor() {
        return mTablesHolder.getShoppingListTable().getAllShoppingListsCursor();
    }

    public void add(final ShoppingList list, ExecutorListener<ShoppingList> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<ShoppingList>() {
            @Override
            public ShoppingList call() throws Exception {
                mTablesHolder.getShoppingListTable().put(list);

                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return list;
            }
        }, listener);
    }

    public void updateAll(final Collection<ShoppingList> newItems, ExecutorListener<Collection<ShoppingList>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Collection<ShoppingList>>() {
            @Override
            public Collection<ShoppingList> call() throws Exception {
                mTablesHolder.getShoppingListTable().update(newItems);

                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return newItems;
            }
        }, listener);
    }

    public void update(final ShoppingList newItem, ExecutorListener<ShoppingList> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<ShoppingList>() {
            @Override
            public ShoppingList call() throws Exception {
                mTablesHolder.getShoppingListTable().update(newItem);
                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return newItem;
            }
        }, listener);
    }

    public void deleteAll(final Collection<ShoppingList> lists, ExecutorListener<Collection<ShoppingList>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Collection<ShoppingList>>() {
            @Override
            public Collection<ShoppingList> call() throws Exception {
                List<ShoppingList> toDelete = new ArrayList<>();
                List<ShoppingList> toUpdate = new ArrayList<>();
                for (ShoppingList list : lists) {
                    if (list.getServerId() == null) {
                        toDelete.add(list);
                    } else {
                        list.setDirty(true);
                        list.setDelete(true);
                        toUpdate.add(list);
                    }
                }
                mTablesHolder.getShoppingListTable().delete(toDelete);
                mTablesHolder.getShoppingListTable().update(toUpdate);

                for (ShoppingList list : toUpdate) {
                    mTablesHolder.getShoppingListItemTable().markListItemsAsDeleted(list.getId());
                }
                for (ShoppingList list : toDelete) {
                    mTablesHolder.getShoppingListItemTable().deleteListItems(list.getId());
                }

                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return lists;
            }
        }, listener);
    }
}
