/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.justplay1.shoppist.repository.datasource.local.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.justplay1.shoppist.ApplicationTestCase;
import com.justplay1.shoppist.entity.CurrencyDAO;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import java.util.Collections;
import java.util.List;

import rx.schedulers.Schedulers;

import static com.justplay1.shoppist.entity.DAOUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCurrencyDAO;
import static com.justplay1.shoppist.repository.datasource.local.database.DatabaseUtil.checkDataInDatabase;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Mkhytar Mkhoian.
 */
public class LocalCurrencyDataStoreImplTest extends ApplicationTestCase {

    private LocalCurrencyDataStoreImpl dataStore;
    private DBHelper helper;

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.application;

        helper = new DBHelper(context) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                try {
                    db.beginTransaction();
                    db.execSQL(CREATE_CURRENCIES_TABLE);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }
        };
        SqlBrite sqlBrite = SqlBrite.create();
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.immediate());

        dataStore = new LocalCurrencyDataStoreImpl(db, context);
    }

    @After
    public void tearDown() {
        helper.close();
    }

    @Test
    public void saveAndQuery_HappyCase() {
        List<CurrencyDAO> fakeData = Collections.singletonList(createFakeCurrencyDAO());

        dataStore.save(fakeData);

        List<CurrencyDAO> resultData = checkDataInDatabase(dataStore);

        assertThat(resultData, is(fakeData));
    }

    @Test
    public void delete_HappyCase() {
        List<CurrencyDAO> fakeData = Collections.singletonList(createFakeCurrencyDAO());
        dataStore.save(fakeData);

        dataStore.delete(fakeData);

        List<CurrencyDAO> resultData = checkDataInDatabase(dataStore);

        assertThat(resultData.size(), is(0));
    }

    @Test()
    public void update_HappyCase() {
        List<CurrencyDAO> fakeData = Collections.singletonList(createFakeCurrencyDAO());
        dataStore.save(fakeData);

        CurrencyDAO toUpdate = new CurrencyDAO(FAKE_ID, "name_updated");
        dataStore.update(Collections.singletonList(toUpdate));

        CurrencyDAO resultData = checkDataInDatabase(dataStore, FAKE_ID);

        assertThat(resultData.getName(), is("name_updated"));
    }

    @Test
    public void clear_HappyCase() {
        List<CurrencyDAO> fakeData = Collections.singletonList(createFakeCurrencyDAO());
        dataStore.save(fakeData);

        dataStore.clear();
        List<CurrencyDAO> resultData = checkDataInDatabase(dataStore);

        assertThat(resultData.size(), is(0));
    }
}