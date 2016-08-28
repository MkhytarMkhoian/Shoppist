package com.justplay1.shoppist.repository.datasource.local.database;

import android.content.ContentValues;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import rx.Observable;

/**
 * Created by Mkhytar on 28.04.2016.
 */
public abstract class BaseLocalDataStore<T> {

    protected BriteDatabase db;

    public BaseLocalDataStore(BriteDatabase db) {
        this.db = db;
    }

    protected abstract ContentValues getValue(T data);

    protected Observable<Long> getValue(String table, String sql) {
        return getValue(table, sql, null);
    }

    protected Observable<Long> getValue(String table, String sql, String[] selectionArgs) {
        return db.createQuery(table, sql, selectionArgs)
                .mapToOne(cursor -> {
                    if (!cursor.isClosed() && cursor.moveToFirst()) {
                        return cursor.getLong(0);
                    }
                    return null;
                });
    }

    protected String toStringItemIds(List<String> ids) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            builder.append(ids.get(i));
            if (i < ids.size() - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    protected int clear(String table) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        int result = 0;
        try {
            result = db.delete(table, null, "");
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
        return result;
    }
}