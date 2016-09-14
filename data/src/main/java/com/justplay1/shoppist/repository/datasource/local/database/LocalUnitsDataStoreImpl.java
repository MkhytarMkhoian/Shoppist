/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

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
 * Created by Mkhytar Mkhoian.
 */
@Singleton
public class LocalUnitsDataStoreImpl extends BaseLocalDataStore<UnitDAO> implements LocalUnitsDataStore {

    private static String UNIT_QUERY(String selection) {
        if (selection == null){
            return "SELECT * FROM " + UnitDAO.TABLE;
        }
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
            UnitDAO unit1 = new UnitDAO(id, name, shortName);
            unitList.put(unit1.getId(), unit1);
        }
        return Observable.just(unitList);
    }

    @Override
    protected ContentValues getValue(UnitDAO unit) {
        return new UnitDAO.Builder()
                .id(unit.getId())
                .fullName(unit.getName())
                .shortName(unit.getShortName())
                .build();
    }

    @Override
    public Observable<List<UnitDAO>> getItems() {
        return getAllUnits();
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

    private Observable<List<UnitDAO>> getAllUnits() {
        return db.createQuery(UnitDAO.TABLE, UNIT_QUERY(null), new String[]{})
                .mapToList(UnitDAO.MAPPER::call);
    }
}
