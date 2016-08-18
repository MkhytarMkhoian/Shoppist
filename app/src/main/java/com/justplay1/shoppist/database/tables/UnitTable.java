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
import com.justplay1.shoppist.database.ShoppingListContact.Units;
import com.justplay1.shoppist.models.Unit;
import com.justplay1.shoppist.utils.ShoppistUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mkhitar on 14.02.2015.
 */
public class UnitTable extends BaseTable<Unit> {

    public static final String TAG = UnitTable.class.getSimpleName();

    public UnitTable(Context context) {
        super(context);
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL("create table " + DBHelper.Tables.UNITS + "(" +
                ShoppingListContact.COLUMN_ID + " integer primary key autoincrement, " +
                ShoppingListContact.UnitColumns.FULL_NAME + " text, " +
                ShoppingListContact.UnitColumns.SHORT_NAME + " text, " +
                ShoppingListContact.UnitColumns.UNIT_ID + " text, " +
                ShoppingListContact.UnitColumns.IS_DIRTY + " integer DEFAULT 0, " +
                ShoppingListContact.UnitColumns.TIMESTAMP + " integer DEFAULT 0, " +
                ShoppingListContact.UnitColumns.IS_DELETED + " integer DEFAULT 0, " +
                ShoppingListContact.UnitColumns.SERVER_ID + " text" +
                ");");
    }

    private void notifyUnitsChange() {
        mContext.getContentResolver().notifyChange(ShoppingListContact.Units.CONTENT_URI, null, false);
    }

    private void notifyShoppingListItemsChange() {
        mContext.getContentResolver().notifyChange(ShoppingListContact.ShoppingListItems.CONTENT_URI, null, false);
    }

    @Override
    public Uri put(Unit unit) throws RemoteException, OperationApplicationException {
        return put(Collections.singletonList(unit))[0].uri;
    }

    @Override
    public ContentProviderResult delete(Unit unit) throws RemoteException, OperationApplicationException {
        return delete(Collections.singletonList(unit))[0];
    }

    @Override
    public ContentProviderResult update(Unit newObj) throws RemoteException, OperationApplicationException {
        return update(Collections.singletonList(newObj))[0];
    }

    @Override
    public ContentProviderResult[] put(Collection<Unit> obj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (Unit unit : obj) {
            operations.add(ContentProviderOperation.newInsert(Units.CONTENT_URI).withValues(getValue(unit)).build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyUnitsChange();
        notifyShoppingListItemsChange();
        return result;
    }

    @Override
    public ContentProviderResult[] delete(Collection<Unit> obj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (Unit unit : obj) {
            operations.add(ContentProviderOperation.newDelete(Units.buildUnitUri(unit.getId())).build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyUnitsChange();
        notifyShoppingListItemsChange();
        return result;
    }

    @Override
    public ContentProviderResult[] update(Collection<Unit> newObj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        for (Unit unit : newObj) {
            operations.add(ContentProviderOperation.newUpdate(Units.CONTENT_URI)
                    .withSelection(Units.WHERE_STRING, new String[]{unit.getId()})
                    .withValues(getValue(unit))
                    .build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyUnitsChange();
        notifyShoppingListItemsChange();
        return result;
    }

    @Override
    public int clear() {
        int result = mContext.getContentResolver().delete(Units.CONTENT_URI, null, null);
        notifyUnitsChange();
        return result;
    }

    @Override
    protected ContentValues getValue(Unit unit) {
        ContentValues values = new ContentValues();
        values.put(Units.UNIT_ID, unit.getId());
        values.put(Units.SERVER_ID, unit.getServerId());
        values.put(Units.FULL_NAME, unit.getName());
        values.put(Units.SHORT_NAME, unit.getShortName());
        values.put(Units.IS_DIRTY, unit.isDirty() ? 1 : 0);
        values.put(Units.TIMESTAMP, unit.getTimestamp());
        values.put(Units.IS_DELETED, unit.isDelete() ? 1 : 0);
        return values;
    }

    public long getLastTimestamp() {
        return getValue(Units.LAST_TIMESTAMP_URI);
    }

    public static Map<String, Unit> getAllUnits(Cursor data) {
        Map<String, Unit> units = new HashMap<>();
        try {
            if (!data.isClosed() && data.moveToFirst()) {
                do {
                    Unit unit = new Unit(data);
                    units.put(unit.getId(), unit);
                } while (data.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (data != null) {
                data.close();
            }
        }
        return units;
    }

    public Map<String, Unit> getUnits(List<String> ids) {
        return getAllUnits(mContext.getContentResolver().query(Units.buildUnitUri(ShoppistUtils.toStringItemIds(ids)), null, null, null, null));
    }

    public Map<String, Unit> getAllUnits() {
        return getAllUnits(getAllUnitsCursor(0, false));
    }

    public Map<String, Unit> getAllUnits(long timestamp) {
        return getAllUnits(getAllUnitsCursor(timestamp, false));
    }

    public Map<String, Unit> getDirtyUnits() {
        return getAllUnits(getAllUnitsCursor(0, true));
    }

    public Unit getNoUnit() {
        return getUnits(Collections.singletonList(Unit.NO_UNIT_ID)).get(Unit.NO_UNIT_ID);
    }

    public Cursor getAllUnitsCursor() {
        return getAllUnitsCursor(0, false);
    }

    public Cursor getAllUnitsCursor(long timestamp, boolean getDirty) {
        String selection = Units.WITHOUT_DELETED;
        String[] selectionArgs = null;
        if (timestamp > 0 && getDirty) {
            selection = Units.TIMESTAMP + " > ? AND " + Units.IS_DIRTY + " = ?";
            selectionArgs = new String[]{timestamp + "", 1 + ""};
        } else if (timestamp > 0) {
            selection = Units.TIMESTAMP + " > ?";
            selectionArgs = new String[]{timestamp + ""};
        } else if (getDirty) {
            selection = Units.IS_DIRTY + " = ?";
            selectionArgs = new String[]{1 + ""};
        }
        return mContext.getContentResolver().query(Units.CONTENT_URI, null, selection, selectionArgs, null);
    }
}
