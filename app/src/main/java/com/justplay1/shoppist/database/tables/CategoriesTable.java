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

import com.justplay1.shoppist.adapters.BaseAdapter;
import com.justplay1.shoppist.database.BaseTable;
import com.justplay1.shoppist.database.DBHelper;
import com.justplay1.shoppist.database.ShoppingListContact;
import com.justplay1.shoppist.database.ShoppingListContact.Categories;
import com.justplay1.shoppist.database.ShoppingListContact.CategoriesColumns;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ShoppistUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mkhitar on 13.01.2015.
 */
public class CategoriesTable extends BaseTable<Category> {

    public static final String TAG = CategoriesTable.class.getSimpleName();

    public CategoriesTable(Context context) {
        super(context);
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL("create table " + DBHelper.Tables.CATEGORIES + "(" +
                ShoppingListContact.COLUMN_ID + " integer primary key autoincrement, " +
                CategoriesColumns.NAME + " text, " +
                CategoriesColumns.COLOR + " integer, " +
                CategoriesColumns.CREATE_BY_USER + " integer DEFAULT 0, " +
                CategoriesColumns.IS_DELETED + " integer DEFAULT 0, " +
                CategoriesColumns.MANUAL_SORT_POSITION + " integer DEFAULT 0, " +
                CategoriesColumns.IS_DIRTY + " integer DEFAULT 0, " +
                CategoriesColumns.TIMESTAMP + " integer DEFAULT 0, " +
                CategoriesColumns.CATEGORY_ID + " text, " +
                CategoriesColumns.SERVER_ID + " text, " +
                CategoriesColumns.ENABLE + " integer " +
                ");");
    }

    private void notifyCategoriesChange() {
        mContext.getContentResolver().notifyChange(ShoppingListContact.Categories.CONTENT_URI, null, false);
    }

    @Override
    public Uri put(Category obj) throws RemoteException, OperationApplicationException {
        return put(Collections.singletonList(obj))[0].uri;
    }

    @Override
    public ContentProviderResult delete(Category obj) throws RemoteException, OperationApplicationException {
        return delete(Collections.singletonList(obj))[0];
    }

    @Override
    public ContentProviderResult update(Category newObj) throws RemoteException, OperationApplicationException {
        return update(Collections.singletonList(newObj))[0];
    }

    @Override
    public ContentProviderResult[] put(Collection<Category> obj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (Category category : obj) {
            operations.add(ContentProviderOperation.newInsert(Categories.CONTENT_URI).withValues(getValue(category)).build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyCategoriesChange();
        return result;
    }

    @Override
    public ContentProviderResult[] delete(Collection<Category> obj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (Category category : obj) {
            operations.add(ContentProviderOperation.newDelete(Categories.buildCategoriesUri(category.getId())).build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyCategoriesChange();
        return result;
    }

    @Override
    public ContentProviderResult[] update(Collection<Category> newObj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (Category category : newObj) {
            operations.add(ContentProviderOperation.newUpdate(Categories.CONTENT_URI)
                    .withSelection(Categories.WHERE_CATEGORY_ID, new String[]{category.getId()})
                    .withValues(getValue(category))
                    .build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyCategoriesChange();
        return result;
    }

    @Override
    public int clear() {
        int result = mContext.getContentResolver().delete(Categories.CONTENT_URI, null, null);
        notifyCategoriesChange();
        return result;
    }

    public Category getNoCategory() {
        return getCategory(Collections.singletonList(Category.NO_CATEGORY_ID)).get(Category.NO_CATEGORY_ID);
    }

    @Override
    protected ContentValues getValue(Category data) {
        ContentValues values = new ContentValues();
        if (data.getServerId() != null) {
            values.put(CategoriesColumns.SERVER_ID, data.getServerId());
        }
        values.put(CategoriesColumns.CATEGORY_ID, data.getId());
        values.put(CategoriesColumns.NAME, data.getName());
        values.put(CategoriesColumns.COLOR, data.getColor());
        values.put(CategoriesColumns.ENABLE, data.isEnable() ? 1 : 0);
        values.put(CategoriesColumns.CREATE_BY_USER, data.isCreateByUser() ? 1 : 0);
        if (data.getPosition() != -1){
            values.put(CategoriesColumns.MANUAL_SORT_POSITION, data.getPosition());
        }
        values.put(CategoriesColumns.IS_DELETED, data.isDelete() ? 1 : 0);
        values.put(CategoriesColumns.IS_DIRTY, data.isDirty() ? 1 : 0);
        values.put(CategoriesColumns.TIMESTAMP, data.getTimestamp());
        return values;
    }

    public long getLastTimestamp() {
        return getValue(Categories.LAST_TIMESTAMP_URI);
    }

    public static Map<String, Category> getAllCategories(Cursor data) {
        Map<String, Category> categories = new HashMap<>();
        try {
            if (!data.isClosed() && data.moveToFirst()) {
                do {
                    Category item = new Category(data, ShoppingListContact.Categories.CATEGORY_ID);
                    categories.put(item.getId(), item);
                } while (data.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (data != null) {
                data.close();
            }
        }
        return categories;
    }

    public Map<String, Category> getAllCategories() {
        return getAllCategories(getAllCategoriesCursor(0, false));
    }

    public Map<String, Category> getAllCategories(long timestamp) {
        return getAllCategories(getAllCategoriesCursor(timestamp, false));
    }

    public Map<String, Category> getDirtyCategories() {
        return getAllCategories(getAllCategoriesCursor(0, true));
    }

    public Cursor getAllCategoriesCursor() {
        return getAllCategoriesCursor(0, false);
    }

    public Cursor getAllCategoriesCursor(long timestamp, boolean getDirty) {
        String sortOrder = null;
        if (ShoppistPreferences.isManualSortEnableForCategories()) {
            sortOrder = Categories.MANUAL_SORT_POSITION + " ASC";
        } else if (ShoppistPreferences.getSortForCategories() == BaseAdapter.SORT_BY_NAME) {
            sortOrder = Categories.NAME + " ASC";
        }
        String selection = Categories.WITHOUT_DELETED;
        String[] selectionArgs = null;
        if (timestamp > 0 && getDirty) {
            selection = Categories.TIMESTAMP + " > ? AND " + Categories.IS_DIRTY + " = ?";
            selectionArgs = new String[]{timestamp + "", "1"};
        } else if (timestamp > 0) {
            selection = Categories.TIMESTAMP + " > ?";
            selectionArgs = new String[]{timestamp + ""};
        } else if (getDirty) {
            selection = Categories.IS_DIRTY + " = ?";
            selectionArgs = new String[]{"1"};
        }
        return mContext.getContentResolver().query(Categories.CONTENT_URI, null, selection, selectionArgs, sortOrder);
    }

    public Map<String, Category> getCategory(List<String> ids) {
        Map<String, Category> categories = new HashMap<>();
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(Categories.buildCategoriesUri(ShoppistUtils.toStringItemIds(ids)), null,
                    null, null, null);
            if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
                Category category = new Category(cursor, ShoppingListContact.Categories.CATEGORY_ID);
                categories.put(category.getId(), category);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return categories;
    }
}
