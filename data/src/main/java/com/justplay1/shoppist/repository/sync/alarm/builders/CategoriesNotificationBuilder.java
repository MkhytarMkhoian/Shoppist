//package com.justplay1.shoppist.repository.sync.alarm.builders;
//
//import android.content.Context;
//
//import com.justplay1.shoppist.data.R;
//import com.justplay1.shoppist.di.scopes.PerSync;
//import com.justplay1.shoppist.entity.CategoryDAO;
//import com.justplay1.shoppist.entity.NotificationDAO;
//import com.justplay1.shoppist.entity.NotificationType;
//import com.justplay1.shoppist.preferences.ShoppistPreferences;
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
//public class CategoriesNotificationBuilder extends NotificationBuilder<CategoryDAO> {
//
//    @Inject
//    public CategoriesNotificationBuilder(Context context, ShoppistPreferences preferences, LocalSetData<NotificationDAO> dataStore) {
//        super(context, preferences, dataStore);
//    }
//
//    @Override
//    protected int getNotificationType() {
//        return NotificationType.SYNC_CATEGORIES_DATA;
//    }
//
//    @Override
//    protected String getTitleForNewObjects(List<CategoryDAO> items) {
//        return mContext.getResources().getQuantityString(R.plurals.added_categories, items.size(), items.size());
//    }
//
//    @Override
//    protected String getTitleForUpdatedObjects(Map<CategoryDAO, CategoryDAO> items) {
//        return mContext.getResources().getQuantityString(R.plurals.update_categories, items.size(), items.size());
//    }
//
//    @Override
//    protected String getTitleForDeletedObjects(List<CategoryDAO> items) {
//        return mContext.getResources().getQuantityString(R.plurals.delete_categories, items.size(), items.size());
//    }
//
//    @Override
//    protected String getName(CategoryDAO item) {
//        return item.getName();
//    }
//}
