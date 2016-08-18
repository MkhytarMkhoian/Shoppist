package com.justplay1.shoppist.database;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.justplay1.shoppist.database.DBHelper.Tables;
import com.justplay1.shoppist.database.ShoppingListContact.Categories;
import com.justplay1.shoppist.database.ShoppingListContact.Currencies;
import com.justplay1.shoppist.database.ShoppingListContact.Notifications;
import com.justplay1.shoppist.database.ShoppingListContact.Products;
import com.justplay1.shoppist.database.ShoppingListContact.ShoppingListItems;
import com.justplay1.shoppist.database.ShoppingListContact.ShoppingLists;
import com.justplay1.shoppist.database.ShoppingListContact.Units;
import com.justplay1.shoppist.utils.ShoppistUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mkhitar on 25.04.2015.
 */
public class ShoppingListProvider extends ContentProvider {

    protected static final String TAG = ShoppingListProvider.class.getSimpleName();

    protected static final int CATEGORIES = 100;
    protected static final int CATEGORIES_ID = 101;
    protected static final int CATEGORIES_LAST_TIMESTAMP = 102;

    protected static final int ALL_SHOPPING_LIST_ITEMS = 300;
    protected static final int SHOPPING_LIST_ITEM_ID = 301;
    protected static final int SHOPPING_LIST_ITEMS = 302;
    protected static final int SHOPPING_LIST_ITEMS_LAST_TIMESTAMP = 303;
    protected static final int MARK_SHOPPING_LIST_ITEMS_AS_DELETED = 304;

    protected static final int SHOPPING_LISTS = 400;
    protected static final int SHOPPING_LIST_ID = 401;
    protected static final int SHOPPING_LISTS_LAST_TIMESTAMP = 402;

    protected static final int UNITS = 600;
    protected static final int UNITS_ID = 601;
    protected static final int UNITS_LAST_TIMESTAMP = 602;

    protected static final int CURRENCY = 700;
    protected static final int CURRENCY_ID = 701;
    protected static final int CURRENCY_LAST_TIMESTAMP = 702;

    protected static final int PRODUCTS = 900;
    protected static final int PRODUCTS_ID = 901;
    protected static final int PRODUCTS_LAST_TIMESTAMP = 902;

    protected static final int NOTIFICATIONS = 1000;
    protected static final int NOTIFICATIONS_ID = 1001;
    protected static final int NEW_NOTIFICATIONS_COUNT = 1002;

    protected UriMatcher mUriMatcher;
    protected DBHelper mDbHelper;

