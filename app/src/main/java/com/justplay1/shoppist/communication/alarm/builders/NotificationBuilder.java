package com.justplay1.shoppist.communication.alarm.builders;

import android.content.Context;

import com.justplay1.shoppist.communication.alarm.ShoppistNotificationManager;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Notification;
import com.justplay1.shoppist.models.NotificationStatus;
import com.justplay1.shoppist.models.NotificationType;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mkhytar on 13.11.2015.
 */
public abstract class NotificationBuilder<T> {

    protected Context mContext;
    protected ShoppistNotificationManager mNotificationManager;
    protected List<Notification> mNotifications;

    public NotificationBuilder(Context context) {
        mContext = context;
        mNotificationManager = new ShoppistNotificationManager(context);
        mNotifications = new ArrayList<>();
    }

    protected abstract
    @NotificationType
    int getNotificationType();

    protected abstract String getName(T item);

    protected abstract String getTitleForNewObjects(List<T> items);

    protected abstract String getTitleForUpdatedObjects(Map<T, T> items);

    protected abstract String getTitleForDeletedObjects(List<T> items);

    public void addUpdatedItems(Map<T, T> items) {
        if (!ShoppistPreferences.isAvailableUpdateNotification()) return;
        if (items == null) return;
        if (items.size() == 0) return;

        String title = getTitleForUpdatedObjects(items);

        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setItemNames(buildFullMessageForUpdatedItems(items));
        notification.setTime(System.currentTimeMillis());
        notification.setStatus(NotificationStatus.UPDATE);
        notification.setType(getNotificationType());
        notification.setId(UUID.nameUUIDFromBytes((title + notification.getItemNames()
                + notification.getTime() + notification.getStatus().name()).getBytes()).toString());
        mNotifications.add(notification);
    }

    public void addNewItems(List<T> items) {
        if (!ShoppistPreferences.isAvailableNewNotification()) return;
        if (items == null) return;
        if (items.size() == 0) return;

        String title = getTitleForNewObjects(items);

        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setItemNames(buildSimpleFullMessage(items));
        notification.setTime(System.currentTimeMillis());
        notification.setStatus(NotificationStatus.NEW);
        notification.setType(getNotificationType());
        notification.setId(UUID.nameUUIDFromBytes((title + notification.getItemNames()
                + notification.getTime() + notification.getStatus().name()).getBytes()).toString());
        mNotifications.add(notification);
    }

    public void addDeletedItems(List<T> items) {
        if (!ShoppistPreferences.isAvailableDeleteNotification()) return;
        if (items == null) return;
        if (items.size() == 0) return;

        String title = getTitleForDeletedObjects(items);

        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setItemNames(buildSimpleFullMessage(items));
        notification.setTime(System.currentTimeMillis());
        notification.setStatus(NotificationStatus.DELETE);
        notification.setType(getNotificationType());
        notification.setId(UUID.nameUUIDFromBytes((title + notification.getItemNames()
                + notification.getTime() + notification.getStatus().name()).getBytes()).toString());
        mNotifications.add(notification);
    }

    public List<Notification> save() {
        App.get().getNotificationsManager().addAll(mNotifications);
        return mNotifications;
    }

    protected List<String> buildSimpleFullMessage(Collection<T> items) {
        List<String> result = new ArrayList<>();
        for (T item : items) {
            result.add(getName(item));
        }
        return result;
    }

    protected List<String> buildFullMessageForUpdatedItems(Map<T, T> items) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<T, T> item : items.entrySet()) {
            T old = item.getKey();
            T value = item.getValue();
            if (!getName(old).equals(getName(value))) {
                result.add(getName(old) + " -> " + getName(value));
            } else {
                result.add(getName(value));
            }
        }
        return result;
    }

    public void notifyUser() {
        mNotificationManager.sendSyncDataNotification(mNotifications);
    }
}
