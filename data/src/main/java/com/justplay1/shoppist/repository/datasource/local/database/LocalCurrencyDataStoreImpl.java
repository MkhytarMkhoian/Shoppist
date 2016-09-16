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

import com.justplay1.shoppist.bus.DataEventBus;
import com.justplay1.shoppist.bus.ListItemsDataUpdatedEvent;
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
 * Created by Mkhytar Mkhoian.
 */
@Singleton
public class LocalCurrencyDataStoreImpl extends BaseLocalDataStore<CurrencyDAO> implements LocalCurrencyDataStore {

    private static String CURRENCY_QUERY(String selection) {
        if (selection == null) {
            return "SELECT * FROM " + CurrencyDAO.TABLE;
        }
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
            String name = currencyId[0];
            CurrencyDAO currency1 = new CurrencyDAO(id, name);
            currencyList.put(currency1.getId(), currency1);
        }
        return Observable.just(currencyList);
    }

    @Override
    public Observable<List<CurrencyDAO>> getItems() {
        return getAllCurrency();
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
        DataEventBus.instanceOf().post(new ListItemsDataUpdatedEvent());
    }

    @Override
    protected ContentValues getValue(CurrencyDAO data) {
        return new CurrencyDAO.Builder()
                .id(data.getId())
                .name(data.getName())
                .build();
    }

    public Observable<List<CurrencyDAO>> getAllCurrency() {
        return db.createQuery(CurrencyDAO.TABLE, CURRENCY_QUERY(null), new String[]{})
                .mapToList(CurrencyDAO.MAPPER::call);
    }
}