    protected String getAuthority() {
        return "com.justplay1.shoppist." + DBHelper.DB_NAME;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = DBHelper.getInstance(getContext());
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        mUriMatcher.addURI(getAuthority(), Tables.NOTIFICATIONS, NOTIFICATIONS);
        mUriMatcher.addURI(getAuthority(), Tables.NOTIFICATIONS + "/#", NOTIFICATIONS_ID);
        mUriMatcher.addURI(getAuthority(), Tables.NOTIFICATIONS + "/" + Notifications.NEW_NOTIFICATIONS_COUNT, NEW_NOTIFICATIONS_COUNT);

        mUriMatcher.addURI(getAuthority(), Tables.PRODUCTS, PRODUCTS);
        mUriMatcher.addURI(getAuthority(), Tables.PRODUCTS + "/" + Products.LAST_TIMESTAMP, PRODUCTS_LAST_TIMESTAMP);
        mUriMatcher.addURI(getAuthority(), Tables.PRODUCTS + "/*", PRODUCTS_ID);

        mUriMatcher.addURI(getAuthority(), Tables.CATEGORIES, CATEGORIES);
        mUriMatcher.addURI(getAuthority(), Tables.CATEGORIES + "/" + Categories.LAST_TIMESTAMP, CATEGORIES_LAST_TIMESTAMP);
        mUriMatcher.addURI(getAuthority(), Tables.CATEGORIES + "/*", CATEGORIES_ID);

        mUriMatcher.addURI(getAuthority(), Tables.CURRENCY, CURRENCY);
        mUriMatcher.addURI(getAuthority(), Tables.CURRENCY + "/" + Currencies.LAST_TIMESTAMP, CURRENCY_LAST_TIMESTAMP);
        mUriMatcher.addURI(getAuthority(), Tables.CURRENCY + "/*", CURRENCY_ID);

        mUriMatcher.addURI(getAuthority(), Tables.UNITS, UNITS);
        mUriMatcher.addURI(getAuthority(), Tables.UNITS + "/" + Units.LAST_TIMESTAMP, UNITS_LAST_TIMESTAMP);
        mUriMatcher.addURI(getAuthority(), Tables.UNITS + "/*", UNITS_ID);

        mUriMatcher.addURI(getAuthority(), Tables.SHOPPING_LISTS, SHOPPING_LISTS);
        mUriMatcher.addURI(getAuthority(), Tables.SHOPPING_LISTS + "/" + ShoppingLists.LAST_TIMESTAMP, SHOPPING_LISTS_LAST_TIMESTAMP);
        mUriMatcher.addURI(getAuthority(), Tables.SHOPPING_LISTS + "/*", SHOPPING_LIST_ID);

        mUriMatcher.addURI(getAuthority(), Tables.SHOPPING_LIST_ITEMS, ALL_SHOPPING_LIST_ITEMS);
        mUriMatcher.addURI(getAuthority(), Tables.SHOPPING_LIST_ITEMS + "/" + ShoppingListItems.LAST_TIMESTAMP, SHOPPING_LIST_ITEMS_LAST_TIMESTAMP);
        mUriMatcher.addURI(getAuthority(), Tables.SHOPPING_LIST_ITEMS + "/" + ShoppingListItems.MARK_SHOPPING_LIST_ITEMS_AS_DELETED + "/*", MARK_SHOPPING_LIST_ITEMS_AS_DELETED);
        mUriMatcher.addURI(getAuthority(), Tables.SHOPPING_LIST_ITEMS + "/*/*", SHOPPING_LIST_ITEM_ID);
        mUriMatcher.addURI(getAuthority(), Tables.SHOPPING_LIST_ITEMS + "/*", SHOPPING_LIST_ITEMS);

        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        List<String> segments;
        Cursor cursor;
        switch (mUriMatcher.match(uri)) {
            case NEW_NOTIFICATIONS_COUNT:
                cursor = db.rawQuery("SELECT COUNT(" + Notifications.TIME + ") FROM " + Tables.NOTIFICATIONS +
                        " WHERE " + selection, selectionArgs);
                break;

            case CATEGORIES_LAST_TIMESTAMP:
                cursor = db.rawQuery("SELECT MAX(" + Categories.TIMESTAMP + ") FROM " + Tables.CATEGORIES, null);
                break;
            case CURRENCY_LAST_TIMESTAMP:
                cursor = db.rawQuery("SELECT MAX(" + Currencies.TIMESTAMP + ") FROM " + Tables.CURRENCY, null);
                break;
            case UNITS_LAST_TIMESTAMP:
                cursor = db.rawQuery("SELECT MAX(" + Units.TIMESTAMP + ") FROM " + Tables.UNITS, null);
                break;
            case PRODUCTS_LAST_TIMESTAMP:
                cursor = db.rawQuery("SELECT MAX(" + Products.TIMESTAMP + ") FROM " + Tables.PRODUCTS, null);
                break;
            case SHOPPING_LISTS_LAST_TIMESTAMP:
                cursor = db.rawQuery("SELECT MAX(" + ShoppingLists.TIMESTAMP + ") FROM " + Tables.SHOPPING_LISTS, null);
                break;
            case SHOPPING_LIST_ITEMS_LAST_TIMESTAMP:
                cursor = db.rawQuery("SELECT MAX(" + ShoppingListItems.TIMESTAMP + ") FROM " + Tables.SHOPPING_LIST_ITEMS, null);
                break;

            case NOTIFICATIONS:
                cursor = db.query(Tables.NOTIFICATIONS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PRODUCTS:
                builder.setTables(Products.PRODUCTS);
                cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PRODUCTS_ID:
                selection = Products.WHERE_PRODUCT_ID;
                selectionArgs = new String[]{uri.getLastPathSegment()};
                cursor = db.query(Tables.PRODUCTS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORIES:
                cursor = db.query(Tables.CATEGORIES, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORIES_ID:
                selection = Categories.WHERE_CATEGORY_ID;
                selectionArgs = new String[]{uri.getLastPathSegment()};
                cursor = db.query(Tables.CATEGORIES, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CURRENCY:
                cursor = db.query(Tables.CURRENCY, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CURRENCY_ID:
                selection = Currencies.WHERE_STRING;
                selectionArgs = new String[]{uri.getLastPathSegment()};
                cursor = db.query(Tables.CURRENCY, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case UNITS:
                cursor = db.query(Tables.UNITS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case UNITS_ID:
                selection = Units.WHERE_STRING;
                selectionArgs = new String[]{uri.getLastPathSegment()};
                cursor = db.query(Tables.UNITS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SHOPPING_LISTS:
                cursor = db.rawQuery(ShoppingLists.buildGetShoppingList(selection), selectionArgs);
                break;
            case SHOPPING_LIST_ID:
                selectionArgs = new String[]{uri.getLastPathSegment()};
                cursor = db.rawQuery(ShoppingLists.buildGetShoppingList(selection), selectionArgs);
                break;
            case ALL_SHOPPING_LIST_ITEMS:
                builder.setTables(ShoppingListItems.ALL_SHOPPING_LIST_ITEMS);
                cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SHOPPING_LIST_ITEMS:
                builder.setTables(ShoppingListItems.SHOPPING_LIST_ITEMS);
                selectionArgs = new String[]{uri.getLastPathSegment()};
                cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SHOPPING_LIST_ITEM_ID:
                builder.setTables(ShoppingListItems.ALL_SHOPPING_LIST_ITEMS);
                segments = uri.getPathSegments();
                ShoppistUtils.concat(selectionArgs, new String[]{segments.get(1), segments.get(2)});

                cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case NOTIFICATIONS:
                return Notifications.CONTENT_TYPE;
            case NOTIFICATIONS_ID:
                return Notifications.CONTENT_ITEM_TYPE;
            case NEW_NOTIFICATIONS_COUNT:
                return Notifications.CONTENT_ITEM_TYPE;

            case PRODUCTS:
                return Products.CONTENT_TYPE;
            case PRODUCTS_ID:
                return Products.CONTENT_ITEM_TYPE;
            case PRODUCTS_LAST_TIMESTAMP:
                return Products.CONTENT_ITEM_TYPE;

            case CATEGORIES:
                return Categories.CONTENT_TYPE;
            case CATEGORIES_ID:
                return Categories.CONTENT_ITEM_TYPE;
            case CATEGORIES_LAST_TIMESTAMP:
                return Categories.CONTENT_ITEM_TYPE;

            case CURRENCY:
                return Currencies.CONTENT_TYPE;
            case CURRENCY_ID:
                return Currencies.CONTENT_ITEM_TYPE;
            case CURRENCY_LAST_TIMESTAMP:
                return Currencies.CONTENT_ITEM_TYPE;

            case UNITS:
                return Units.CONTENT_TYPE;
            case UNITS_ID:
                return Units.CONTENT_ITEM_TYPE;
            case UNITS_LAST_TIMESTAMP:
                return Units.CONTENT_ITEM_TYPE;

            case SHOPPING_LISTS:
                return ShoppingLists.CONTENT_TYPE;
            case SHOPPING_LIST_ID:
                return ShoppingLists.CONTENT_ITEM_TYPE;
            case SHOPPING_LISTS_LAST_TIMESTAMP:
                return ShoppingLists.CONTENT_ITEM_TYPE;
            case MARK_SHOPPING_LIST_ITEMS_AS_DELETED:
                return ShoppingLists.CONTENT_ITEM_TYPE;

            case ALL_SHOPPING_LIST_ITEMS:
                return ShoppingListItems.CONTENT_TYPE;
            case SHOPPING_LIST_ITEMS:
                return ShoppingListItems.CONTENT_TYPE;
            case SHOPPING_LIST_ITEM_ID:
                return ShoppingListItems.CONTENT_ITEM_TYPE;
            case SHOPPING_LIST_ITEMS_LAST_TIMESTAMP:
                return ShoppingListItems.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri result;
        long id;
        switch (mUriMatcher.match(uri)) {
            case NOTIFICATIONS:
                id = db.insertOrThrow(Tables.NOTIFICATIONS, null, values);
                result = Categories.buildLogUri(id + "");
                break;
            case PRODUCTS:
                id = db.insertOrThrow(Tables.PRODUCTS, null, values);
                result = Categories.buildLogUri(id + "");
                break;
            case CATEGORIES:
                id = db.insertOrThrow(Tables.CATEGORIES, null, values);
                result = Categories.buildLogUri(id + "");
                break;
            case CURRENCY:
                id = db.insertOrThrow(Tables.CURRENCY, null, values);
                result = Currencies.buildLogUri(id + "");
                break;
            case UNITS:
                id = db.insertOrThrow(Tables.UNITS, null, values);
                result = Units.buildLogUri(id + "");
                break;
            case SHOPPING_LISTS:
                id = db.insertOrThrow(Tables.SHOPPING_LISTS, null, values);
                result = ShoppingLists.buildShoppingListsUri(id + "");
                break;
            case ALL_SHOPPING_LIST_ITEMS:
                id = db.insertOrThrow(Tables.SHOPPING_LIST_ITEMS, null, values);
                result = ShoppingListItems.buildLogUri(id + "");
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        List<String> segments;
        int result = -1;
        switch (mUriMatcher.match(uri)) {
            case NOTIFICATIONS_ID:
                selection = Notifications.WHERE_STRING;
                selectionArgs = new String[]{uri.getLastPathSegment()};
                result = db.delete(Tables.NOTIFICATIONS, selection, selectionArgs);
                break;
            case NOTIFICATIONS:
                result = db.delete(Tables.NOTIFICATIONS, selection, selectionArgs);
                break;
            case PRODUCTS:
                result = db.delete(Tables.PRODUCTS, selection, selectionArgs);
                break;
            case CATEGORIES:
                result = db.delete(Tables.CATEGORIES, selection, selectionArgs);
                break;
            case CATEGORIES_ID:
                selection = Categories.WHERE_CATEGORY_ID;
                selectionArgs = new String[]{uri.getLastPathSegment()};
                result = db.delete(Tables.CATEGORIES, selection, selectionArgs);
                break;
            case CURRENCY:
                result = db.delete(Tables.CURRENCY, selection, selectionArgs);
                break;
            case CURRENCY_ID:
                selection = Currencies.WHERE_STRING;
                selectionArgs = new String[]{uri.getLastPathSegment()};
                result = db.delete(Tables.CURRENCY, selection, selectionArgs);
                break;
            case UNITS:
                result = db.delete(Tables.UNITS, selection, selectionArgs);
                break;
            case UNITS_ID:
                selection = Units.WHERE_STRING;
                selectionArgs = new String[]{uri.getLastPathSegment()};
                result = db.delete(Tables.UNITS, selection, selectionArgs);
                break;
            case SHOPPING_LISTS:
                result = db.delete(Tables.SHOPPING_LISTS, selection, selectionArgs);
                break;
            case SHOPPING_LIST_ID:
                selection = ShoppingLists.WHERE_STRING;
                selectionArgs = new String[]{uri.getLastPathSegment()};
                result = db.delete(Tables.SHOPPING_LISTS, selection, selectionArgs);
                break;
            case SHOPPING_LIST_ITEMS:
                selection = ShoppingListItems.PARENT_LIST_ID + "=? AND "
                        + ShoppingListItems.SERVER_ID + " is null or " + ShoppingListItems.SERVER_ID + "=?";
                selectionArgs = new String[]{uri.getLastPathSegment(), ""};
                result = db.delete(Tables.SHOPPING_LIST_ITEMS, selection, selectionArgs);
                break;
            case SHOPPING_LIST_ITEM_ID:
                selection = ShoppingListItems.WHERE_STRING;
                segments = uri.getPathSegments();
                selectionArgs = new String[]{segments.get(1), segments.get(2)};
                result = db.delete(Tables.SHOPPING_LIST_ITEMS, selection, selectionArgs);
                break;
            case ALL_SHOPPING_LIST_ITEMS:
                result = db.delete(Tables.SHOPPING_LIST_ITEMS, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return result;
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int result = -1;
        switch (mUriMatcher.match(uri)) {
            case NOTIFICATIONS:
                result = db.update(Tables.NOTIFICATIONS, values, selection, selectionArgs);
                break;
            case PRODUCTS:
                result = db.updateWithOnConflict(Tables.PRODUCTS, values, selection, selectionArgs, SQLiteDatabase.CONFLICT_REPLACE);
                break;
            case CATEGORIES:
                result = db.update(Tables.CATEGORIES, values, selection, selectionArgs);
                break;
            case CURRENCY:
                result = db.update(Tables.CURRENCY, values, selection, selectionArgs);
                break;
            case UNITS:
                result = db.update(Tables.UNITS, values, selection, selectionArgs);
                break;
            case SHOPPING_LIST_ID:
                selection = ShoppingLists.WHERE_STRING;
                selectionArgs = new String[]{uri.getLastPathSegment()};
                result = db.updateWithOnConflict(Tables.SHOPPING_LISTS, values, selection, selectionArgs, SQLiteDatabase.CONFLICT_REPLACE);
                break;
            case MARK_SHOPPING_LIST_ITEMS_AS_DELETED:
                selection = ShoppingListItems.PARENT_LIST_ID + "=?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                result = db.updateWithOnConflict(Tables.SHOPPING_LIST_ITEMS, values, selection, selectionArgs, SQLiteDatabase.CONFLICT_REPLACE);
                break;
            case ALL_SHOPPING_LIST_ITEMS:
                result = db.updateWithOnConflict(Tables.SHOPPING_LIST_ITEMS, values, selection, selectionArgs, SQLiteDatabase.CONFLICT_REPLACE);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return result;
    }
}
