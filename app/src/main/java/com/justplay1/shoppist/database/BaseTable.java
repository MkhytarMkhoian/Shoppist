package com.justplay1.shoppist.database;

import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import java.util.Collection;
import java.util.List;

public abstract class BaseTable<T> {

    protected Context mContext;

    public BaseTable(Context context) {
        mContext = context;
    }

    public abstract Uri put(final T obj) throws RemoteException, OperationApplicationException;

    public abstract ContentProviderResult delete(final T obj) throws RemoteException, OperationApplicationException;

    public abstract ContentProviderResult update(final T newObj) throws RemoteException, OperationApplicationException;

    public abstract ContentProviderResult[] put(final Collection<T> obj) throws RemoteException, OperationApplicationException;

    public abstract ContentProviderResult[] delete(final Collection<T> obj) throws RemoteException, OperationApplicationException;

    public abstract ContentProviderResult[] update(final Collection<T> newObj) throws RemoteException, OperationApplicationException;

    public abstract int clear();

    protected abstract ContentValues getValue(T data);

    public Context getContext() {
        return mContext;
    }

    protected long getValue(Uri uri) {
        return getValue(uri, null, null);
    }

    protected long getValue(Uri uri, String selection, String[] selectionArgs) {
        Cursor data = mContext.getContentResolver().query(uri, null, selection, selectionArgs, null);
        try {
            if (data != null && !data.isClosed() && data.moveToFirst()) {
                return data.getLong(0);
            }
        } finally {
            if (data != null) {
                data.close();
            }
        }
        return 0;
    }
}
