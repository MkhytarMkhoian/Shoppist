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
import com.justplay1.shoppist.database.ShoppingListContact.Currencies;
import com.justplay1.shoppist.models.Currency;
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
public class CurrenciesTable extends BaseTable<Currency> {

    public static final String TAG = CurrenciesTable.class.getSimpleName();

    public CurrenciesTable(Context context) {
        super(context);
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL("create table " + DBHelper.Tables.CURRENCY + "(" +
                ShoppingListContact.COLUMN_ID + " integer primary key autoincrement, " +
                ShoppingListContact.CurrencyColumns.NAME + " text, " +
                ShoppingListContact.CurrencyColumns.CURRENCY_ID + " text, " +
                ShoppingListContact.CurrencyColumns.IS_DIRTY + " integer DEFAULT 0, " +
                ShoppingListContact.CurrencyColumns.TIMESTAMP + " integer DEFAULT 0, " +
                ShoppingListContact.CurrencyColumns.IS_DELETED + " integer DEFAULT 0, " +
                ShoppingListContact.CurrencyColumns.SERVER_ID + " text" +
                ");");
    }

    private void notifyCurrenciesChange() {
        mContext.getContentResolver().notifyChange(ShoppingListContact.Currencies.CONTENT_URI, null, false);
    }

    private void notifyShoppingListItemsChange() {
        mContext.getContentResolver().notifyChange(ShoppingListContact.ShoppingListItems.CONTENT_URI, null, false);
    }

    @Override
    public Uri put(Currency currency) throws RemoteException, OperationApplicationException {
        return put(Collections.singletonList(currency))[0].uri;
    }

    @Override
    public ContentProviderResult delete(Currency currency) throws RemoteException, OperationApplicationException {
        return delete(Collections.singletonList(currency))[0];
    }

    @Override
    public ContentProviderResult update(Currency newObj) throws RemoteException, OperationApplicationException {
        return update(Collections.singletonList(newObj))[0];
    }

    @Override
    public ContentProviderResult[] put(Collection<Currency> obj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (Currency currency : obj) {
            operations.add(ContentProviderOperation.newInsert(Currencies.CONTENT_URI).withValues(getValue(currency)).build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyCurrenciesChange();
        notifyShoppingListItemsChange();
        return result;
    }

    @Override
    public ContentProviderResult[] delete(Collection<Currency> obj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (Currency currency : obj) {
            operations.add(ContentProviderOperation.newDelete(Currencies.buildCurrencyUri(currency.getId())).build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyCurrenciesChange();
        notifyShoppingListItemsChange();
        return result;
    }

    @Override
    public ContentProviderResult[] update(Collection<Currency> newObj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (Currency currency : newObj) {
            operations.add(ContentProviderOperation.newUpdate(Currencies.CONTENT_URI)
                    .withSelection(Currencies.WHERE_STRING, new String[]{currency.getId()})
                    .withValues(getValue(currency))
                    .build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyCurrenciesChange();
        notifyShoppingListItemsChange();
        return result;
    }

    @Override
    public int clear() {
        int result = mContext.getContentResolver().delete(Currencies.CONTENT_URI, null, null);
        notifyCurrenciesChange();
        return result;
    }

    @Override
    protected ContentValues getValue(Currency data) {
        ContentValues values = new ContentValues();
        values.put(Currencies.CURRENCY_ID, data.getId());
        values.put(Currencies.SERVER_ID, data.getServerId());
        values.put(Currencies.NAME, data.getName());
        values.put(Currencies.IS_DIRTY, data.isDirty() ? 1 : 0);
        values.put(Currencies.TIMESTAMP, data.getTimestamp());
        values.put(Currencies.IS_DELETED, data.isDelete() ? 1 : 0);
        return values;
    }

    public long getLastTimestamp() {
        return getValue(Currencies.LAST_TIMESTAMP_URI);
    }

    public Map<String, Currency> getCurrencies(List<String> ids) {
        return getAllCurrencies(mContext.getContentResolver().query(Currencies.buildCurrencyUri(ShoppistUtils.toStringItemIds(ids)), null, null, null, null));
    }

    public static Map<String, Currency> getAllCurrencies(Cursor data) {
        Map<String, Currency> currencies = new HashMap<>();
        try {
            if (!data.isClosed() && data.moveToFirst()) {
                do {
                    Currency currency = new Currency(data);
                    currencies.put(currency.getId(), currency);
                } while (data.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (data != null) {
                data.close();
            }
        }
        return currencies;
    }

    public Map<String, Currency> getAllCurrencies(long timestamp) {
        return getAllCurrencies(getAllCurrencyCursor(timestamp, false));
    }

    public Map<String, Currency> getDirtyCurrencies() {
        return getAllCurrencies(getAllCurrencyCursor(0, true));
    }

    public Map<String, Currency> getAllCurrencies() {
        return getAllCurrencies(getAllCurrencyCursor(0, false));
    }

    public Currency getNoCurrency() {
        return getCurrencies(Collections.singletonList(Currency.NO_CURRENCY_ID)).get(Currency.NO_CURRENCY_ID);
    }

    public Cursor getAllCurrencyCursor() {
        return getAllCurrencyCursor(0, false);
    }

    public Cursor getAllCurrencyCursor(long timestamp, boolean getDirty) {
        String selection = Currencies.WITHOUT_DELETED;
        String[] selectionArgs = null;
        if (timestamp > 0 && getDirty) {
            selection = Currencies.TIMESTAMP + " > ? AND " + Currencies.IS_DIRTY + " = ?";
            selectionArgs = new String[]{timestamp + "", 1 + ""};
        } else if (timestamp > 0) {
            selection = Currencies.TIMESTAMP + " > ?";
            selectionArgs = new String[]{timestamp + ""};
        } else if (getDirty) {
            selection = Currencies.IS_DIRTY + " = ?";
            selectionArgs = new String[]{1 + ""};
        }
        return mContext.getContentResolver().query(Currencies.CONTENT_URI, null, selection, selectionArgs, null);
    }
}
