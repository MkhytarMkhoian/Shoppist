package com.justplay1.shoppist.database.tables;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.justplay1.shoppist.database.BaseTable;
import com.justplay1.shoppist.database.DBHelper;
import com.justplay1.shoppist.database.ShoppingListContact;
import com.justplay1.shoppist.database.ShoppingListContact.Products;
import com.justplay1.shoppist.models.Product;
import com.justplay1.shoppist.models.Unit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mkhitar on 14.02.2015.
 */
public class ProductsTable extends BaseTable<Product> {

    public static final String TAG = ProductsTable.class.getSimpleName();

    public ProductsTable(Context context) {
        super(context);
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL("create table " + DBHelper.Tables.PRODUCTS + "(" +
                ShoppingListContact.COLUMN_ID + " integer primary key autoincrement, " +
                ShoppingListContact.ProductsColumns.PRODUCT_ID + " text, " +
                ShoppingListContact.ProductsColumns.NAME + " text, " +
                ShoppingListContact.ProductsColumns.IS_CREATE_BY_USER + " integer DEFAULT 0, " +
                ShoppingListContact.ProductsColumns.TIME_CREATED + " integer, " +
                ShoppingListContact.ProductsColumns.IS_DELETED + " integer DEFAULT 0, " +
                ShoppingListContact.ProductsColumns.IS_DIRTY + " integer DEFAULT 0, " +
                ShoppingListContact.ProductsColumns.TIMESTAMP + " integer DEFAULT 0, " +
                ShoppingListContact.ProductsColumns.SERVER_ID + " text, " +
                ShoppingListContact.ProductsColumns.CATEGORY_ID + " text, " +
                ShoppingListContact.ProductsColumns.UNIT_ID + " text DEFAULT " + Unit.NO_UNIT_ID +
                ");");
    }

    private void notifyProductsChange() {
        mContext.getContentResolver().notifyChange(ShoppingListContact.Products.CONTENT_URI, null, false);
    }

    @Override
    public Uri put(Product obj) throws RemoteException, OperationApplicationException {
        return put(Collections.singletonList(obj))[0].uri;
    }

    @Override
    public ContentProviderResult delete(Product obj) throws RemoteException, OperationApplicationException {
        return delete(Collections.singletonList(obj))[0];
    }

    @Override
    public ContentProviderResult update(Product newObj) throws RemoteException, OperationApplicationException {
        return update(Collections.singletonList(newObj))[0];
    }

    @Override
    public ContentProviderResult[] put(Collection<Product> obj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (Product product : obj) {
            operations.add(ContentProviderOperation.newInsert(Products.CONTENT_URI).withValues(getValue(product)).build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyProductsChange();
        return result;
    }

    @Override
    public ContentProviderResult[] delete(Collection<Product> obj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (Product product : obj) {
            operations.add(ContentProviderOperation.newDelete(Products.CONTENT_URI)
                    .withSelection(Products.WHERE_PRODUCT_ID, new String[]{product.getId()})
                    .build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyProductsChange();
        return result;
    }

    @Override
    public ContentProviderResult[] update(Collection<Product> newObj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (Product product : newObj) {
            operations.add(ContentProviderOperation.newUpdate(Products.CONTENT_URI)
                    .withSelection(Products.WHERE_PRODUCT_ID, new String[]{product.getId()})
                    .withValues(getValue(product))
                    .build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyProductsChange();
        return result;
    }

    @Override
    public int clear() {
        return mContext.getContentResolver().delete(Products.CONTENT_URI, null, null);
    }

    @Override
    protected ContentValues getValue(Product product) {
        ContentValues values = new ContentValues();
        if (product.getServerId() != null) {
            values.put(Products.SERVER_ID, product.getServerId());
        }
        values.put(Products.PRODUCT_ID, product.getId());
        values.put(Products.NAME, product.getName());
        values.put(Products.CATEGORY_ID, product.getCategory().getId());
        values.put(Products.IS_CREATE_BY_USER, product.isCreateByUser() ? 1 : 0);
        values.put(Products.TIME_CREATED, product.getTimeCreated());
        values.put(Products.IS_DELETED, product.isDelete() ? 1 : 0);
        values.put(Products.IS_DIRTY, product.isDirty() ? 1 : 0);
        values.put(Products.TIMESTAMP, product.getTimestamp());
        values.put(Products.UNIT_ID, product.getUnit().getId());
        return values;
    }

    public long getLastTimestamp() {
        return getValue(Products.LAST_TIMESTAMP_URI);
    }

    public static Map<String, Product> getAllProducts(Cursor data) {
        Map<String, Product> products = new HashMap<>();
        try {
            if (!data.isClosed() && data.moveToFirst()) {
                do {
                    Product product = new Product(data);
                    products.put(product.getId(), product);
                } while (data.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (data != null) {
                data.close();
            }
        }
        return products;
    }

    public Map<String, Product> getAllProducts(long timestamp) {
        return getAllProducts(getAllProductsCursor(timestamp, false));
    }

    public Map<String, Product> getDirtyProducts() {
        return getAllProducts(getAllProductsCursor(0, true));
    }

    public Map<String, Product> getAllProducts() {
        return getAllProducts(getAllProductsCursor(0, false));
    }

    public Cursor getAllProductsCursor() {
        return getAllProductsCursor(0, false);
    }

    public Cursor getAllProductsCursor(long timestamp, boolean getDirty) {
        String selection = Products.WITHOUT_DELETED;
        String[] selectionArgs = null;
        if (timestamp > 0 && getDirty) {
            selection = Products.TIMESTAMP + " > ? AND " + Products.IS_DIRTY + " = ?";
            selectionArgs = new String[]{timestamp + "", 1 + ""};
        } else if (timestamp > 0) {
            selection = Products.TIMESTAMP + " > ?";
            selectionArgs = new String[]{timestamp + ""};
        } else if (getDirty) {
            selection = Products.IS_DIRTY + " = ?";
            selectionArgs = new String[]{1 + ""};
        }
        return mContext.getContentResolver().query(Products.CONTENT_URI, null, selection, selectionArgs, null);
    }
}
