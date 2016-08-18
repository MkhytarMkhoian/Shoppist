//package com.justplay1.shoppist.repository.sync.alarm.builders;
//
//import android.content.Context;
//
//import com.justplay1.shoppist.data.R;
//import com.justplay1.shoppist.di.scopes.PerSync;
//import com.justplay1.shoppist.entity.ListItemDAO;
//import com.justplay1.shoppist.entity.NotificationType;
//import com.justplay1.shoppist.models.ListModel;
//import com.justplay1.shoppist.preferences.ShoppistPreferences;
//import com.justplay1.shoppist.repository.datasource.local.LocalNotificationDataStore;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.inject.Inject;
//
///**
// * Created by Mkhytar on 13.11.2015.
// */
//@PerSync
//public class ListItemsNotificationBuilder extends BaseShoppingListItemsNotificationBuilder {
//
//    private Map<String, ListModel> mShoppingLists;
//
//    @Inject
//    public ListItemsNotificationBuilder(Context context,
//                                        ShoppistPreferences preferences,
//                                        LocalNotificationDataStore dataStore) {
//        super(context, preferences, dataStore);
//        mShoppingLists = new HashMap<>();
//    }
//
//    public void setShoppingLists(Collection<ListModel> lists) {
//        for (ListModel list : lists) {
//            mShoppingLists.put(list.getId(), list);
//        }
//    }
//
//    @Override
//    protected int getNotificationType() {
//        return NotificationType.SYNC_SHOPPING_LIST_ITEMS_DATA;
//    }
//
//    @Override
//    protected String getName(ListItemDAO item) {
//        return item.getName() + " (" + mShoppingLists.get(item.getParentListId()).getName() + ")";
//    }
//
//    @Override
//    protected String getTitleForStatusDoneObjects(Map<ListItemDAO, ListItemDAO> items) {
//        return mContext.getResources().getQuantityString(R.plurals.bought_goods, items.size(), items.size());
//    }
//
//    @Override
//    protected String getTitleForStatusNotDoneObjects(Map<ListItemDAO, ListItemDAO> items) {
//        return mContext.getResources().getQuantityString(R.plurals.not_bought_goods, items.size(), items.size());
//    }
//
//    @Override
//    protected String getTitleForUpdatedObjects(Map<ListItemDAO, ListItemDAO> items) {
//        return mContext.getResources().getQuantityString(R.plurals.update_shopping_list_items, items.size(), items.size());
//    }
//
//    @Override
//    protected String getTitleForDeletedObjects(java.util.List items) {
//        return mContext.getResources().getQuantityString(R.plurals.delete_shopping_list_items, items.size(), items.size());
//    }
//
//    @Override
//    protected String getTitleForNewObjects(java.util.List items) {
//        return mContext.getResources().getQuantityString(R.plurals.added_shopping_list_items, items.size(), items.size());
//    }
//}
