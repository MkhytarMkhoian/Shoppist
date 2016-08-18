package com.justplay1.shoppist.repository.datasource.local.database;

import android.content.ContentValues;

import com.justplay1.shoppist.entity.NotificationDAO;
import com.justplay1.shoppist.repository.datasource.local.LocalNotificationDataStore;
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
public class LocalNotificationDataStoreImpl extends BaseLocalDataStore<NotificationDAO> implements LocalNotificationDataStore {

    private static String NEW_NOTIFICATIONS_COUNT_QUERY =
            "SELECT COUNT(" + NotificationDAO.TIME + ") FROM " + NotificationDAO.TABLE +
                    " WHERE " + NotificationDAO.TIME + " > ?";

    public static String NOTIFICATIONS_QUERY(String selection) {
        return "SELECT * FROM " + NotificationDAO.TABLE + " s" +
                " WHERE " + selection +
                " GROUP BY s." + NotificationDAO.TIME + " DESC";
    }

    @Inject
    public LocalNotificationDataStoreImpl(BriteDatabase db) {
        super(db);
    }

    @Override
    public Observable<List<NotificationDAO>> getItems() {
        return getAllNotifications(0);
    }

    @Override
    public Observable<List<NotificationDAO>> getItems(long timestamp) {
        return getAllNotifications(timestamp);
    }

    @Override
    public Observable<List<NotificationDAO>> getItems(List<String> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<NotificationDAO> getItem(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(Collection<NotificationDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (NotificationDAO notification : data) {
                db.insert(NotificationDAO.TABLE, getValue(notification));
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public void save(NotificationDAO data) {
        save(Collections.singletonList(data));
    }

    @Override
    public void delete(Collection<NotificationDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (NotificationDAO notification : data) {
                db.delete(NotificationDAO.TABLE, NotificationDAO.WHERE_STRING, notification.getId());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public void delete(NotificationDAO data) {
        delete(Collections.singletonList(data));
    }

    @Override
    public void update(Collection<NotificationDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (NotificationDAO notification : data) {
                db.update(NotificationDAO.TABLE, getValue(notification),
                        NotificationDAO.WHERE_STRING, notification.getId());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public void update(NotificationDAO data) {
        update(Collections.singletonList(data));
    }

    @Override
    public int clear() {
        return clear(NotificationDAO.TABLE);
    }

    @Override
    protected ContentValues getValue(NotificationDAO data) {
        return new NotificationDAO.Builder()
                .id(data.getId())
                .itemNames(NotificationDAO.toJson(data.getItemNames()))
                .status(data.getStatus())
                .time(data.getTime())
                .title(data.getTitle())
                .type(data.getType())
                .build();
    }

    @Override
    public Observable<Long> getNewNotificationCount(long time) {
        return getValue(NotificationDAO.TABLE, NEW_NOTIFICATIONS_COUNT_QUERY, new String[]{time + ""});
    }

    private Observable<List<NotificationDAO>> getAllNotifications(long time) {
        String selection = null;
        String[] selectionArgs = new String[]{};
        if (time > 0) {
            selection = NotificationDAO.TIME + " => ?";
            selectionArgs = new String[]{time + ""};
        }

        return db.createQuery(NotificationDAO.TABLE, NOTIFICATIONS_QUERY(selection), selectionArgs)
                .mapToList(NotificationDAO.MAPPER::call);
    }
}
