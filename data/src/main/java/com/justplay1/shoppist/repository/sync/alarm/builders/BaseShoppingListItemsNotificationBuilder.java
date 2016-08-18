//package com.justplay1.shoppist.repository.sync.alarm.builders;
//
///**
// * Created by Mkhytar on 14.11.2015.
// */
//
//import android.content.Context;
//
//import com.justplay1.shoppist.entity.ListItemDAO;
//import com.justplay1.shoppist.entity.NotificationDAO;
//import com.justplay1.shoppist.entity.NotificationStatus;
//import com.justplay1.shoppist.preferences.ShoppistPreferences;
//import com.justplay1.shoppist.repository.datasource.local.LocalSetData;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//public abstract class BaseShoppingListItemsNotificationBuilder extends NotificationBuilder<ListItemDAO> {
//
//    public BaseShoppingListItemsNotificationBuilder(Context context,
//                                                    ShoppistPreferences preferences,
//                                                    LocalSetData<NotificationDAO> dataStore) {
//        super(context, preferences, dataStore);
//    }
//
//    @Override
//    public void addUpdatedItems(Map<ListItemDAO, ListItemDAO> items) {
//        if (items == null) return;
//
//        Map<ListItemDAO, ListItemDAO> simpleUpdated = new HashMap<>();
//        Map<ListItemDAO, ListItemDAO> boughtItems = new HashMap<>();
//        Map<ListItemDAO, ListItemDAO> changeStatusToNotBoughtItems = new HashMap<>();
//
//        for (Map.Entry<ListItemDAO, ListItemDAO> item : items.entrySet()) {
//            ListItemDAO old = item.getKey();
//            ListItemDAO value = item.getValue();
//            if (old.getStatus() != value.getStatus() && value.getStatus() == 1) {
//                boughtItems.put(old, value);
//            } else if (old.getStatus() != value.getStatus() && value.getStatus() == 0) {
//                changeStatusToNotBoughtItems.put(old, value);
//            } else {
//                simpleUpdated.put(old, value);
//            }
//        }
//
//        if (boughtItems.size() > 0 && mPreferences.isAvailableBoughtNotification()) {
//            String title = getTitleForStatusDoneObjects(boughtItems);
//            NotificationDAO notification = new NotificationDAO();
//            notification.setTitle(title);
//            notification.setItemNames(buildFullMessageForUpdatedItems(boughtItems));
//            notification.setTime(System.currentTimeMillis());
//            notification.setStatus(NotificationStatus.BOUGHT);
//            notification.setType(getNotificationType());
//            notification.setId(UUID.nameUUIDFromBytes((title + notification.getItemNames()
//                    + notification.getTime() + notification.getStatus()).getBytes()).toString());
//            mNotifications.add(notification);
//        }
//
//        if (changeStatusToNotBoughtItems.size() > 0 && mPreferences.isAvailableNotBoughtNotification()) {
//            String title = getTitleForStatusNotDoneObjects(changeStatusToNotBoughtItems);
//            NotificationDAO notification = new NotificationDAO();
//            notification.setTitle(title);
//            notification.setItemNames(buildFullMessageForUpdatedItems(changeStatusToNotBoughtItems));
//            notification.setTime(System.currentTimeMillis());
//            notification.setStatus(NotificationStatus.NOT_BOUGHT);
//            notification.setType(getNotificationType());
//            notification.setId(UUID.nameUUIDFromBytes((title + notification.getItemNames()
//                    + notification.getTime() + notification.getStatus()).getBytes()).toString());
//            mNotifications.add(notification);
//        }
//
//        if (simpleUpdated.size() > 0 && mPreferences.isAvailableUpdateNotification()) {
//            String title = getTitleForUpdatedObjects(simpleUpdated);
//            NotificationDAO notification = new NotificationDAO();
//            notification.setTitle(title);
//            notification.setItemNames(buildFullMessageForUpdatedItems(simpleUpdated));
//            notification.setTime(System.currentTimeMillis());
//            notification.setStatus(NotificationStatus.UPDATE);
//            notification.setType(getNotificationType());
//            notification.setId(UUID.nameUUIDFromBytes((title + notification.getItemNames()
//                    + notification.getTime() + notification.getStatus()).getBytes()).toString());
//            mNotifications.add(notification);
//        }
//    }
//
//    protected abstract String getTitleForStatusDoneObjects(Map<ListItemDAO, ListItemDAO> items);
//
//    protected abstract String getTitleForStatusNotDoneObjects(Map<ListItemDAO, ListItemDAO> items);
//}
