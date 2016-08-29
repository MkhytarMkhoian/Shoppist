package com.justplay1.shoppist.repository.datasource.local.database;

import android.content.ContentValues;
import android.content.Context;

import com.justplay1.shoppist.data.R;
import com.justplay1.shoppist.entity.CurrencyDAO;
import com.justplay1.shoppist.repository.datasource.local.LocalCurrencyDataStore;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Mkhytar on 28.04.2016.
 */
@Singleton
public class LocalCurrencyDataStoreImpl extends BaseLocalDataStore<CurrencyDAO> implements LocalCurrencyDataStore {

    private static final String WITHOUT_DELETED = CurrencyDAO.IS_DELETED + "<1";
    private static final String LAST_TIMESTAMP_QUERY =
            "SELECT MAX(" + CurrencyDAO.TIMESTAMP + ") FROM " + CurrencyDAO.TABLE;

    private static String CURRENCY_QUERY(String selection) {
        return "SELECT * FROM " + CurrencyDAO.TABLE +
                " WHERE " + selection;
    }

    private Context mContext;

    @Inject
    public LocalCurrencyDataStoreImpl(BriteDatabase db, Context context) {
        super(db);
        this.mContext = context;
    }

    @Override
    public Observable<Map<String, CurrencyDAO>> getDefaultData() {
        String[] currency = mContext.getResources().getStringArray(R.array.currency);
        Map<String, CurrencyDAO> currencyList = new HashMap<>(currency.length);
        for (String c : currency) {
            String[] currencyId = c.split(" ! ");
            String id = currencyId[1];
            String serverId = null;
            String name = currencyId[0];
            long timestamp = 0;
            boolean isDirty = false;
            boolean isDelete = false;
            CurrencyDAO currency1 = new CurrencyDAO(id, serverId, name, timestamp, isDirty, isDelete);
            currencyList.put(currency1.getId(), currency1);
        }
        return Observable.just(currencyList);
    }

    @Override
    public Observable<List<CurrencyDAO>> getItems() {
        return getAllCurrency(0, false);
    }

    @Override
    public Observable<List<CurrencyDAO>> getDirtyItems() {
        return getAllCurrency(0, true);
    }

    @Override
    public Observable<List<CurrencyDAO>> getItems(long timestamp) {
        return getAllCurrency(timestamp, false);
    }

    @Override
    public Observable<CurrencyDAO> getItem(String id) {
        return db.createQuery(CurrencyDAO.TABLE, CURRENCY_QUERY(CurrencyDAO.WHERE_STRING), id)
                .mapToOne(CurrencyDAO.MAPPER::call);
    }

    @Override
    public void save(Collection<CurrencyDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (CurrencyDAO currency : data) {
                db.insert(CurrencyDAO.TABLE, getValue(currency));
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
        notifyShoppingListItemsChange();
    }

    @Override
    public void save(CurrencyDAO data) {
        save(Collections.singletonList(data));
    }

    @Override
    public void delete(Collection<CurrencyDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (CurrencyDAO currency : data) {
                db.delete(CurrencyDAO.TABLE, CurrencyDAO.WHERE_STRING, currency.getId());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
        notifyShoppingListItemsChange();
    }

    @Override
    public void delete(CurrencyDAO data) {
        delete(Collections.singletonList(data));
    }

    @Override
    public void update(Collection<CurrencyDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (CurrencyDAO currency : data) {
                db.update(CurrencyDAO.TABLE, getValue(currency), CurrencyDAO.WHERE_STRING, currency.getId());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
        notifyShoppingListItemsChange();
    }

    @Override
    public void update(CurrencyDAO data) {
        update(Collections.singletonList(data));
    }

    @Override
    public int clear() {
        return clear(CurrencyDAO.TABLE);
    }

    private void notifyShoppingListItemsChange() {
//        mContext.getContentResolver().notifyChange(ShoppingListContact.ShoppingListItems.CONTENT_URI, null, false);
    }

    @Override
    protected ContentValues getValue(CurrencyDAO data) {
        return new CurrencyDAO.Builder()
                .id(data.getId())
                .name(data.getName())
                .isDirty(data.isDirty())
                .timestamp(data.getTimestamp())
                .isDelete(data.isDelete())
                .serverId(data.getServerId())
                .build();
    }

    @Override
    public Observable<Long> getLastTimestamp() {
        return getValue(CurrencyDAO.TABLE, LAST_TIMESTAMP_QUERY, null);
    }

    public Observable<List<CurrencyDAO>> getAllCurrency(long timestamp, boolean getDirty) {
        String selection = WITHOUT_DELETED;
        String[] selectionArgs = null;
        if (timestamp > 0 && getDirty) {
            selection = CurrencyDAO.TIMESTAMP + " > ? AND " + CurrencyDAO.IS_DIRTY + " = ?";
            selectionArgs = new String[]{timestamp + "", 1 + ""};
        } else if (timestamp > 0) {
            selection = CurrencyDAO.TIMESTAMP + " > ?";
            selectionArgs = new String[]{timestamp + ""};
        } else if (getDirty) {
            selection = CurrencyDAO.IS_DIRTY + " = ?";
            selectionArgs = new String[]{1 + ""};
        }
        return db.createQuery(CurrencyDAO.TABLE, CURRENCY_QUERY(selection), selectionArgs)
                .mapToList(CurrencyDAO.MAPPER::call);
    }
}
