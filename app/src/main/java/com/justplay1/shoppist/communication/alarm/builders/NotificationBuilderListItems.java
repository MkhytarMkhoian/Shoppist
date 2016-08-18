package com.justplay1.shoppist.communication.alarm.builders;

/**
 * Created by Mkhytar on 14.11.2015.
 */

import android.content.Context;

import com.justplay1.shoppist.models.BaseModel;
import com.justplay1.shoppist.models.Notification;
import com.justplay1.shoppist.models.NotificationStatus;
import com.justplay1.shoppist.models.Status;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class NotificationBuilderListItems<T extends BaseModel> extends NotificationBuilder<T> {

    public NotificationBuilderListItems(Context context) {
        super(context);
    }

    @Override
    public void addUpdatedItems(Map<T, T> items) {
        if (items == null) return;

        Map<T, T> simpleUpdated = new HashMap<>();
        Map<T, T> boughtItems = new HashMap<>();
        Map<T, T> changeStatusToNotBoughtItems = new HashMap<>();

        for (Map.Entry<T, T> item : items.entrySet()) {
            T old = item.getKey();
            T value = item.getValue();
            if (old.getStatus() != value.getStatus() && value.getStatus() == Status.DONE) {
                boughtItems.put(old, value);
            } else if (old.getStatus() != value.getStatus() && value.getStatus() == Status.NOT_DONE) {
                changeStatusToNotBoughtItems.put(old, value);
            } else {
                simpleUpdated.put(old, value);
            }
        }

        if (boughtItems.size() > 0 && ShoppistPreferences.isAvailableBoughtNotification()) {
            String title = getTitleForStatusDoneObjects(boughtItems);
            Notification notification = new Notification();
            notification.setTitle(title);
            notification.setItemNames(buildFullMessageForUpdatedItems(boughtItems));
            notification.setTime(System.currentTimeMillis());
            notification.setStatus(NotificationStatus.BOUGHT);
            notification.setType(getNotificationType());
            notification.setId(UUID.nameUUIDFromBytes((title + notification.getItemNames()
                    + notification.getTime() + notification.getStatus().name()).getBytes()).toString());
            mNotifications.add(notification);
        }

        if (changeStatusToNotBoughtItems.size() > 0 && ShoppistPreferences.isAvailableNotBoughtNotification()) {
            String title = getTitleForStatusNotDoneObjects(changeStatusToNotBoughtItems);
            Notification notification = new Notification();
            notification.setTitle(title);
            notification.setItemNames(buildFullMessageForUpdatedItems(changeStatusToNotBoughtItems));
            notification.setTime(System.currentTimeMillis());
            notification.setStatus(NotificationStatus.NOT_BOUGHT);
            notification.setType(getNotificationType());
            notification.setId(UUID.nameUUIDFromBytes((title + notification.getItemNames()
                    + notification.getTime() + notification.getStatus().name()).getBytes()).toString());
            mNotifications.add(notification);
        }

        if (simpleUpdated.size() > 0 && ShoppistPreferences.isAvailableUpdateNotification()) {
            String title = getTitleForUpdatedObjects(simpleUpdated);
            Notification notification = new Notification();
            notification.setTitle(title);
            notification.setItemNames(buildFullMessageForUpdatedItems(simpleUpdated));
            notification.setTime(System.currentTimeMillis());
            notification.setStatus(NotificationStatus.UPDATE);
            notification.setType(getNotificationType());
            notification.setId(UUID.nameUUIDFromBytes((title + notification.getItemNames()
                    + notification.getTime() + notification.getStatus().name()).getBytes()).toString());
            mNotifications.add(notification);
        }
    }

    protected abstract String getTitleForStatusDoneObjects(Map<T, T> items);

    protected abstract String getTitleForStatusNotDoneObjects(Map<T, T> items);
}
