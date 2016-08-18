package com.justplay1.shoppist.database.tables;

import android.content.ContentProviderClient;
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
import com.justplay1.shoppist.database.ShoppingListContact.ShoppingLists;
import com.justplay1.shoppist.models.ShoppingList;
import com.justplay1.shoppist.utils.ShoppistUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mkhitar on 10.01.2015.
 */
public class ShoppingListTable extends BaseTable<ShoppingList> {

    public static final String TAG = ShoppingListTable.class.getSimpleName();

    public ShoppingListTable(Context context) {
        super(context);
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL("create table " + DBHelper.Tables.SHOPPING_LISTS + "(" +
                ShoppingListContact.COLUMN_ID + " integer primary key autoincrement, " +
                ShoppingListContact.ShoppingListColumns.LIST_ID + " text, " +
                ShoppingListContact.ShoppingListColumns.LIST_NAME + " text, " +
                ShoppingListContact.ShoppingListColumns.SERVER_ID + " text, " +
                ShoppingListContact.ShoppingListColumns.PRIORITY + " integer, " +
                ShoppingListContact.ShoppingListColumns.COLOR + " integer, " +
                ShoppingListContact.ShoppingListColumns.MANUAL_SORT_POSITION + " integer DEFAULT 0, " +
                ShoppingListContact.ShoppingListColumns.IS_DELETED + " integer DEFAULT 0, " +
                ShoppingListContact.ShoppingListColumns.IS_DIRTY + " integer DEFAULT 0, " +
                ShoppingListContact.ShoppingListColumns.TIMESTAMP + " integer DEFAULT 0, " +
                ShoppingListContact.ShoppingListColumns.TIME_CREATED + " integer " +
                ");");
    }

    private void notifyChange() {
        mContext.getContentResolver().notifyChange(ShoppingLists.CONTENT_URI, null, false);
    }

    @Override
    public Uri put(ShoppingList obj) throws RemoteException, OperationApplicationException {
        return put(Collections.singletonList(obj))[0].uri;
    }

    @Override
    public ContentProviderResult delete(ShoppingList obj) throws RemoteException, OperationApplicationException {
        return delete(Collections.singletonList(obj))[0];
    }

    @Override
    public ContentProviderResult update(ShoppingList newObj) throws RemoteException, OperationApplicationException {
        return update(Collections.singletonList(newObj))[0];
    }

    @Override
    public ContentProviderResult[] put(Collection<ShoppingList> obj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (ShoppingList shoppingList : obj) {
            operations.add(ContentProviderOperation.newInsert(ShoppingLists.CONTENT_URI).withValues(getValue(shoppingList)).build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyChange();
        return result;
    }

    @Override
    public ContentProviderResult[] delete(Collection<ShoppingList> obj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (ShoppingList shoppingList : obj) {
            operations.add(ContentProviderOperation.newDelete(ShoppingLists.buildShoppingListsUri(shoppingList.getId())).build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyChange();
        return result;
    }

    @Override
    public ContentProviderResult[] update(Collection<ShoppingList> newObj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (ShoppingList shoppingList : newObj) {
            operations.add(ContentProviderOperation.newUpdate(ShoppingLists.buildShoppingListsUri(shoppingList.getId()))
                    .withValues(getValue(shoppingList))
                    .build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyChange();
        return result;
    }

    @Override
    public int clear() {
        int result = mContext.getContentResolver().delete(ShoppingLists.CONTENT_URI, null, null);
        notifyChange();
        return result;
    }

    public long getLastTimestamp() {
        return getValue(ShoppingLists.LAST_TIMESTAMP_URI);
    }

    public Cursor getAllShoppingListsCursor(long timestamp, boolean getDirty) {
        Cursor cursor = null;
        try {
            String selection = ShoppingLists.WITHOUT_DELETED;
            String[] selectionArgs = null;
            if (timestamp > 0 && getDirty) {
                selection = ShoppingLists.TIMESTAMP + " > ? AND " + ShoppingLists.IS_DIRTY + " = ?";
                selectionArgs = new String[]{timestamp + "", "1"};
            } else if (timestamp > 0) {
                selection = ShoppingLists.TIMESTAMP + " > ?";
                selectionArgs = new String[]{timestamp + ""};
            } else if (getDirty) {
                selection = ShoppingLists.IS_DIRTY + " = ?";
                selectionArgs = new String[]{"1"};
            }
            cursor = mContext.getContentResolver().query(ShoppingLists.CONTENT_URI, null, selection, selectionArgs, null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return cursor;
    }

    public Map<String, ShoppingList> getDirtyShoppingLists() {
        return getAllShoppingLists(getAllShoppingListsCursor(0, true));
    }

    public Map<String, ShoppingList> getAllShoppingLists(long timestamp) {
        return getAllShoppingLists(getAllShoppingListsCursor(timestamp, false));
    }

    public Map<String, ShoppingList> getAllShoppingLists() {
        return getAllShoppingLists(getAllShoppingListsCursor(0, false));
    }

    public Map<String, ShoppingList> getShoppingLists(List<String> ids) {
        return getAllShoppingLists(mContext.getContentResolver().query(ShoppingLists.buildShoppingListsUri(ShoppistUtils.toStringItemIds(ids)), null, null, null, null));
    }

    public Cursor getAllShoppingListsCursor() {
        return getAllShoppingListsCursor(0, false);
    }

    public static Map<String, ShoppingList> getAllShoppingLists(Cursor data) {
        Map<String, ShoppingList> shoppingLists = new HashMap<>();
        try {
            if (data != null && !data.isClosed() && data.moveToFirst()) {
                do {
                    ShoppingList shoppingList = new ShoppingList(data);
                    shoppingLists.put(shoppingList.getId(), shoppingList);
                } while (data.moveToNext());
            }
        } finally {
            if (data != null) {
                data.close();
            }
        }
        return shoppingLists;
    }

    public ShoppingList getShoppingList(String id) {
        ShoppingList shoppingList = null;
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(ShoppingLists.CONTENT_URI, null,
                    ShoppingLists.WHERE_STRING, new String[]{id}, null);
            if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
                shoppingList = new ShoppingList(cursor);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return shoppingList;
    }

    @Override
    protected ContentValues getValue(ShoppingList list) {
        ContentValues values = new ContentValues();
        if (list.getServerId() != null) {
            values.put(ShoppingLists.SERVER_ID, list.getServerId());
        }
        values.put(ShoppingLists.LIST_ID, list.getId());
        values.put(ShoppingLists.LIST_NAME, list.getName());
        values.put(ShoppingLists.COLOR, list.getColor());
        values.put(ShoppingLists.PRIORITY, list.getPriority().ordinal());
        values.put(ShoppingLists.TIME_CREATED, list.getTimeCreated());
        if (list.getPosition() != -1){
            values.put(ShoppingLists.MANUAL_SORT_POSITION, list.getPosition());
        }
        values.put(ShoppingLists.IS_DELETED, list.isDelete() ? 1 : 0);
        values.put(ShoppingLists.IS_DIRTY, list.isDirty() ? 1 : 0);
        values.put(ShoppingLists.TIMESTAMP, list.getTimestamp());
        return values;
    }
}
