package com.justplay1.shoppist.communication.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.justplay1.shoppist.communication.network.ServerConstants;
import com.justplay1.shoppist.communication.network.ServerRequests;
import com.justplay1.shoppist.communication.sync.fromserver.CategorySyncFromServer;
import com.justplay1.shoppist.communication.sync.fromserver.CurrencySyncFromServer;
import com.justplay1.shoppist.communication.sync.fromserver.GoodsSyncFromServer;
import com.justplay1.shoppist.communication.sync.fromserver.ShoppingListItemsSyncFromServer;
import com.justplay1.shoppist.communication.sync.fromserver.ShoppingListSyncFromServer;
import com.justplay1.shoppist.communication.sync.fromserver.SyncFromServer;
import com.justplay1.shoppist.communication.sync.fromserver.UnitsSyncFromServer;
import com.justplay1.shoppist.communication.sync.toserver.CategorySyncToServer;
import com.justplay1.shoppist.communication.sync.toserver.CurrencySyncToServer;
import com.justplay1.shoppist.communication.sync.toserver.GoodsSyncToServer;
import com.justplay1.shoppist.communication.sync.toserver.ShoppingListItemsSyncToServer;
import com.justplay1.shoppist.communication.sync.toserver.ShoppingListSyncToServer;
import com.justplay1.shoppist.communication.sync.toserver.SyncToServer;
import com.justplay1.shoppist.communication.sync.toserver.UnitsSyncToServer;
import com.justplay1.shoppist.database.ShoppingListContact;
import com.justplay1.shoppist.database.TablesHolder;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.models.Currency;
import com.justplay1.shoppist.models.Product;
import com.justplay1.shoppist.models.ShoppingList;
import com.justplay1.shoppist.models.ShoppingListItem;
import com.justplay1.shoppist.models.SyncDataFlag;
import com.justplay1.shoppist.models.Unit;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.Collection;

/**
 * Created by Mkhytar on 28.09.2015.
 */
