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
import com.justplay1.shoppist.entity.ProductDAO;
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

import static com.justplay1.shoppist.entity.DAOUtil.FAKE_CREATE_BY_USER;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCategoryDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeProductDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeUnitDAO;
import static com.justplay1.shoppist.repository.datasource.local.database.DatabaseUtil.checkDataInDatabase;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Mkhytar Mkhoian.
 */
public class LocalGoodsDataStoreImplTest extends ApplicationTestCase {

    private LocalGoodsDataStoreImpl dataStore;
    private DBHelper helper;

    private CategoryDAO categoryDAO;
    private UnitDAO unitDAO;
    private ProductDAO productDAO;

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.application;

        helper = new DBHelper(context) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                try {
                    db.beginTransaction();
                    db.execSQL(CREATE_PRODUCTS_TABLE);
                    db.execSQL(CREATE_CATEGORIES_TABLE);
                    db.execSQL(CREATE_UNITS_TABLE);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }
        };
        SqlBrite sqlBrite = SqlBrite.create();
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.immediate());

        dataStore = new LocalGoodsDataStoreImpl(db, context);
        LocalCategoryDataStoreImpl categoryDataStore = new LocalCategoryDataStoreImpl(db, context);
        LocalUnitsDataStoreImpl unitsDataStore = new LocalUnitsDataStoreImpl(db, context);

        unitDAO = createFakeUnitDAO();
        categoryDAO = createFakeCategoryDAO();
        productDAO = createFakeProductDAO(unitDAO, categoryDAO);

        categoryDataStore.save(Collections.singletonList(categoryDAO));
        unitsDataStore.save(Collections.singletonList(unitDAO));
    }

    @After
    public void tearDown() {
        helper.close();
    }

    @Test
    public void saveAndQuery_HappyCase() {
        List<ProductDAO> fakeData = Collections.singletonList(productDAO);

        dataStore.save(fakeData);

        List<ProductDAO> resultData = checkDataInDatabase(dataStore);

        assertThat(resultData, is(fakeData));
    }

    @Test
    public void delete_HappyCase() {
        List<ProductDAO> fakeData = Collections.singletonList(productDAO);
        dataStore.save(fakeData);

        dataStore.delete(fakeData);

        List<ProductDAO> resultData = checkDataInDatabase(dataStore);

        assertThat(resultData.size(), is(0));
    }

    @Test
    public void update_HappyCase() {
        List<ProductDAO> fakeData = Collections.singletonList(productDAO);
        dataStore.save(fakeData);

        ProductDAO toUpdate = new ProductDAO(FAKE_ID, "name_updated", categoryDAO, FAKE_CREATE_BY_USER, 111L, unitDAO);
        dataStore.update(Collections.singletonList(toUpdate));

        ProductDAO resultData = checkDataInDatabase(dataStore, FAKE_ID);

        assertThat(resultData.getId(), is(FAKE_ID));
        assertThat(resultData.getName(), is("name_updated"));
        assertThat(resultData.getTimeCreated(), is(111L));

        assertThat(resultData.getCategory().getName(), is(categoryDAO.getName()));
        assertThat(resultData.getCategory().getColor(), is(categoryDAO.getColor()));
        assertThat(resultData.getCategory().getId(), is(categoryDAO.getId()));

        assertThat(resultData.getUnit().getId(), is(unitDAO.getId()));
        assertThat(resultData.getUnit().getShortName(), is(unitDAO.getShortName()));
        assertThat(resultData.getUnit().getName(), is(unitDAO.getName()));
    }

    @Test
    public void clear_HappyCase() {
        List<ProductDAO> fakeData = Collections.singletonList(productDAO);
        dataStore.save(fakeData);

        dataStore.clear();
        List<ProductDAO> resultData = checkDataInDatabase(dataStore);

        assertThat(resultData.size(), is(0));
    }
}