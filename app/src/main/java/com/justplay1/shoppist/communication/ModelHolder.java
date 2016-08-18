package com.justplay1.shoppist.communication;

import android.content.Context;

import com.justplay1.shoppist.communication.managers.CategoriesManager;
import com.justplay1.shoppist.communication.managers.CurrenciesManager;
import com.justplay1.shoppist.communication.managers.NotificationsManager;
import com.justplay1.shoppist.communication.managers.ProductsManager;
import com.justplay1.shoppist.communication.managers.ShoppingListItemsManager;
import com.justplay1.shoppist.communication.managers.ShoppingListsManager;
import com.justplay1.shoppist.communication.managers.UnitManager;
import com.justplay1.shoppist.database.TablesHolder;

/**
 * Created by Mkhytar on 12.11.2015.
 */
public class ModelHolder {
    private TablesHolder mTablesHolder;
    private ShoppingListsManager mShoppingListsManager;
    private ShoppingListItemsManager mShoppingListItemsManager;
    private CategoriesManager mCategoriesManager;
    private UnitManager mUnitManager;
    private CurrenciesManager mCurrenciesManager;
    private ProductsManager mProductsManager;
    private NotificationsManager mNotificationsManager;

    public ModelHolder(Context context) {
        mTablesHolder = new TablesHolder(context);
        mShoppingListsManager = new ShoppingListsManager(mTablesHolder);
        mShoppingListItemsManager = new ShoppingListItemsManager(mTablesHolder);
        mCategoriesManager = new CategoriesManager(mTablesHolder);
        mUnitManager = new UnitManager(mTablesHolder);
        mCurrenciesManager = new CurrenciesManager(mTablesHolder);
        mProductsManager = new ProductsManager(mTablesHolder);
        mNotificationsManager = new NotificationsManager(context);
    }

    public NotificationsManager getNotificationsManager() {
        return mNotificationsManager;
    }

    public ProductsManager getProductsManager() {
        return mProductsManager;
    }

    public CurrenciesManager getCurrenciesManager() {
        return mCurrenciesManager;
    }

    public UnitManager getUnitManager() {
        return mUnitManager;
    }

    public TablesHolder getTablesHolder() {
        return mTablesHolder;
    }

    public ShoppingListsManager getShoppingListsManager() {
        return mShoppingListsManager;
    }

    public ShoppingListItemsManager getShoppingListItemsManager() {
        return mShoppingListItemsManager;
    }

    public CategoriesManager getCategoriesManager() {
        return mCategoriesManager;
    }

    public void start() {
        mTablesHolder.open();
    }

    public void stop() {
        mTablesHolder.close();
    }

    public void clear() {
        mTablesHolder.clearAllTables();
    }
}