public class ShoppistSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String DELETE_DATA_BEFORE = "delete_data_before";
    public static final String WHAT_UPLOAD_ON_SERVER_FLAGS = "what_upload_on_server_flags";
    public static final String NOTIFY_USER = "notify_user";

    private final AccountManager mAccountManager;
    private final Context mContext;
    private boolean mNeedSendNotification = false;

    public ShoppistSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mAccountManager = AccountManager.get(context);
    }

    public ShoppistSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContext = context;
        mAccountManager = AccountManager.get(context);
    }

    private static void requestSync(boolean deleteDataBefore, boolean notifyUser, String whatUploadOnServerFlags) {
//        if (!App.isSyncAvailable) return;

        Bundle extras = new Bundle();
        extras.putBoolean(NOTIFY_USER, notifyUser);
        extras.putBoolean(DELETE_DATA_BEFORE, deleteDataBefore);
        extras.putString(WHAT_UPLOAD_ON_SERVER_FLAGS, whatUploadOnServerFlags);
        ContentResolver.requestSync(App.get().mAccount, ShoppingListContact.AUTHORITY, extras);
    }

    public static void requestSync(boolean deleteDataBefore) {
        requestSync(deleteDataBefore, true, SyncDataFlag.combineFlags(SyncDataFlag.ALL));
    }

    public static void requestSync(boolean deleteDataBefore, boolean notifyUser) {
        requestSync(deleteDataBefore, notifyUser, SyncDataFlag.combineFlags(SyncDataFlag.ALL));
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(ShoppistUtils.MAIN_TAG, "onPerformSync for account[" + account.name + "]");
        try {
            if (extras == null) return;
            if (!extras.containsKey(DELETE_DATA_BEFORE)) return;

            String authToken = mAccountManager.blockingGetAuthToken(account, ShoppistAuthenticator.AUTHTOKEN_TYPE_FULL_ACCESS, true);
            ParseUser user = ParseUser.getCurrentUser();
            if (user == null) {
                user = ParseUser.become(authToken);
            } else {
                user.fetch();
            }

            if (user != null) {
                fromServerToDevice(extras.getBoolean(DELETE_DATA_BEFORE, false), extras.getBoolean(NOTIFY_USER, true), syncResult, user);
                fromDeviceToServer(extras.getString(WHAT_UPLOAD_ON_SERVER_FLAGS, SyncDataFlag.combineFlags(SyncDataFlag.ALL)), syncResult, user);
                syncResult.stats.numEntries++;
            } else {
                syncResult.stats.numParseExceptions++;
            }
        } catch (Exception e) {
            syncResult.stats.numParseExceptions++;
            e.printStackTrace();
        }
    }

    private void fromServerToDevice(boolean deleteDataBefore, boolean notifyUser, SyncResult syncResult, ParseUser user) {
        try {
            SyncFromServer<Category> category = new CategorySyncFromServer();
            SyncFromServer<Currency> currency = new CurrencySyncFromServer();
            SyncFromServer<Unit> units = new UnitsSyncFromServer();
            SyncFromServer<ShoppingList> shoppingList = new ShoppingListSyncFromServer();
            SyncFromServer<ShoppingListItem> shoppingListItem = new ShoppingListItemsSyncFromServer();
            SyncFromServer<Product> goods = new GoodsSyncFromServer();

            if (deleteDataBefore) {
                category.sync(true, 0, notifyUser);
                currency.sync(true, 0, notifyUser);
                units.sync(true, 0, notifyUser);
                shoppingListItem.sync(true, 0, notifyUser);
                shoppingList.sync(true, 0, notifyUser);
                goods.sync(true, 0, notifyUser);
                Log.d("fromServerToDevice", "deleteDataBefore");
                return;
            }
            final TablesHolder tablesHolder = App.get().getTablesHolder();

            final long categoryOldRefTime = tablesHolder.getCategoriesTable().getLastTimestamp();
            final long currencyOldRefTime = tablesHolder.getCurrenciesTable().getLastTimestamp();
            final long shoppingListOldRefTime = tablesHolder.getShoppingListTable().getLastTimestamp();
            final long shoppingListItemsOldRefTime = tablesHolder.getShoppingListItemTable().getLastTimestamp();
            final long unitOldRefTime = tablesHolder.getUnitTable().getLastTimestamp();
            final long goodsOldRefTime = tablesHolder.getProductsTable().getLastTimestamp();

            final long categoryNewRefTime = user.getLong(ServerRequests.CATEGORY_TIMESTAMP);
            final long currencyNewRefTime = user.getLong(ServerRequests.CURRENCY_TIMESTAMP);
            final long shoppingListNewRefTime = user.getLong(ServerRequests.SHOPPING_LIST_TIMESTAMP);
            final long shoppingListItemsNewRefTime = user.getLong(ServerRequests.SHOPPING_LIST_ITEM_TIMESTAMP);
            final long unitNewRefTime = user.getLong(ServerRequests.UNIT_TIMESTAMP);
            final long goodsNewRefTime = user.getLong(ServerRequests.GOODS_TIMESTAMP);

            if (categoryNewRefTime > categoryOldRefTime) {
                category.sync(false, categoryOldRefTime, notifyUser);
            }
            if (currencyNewRefTime > currencyOldRefTime) {
                currency.sync(false, currencyOldRefTime, notifyUser);
            }
            if (unitNewRefTime > unitOldRefTime) {
                units.sync(false, unitOldRefTime, notifyUser);
            }
            if (shoppingListItemsNewRefTime > shoppingListItemsOldRefTime) {
                shoppingListItem.sync(false, shoppingListItemsOldRefTime, notifyUser);
            }
            if (shoppingListNewRefTime > shoppingListOldRefTime) {
                shoppingList.sync(false, shoppingListOldRefTime, notifyUser);
            }
            if (goodsNewRefTime > goodsOldRefTime) {
                goods.sync(false, goodsOldRefTime, notifyUser);
            }

            Log.d("fromServerToDevice", "ok");
        } catch (Exception e) {
            syncResult.stats.numParseExceptions++;
            e.printStackTrace();
        }
    }

    private void fromDeviceToServer(final String dataFlag, SyncResult syncResult, ParseUser currentUser) {
        try {
            for (int flag : SyncDataFlag.parseFlags(dataFlag)) {
                switch (flag) {
                    case SyncDataFlag.ALL:
                        uploadCategoryToServer();
                        uploadCurrenciesToServer();
                        uploadUnitsToServer();
                        uploadShoppingListToServer();
                        uploadShoppingListItemToServer();
                        uploadProductToServer();
                        break;
                    case SyncDataFlag.CATEGORIES:
                        uploadCategoryToServer();
                        break;
                    case SyncDataFlag.CURRENCIES:
                        uploadCurrenciesToServer();
                        break;
                    case SyncDataFlag.SHOPPING_LIST_ITEMS:
                        uploadShoppingListItemToServer();
                        break;
                    case SyncDataFlag.SHOPPING_LISTS:
                        uploadShoppingListToServer();
                        break;
                    case SyncDataFlag.UNITS:
                        uploadUnitsToServer();
                        break;
                    case SyncDataFlag.GOODS:
                        uploadProductToServer();
                        break;
                }
            }

            if (mNeedSendNotification) {
                ParsePush push = new ParsePush();
                push.setChannel(ServerConstants.USER_CHANEL_NAME_FIRST_PART + currentUser.getObjectId());

                JSONObject data = new JSONObject();
                data.put(SyncPushBroadcastReceiver.SYNC_FLAG, "Update data");
                data.put(SyncPushBroadcastReceiver.NOT_UPDATE_INSTALLATION, ParseInstallation.getCurrentInstallation().getInstallationId());

                push.setData(data);
                push.send();
                mNeedSendNotification = false;
            }
            Log.d("fromDeviceToServer", "ok");
        } catch (Exception e) {
            syncResult.stats.numParseExceptions++;
            e.printStackTrace();
        }
    }

    private void uploadCurrenciesToServer() throws Exception {
        Collection<Currency> currencies = App.get().getCurrenciesManager().getDirtyCurrencies();
        SyncToServer<Currency> sync = new CurrencySyncToServer();
        boolean result = sync.sync(currencies);
        if (!mNeedSendNotification) {
            mNeedSendNotification = result;
        }
    }

    private void uploadUnitsToServer() throws Exception {
        Collection<Unit> units = App.get().getUnitManager().getDirtyUnits();
        SyncToServer<Unit> sync = new UnitsSyncToServer();
        boolean result = sync.sync(units);
        if (!mNeedSendNotification) {
            mNeedSendNotification = result;
        }
    }

    private void uploadShoppingListToServer() throws Exception {
        Collection<ShoppingList> shoppingLists = App.get().getShoppingListsManager().getDirtyShoppingLists();
        SyncToServer<ShoppingList> sync = new ShoppingListSyncToServer();
        boolean result = sync.sync(shoppingLists);
        if (!mNeedSendNotification) {
            mNeedSendNotification = result;
        }
    }

    private void uploadShoppingListItemToServer() throws Exception {
        Collection<ShoppingListItem> shoppingListItems = App.get().getShoppingListItemsManager().getDirtyShoppingListItems();
        SyncToServer<ShoppingListItem> sync = new ShoppingListItemsSyncToServer();
        boolean result = sync.sync(shoppingListItems);
        if (!mNeedSendNotification) {
            mNeedSendNotification = result;
        }
    }

    private void uploadProductToServer() throws Exception {
        Collection<Product> products = App.get().getProductsManager().getDirtyProducts();
        SyncToServer<Product> sync = new GoodsSyncToServer();
        boolean result = sync.sync(products);
        if (!mNeedSendNotification) {
            mNeedSendNotification = result;
        }
    }

    private void uploadCategoryToServer() throws Exception {
        Collection<Category> categories = App.get().getCategoriesManager().getDirtyCategories();
        SyncToServer<Category> sync = new CategorySyncToServer();
        boolean result = sync.sync(categories);
        if (!mNeedSendNotification) {
            mNeedSendNotification = result;
        }
    }
}
