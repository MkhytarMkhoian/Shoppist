package com.justplay1.shoppist.communication.managers;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.communication.ThreadExecutor;
import com.justplay1.shoppist.database.TablesHolder;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.ShoppingList;
import com.justplay1.shoppist.models.ShoppingListItem;
import com.justplay1.shoppist.widget.ShoppistWidgetProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Mkhitar on 20.01.2015.
 */
public class ShoppingListItemsManager {

    private TablesHolder mTablesHolder;

    public ShoppingListItemsManager(TablesHolder cacheHolder) {
        this.mTablesHolder = cacheHolder;
    }

    public Collection<ShoppingListItem> getShoppingListItems(final ShoppingList parentList) {
        return mTablesHolder.getShoppingListItemTable().getShoppingListItems(parentList.getId());
    }

    public Cursor getShoppingListItemsCursor(final ShoppingList parentList) {
        return getShoppingListItemsCursor(parentList.getId());
    }

    public Cursor getShoppingListItemsCursor(final String parentList) {
        return mTablesHolder.getShoppingListItemTable().getShoppingListItemsCursor(parentList);
    }

    public Collection<ShoppingListItem> getAllShoppingListItems() {
        return mTablesHolder.getShoppingListItemTable().getAllShoppingListItems().values();
    }

    public Collection<ShoppingListItem> getDirtyShoppingListItems() {
        return mTablesHolder.getShoppingListItemTable().getDirtyShoppingListItems().values();
    }

    public Collection<ShoppingListItem> getAllShoppingListItems(long timestamp) {
        return mTablesHolder.getShoppingListItemTable().getAllShoppingListItems(timestamp).values();
    }

    public Collection<ShoppingListItem> getShoppingListItems(String shoppingListId, List<String> ids) {
        return mTablesHolder.getShoppingListItemTable().getShoppingListItems(shoppingListId, ids).values();
    }

    public void getShoppingListItem(final String shoppingListId, final String itemId, ExecutorListener<ShoppingListItem> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<ShoppingListItem>() {
            @Override
            public ShoppingListItem call() throws Exception {
                return mTablesHolder.getShoppingListItemTable().getShoppingListItem(shoppingListId, itemId);
            }
        }, listener);
    }

    public void add(final ShoppingListItem item, ExecutorListener<ShoppingListItem> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<ShoppingListItem>() {
            @Override
            public ShoppingListItem call() throws Exception {
                mTablesHolder.getShoppingListItemTable().put(item);

                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return item;
            }
        }, listener);
    }

    public void addAll(final List<ShoppingListItem> items, ExecutorListener<List<ShoppingListItem>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<List<ShoppingListItem>>() {
            @Override
            public List<ShoppingListItem> call() throws Exception {
                mTablesHolder.getShoppingListItemTable().put(items);
                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return items;
            }
        }, listener);
    }

    public void update(final ShoppingListItem newItem, ExecutorListener<ShoppingListItem> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<ShoppingListItem>() {
            @Override
            public ShoppingListItem call() throws Exception {
                mTablesHolder.getShoppingListItemTable().update(newItem);

                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return newItem;
            }
        }, listener);
    }

    public void updateAll(final List<ShoppingListItem> oldItem, final List<ShoppingListItem> newItem, ExecutorListener<List<ShoppingListItem>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<List<ShoppingListItem>>() {
            @Override
            public List<ShoppingListItem> call() throws Exception {
                mTablesHolder.getShoppingListItemTable().update(newItem);

                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return newItem;
            }
        }, listener);
    }

    public void moveTo(final ShoppingList newList,
                       @NonNull final List<ShoppingListItem> items,
                       ExecutorListener<List<ShoppingListItem>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<List<ShoppingListItem>>() {
            @Override
            public List<ShoppingListItem> call() throws Exception {
                mTablesHolder.getShoppingListItemTable().moveTo(newList.getId(), items);

                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return items;
            }
        }, listener);
    }

    public void copyTo(final ShoppingList newList, @NonNull final List<ShoppingListItem> items,
                       ExecutorListener<List<ShoppingListItem>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<List<ShoppingListItem>>() {
            @Override
            public List<ShoppingListItem> call() throws Exception {
                mTablesHolder.getShoppingListItemTable().copyTo(newList.getId(), items);

                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return items;
            }
        }, listener);
    }

    public void deleteAll(final Collection<ShoppingListItem> items, ExecutorListener<Collection<ShoppingListItem>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Collection<ShoppingListItem>>() {
            @Override
            public Collection<ShoppingListItem> call() throws Exception {
                List<ShoppingListItem> toDelete = new ArrayList<>();
                List<ShoppingListItem> toUpdate = new ArrayList<>();
                for (ShoppingListItem item : items) {
                    if (item.getServerId() == null) {
                        toDelete.add(item);
                    } else {
                        item.setDirty(true);
                        item.setDelete(true);
                        toUpdate.add(item);
                    }
                }
                mTablesHolder.getShoppingListItemTable().update(toUpdate);
                mTablesHolder.getShoppingListItemTable().delete(toDelete);

                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return items;
            }
        }, listener);
    }
}
