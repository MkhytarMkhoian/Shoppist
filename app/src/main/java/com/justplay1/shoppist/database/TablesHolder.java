package com.justplay1.shoppist.database;

import android.content.Context;
import android.util.Log;

import com.justplay1.shoppist.database.tables.CategoriesTable;
import com.justplay1.shoppist.database.tables.CurrenciesTable;
import com.justplay1.shoppist.database.tables.ProductsTable;
import com.justplay1.shoppist.database.tables.ShoppingListItemTable;
import com.justplay1.shoppist.database.tables.ShoppingListTable;
import com.justplay1.shoppist.database.tables.UnitTable;

/**
 * Created by Mkhitar on 28.10.2014.
 */
public class TablesHolder {

    public static final String TAG = BaseTable.class.getSimpleName();

    protected Context mContext;
    protected ShoppingListTable mShoppingListsTable;
    protected ShoppingListItemTable mShoppingListItemsTable;
    protected CategoriesTable mCategoriesTable;
    protected UnitTable mUnitTable;
    protected CurrenciesTable mCurrenciesTable;
    protected ProductsTable mProductsTable;

    public TablesHolder(Context context) {
        mContext = context;
    }

    public void open() {
        Log.d(TAG, "open");
        mCategoriesTable = new CategoriesTable(mContext);
        mShoppingListItemsTable = new ShoppingListItemTable(mContext);
        mShoppingListsTable = new ShoppingListTable(mContext);
        mCurrenciesTable = new CurrenciesTable(mContext);
        mUnitTable = new UnitTable(mContext);
        mProductsTable = new ProductsTable(mContext);
    }

    public void close() {
        Log.d(TAG, "close");
        DBHelper.getInstance(getContext()).close();
    }

    public void clearAllTables() {
        mCategoriesTable.clear();
        mShoppingListItemsTable.clear();
        mShoppingListsTable.clear();
        mCurrenciesTable.clear();
        mUnitTable.clear();
        mProductsTable.clear();
    }

    public ProductsTable getProductsTable() {
        return mProductsTable;
    }

    public UnitTable getUnitTable() {
        return mUnitTable;
    }

    public CurrenciesTable getCurrenciesTable() {
        return mCurrenciesTable;
    }

    public CategoriesTable getCategoriesTable() {
        return mCategoriesTable;
    }

    public ShoppingListTable getShoppingListTable() {
        return mShoppingListsTable;
    }

    public ShoppingListItemTable getShoppingListItemTable() {
        return mShoppingListItemsTable;
    }

    public Context getContext() {
        return mContext;
    }
}
