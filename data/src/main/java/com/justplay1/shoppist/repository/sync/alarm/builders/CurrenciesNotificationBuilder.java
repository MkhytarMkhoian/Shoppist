//package com.justplay1.shoppist.repository.sync.alarm.builders;
//
//import android.content.Context;
//
//import com.justplay1.shoppist.data.R;
//import com.justplay1.shoppist.di.scopes.PerSync;
//import com.justplay1.shoppist.entity.CurrencyDAO;
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
//public class CurrenciesNotificationBuilder extends NotificationBuilder<CurrencyDAO> {
//
//    @Inject
//    public CurrenciesNotificationBuilder(Context context, AppPreferences preferences, LocalSetData<NotificationDAO> dataStore) {
//        super(context, preferences, dataStore);
//    }
//
//    @Override
//    protected int getNotificationType() {
//        return NotificationType.SYNC_CURRENCIES_DATA;
//    }
//
//    @Override
//    protected String getTitleForNewObjects(List<CurrencyDAO> items) {
//        return mContext.getResources().getQuantityString(R.plurals.added_currencies, items.size(), items.size());
//    }
//
//    @Override
//    protected String getTitleForUpdatedObjects(Map<CurrencyDAO, CurrencyDAO> items) {
//        return mContext.getResources().getQuantityString(R.plurals.update_currencies, items.size(), items.size());
//    }
//
//    @Override
//    protected String getTitleForDeletedObjects(List<CurrencyDAO> items) {
//        return mContext.getResources().getQuantityString(R.plurals.delete_currencies, items.size(), items.size());
//    }
//
//    @Override
//    protected String getName(CurrencyDAO item) {
//        return item.getName();
//    }
//}
