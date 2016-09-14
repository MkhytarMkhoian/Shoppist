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

import com.justplay1.shoppist.entity.CategoryDAO;
import com.justplay1.shoppist.entity.CurrencyDAO;
import com.justplay1.shoppist.entity.ListItemDAO;
import com.justplay1.shoppist.entity.UnitDAO;
import com.justplay1.shoppist.repository.datasource.local.LocalListItemsDataStore;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Mkhytar Mkhoian.
 */
@Singleton
public class LocalListItemsDataStoreImpl extends BaseLocalDataStore<ListItemDAO> implements LocalListItemsDataStore {

    public static final String ALL_LIST_ITEMS =
            "SELECT * FROM " + ListItemDAO.TABLE +
                    " LEFT OUTER JOIN " + CategoryDAO.TABLE
                    + " ON " + ListItemDAO.CATEGORY_ID + " = " + CategoryDAO.TABLE + "." + CategoryDAO.CATEGORY_ID +
                    " LEFT OUTER JOIN " + UnitDAO.TABLE
                    + " ON " + ListItemDAO.UNIT_ID + " = " + UnitDAO.TABLE + "." + UnitDAO.UNIT_ID +
                    " LEFT OUTER JOIN " + CurrencyDAO.TABLE
                    + " ON " + ListItemDAO.CURRENCY_ID + " = " + CurrencyDAO.TABLE + "." + CurrencyDAO.CURRENCY_ID;

    public static String LIST_ITEMS_QUERY(String selection) {
        if (selection == null){
            return ALL_LIST_ITEMS;
        }
        return ALL_LIST_ITEMS + " WHERE " + selection;
    }

    @Inject
    public LocalListItemsDataStoreImpl(BriteDatabase db) {
        super(db);
    }

    @Override
    public Observable<List<ListItemDAO>> getItems(String parentId) {
        return getListItems(parentId);
    }

    @Override
    public Observable<List<ListItemDAO>> getItems() {
        return getAllShoppingListItems();
    }

    @Override
    public Observable<ListItemDAO> getItem(String id) {
        return null;
    }

    @Override
    public void save(Collection<ListItemDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (ListItemDAO item : data) {
                db.insert(ListItemDAO.TABLE, getValue(item));
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
        notifyParentListChange();
    }

    @Override
    public void save(ListItemDAO data) {
        save(Collections.singletonList(data));
    }

    @Override
    public void delete(Collection<ListItemDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (ListItemDAO item : data) {
                db.delete(ListItemDAO.TABLE, ListItemDAO.WHERE_STRING, item.getParentListId(), item.getId());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
        notifyParentListChange();
    }

    @Override
    public void delete(ListItemDAO data) {
        delete(Collections.singletonList(data));
    }

    @Override
    public void update(Collection<ListItemDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (ListItemDAO item : data) {
                db.update(ListItemDAO.TABLE, getValue(item), ListItemDAO.WHERE_STRING, item.getParentListId(), item.getId());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
        notifyParentListChange();
    }

    @Override
    public void update(ListItemDAO data) {
        update(Collections.singletonList(data));
    }

    @Override
    public int clear() {
        return clear(ListItemDAO.TABLE);
    }

    private void notifyParentListChange() {//TODO
//        mContext.getContentResolver().notifyChange(ShoppingListContact.ShoppingLists.CONTENT_URI, null, false);
    }

    public Observable<List<ListItemDAO>> getAllShoppingListItems() {
        return db.createQuery(ListItemDAO.TABLE, LIST_ITEMS_QUERY(null), new String[]{})
                .mapToList(ListItemDAO.MAPPER::call);
    }

    public Observable<List<ListItemDAO>> getListItems(String listId) {
        return db.createQuery(ListItemDAO.TABLE,
                LIST_ITEMS_QUERY(ListItemDAO.PARENT_LIST_ID + "=?"),
                listId)
                .mapToList(ListItemDAO.MAPPER::call);
    }

    @Override
    protected ContentValues getValue(ListItemDAO item) {
        ListItemDAO.Builder builder = new ListItemDAO.Builder();
        builder.id(item.getId());
        builder.name(item.getName());
        builder.parentId(item.getParentListId());
        builder.unitId(item.getUnit().getId());
        builder.currencyId(item.getCurrency().getId());
        builder.price(item.getPrice());
        builder.quantity(item.getQuantity());
        builder.description(item.getNote());
        builder.status(item.getStatus());
        builder.priority(item.getPriority());
        builder.timeCreated(item.getTimeCreated());
        builder.categoryId(item.getCategory().getId());
        if (item.getPosition() != -1) {
            builder.position(item.getPosition());
        }
        return builder.build();
    }
}
