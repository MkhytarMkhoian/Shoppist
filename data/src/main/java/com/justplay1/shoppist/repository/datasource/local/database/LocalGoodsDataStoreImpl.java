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
import com.justplay1.shoppist.entity.CategoryDAO;
import com.justplay1.shoppist.entity.ProductDAO;
import com.justplay1.shoppist.entity.UnitDAO;
import com.justplay1.shoppist.repository.datasource.local.LocalGoodsDataStore;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Mkhytar Mkhoian.
 */
@Singleton
public class LocalGoodsDataStoreImpl extends BaseLocalDataStore<ProductDAO> implements LocalGoodsDataStore {

    private static String PRODUCTS_QUERY(String selection) {
        String query = "SELECT * FROM " + ProductDAO.TABLE +
                " LEFT OUTER JOIN " + CategoryDAO.TABLE
                + " ON " + ProductDAO.CATEGORY_ID + " = " + CategoryDAO.TABLE
                + "." + CategoryDAO.CATEGORY_ID +
                " LEFT OUTER JOIN " + UnitDAO.TABLE
                + " ON " + ProductDAO.UNIT_ID + " = " + UnitDAO.TABLE
                + "." + UnitDAO.UNIT_ID;
        if (selection == null) {
            return query;
        }
        return query + " WHERE " + selection;
    }

    private Context context;

    @Inject
    public LocalGoodsDataStoreImpl(BriteDatabase db, Context context) {
        super(db);
        this.context = context;
    }

    public Observable<Map<String, ProductDAO>> getDefaultData() {
        String[] products = context.getResources().getStringArray(R.array.products);
        Map<String, ProductDAO> newProducts = new HashMap<>(products.length);

        for (String item : products) {
            String[] productsName = item.split(" ! ");

            CategoryDAO category = new CategoryDAO(productsName[1]);
            UnitDAO unit = new UnitDAO(UnitDAO.NO_UNIT_ID);

            ProductDAO product = new ProductDAO(UUID.nameUUIDFromBytes((System.nanoTime() + "").getBytes()).toString(),
                    productsName[0], category, false, System.currentTimeMillis(), unit);
            newProducts.put(product.getId(), product);
        }
        return Observable.just(newProducts);
    }

    @Override
    public Observable<List<ProductDAO>> getItems() {
        return getAllProducts();
    }

    @Override
    public Observable<ProductDAO> getItem(String id) {
        return db.createQuery(ProductDAO.TABLE, PRODUCTS_QUERY(ProductDAO.WHERE_PRODUCT_ID), id)
                .mapToOne(ProductDAO.MAPPER);
    }

    @Override
    public void save(Collection<ProductDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (ProductDAO product : data) {
                db.insert(ProductDAO.TABLE, getValue(product));
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public void save(ProductDAO data) {
        save(Collections.singletonList(data));
    }

    @Override
    public void delete(Collection<ProductDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (ProductDAO product : data) {
                db.delete(ProductDAO.TABLE, ProductDAO.WHERE_PRODUCT_ID, product.getId());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public void delete(ProductDAO data) {
        delete(Collections.singletonList(data));
    }

    @Override
    public void update(Collection<ProductDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (ProductDAO product : data) {
                db.update(ProductDAO.TABLE, getValue(product), ProductDAO.WHERE_PRODUCT_ID, product.getId());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public void update(ProductDAO data) {
        update(Collections.singletonList(data));
    }

    @Override
    protected ContentValues getValue(ProductDAO product) {
        ProductDAO.Builder builder = new ProductDAO.Builder();
        builder.id(product.getId());
        builder.name(product.getName());
        builder.categoryId(product.getCategory().getId());
        builder.isCreateByUser(product.isCreateByUser());
        builder.timeCreated(product.getTimeCreated());
        builder.unitId(product.getUnit().getId());
        return builder.build();
    }

    @Override
    public int clear() {
        return clear(ProductDAO.TABLE);
    }

    private Observable<List<ProductDAO>> getAllProducts() {
        return db.createQuery(ProductDAO.TABLE, PRODUCTS_QUERY(null), new String[]{})
                .mapToList(ProductDAO.MAPPER);
    }
}
