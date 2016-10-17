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
import com.justplay1.shoppist.entity.CategoryDAO;
import com.justplay1.shoppist.repository.datasource.local.LocalCategoryDataStore;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.Collection;
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
public class LocalCategoryDataStoreImpl extends BaseLocalDataStore<CategoryDAO> implements LocalCategoryDataStore {

    private static String CATEGORY_QUERY(String selection) {
        if (selection == null) {
            return "SELECT * FROM " + CategoryDAO.TABLE;
        }
        return "SELECT * FROM " + CategoryDAO.TABLE +
                " WHERE " + selection;
    }

    private final Context context;

    @Inject
    public LocalCategoryDataStoreImpl(BriteDatabase db, Context context) {
        super(db);
        this.context = context;
    }

    public Observable<Map<String, CategoryDAO>> getDefaultData() {
        String[] categories = context.getResources().getStringArray(R.array.categories);
        int[] categoriesColors = context.getResources().getIntArray(R.array.categories_colors);
        String[] categoriesName;

        Map<String, CategoryDAO> categoryList = new HashMap<>(categories.length);
        for (int i = 0; i < categories.length; i++) {
            categoriesName = categories[i].split(" ! ");
            String id = categoriesName[1];
            String name = categoriesName[0];
            int color = categoriesColors[i];
            boolean isCreateByUser = false;
            CategoryDAO category = new CategoryDAO(id, name,
                    color, isCreateByUser);
            categoryList.put(category.getId(), category);
        }
        return Observable.just(categoryList);
    }

    @Override
    public Observable<List<CategoryDAO>> getItems() {
        return db.createQuery(CategoryDAO.TABLE, CATEGORY_QUERY(null), new String[]{})
                .mapToList(cursor -> CategoryDAO.map(cursor, CategoryDAO.COL_ID));
    }

    @Override
    public Observable<CategoryDAO> getItem(String id) {
        return db.createQuery(CategoryDAO.TABLE, CATEGORY_QUERY(CategoryDAO.WHERE_CATEGORY_ID), id)
                .mapToOne(cursor -> CategoryDAO.map(cursor, CategoryDAO.COL_ID));
    }

    @Override
    public void save(Collection<CategoryDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (CategoryDAO category : data) {
                db.insert(CategoryDAO.TABLE, getValue(category));
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
        notifyShoppingListItemsChange();
    }

    @Override
    public void delete(Collection<CategoryDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (CategoryDAO category : data) {
                db.delete(CategoryDAO.TABLE, CategoryDAO.WHERE_CATEGORY_ID, category.getId());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
        notifyShoppingListItemsChange();
    }

    @Override
    public void update(Collection<CategoryDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (CategoryDAO category : data) {
                db.update(CategoryDAO.TABLE, getValue(category),
                        CategoryDAO.WHERE_CATEGORY_ID, category.getId());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
        notifyShoppingListItemsChange();
    }

    @Override
    public int clear() {
        return clear(CategoryDAO.TABLE);
    }

    @Override
    protected ContentValues getValue(CategoryDAO data) {
        CategoryDAO.Builder builder = new CategoryDAO.Builder();
        builder.id(data.getId());
        builder.name(data.getName());
        builder.color(data.getColor());
        builder.createByUser(data.isCreateByUser());
        return builder.build();
    }

    private void notifyShoppingListItemsChange() {
        DataEventBus.instanceOf().post(new ListItemsDataUpdatedEvent());
    }
}
