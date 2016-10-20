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

import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static com.justplay1.shoppist.entity.DAOUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_PARENT_LIST_ID;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_STATUS;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_TIME_CREATED;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCategoryDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCurrencyDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeListItemDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeUnitDAO;
import static com.justplay1.shoppist.repository.datasource.local.database.DatabaseUtil.checkDataInDatabase;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Mkhytar Mkhoian.
 */
public class LocalListItemsDataStoreImplTest extends ApplicationTestCase {

    private LocalListItemsDataStoreImpl dataStore;
    private DBHelper helper;

    private CategoryDAO categoryDAO;
    private CurrencyDAO currencyDAO;
    private UnitDAO unitDAO;
    private ListItemDAO listItemDAO;

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.application;

        helper = new DBHelper(context) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                try {
                    db.beginTransaction();
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
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.immediate());

        dataStore = new LocalListItemsDataStoreImpl(db);
        LocalCategoryDataStoreImpl categoryDataStore = new LocalCategoryDataStoreImpl(db, context);
        LocalUnitsDataStoreImpl unitsDataStore = new LocalUnitsDataStoreImpl(db, context);
        LocalCurrencyDataStoreImpl currencyDataStore = new LocalCurrencyDataStoreImpl(db, context);

        unitDAO = createFakeUnitDAO();
        categoryDAO = createFakeCategoryDAO();
        currencyDAO = createFakeCurrencyDAO();
        listItemDAO = createFakeListItemDAO(categoryDAO, unitDAO, currencyDAO);

        categoryDataStore.save(Collections.singletonList(categoryDAO));
        unitsDataStore.save(Collections.singletonList(unitDAO));
        currencyDataStore.save(Collections.singletonList(currencyDAO));
    }

    @After
    public void tearDown() {
        helper.close();
    }

    @Test
    public void saveAndQuery_HappyCase() {
        List<ListItemDAO> fakeData = Collections.singletonList(listItemDAO);

        dataStore.save(fakeData);

        List<ListItemDAO> resultData = checkDataInDatabase(dataStore);

        assertThat(resultData, is(fakeData));
    }

    @Test
    public void delete_HappyCase() {
        List<ListItemDAO> fakeData = Collections.singletonList(listItemDAO);
        dataStore.save(fakeData);

        dataStore.delete(fakeData);

        List<ListItemDAO> resultData = checkDataInDatabase(dataStore);

        assertThat(resultData.size(), is(0));
    }

    @Test
    public void getItemsByParentId_HappyCase() {
        List<ListItemDAO> fakeData = Arrays.asList(listItemDAO, listItemDAO, listItemDAO);
        dataStore.save(fakeData);

        TestSubscriber<List<ListItemDAO>> subscriber = new TestSubscriber<>();
        dataStore.getItems(FAKE_PARENT_LIST_ID).subscribe(subscriber);

        subscriber.assertNoErrors();
        List<List<ListItemDAO>> onNextEvents = subscriber.getOnNextEvents();
        List<ListItemDAO> resultData = onNextEvents.get(0);

        assertThat(resultData.size(), is(3));
    }

    @Test
    public void update_HappyCase() {
        List<ListItemDAO> fakeData = Collections.singletonList(listItemDAO);
        dataStore.save(fakeData);

        ListItemDAO toUpdate = new ListItemDAO(FAKE_ID,
                "name_updated",
                "parent_list_id_updated",
                "note_updated",
                FAKE_STATUS,
                categoryDAO,
                3,
                55.55,
                33.33,
                unitDAO,
                FAKE_TIME_CREATED,
                currencyDAO);
        dataStore.update(Collections.singletonList(toUpdate));

        ListItemDAO resultData = checkDataInDatabase(dataStore, FAKE_ID);

        assertThat(resultData.getId(), is(FAKE_ID));
        assertThat(resultData.getName(), is("name_updated"));
        assertThat(resultData.getParentListId(), is("parent_list_id_updated"));
        assertThat(resultData.getNote(), is("note_updated"));
        assertThat(resultData.getQuantity(), is(33.33));
        assertThat(resultData.getPrice(), is(55.55));
        assertThat(resultData.getPriority(), is(3));

        assertThat(resultData.getCategory().getName(), is(categoryDAO.getName()));
        assertThat(resultData.getCategory().getColor(), is(categoryDAO.getColor()));
        assertThat(resultData.getCategory().getId(), is(categoryDAO.getId()));

        assertThat(resultData.getUnit().getId(), is(unitDAO.getId()));
        assertThat(resultData.getUnit().getShortName(), is(unitDAO.getShortName()));
        assertThat(resultData.getUnit().getName(), is(unitDAO.getName()));

        assertThat(resultData.getCurrency().getId(), is(currencyDAO.getId()));
        assertThat(resultData.getCurrency().getName(), is(currencyDAO.getName()));
    }

    @Test
    public void clear_HappyCase() {
        List<ListItemDAO> fakeData = Collections.singletonList(listItemDAO);
        dataStore.save(fakeData);

        dataStore.clear();
        List<ListItemDAO> resultData = checkDataInDatabase(dataStore);

        assertThat(resultData.size(), is(0));
    }
}