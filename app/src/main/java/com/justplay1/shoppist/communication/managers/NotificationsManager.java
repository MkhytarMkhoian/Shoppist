package com.justplay1.shoppist.communication.managers;

import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;

import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.communication.ThreadExecutor;
import com.justplay1.shoppist.database.tables.NotificationsTable;
import com.justplay1.shoppist.models.Notification;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Mkhytar on 12.11.2015.
 */
public class NotificationsManager {

    private NotificationsTable mNotificationsTable;

    public NotificationsManager(Context context) {
        mNotificationsTable = new NotificationsTable(context);
    }

    public NotificationsTable getNotificationsTable() {
        return mNotificationsTable;
    }

    public List<Notification> getNotifications() {
        return mNotificationsTable.getAllNotifications();
    }

    public List<Notification> getNotifications(long time) {
        return mNotificationsTable.getAllNotifications(time);
    }

    public Cursor getNotificationsCursor() {
        return mNotificationsTable.getAllNotificationsCursor(0);
    }

    public void add(Notification notification) {
        try {
            mNotificationsTable.put(notification);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    public void addAll(List<Notification> notifications) {
        try {
            mNotificationsTable.put(notifications);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    public void clearNotifications(ExecutorListener<Boolean> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mNotificationsTable.clear();
                return true;
            }
        }, listener);
    }

    public void clearNotifications() {
        mNotificationsTable.clear();
    }

    public void delete(final Notification notification, ExecutorListener<Boolean> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mNotificationsTable.delete(notification);
                return true;
            }
        }, listener);
    }

    public void getNewNotificationCount(final long time, ExecutorListener<Integer> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return mNotificationsTable.getNewNotificationCount(time);
            }
        }, listener);
    }

    public int getNewNotificationCount(long time) {
        return mNotificationsTable.getNewNotificationCount(time);
    }
}
