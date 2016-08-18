//package com.justplay1.shoppist.repository.sync.alarm.builders;
//
//import android.content.Context;
//import android.content.Intent;
//
//import com.justplay1.shoppist.entity.NotificationDAO;
//import com.justplay1.shoppist.entity.NotificationStatus;
//import com.justplay1.shoppist.preferences.ShoppistPreferences;
//import com.justplay1.shoppist.repository.datasource.local.LocalSetData;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
///**
// * Created by Mkhytar on 13.11.2015.
// */
//public abstract class NotificationBuilder<T> {
//
//    protected Context mContext;
//    protected List<NotificationDAO> mNotifications;
//    protected ShoppistPreferences mPreferences;
//    protected LocalSetData<NotificationDAO> mSetData;
//
//    public NotificationBuilder(Context context, ShoppistPreferences preferences, LocalSetData<NotificationDAO> dataStore) {
//        mContext = context;
//        mPreferences = preferences;
//        mNotifications = new ArrayList<>();
//        mSetData = dataStore;
//    }
//
//    protected abstract int getNotificationType();
//
//    protected abstract String getName(T item);
//
//    protected abstract String getTitleForNewObjects(List<T> items);
//
//    protected abstract String getTitleForUpdatedObjects(Map<T, T> items);
//
//    protected abstract String getTitleForDeletedObjects(List<T> items);
//
//    public void addUpdatedItems(Map<T, T> items) {
//        if (!mPreferences.isAvailableUpdateNotification()) return;
//        if (items == null) return;
//        if (items.size() == 0) return;
//
//        String title = getTitleForUpdatedObjects(items);
//
//        NotificationDAO notification = new NotificationDAO();
//        notification.setTitle(title);
//        notification.setItemNames(buildFullMessageForUpdatedItems(items));
//        notification.setTime(System.currentTimeMillis());
//        notification.setStatus(NotificationStatus.UPDATE);
//        notification.setType(getNotificationType());
//        notification.setId(UUID.nameUUIDFromBytes((title + notification.getItemNames()
//                + notification.getTime() + notification.getStatus()).getBytes()).toString());
//        mNotifications.add(notification);
//    }
//
//    public void addNewItems(List<T> items) {
//        if (!mPreferences.isAvailableNewNotification()) return;
//        if (items == null) return;
//        if (items.size() == 0) return;
//
//        String title = getTitleForNewObjects(items);
//
//        NotificationDAO notification = new NotificationDAO();
//        notification.setTitle(title);
//        notification.setItemNames(buildSimpleFullMessage(items));
//        notification.setTime(System.currentTimeMillis());
//        notification.setStatus(NotificationStatus.NEW);
//        notification.setType(getNotificationType());
//        notification.setId(UUID.nameUUIDFromBytes((title + notification.getItemNames()
//                + notification.getTime() + notification.getStatus()).getBytes()).toString());
//        mNotifications.add(notification);
//    }
//
//    public void addDeletedItems(List<T> items) {
//        if (!mPreferences.isAvailableDeleteNotification()) return;
//        if (items == null) return;
//        if (items.size() == 0) return;
//
//        String title = getTitleForDeletedObjects(items);
//
//        NotificationDAO notification = new NotificationDAO();
//        notification.setTitle(title);
//        notification.setItemNames(buildSimpleFullMessage(items));
//        notification.setTime(System.currentTimeMillis());
//        notification.setStatus(NotificationStatus.DELETE);
//        notification.setType(getNotificationType());
//        notification.setId(UUID.nameUUIDFromBytes((title + notification.getItemNames()
//                + notification.getTime() + notification.getStatus()).getBytes()).toString());
//        mNotifications.add(notification);
//    }
//
//    public List<NotificationDAO> save() throws Exception {
//        mSetData.save(mNotifications);
//        return mNotifications;
//    }
//
//    protected List<String> buildSimpleFullMessage(Collection<T> items) {
//        List<String> result = new ArrayList<>();
//        for (T item : items) {
//            result.add(getName(item));
//        }
//        return result;
//    }
//
//    protected List<String> buildFullMessageForUpdatedItems(Map<T, T> items) {
//        List<String> result = new ArrayList<>();
//        for (Map.Entry<T, T> item : items.entrySet()) {
//            T old = item.getKey();
//            T value = item.getValue();
//            if (!getName(old).equals(getName(value))) {
//                result.add(getName(old) + " -> " + getName(value));
//            } else {
//                result.add(getName(value));
//            }
//        }
//        return result;
//    }
//
//    public void notifyUser() {
//        mContext.sendBroadcast(new Intent("notify_user"));
//    }
//}
