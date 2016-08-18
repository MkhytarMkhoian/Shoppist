package com.justplay1.shoppist.repository.datasource.local.database;

import android.content.ContentValues;
import android.content.Context;

import com.justplay1.shoppist.data.R;
import com.justplay1.shoppist.entity.UnitDAO;
import com.justplay1.shoppist.repository.datasource.local.LocalUnitsDataStore;
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
public class LocalUnitsDataStoreImpl extends BaseLocalDataStore<UnitDAO> implements LocalUnitsDataStore {

    private static final String WITHOUT_DELETED = UnitDAO.IS_DELETED + "<1";
    private static final String LAST_TIMESTAMP_QUERY =
            "SELECT MAX(" + UnitDAO.TIMESTAMP + ") FROM " + UnitDAO.TABLE;

    private static String UNIT_QUERY(String selection) {
        return "SELECT * FROM " + UnitDAO.TABLE +
                " WHERE " + selection;
    }

    private Context mContext;

    @Inject
    public LocalUnitsDataStoreImpl(BriteDatabase db, Context context) {
        super(db);
        this.mContext = context;
    }

    @Override
    public Observable<Map<String, UnitDAO>> getDefaultData() {
        String[] units = mContext.getResources().getStringArray(R.array.units);
        Map<String, UnitDAO> unitList = new HashMap<>(units.length);
        for (String item : units) {
            String[] unit = item.split("/");
            String[] unitId = unit[1].split(" ! ");
            String id = unitId[1];
            String name = unit[0];
            String shortName = unitId[0];
            UnitDAO unit1 = new UnitDAO(id, null, name, 0, false, false, shortName);
            unitList.put(unit1.getId(), unit1);
        }
        return Observable.just(unitList);
    }

    @Override
    protected ContentValues getValue(UnitDAO unit) {
        return new UnitDAO.Builder()
                .id(unit.getId())
                .serverId(unit.getServerId())
                .fullName(unit.getName())
                .shortName(unit.getShortName())
                .isDirty(unit.isDirty())
                .isDelete(unit.isDelete())
                .timestamp(unit.getTimestamp())
                .build();
    }

    @Override
    public Observable<List<UnitDAO>> getItems() {
        return getAllUnits(0, false);
    }

    @Override
    public Observable<List<UnitDAO>> getDirtyItems() {
        return getAllUnits(0, true);
    }

    @Override
    public Observable<List<UnitDAO>> getItems(long timestamp) {
        return getAllUnits(timestamp, false);
    }

    @Override
    public Observable<UnitDAO> getItem(String id) {
        return db.createQuery(UnitDAO.TABLE, UNIT_QUERY(UnitDAO.WHERE_STRING), id)
                .mapToOne(UnitDAO.MAPPER::call);
    }

    @Override
    public void save(Collection<UnitDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (UnitDAO unit : data) {
                db.insert(UnitDAO.TABLE, getValue(unit));
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
        notifyShoppingListItemsChange();
    }

    @Override
    public void save(UnitDAO data) {
        save(Collections.singletonList(data));
    }

    @Override
    public void delete(Collection<UnitDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (UnitDAO unit : data) {
                db.delete(UnitDAO.TABLE, UnitDAO.WHERE_STRING, unit.getId());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
        notifyShoppingListItemsChange();
    }

    @Override
    public void delete(UnitDAO data) {
        delete(Collections.singletonList(data));
    }

    @Override
    public void update(Collection<UnitDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (UnitDAO unit : data) {
                db.update(UnitDAO.TABLE, getValue(unit), UnitDAO.WHERE_STRING, unit.getId());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
        notifyShoppingListItemsChange();
    }

    @Override
    public void update(UnitDAO data) {
        update(Collections.singletonList(data));
    }

    @Override
    public int clear() {
        return clear(UnitDAO.TABLE);
    }

    private void notifyShoppingListItemsChange() { // TODO
//        mContext.getContentResolver().notifyChange(ShoppingListContact.ShoppingListItems.CONTENT_URI, null, false);
    }

    @Override
    public Observable<Long> getLastTimestamp() {
        return getValue(UnitDAO.TABLE, LAST_TIMESTAMP_QUERY, null);
    }

    private Observable<List<UnitDAO>> getAllUnits(long timestamp, boolean getDirty) {
        String selection = WITHOUT_DELETED;
        String[] selectionArgs = null;
        if (timestamp > 0 && getDirty) {
            selection = UnitDAO.TIMESTAMP + " > ? AND " + UnitDAO.IS_DIRTY + " = ?";
            selectionArgs = new String[]{timestamp + "", 1 + ""};
        } else if (timestamp > 0) {
            selection = UnitDAO.TIMESTAMP + " > ?";
            selectionArgs = new String[]{timestamp + ""};
        } else if (getDirty) {
            selection = UnitDAO.IS_DIRTY + " = ?";
            selectionArgs = new String[]{1 + ""};
        }
        return db.createQuery(UnitDAO.TABLE, UNIT_QUERY(selection), selectionArgs)
                .mapToList(UnitDAO.MAPPER::call);
    }
}
