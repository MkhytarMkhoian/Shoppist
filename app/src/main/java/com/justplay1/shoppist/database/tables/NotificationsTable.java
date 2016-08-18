package com.justplay1.shoppist.database.tables;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.justplay1.shoppist.database.DBHelper;
import com.justplay1.shoppist.database.BaseTable;
import com.justplay1.shoppist.database.ShoppingListContact;
import com.justplay1.shoppist.database.ShoppingListContact.Notifications;
import com.justplay1.shoppist.models.Notification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mkhytar on 12.11.2015.
 */
public class NotificationsTable extends BaseTable<Notification> {

    public static final String TAG = NotificationsTable.class.getSimpleName();

    public NotificationsTable(Context context) {
        super(context);
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL("create table " + DBHelper.Tables.NOTIFICATIONS + "(" +
                ShoppingListContact.COLUMN_ID + " integer primary key autoincrement, " +
                Notifications.NOTIFICATION_ID + " text, " +
                Notifications.TITLE + " text, " +
                Notifications.ITEM_NAMES + " text, " +
                Notifications.STATUS + " integer, " +
                Notifications.TYPE + " integer, " +
                Notifications.TIME + " integer DEFAULT 0 " +
                ");");
    }

    private void notifyNotificationsChange() {
        mContext.getContentResolver().notifyChange(ShoppingListContact.Notifications.CONTENT_URI, null, false);
    }

    @Override
    public Uri put(Notification obj) throws RemoteException, OperationApplicationException {
        return put(Collections.singletonList(obj))[0].uri;
    }

    @Override
    public ContentProviderResult delete(Notification obj) throws RemoteException, OperationApplicationException {
        return delete(Collections.singletonList(obj))[0];
    }

    @Override
    public ContentProviderResult update(Notification newObj) throws RemoteException, OperationApplicationException {
        return update(Collections.singletonList(newObj))[0];
    }

    @Override
    public ContentProviderResult[] put(Collection<Notification> obj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (Notification notification : obj) {
            operations.add(ContentProviderOperation.newInsert(Notifications.CONTENT_URI).withValues(getValue(notification)).build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyNotificationsChange();
        return result;
    }

    @Override
    public ContentProviderResult[] delete(Collection<Notification> obj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (Notification notification : obj) {
            operations.add(ContentProviderOperation.newDelete(Notifications.buildNotificationsUri(notification.getId())).build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyNotificationsChange();
        return result;
    }

    @Override
    public ContentProviderResult[] update(Collection<Notification> newObj) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (Notification notification : newObj) {
            operations.add(ContentProviderOperation.newUpdate(Notifications.CONTENT_URI)
                    .withSelection(Notifications.WHERE_STRING, new String[]{notification.getId()})
                    .withValues(getValue(notification))
                    .build());
        }
        ContentProviderResult[] result = mContext.getContentResolver().applyBatch(ShoppingListContact.AUTHORITY, operations);
        notifyNotificationsChange();
        return result;
    }

    @Override
    public int clear() {
        int result = mContext.getContentResolver().delete(Notifications.CONTENT_URI, null, null);
        notifyNotificationsChange();
        return result;
    }

    @Override
    protected ContentValues getValue(Notification data) {
        ContentValues values = new ContentValues();
        values.put(Notifications.NOTIFICATION_ID, data.getId());
        values.put(Notifications.ITEM_NAMES, data.toJson(data.getItemNames()));
        values.put(Notifications.TITLE, data.getTitle());
        values.put(Notifications.TIME, data.getTime());
        values.put(Notifications.TYPE, data.getType());
        values.put(Notifications.STATUS, data.getStatus().ordinal());
        return values;
    }

    public int getNewNotificationCount(long time) {
        return (int) getValue(Notifications.NEW_NOTIFICATIONS_COUNT_URI, Notifications.TIME + " > ?", new String[]{time + ""});
    }

    private List<Notification> getAllNotifications(Cursor data) {
        List<Notification> categories = new ArrayList<>();
        try {
            if (!data.isClosed() && data.moveToFirst()) {
                do {
                    Notification item = new Notification(data);
                    categories.add(item);
                } while (data.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (data != null) {
                data.close();
            }
        }
        return categories;
    }

    public List<Notification> getAllNotifications() {
        return getAllNotifications(getAllNotificationsCursor(0));
    }

    public List<Notification> getAllNotifications(long time) {
        return getAllNotifications(getAllNotificationsCursor(time));
    }

    public Cursor getAllNotificationsCursor(long time) {
        String sortOrder = Notifications.TIME + " DESC";

        String selection = null;
        String[] selectionArgs = null;
        if (time > 0) {
            selection = Notifications.TIME + " => ?";
            selectionArgs = new String[]{time + ""};
        }

        return mContext.getContentResolver().query(Notifications.CONTENT_URI, null, selection, selectionArgs, sortOrder);
    }
}
