//package com.justplay1.shoppist.repository.sync.alarm.builders;
//
//import android.content.Context;
//
//import com.justplay1.shoppist.data.R;
//import com.justplay1.shoppist.di.scopes.PerSync;
//import com.justplay1.shoppist.entity.ListDAO;
//import com.justplay1.shoppist.entity.NotificationDAO;
//import com.justplay1.shoppist.entity.NotificationType;
//import com.justplay1.shoppist.preferences.AppPreferences;
//import com.justplay1.shoppist.repository.datasource.local.LocalSetData;
//
//import java.util.List;
//import java.util.Map;
//
//import javax.inject.Inject;
//
///**
// * Created by Mkhytar on 13.11.2015.
// */
//@PerSync
//public class ListsNotificationBuilder extends NotificationBuilder<ListDAO> {
//
//    @Inject
//    public ListsNotificationBuilder(Context context, AppPreferences preferences, LocalSetData<NotificationDAO> dataStore) {
//        super(context, preferences, dataStore);
//    }
//
//    @Override
//    protected int getNotificationType() {
//        return NotificationType.SYNC_SHOPPING_LIST_DATA;
//    }
//
//    @Override
//    protected String getTitleForNewObjects(List<ListDAO> items) {
//        return mContext.getResources().getQuantityString(R.plurals.added_shopping_lists, items.size(), items.size());
//    }
//
//    @Override
//    protected String getTitleForUpdatedObjects(Map<ListDAO, ListDAO> items) {
//        return mContext.getResources().getQuantityString(R.plurals.update_shopping_lists, items.size(), items.size());
//    }
//
//    @Override
//    protected String getTitleForDeletedObjects(List<ListDAO> items) {
//        return mContext.getResources().getQuantityString(R.plurals.delete_shopping_lists, items.size(), items.size());
//    }
//
//    @Override
//    protected String getName(ListDAO item) {
//        return item.getName();
//    }
//}
