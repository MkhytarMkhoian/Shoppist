//package com.justplay1.shoppist.repository.sync;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//
//import com.justplay1.shoppist.di.scopes.PerSync;
//import com.justplay1.shoppist.net.ServerConstants;
//import com.justplay1.shoppist.repository.datasource.local.LocalCategoryDataStore;
//import com.justplay1.shoppist.repository.datasource.local.LocalCurrencyDataStore;
//import com.justplay1.shoppist.repository.datasource.local.LocalGoodsDataStore;
//import com.justplay1.shoppist.repository.datasource.local.LocalListDataStore;
//import com.justplay1.shoppist.repository.datasource.local.LocalListItemsDataStore;
//import com.justplay1.shoppist.repository.datasource.local.LocalUnitsDataStore;
//import com.justplay1.shoppist.repository.sync.fromserver.CategorySyncFromServer;
//import com.justplay1.shoppist.repository.sync.fromserver.CurrencySyncFromServer;
//import com.justplay1.shoppist.repository.sync.fromserver.GoodsSyncFromServer;
//import com.justplay1.shoppist.repository.sync.fromserver.ListItemsSyncFromServer;
//import com.justplay1.shoppist.repository.sync.fromserver.ListSyncFromServer;
//import com.justplay1.shoppist.repository.sync.fromserver.UnitsSyncFromServer;
//import com.justplay1.shoppist.repository.sync.toserver.CategorySyncToServer;
//import com.justplay1.shoppist.repository.sync.toserver.CurrencySyncToServer;
//import com.justplay1.shoppist.repository.sync.toserver.GoodsSyncToServer;
//import com.justplay1.shoppist.repository.sync.toserver.ListItemsSyncToServer;
//import com.justplay1.shoppist.repository.sync.toserver.ListSyncToServer;
//import com.justplay1.shoppist.repository.sync.toserver.UnitsSyncToServer;
//import com.parse.ParseInstallation;
//import com.parse.ParsePush;
//import com.parse.ParseUser;
//
//import org.json.JSONObject;
//
//import javax.inject.Inject;
//
///**
// * Created by Mkhytar on 30.05.2016.
// */
//@PerSync
//public class DataSync {
//
//    public static final String DELETE_DATA_BEFORE = "delete_data_before";
//    public static final String NOTIFY_USER = "notify_user";
//
//    private final Context mContext;
//    private boolean mNeedSendNotification = false;
//
//    @Inject
//    CategorySyncFromServer mCategorySyncFromServer;
//    @Inject
//    CurrencySyncFromServer mCurrencySyncFromServer;
//    @Inject
//    UnitsSyncFromServer mUnitsSyncFromServer;
//    @Inject
//    ListSyncFromServer mListSyncFromServer;
//    @Inject
//    ListItemsSyncFromServer mListItemsSyncFromServer;
//    @Inject
//    GoodsSyncFromServer mGoodsSyncFromServer;
//
//    @Inject
//    CurrencySyncToServer mCurrencySyncToServer;
//    @Inject
//    UnitsSyncToServer mUnitsSyncToServer;
//    @Inject
//    ListSyncToServer mListSyncToServer;
//    @Inject
//    ListItemsSyncToServer mListItemsSyncToServer;
//    @Inject
//    GoodsSyncToServer mGoodsSyncToServer;
//    @Inject
//    CategorySyncToServer mCategorySyncToServer;
//
//    @Inject
//    LocalCurrencyDataStore mLocalCurrencyDataStore;
//    @Inject
//    LocalCategoryDataStore mLocalCategoryDataStore;
//    @Inject
//    LocalGoodsDataStore mLocalGoodsDataStore;
//    @Inject
//    LocalListDataStore mLocalListDataStore;
//    @Inject
//    LocalUnitsDataStore mLocalUnitsDataStore;
//    @Inject
//    LocalListItemsDataStore mLocalListItemsDataStore;
//
//    @Inject
//    public DataSync(Context context) {
//        this.mContext = context;
//    }
//
//    public static void requestSync(boolean deleteDataBefore) {
//        requestSync(deleteDataBefore, true);
//    }
//
//    public static void requestSync(boolean deleteDataBefore, boolean notifyUser) {
//        Bundle extras = new Bundle();
//        extras.putBoolean(NOTIFY_USER, notifyUser);
//        extras.putBoolean(DELETE_DATA_BEFORE, deleteDataBefore);
////TODO        ContentResolver.requestSync(App.get().mAccount, ShoppingListContact.AUTHORITY, extras);
//    }
//
//    public void doSync(boolean deleteDataBefore, boolean notifyUser) {
//        if (ParseUser.getCurrentUser() != null) {
//            fromServerToDevice(deleteDataBefore, notifyUser, ParseUser.getCurrentUser());
//            fromDeviceToServer(ParseUser.getCurrentUser());
//        }
//    }
//
//    private void fromServerToDevice(boolean deleteDataBefore, boolean notifyUser, ParseUser user) {
//        try {
//            if (deleteDataBefore) {
//                mCategorySyncFromServer.sync(true, 0, notifyUser);
//                mCurrencySyncFromServer.sync(true, 0, notifyUser);
//                mUnitsSyncFromServer.sync(true, 0, notifyUser);
//                mListItemsSyncFromServer.sync(true, 0, notifyUser);
//                mListSyncFromServer.sync(true, 0, notifyUser);
//                mGoodsSyncFromServer.sync(true, 0, notifyUser);
//                Log.d("fromServerToDevice", "deleteDataBefore");
//                return;
//            }
//
//            final long categoryOldRefTime = mLocalCategoryDataStore.getLastTimestamp();
//            final long currencyOldRefTime = mLocalCurrencyDataStore.getLastTimestamp();
//            final long shoppingListOldRefTime = mLocalListDataStore.getLastTimestamp();
//            final long shoppingListItemsOldRefTime = mLocalListItemsDataStore.getLastTimestamp();
//            final long unitOldRefTime = mLocalUnitsDataStore.getLastTimestamp();
//            final long goodsOldRefTime = mLocalGoodsDataStore.getLastTimestamp();
//
//            final long categoryNewRefTime = user.getLong(ServerConstants.CATEGORY_TIMESTAMP);
//            final long currencyNewRefTime = user.getLong(ServerConstants.CURRENCY_TIMESTAMP);
//            final long shoppingListNewRefTime = user.getLong(ServerConstants.SHOPPING_LIST_TIMESTAMP);
//            final long shoppingListItemsNewRefTime = user.getLong(ServerConstants.SHOPPING_LIST_ITEM_TIMESTAMP);
//            final long unitNewRefTime = user.getLong(ServerConstants.UNIT_TIMESTAMP);
//            final long goodsNewRefTime = user.getLong(ServerConstants.GOODS_TIMESTAMP);
//
//            if (categoryNewRefTime > categoryOldRefTime) {
//                mCategorySyncFromServer.sync(false, categoryOldRefTime, notifyUser);
//            }
//            if (currencyNewRefTime > currencyOldRefTime) {
//                mCurrencySyncFromServer.sync(false, currencyOldRefTime, notifyUser);
//            }
//            if (unitNewRefTime > unitOldRefTime) {
//                mUnitsSyncFromServer.sync(false, unitOldRefTime, notifyUser);
//            }
//            if (shoppingListItemsNewRefTime > shoppingListItemsOldRefTime) {
//                mListItemsSyncFromServer.sync(false, shoppingListItemsOldRefTime, notifyUser);
//            }
//            if (shoppingListNewRefTime > shoppingListOldRefTime) {
//                mListSyncFromServer.sync(false, shoppingListOldRefTime, notifyUser);
//            }
//            if (goodsNewRefTime > goodsOldRefTime) {
//                mGoodsSyncFromServer.sync(false, goodsOldRefTime, notifyUser);
//            }
//            Log.d("fromServerToDevice", "ok");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void fromDeviceToServer(ParseUser currentUser) {
//        try {
//            uploadCategoryToServer();
//            uploadCurrenciesToServer();
//            uploadUnitsToServer();
//            uploadShoppingListToServer();
//            uploadShoppingListItemToServer();
//            uploadProductToServer();
//
//            if (mNeedSendNotification) {
//                ParsePush push = new ParsePush();
//                push.setChannel(ServerConstants.USER_CHANEL_NAME_FIRST_PART + currentUser.getObjectId());
//
//                JSONObject data = new JSONObject();
//                data.put(SyncPushBroadcastReceiver.SYNC_FLAG, "Update data");
//                data.put(SyncPushBroadcastReceiver.NOT_UPDATE_INSTALLATION, ParseInstallation.getCurrentInstallation().getInstallationId());
//
//                push.setData(data);
//                push.send();
//                mNeedSendNotification = false;
//            }
//            Log.d("fromDeviceToServer", "ok");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void setNotificationFlag(boolean result) {
//        if (!mNeedSendNotification) {
//            mNeedSendNotification = result;
//        }
//    }
//
//    private void uploadCurrenciesToServer() throws Exception {
//        boolean result = mCurrencySyncToServer.sync();
//        setNotificationFlag(result);
//    }
//
//    private void uploadUnitsToServer() throws Exception {
//        boolean result = mUnitsSyncToServer.sync();
//        setNotificationFlag(result);
//    }
//
//    private void uploadShoppingListToServer() throws Exception {
//        boolean result = mListSyncToServer.sync();
//        setNotificationFlag(result);
//    }
//
//    private void uploadShoppingListItemToServer() throws Exception {
//        boolean result = mListItemsSyncToServer.sync();
//        setNotificationFlag(result);
//    }
//
//    private void uploadProductToServer() throws Exception {
//        boolean result = mGoodsSyncToServer.sync();
//        setNotificationFlag(result);
//    }
//
//    private void uploadCategoryToServer() throws Exception {
//        boolean result = mCategorySyncToServer.sync();
//        setNotificationFlag(result);
//    }
//}
