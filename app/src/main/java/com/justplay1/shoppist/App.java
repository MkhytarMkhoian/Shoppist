package com.justplay1.shoppist;

import android.accounts.Account;
import android.app.Application;
import android.graphics.Typeface;

import com.justplay1.shoppist.communication.ModelHolder;
import com.justplay1.shoppist.communication.ThreadExecutor;
import com.justplay1.shoppist.communication.managers.CategoriesManager;
import com.justplay1.shoppist.communication.managers.CurrenciesManager;
import com.justplay1.shoppist.communication.managers.NotificationsManager;
import com.justplay1.shoppist.communication.managers.ProductsManager;
import com.justplay1.shoppist.communication.managers.ShoppingListItemsManager;
import com.justplay1.shoppist.communication.managers.ShoppingListsManager;
import com.justplay1.shoppist.communication.managers.UnitManager;
import com.justplay1.shoppist.communication.sync.SyncLimitFilter;
import com.justplay1.shoppist.database.TablesHolder;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.parse.Parse;

/**
 * Created by Mkhytar on 20.10.2015.
 */
public class App extends Application {

    public static Typeface fontRobotoLight;
    public static Typeface fontRobotoRegular;
    public static Typeface fontRobotoMedium;
    public Account mAccount;

    private ModelHolder mModelHolder;
    private SyncLimitFilter mSyncLimitFilter;

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        ThreadExecutor.initialize();
        mSyncLimitFilter = new SyncLimitFilter();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);

        fontRobotoLight = Typeface.createFromAsset(getAssets(), "roboto_light.ttf");
        fontRobotoMedium = Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf");
        fontRobotoRegular = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        ShoppistPreferences.initPreferences(getApplicationContext());

        mModelHolder = new ModelHolder(getApplicationContext());
        mModelHolder.start();

        mSyncLimitFilter.requestSync(false);
    }

    public App() {
        instance = this;
    }

    public static App get() {
        return instance;
    }

    public void release() {
        mModelHolder.stop();
        mModelHolder = null;
    }

    public SyncLimitFilter getSyncLimitFilter() {
        return mSyncLimitFilter;
    }

    public NotificationsManager getNotificationsManager() {
        return mModelHolder.getNotificationsManager();
    }

    public ModelHolder getModelHolder() {
        return mModelHolder;
    }

    public TablesHolder getTablesHolder() {
        return mModelHolder.getTablesHolder();
    }

    public CategoriesManager getCategoriesManager() {
        return mModelHolder.getCategoriesManager();
    }

    public ShoppingListItemsManager getShoppingListItemsManager() {
        return mModelHolder.getShoppingListItemsManager();
    }

    public ShoppingListsManager getShoppingListsManager() {
        return mModelHolder.getShoppingListsManager();
    }

    public UnitManager getUnitManager() {
        return mModelHolder.getUnitManager();
    }

    public CurrenciesManager getCurrenciesManager() {
        return mModelHolder.getCurrenciesManager();
    }

    public ProductsManager getProductsManager() {
        return mModelHolder.getProductsManager();
    }
}
