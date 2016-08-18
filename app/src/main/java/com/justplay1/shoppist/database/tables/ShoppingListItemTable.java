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
import android.support.annotation.NonNull;
import android.util.Log;

import com.justplay1.shoppist.database.DBHelper;
import com.justplay1.shoppist.database.BaseTable;
import com.justplay1.shoppist.database.ShoppingListContact;
import com.justplay1.shoppist.database.ShoppingListContact.ShoppingListItems;
import com.justplay1.shoppist.models.ShoppingListItem;
import com.justplay1.shoppist.utils.ShoppistUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mkhitar on 10.01.2015.
 */
public class ShoppingListItemTable extends BaseTable<ShoppingListItem> {

    public static final String TAG = ShoppingListItemTable.class.getSimpleName();

    public ShoppingListItemTable(Context context) {
        super(context);
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL("create table " + DBHelper.Tables.SHOPPING_LIST_ITEMS + "(" +
                ShoppingListContact.COLUMN_ID + " integer primary key autoincrement, " +
                ShoppingListContact.ShoppingListItemColumns.LIST_ITEM_ID + " text, " +
                ShoppingListContact.ShoppingListItemColumns.PARENT_LIST_ID + " text, " +
                ShoppingListContact.ShoppingListItemColumns.LIST_ITEM_NAME + " text, " +
                ShoppingListContact.ShoppingListItemColumns.SERVER_ID + " text, " +
                ShoppingListContact.ShoppingListItemColumns.SHORT_DESCRIPTION + " text, " +
                ShoppingListContact.ShoppingListItemColumns.STATUS + " integer, " +
                ShoppingListContact.ShoppingListItemColumns.PRIORITY + " integer, " +
                ShoppingListContact.ShoppingListItemColumns.PRICE + " real, " +
                ShoppingListContact.ShoppingListItemColumns.QUANTITY + " real, " +
                ShoppingListContact.ShoppingListItemColumns.UNIT_ID + " text, " +
                ShoppingListContact.ShoppingListItemColumns.CURRENCY_ID + " text, " +
                ShoppingListContact.ShoppingListItemColumns.CATEGORY_ID + " text, " +
                ShoppingListContact.ShoppingListItemColumns.TIME_CREATED + " integer, " +
                ShoppingListContact.ShoppingListItemColumns.MANUAL_SORT_POSITION + " integer DEFAULT 0, " +
                ShoppingListContact.ShoppingListItemColumns.IS_DELETED + " integer DEFAULT 0, " +
                ShoppingListContact.ShoppingListItemColumns.IS_DIRTY + " integer DEFAULT 0, " +
                ShoppingListContact.ShoppingListItemColumns.TIMESTAMP + " integer DEFAULT 0 " +
                ");");
    }

    private void notifyParentListChange() {
        mContext.getContentResolver().notifyChange(ShoppingListContact.ShoppingLists.CONTENT_URI, null, false);
    }

    private void notifyCurrentListChange() {
        mContext.getContentResolver().notifyChange(ShoppingListItems.CONTENT_URI, null, false);
    }

    @Override
    public Uri put(ShoppingListItem item) throws RemoteException, OperationApplicationException {
        return put(Collections.singletonList(item))[0].uri;
    }

    @Override
    public ContentProviderResult delete(ShoppingListItem item) throws RemoteException, OperationApplicationException {
        return delete(Collections.singletonList(item))[0];
    }

    @Override
    public ContentProviderResult update(ShoppingListItem newItem) throws RemoteException, OperationApplicationException {
        return update(Collections.singletonList(newItem))[0];
    }

