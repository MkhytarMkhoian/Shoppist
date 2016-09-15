//package com.justplay1.shoppist.repository.sync.alarm.builders;
//
//import android.content.Context;
//
//import com.justplay1.shoppist.data.R;
//import com.justplay1.shoppist.di.scopes.PerSync;
//import com.justplay1.shoppist.entity.NotificationDAO;
//import com.justplay1.shoppist.entity.NotificationType;
//import com.justplay1.shoppist.entity.ProductDAO;
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
//public class GoodsNotificationBuilder extends NotificationBuilder<ProductDAO> {
//
//    @Inject
//    public GoodsNotificationBuilder(Context context, AppPreferences preferences, LocalSetData<NotificationDAO> dataStore) {
//        super(context, preferences, dataStore);
//    }
//
//    @Override
//    protected int getNotificationType() {
//        return NotificationType.SYNC_GOODS_DATA;
//    }
//
//    @Override
//    protected String getTitleForUpdatedObjects(Map<ProductDAO, ProductDAO> items) {
//        return mContext.getResources().getQuantityString(R.plurals.update_goods, items.size(), items.size());
//    }
//
//    @Override
//    protected String getTitleForDeletedObjects(List<ProductDAO> items) {
//        return mContext.getResources().getQuantityString(R.plurals.delete_goods, items.size(), items.size());
//    }
//
//    @Override
//    protected String getTitleForNewObjects(List<ProductDAO> items) {
//        return mContext.getResources().getQuantityString(R.plurals.added_goods, items.size(), items.size());
//    }
//
//    @Override
//    protected String getName(ProductDAO item) {
//        return item.getName();
//    }
//}
