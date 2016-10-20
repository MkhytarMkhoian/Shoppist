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
import com.justplay1.shoppist.entity.CategoryDAO;
import com.justplay1.shoppist.entity.CurrencyDAO;
import com.justplay1.shoppist.entity.ListDAO;
import com.justplay1.shoppist.entity.ListItemDAO;
import com.justplay1.shoppist.entity.UnitDAO;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.schedulers.Schedulers;

import static com.justplay1.shoppist.entity.DAOUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_PARENT_LIST_ID;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCategoryDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCurrencyDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeListDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeListItemDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeUnitDAO;
import static com.justplay1.shoppist.repository.datasource.local.database.DatabaseUtil.checkDataInDatabase;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Mkhytar Mkhoian.
 */
public class LocalListDataStoreImplTest extends ApplicationTestCase {

    private LocalListDataStoreImpl dataStore;
    private DBHelper helper;
    private BriteDatabase db;
    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;

        helper = new DBHelper(context) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                try {
                    db.beginTransaction();
                    db.execSQL(CREATE_LISTS_TABLE);
                    db.execSQL(CREATE_LIST_ITEMS_TABLE);
                    db.execSQL(CREATE_CATEGORIES_TABLE);
                    db.execSQL(CREATE_UNITS_TABLE);
                    db.execSQL(CREATE_CURRENCIES_TABLE);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }
        };
        SqlBrite sqlBrite = SqlBrite.create();
        db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.immediate());

        dataStore = new LocalListDataStoreImpl(db);
    }

    @After
    public void tearDown() {
        helper.close();
    }

    @Test
    public void saveAndQuery_HappyCase() {
        List<ListDAO> fakeData = Collections.singletonList(createFakeListDAO());

        dataStore.save(fakeData);

        List<ListDAO> resultData = checkDataInDatabase(dataStore);

        assertThat(resultData, is(fakeData));
    }

    @Test
    public void delete_HappyCase() {
        List<ListDAO> fakeData = Collections.singletonList(createFakeListDAO());
        dataStore.save(fakeData);

        dataStore.delete(fakeData);

        List<ListDAO> resultData = checkDataInDatabase(dataStore);

        assertThat(resultData.size(), is(0));
    }

    @Test
    public void update_HappyCase() {
        List<ListDAO> fakeData = Collections.singletonList(createFakeListDAO());
        dataStore.save(fakeData);

        ListDAO toUpdate = new ListDAO(FAKE_ID, "name_updated", 0, 111L, 3, 8888, 0);
        dataStore.update(Collections.singletonList(toUpdate));

        ListDAO resultData = checkDataInDatabase(dataStore, FAKE_ID);

        assertThat(resultData.getName(), is("name_updated"));
        assertThat(resultData.getTimeCreated(), is(111L));
        assertThat(resultData.getPriority(), is(3));
        assertThat(resultData.getColor(), is(8888));
    }

    @Test
    public void clear_HappyCase() {
        List<ListDAO> fakeData = Collections.singletonList(createFakeListDAO());
        dataStore.save(fakeData);

        dataStore.clear();
        List<ListDAO> resultData = checkDataInDatabase(dataStore);

        assertThat(resultData.size(), is(0));
    }

    @Test
    public void deleteListItems_HappyCase() {
        List<ListDAO> fakeData = Collections.singletonList(createFakeListDAO());
        dataStore.save(fakeData);

        LocalListItemsDataStoreImpl listItemsDataStore = new LocalListItemsDataStoreImpl(db);
        LocalCategoryDataStoreImpl categoryDataStore = new LocalCategoryDataStoreImpl(db, context);
        LocalUnitsDataStoreImpl unitsDataStore = new LocalUnitsDataStoreImpl(db, context);
        LocalCurrencyDataStoreImpl currencyDataStore = new LocalCurrencyDataStoreImpl(db, context);

        UnitDAO unitDAO = createFakeUnitDAO();
        CategoryDAO categoryDAO = createFakeCategoryDAO();
        CurrencyDAO currencyDAO = createFakeCurrencyDAO();
        ListItemDAO listItemDAO = createFakeListItemDAO(categoryDAO, unitDAO, currencyDAO);

        categoryDataStore.save(Collections.singletonList(categoryDAO));
        unitsDataStore.save(Collections.singletonList(unitDAO));
        currencyDataStore.save(Collections.singletonList(currencyDAO));

        List<ListItemDAO> fakeDataListItems = Arrays.asList(listItemDAO, listItemDAO, listItemDAO);
        listItemsDataStore.save(fakeDataListItems);

        dataStore.deleteListItems(FAKE_PARENT_LIST_ID);

        List<ListItemDAO> resultData = checkDataInDatabase(listItemsDataStore);

        assertThat(resultData.size(), is(0));
    }
}