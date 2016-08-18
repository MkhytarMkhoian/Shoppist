package com.justplay1.shoppist.repository.datasource.local.database;

import android.content.ContentValues;

import com.justplay1.shoppist.entity.ListDAO;
import com.justplay1.shoppist.entity.ListItemDAO;
import com.justplay1.shoppist.repository.datasource.local.LocalListDataStore;
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
public class LocalListDataStoreImpl extends BaseLocalDataStore<ListDAO> implements LocalListDataStore {

    public static final String WITHOUT_DELETED = ListDAO.IS_DELETED + "<1";

    private static final String LAST_TIMESTAMP_QUERY =
            "SELECT MAX(" + ListDAO.TIMESTAMP + ") FROM " + ListDAO.TABLE;

    public static String LIST_QUERY(String selection) {
        return "SELECT *, COUNT(i." + ListItemDAO.STATUS + ") " + ListDAO.SIZE
                + ", SUM(i." + ListItemDAO.STATUS + ") " + ListDAO.BOUGHT_COUNT +
                " FROM " + ListDAO.TABLE + " s" +
                " LEFT OUTER JOIN " + ListItemDAO.TABLE + " i" +
                " ON s." + ListDAO.LIST_ID + " = i." + ListItemDAO.PARENT_LIST_ID +
                " AND " + "i." + ListItemDAO.IS_DELETED + "=0" +
                " WHERE " + selection +
                " GROUP BY s." + ListDAO.LIST_ID;
    }

    @Inject
    public LocalListDataStoreImpl(BriteDatabase db) {
        super(db);
    }

    @Override
    public Observable<List<ListDAO>> getItems() {
        return getAllLists(0, false);
    }

    @Override
    public Observable<List<ListDAO>> getDirtyItems() {
        return getAllLists(0, true);
    }

    @Override
    public Observable<List<ListDAO>> getItems(long timestamp) {
        return getAllLists(timestamp, false);
    }

    @Override
    public Observable<ListDAO> getItem(String id) {
        return db.createQuery(ListDAO.TABLE, LIST_QUERY(ListDAO.WHERE_STRING), id)
                .mapToOne(ListDAO.MAPPER::call);
    }

    @Override
    public void save(Collection<ListDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (ListDAO list : data) {
                db.insert(ListDAO.TABLE, getValue(list));
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public void save(ListDAO data) {
        save(Collections.singletonList(data));
    }

    @Override
    public void delete(Collection<ListDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (ListDAO list : data) {
                db.delete(ListDAO.TABLE, ListDAO.WHERE_STRING, list.getId());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public void delete(ListDAO data) {
        delete(Collections.singletonList(data));
    }

    @Override
    public void update(Collection<ListDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (ListDAO list : data) {
                db.update(ListDAO.TABLE, getValue(list), ListDAO.WHERE_STRING, list.getId());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public void update(ListDAO data) {
        update(Collections.singletonList(data));
    }

    @Override
    public int clear() {
        return clear(ListDAO.TABLE);
    }

    @Override
    public void markListItemsAsDeleted(String id) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            ContentValues values = new ListItemDAO.Builder()
                    .isDirty(true)
                    .isDelete(true)
                    .build();
            db.update(ListItemDAO.TABLE, values, ListItemDAO.PARENT_LIST_ID + "=?", id);
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public void deleteListItems(String id) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            String selection = ListItemDAO.PARENT_LIST_ID + "=? AND "
                    + ListItemDAO.SERVER_ID + " is null or " + ListItemDAO.SERVER_ID + "=?";
            String[] selectionArgs = new String[]{id, ""};
            db.delete(ListItemDAO.TABLE, selection, selectionArgs);
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public Observable<Long> getLastTimestamp() {
        return getValue(ListDAO.TABLE, LAST_TIMESTAMP_QUERY);
    }

    private Observable<List<ListDAO>> getAllLists(long timestamp, boolean getDirty) {
        String selection = WITHOUT_DELETED;
        String[] selectionArgs = null;
        if (timestamp > 0 && getDirty) {
            selection = ListDAO.TIMESTAMP + " > ? AND " + ListDAO.IS_DIRTY + " = ?";
            selectionArgs = new String[]{timestamp + "", "1"};
        } else if (timestamp > 0) {
            selection = ListDAO.TIMESTAMP + " > ?";
            selectionArgs = new String[]{timestamp + ""};
        } else if (getDirty) {
            selection = ListDAO.IS_DIRTY + " = ?";
            selectionArgs = new String[]{"1"};
        }
        return db.createQuery(ListDAO.TABLE, LIST_QUERY(selection), selectionArgs)
                .mapToList(ListDAO.MAPPER::call);
    }

    @Override
    protected ContentValues getValue(ListDAO list) {
        ListDAO.Builder builder = new ListDAO.Builder();
        if (list.getServerId() != null) {
            builder.serverId(list.getServerId());
        }
        builder.id(list.getId());
        builder.name(list.getName());
        builder.color(list.getColor());
        builder.priority(list.getPriority());
        builder.timeCreated(list.getTimeCreated());
        if (list.getPosition() != -1) {
            builder.position(list.getPosition());
        }
        builder.isDelete(list.isDelete());
        builder.isDirty(list.isDirty());
        builder.timestamp(list.getTimestamp());
        return builder.build();
    }
}