    @Override
    public ContentProviderResult[] put(Collection<ShoppingListItem> obj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (ShoppingListItem shoppingListItem : obj) {
            operations.add(ContentProviderOperation.newInsert(ShoppingListItems.CONTENT_URI).withValues(getValue(shoppingListItem)).build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyParentListChange();
        notifyCurrentListChange();
        return result;
    }

    @Override
    public ContentProviderResult[] delete(Collection<ShoppingListItem> obj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (ShoppingListItem item : obj) {
            operations.add(ContentProviderOperation.newDelete(ShoppingListItems.buildItemIdUri(item.getParentListId(), item.getId())).build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyParentListChange();
        notifyCurrentListChange();
        return result;
    }

    @Override
    public ContentProviderResult[] update(Collection<ShoppingListItem> newItem) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (ShoppingListItem item : newItem) {
            operations.add(ContentProviderOperation.newUpdate(ShoppingListItems.CONTENT_URI)
                    .withSelection(ShoppingListItems.WHERE_STRING, new String[]{item.getParentListId(), item.getId()})
                    .withValues(getValue(item))
                    .build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyParentListChange();
        notifyCurrentListChange();
        return result;
    }


    @Override
    public int clear() {
        int result = mContext.getContentResolver().delete(ShoppingListItems.CONTENT_URI, null, null);
        notifyParentListChange();
        return result;
    }

    @Override
    protected ContentValues getValue(ShoppingListItem item) {
        ContentValues values = new ContentValues();

        if (item.getServerId() != null) {
            values.put(ShoppingListItems.SERVER_ID, item.getServerId());
        }
        values.put(ShoppingListItems.LIST_ITEM_ID, item.getId());
        values.put(ShoppingListItems.LIST_ITEM_NAME, item.getName());
        values.put(ShoppingListItems.PARENT_LIST_ID, item.getParentListId());
        values.put(ShoppingListItems.UNIT_ID, item.getUnit().getId());
        values.put(ShoppingListItems.CURRENCY_ID, item.getCurrency().getId());
        values.put(ShoppingListItems.PRICE, item.getPrice());
        values.put(ShoppingListItems.QUANTITY, item.getQuantity());
        values.put(ShoppingListItems.SHORT_DESCRIPTION, item.getNote());
        values.put(ShoppingListItems.STATUS, item.getStatus().ordinal());
        values.put(ShoppingListItems.PRIORITY, item.getPriority().ordinal());
        values.put(ShoppingListItems.TIME_CREATED, item.getTimeCreated());
        values.put(ShoppingListItems.CATEGORY_ID, item.getCategory().getId());
        if (item.getPosition() != -1){
            values.put(ShoppingListItems.MANUAL_SORT_POSITION, item.getPosition());
        }
        values.put(ShoppingListItems.IS_DELETED, item.isDelete() ? 1 : 0);
        values.put(ShoppingListItems.IS_DIRTY, item.isDirty() ? 1 : 0);
        values.put(ShoppingListItems.TIMESTAMP, item.getTimestamp());
        return values;
    }

    public long getLastTimestamp() {
        return getValue(ShoppingListItems.LAST_TIMESTAMP_URI);
    }

    public List<ShoppingListItem> getShoppingListItems(String shoppingListId) {
        List<ShoppingListItem> items = new LinkedList<>();
        Cursor data = null;
        try {
            data = getShoppingListItemsCursor(shoppingListId);
            if (!data.isClosed() && data.moveToFirst()) {
                do {
                    ShoppingListItem item = new ShoppingListItem(data);
                    items.add(item);
                } while (data.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (data != null) {
                data.close();
            }
        }
        return items;
    }

    public Cursor getShoppingListItemsCursor(String shoppingListId) {
        Cursor data = null;
        try {
            data = mContext.getContentResolver().query(ShoppingListItems.buildItemsUri(shoppingListId), null,
                    null, null, null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return data;
    }

    private Map<String, ShoppingListItem> getAllShoppingListItems(Cursor data) {
        Map<String, ShoppingListItem> items = new HashMap<>();
        try {
            if (!data.isClosed() && data.moveToFirst()) {
                do {
                    ShoppingListItem item = new ShoppingListItem(data);
                    items.put(item.getId(), item);
                } while (data.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (data != null) {
                data.close();
            }
        }
        return items;
    }

    public Map<String, ShoppingListItem> getAllShoppingListItems() {
        return getAllShoppingListItems(getAllShoppingListItemsCursor(0, false));
    }

    public Map<String, ShoppingListItem> getDirtyShoppingListItems() {
        return getAllShoppingListItems(getAllShoppingListItemsCursor(0, true));
    }

    public Map<String, ShoppingListItem> getAllShoppingListItems(long timestamp) {
        return getAllShoppingListItems(getAllShoppingListItemsCursor(timestamp, false));
    }

    public Cursor getAllShoppingListItemsCursor(long timestamp, boolean getDirty) {
        Cursor cursor = null;
        try {
            String selection = ShoppingListItems.WITHOUT_DELETED;
            String[] selectionArgs = null;
            if (timestamp > 0 && getDirty) {
                selection = ShoppingListItems.TIMESTAMP + " > ? AND " + ShoppingListItems.IS_DIRTY + " = ?";
                selectionArgs = new String[]{timestamp + "", 1 + ""};
            } else if (timestamp > 0) {
                selection = ShoppingListItems.TIMESTAMP + " > ?";
                selectionArgs = new String[]{timestamp + ""};
            } else if (getDirty) {
                selection = ShoppingListItems.IS_DIRTY + " = ?";
                selectionArgs = new String[]{1 + ""};
            }

            cursor = mContext.getContentResolver().query(ShoppingListItems.CONTENT_URI, null, selection, selectionArgs, null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return cursor;
    }

    public Cursor getShoppingListItemCursor(String parentListId, String itemId) {
        return mContext.getContentResolver().query(ShoppingListItems.buildItemIdUri(parentListId, itemId),
                null, null, null, null);
    }

    public Map<String, ShoppingListItem> getShoppingListItems(String parentListId, List<String> ids) {
        return getAllShoppingListItems(mContext.getContentResolver()
                .query(ShoppingListItems.buildItemIdUri(parentListId, ShoppistUtils.toStringItemIds(ids)), null, null, null, null));
    }

    public ShoppingListItem getShoppingListItem(String parentListId, String itemName) {
        ShoppingListItem item = null;
        Cursor cursor = null;
        try {
            cursor = getShoppingListItemCursor(parentListId, itemName);
            if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
                item = new ShoppingListItem(cursor);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return item;
    }

    public void markListItemsAsDeleted(String listId) {
        try {
            ContentValues values = new ContentValues();
            values.put(ShoppingListItems.IS_DELETED, 1);
            values.put(ShoppingListItems.IS_DIRTY, 1);
            mContext.getContentResolver().update(ShoppingListItems.buildMarkItemsAsDeletedUri(listId), values, null, null);
            notifyParentListChange();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void deleteListItems(String listId) {
        try {
            mContext.getContentResolver().delete(ShoppingListItems.buildItemsUri(listId), null, null);
            notifyParentListChange();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void moveTo(String newParentListId, @NonNull Collection<ShoppingListItem> items) throws Exception {
        moveTo(newParentListId, items, false);
    }

    public void copyTo(String newParentListId, @NonNull Collection<ShoppingListItem> items) throws Exception {
        moveTo(newParentListId, items, true);
    }

    protected void moveTo(String newParentListId, @NonNull Collection<ShoppingListItem> items, boolean copy) throws Exception {
        List<ShoppingListItem> needAdd = new ArrayList<>();

        for (ShoppingListItem item : items) {
            ShoppingListItem newItem = new ShoppingListItem(item);
            newItem.setId(ShoppistUtils.generateId(newItem.getName()));
            newItem.setParentListId(newParentListId);
            newItem.setServerId(null);
            newItem.setDirty(true);
            needAdd.add(newItem);
        }
        if (needAdd.size() > 0) {
            put(needAdd);
        }
        if (!copy) {
            List<ShoppingListItem> toDelete = new ArrayList<>();
            List<ShoppingListItem> toUpdate = new ArrayList<>();
            for (ShoppingListItem item : items) {
                item.setDirty(true);
                item.setDelete(true);
                if (item.getServerId() == null){
                    toDelete.add(item);
                } else {
                    toUpdate.add(item);
                }
            }
            delete(toDelete);
            update(toUpdate);
        }
    }
}
