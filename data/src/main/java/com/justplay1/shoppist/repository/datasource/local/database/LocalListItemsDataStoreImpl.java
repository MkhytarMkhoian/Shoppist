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
 * Created by Mkhytar on 28.04.2016.
 */
@Singleton
public class LocalListItemsDataStoreImpl extends BaseLocalDataStore<ListItemDAO> implements LocalListItemsDataStore {

    private static final String WITHOUT_DELETED = ListItemDAO.IS_DELETED + "<1";
    private static final String LAST_TIMESTAMP_QUERY =
            "SELECT MAX(" + ListItemDAO.TIMESTAMP + ") FROM " + ListItemDAO.TABLE;

    public static final String ALL_LIST_ITEMS =
            "SELECT * FROM " +  ListItemDAO.TABLE +
            " LEFT OUTER JOIN " + CategoryDAO.TABLE
            + " ON " + ListItemDAO.CATEGORY_ID + " = " + CategoryDAO.TABLE + "." + CategoryDAO.CATEGORY_ID +
            " LEFT OUTER JOIN " + UnitDAO.TABLE
            + " ON " + ListItemDAO.UNIT_ID + " = " + UnitDAO.TABLE + "." + UnitDAO.UNIT_ID +
            " LEFT OUTER JOIN " + CurrencyDAO.TABLE
            + " ON " + ListItemDAO.CURRENCY_ID + " = " + CurrencyDAO.TABLE + "." + CurrencyDAO.CURRENCY_ID;

    public static String LIST_ITEMS_QUERY(String selection) {
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
        return getAllShoppingListItems(0, false);
    }

    @Override
    public Observable<List<ListItemDAO>> getDirtyItems() {
        return getAllShoppingListItems(0, true);
    }

    @Override
    public Observable<List<ListItemDAO>> getItems(long timestamp) {
        return getAllShoppingListItems(timestamp, false);
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

    public Observable<List<ListItemDAO>> getAllShoppingListItems(long timestamp, boolean getDirty) {
        String selection = WITHOUT_DELETED;
        String[] selectionArgs = null;
        if (timestamp > 0 && getDirty) {
            selection = ListItemDAO.TIMESTAMP + " > ? AND " + ListItemDAO.IS_DIRTY + " = ?";
            selectionArgs = new String[]{timestamp + "", 1 + ""};
        } else if (timestamp > 0) {
            selection = ListItemDAO.TIMESTAMP + " > ?";
            selectionArgs = new String[]{timestamp + ""};
        } else if (getDirty) {
            selection = ListItemDAO.IS_DIRTY + " = ?";
            selectionArgs = new String[]{1 + ""};
        }
        return db.createQuery(ListItemDAO.TABLE, LIST_ITEMS_QUERY(selection), selectionArgs)
                .mapToList(ListItemDAO.MAPPER::call);
    }

    public Observable<List<ListItemDAO>> getListItems(String listId) {
        return db.createQuery(ListItemDAO.TABLE,
                LIST_ITEMS_QUERY(ListItemDAO.PARENT_LIST_ID + "=? AND " + WITHOUT_DELETED),
                listId)
                .mapToList(ListItemDAO.MAPPER::call);
    }

    @Override
    public Observable<Long> getLastTimestamp() {
        return getValue(ListItemDAO.TABLE, LAST_TIMESTAMP_QUERY);
    }

    @Override
    protected ContentValues getValue(ListItemDAO item) {
        ListItemDAO.Builder builder = new ListItemDAO.Builder();
        if (item.getServerId() != null) {
            builder.serverId(item.getServerId());
        }
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
        builder.isDelete(item.isDelete());
        builder.isDirty(item.isDirty());
        builder.timestamp(item.getTimestamp());
        return builder.build();
    }
}
