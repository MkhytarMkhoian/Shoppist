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
import com.justplay1.shoppist.entity.UnitDAO;
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
import static com.justplay1.shoppist.entity.DAOUtil.createFakeUnitDAO;
import static com.justplay1.shoppist.repository.datasource.local.database.DatabaseUtil.checkDataInDatabase;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Mkhytar Mkhoian.
 */
public class LocalUnitsDataStoreImplTest extends ApplicationTestCase {

    private LocalUnitsDataStoreImpl dataStore;
    private DBHelper helper;


    @Before
    public void setUp() throws Exception {
        Context context = RuntimeEnvironment.application;

        helper = new DBHelper(context) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                try {
                    db.beginTransaction();
                    db.execSQL(CREATE_UNITS_TABLE);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }
        };
        SqlBrite sqlBrite = SqlBrite.create();
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.immediate());

        dataStore = new LocalUnitsDataStoreImpl(db, context);
    }

    @After
    public void tearDown() throws Exception {
        helper.close();
    }

    @Test
    public void saveAndQuery_HappyCase() {
        List<UnitDAO> fakeData = Collections.singletonList(createFakeUnitDAO());

        dataStore.save(fakeData);

        List<UnitDAO> resultData = checkDataInDatabase(dataStore);

        assertThat(resultData, is(fakeData));
    }

    @Test
    public void delete_HappyCase() throws Exception {
        List<UnitDAO> fakeData = Collections.singletonList(createFakeUnitDAO());
        dataStore.save(fakeData);

        dataStore.delete(fakeData);

        List<UnitDAO> resultData = checkDataInDatabase(dataStore);

        assertThat(resultData.size(), is(0));
    }

    @Test
    public void update_HappyCase() throws Exception {
        List<UnitDAO> fakeData = Collections.singletonList(createFakeUnitDAO());
        dataStore.save(fakeData);

        UnitDAO toUpdate = new UnitDAO(FAKE_ID, "name_updated", "short_name_updated");
        dataStore.update(Collections.singletonList(toUpdate));

        UnitDAO resultData = checkDataInDatabase(dataStore, FAKE_ID);

        assertThat(resultData.getName(), is("name_updated"));
        assertThat(resultData.getShortName(), is("short_name_updated"));
    }

    @Test
    public void clear_HappyCase() {
        List<UnitDAO> fakeData = Collections.singletonList(createFakeUnitDAO());
        dataStore.save(fakeData);

        dataStore.clear();
        List<UnitDAO> resultData = checkDataInDatabase(dataStore);

        assertThat(resultData.size(), is(0));
    }
}