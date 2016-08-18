//package com.justplay1.shoppist.repository.sync.alarm.builders;
//
//import android.content.Context;
//
//import com.justplay1.shoppist.data.R;
//import com.justplay1.shoppist.di.scopes.PerSync;
//import com.justplay1.shoppist.entity.NotificationDAO;
//import com.justplay1.shoppist.entity.NotificationType;
//import com.justplay1.shoppist.entity.UnitDAO;
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
//public class UnitsNotificationBuilder extends NotificationBuilder<UnitDAO> {
//
//    @Inject
//    public UnitsNotificationBuilder(Context context, ShoppistPreferences preferences, LocalSetData<NotificationDAO> dataStore) {
//        super(context, preferences, dataStore);
//    }
//
//    @Override
//    protected int getNotificationType() {
//        return NotificationType.SYNC_UNITS_DATA;
//    }
//
//    @Override
//    protected String getTitleForNewObjects(List<UnitDAO> items) {
//        return mContext.getResources().getQuantityString(R.plurals.added_units, items.size(), items.size());
//    }
//
//    @Override
//    protected String getTitleForUpdatedObjects(Map<UnitDAO, UnitDAO> items) {
//        return mContext.getResources().getQuantityString(R.plurals.update_units, items.size(), items.size());
//    }
//
//    @Override
//    protected String getTitleForDeletedObjects(List<UnitDAO> items) {
//        return mContext.getResources().getQuantityString(R.plurals.delete_units, items.size(), items.size());
//    }
//
//    @Override
//    protected String getName(UnitDAO item) {
//        return item.getName() + " (" + item.getShortName() + ")";
//    }
//}
