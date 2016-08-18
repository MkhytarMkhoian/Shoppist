//package com.justplay1.shoppist.di.components;
//
//import com.justplay1.shoppist.di.modules.NotificationBuildersModule;
//import com.justplay1.shoppist.di.modules.RemoteDataStoreModule;
//import com.justplay1.shoppist.di.modules.SyncModule;
//import com.justplay1.shoppist.di.scopes.PerSync;
//import com.justplay1.shoppist.repository.datasource.local.LocalCategoryDataStore;
//import com.justplay1.shoppist.repository.datasource.local.LocalCurrencyDataStore;
//import com.justplay1.shoppist.repository.datasource.local.LocalGoodsDataStore;
//import com.justplay1.shoppist.repository.datasource.local.LocalListDataStore;
//import com.justplay1.shoppist.repository.datasource.local.LocalListItemsDataStore;
//import com.justplay1.shoppist.repository.datasource.local.LocalNotificationDataStore;
//import com.justplay1.shoppist.repository.datasource.local.LocalUnitsDataStore;
//import com.justplay1.shoppist.repository.sync.DataSync;
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
//
//import dagger.Component;
//
///**
// * Created by Mkhytar on 02.06.2016.
// */
//@PerSync
//@Component(dependencies = DataComponent.class, modules = {RemoteDataStoreModule.class,
//        SyncModule.class, NotificationBuildersModule.class})
//public interface SyncComponent {
//
//    void inject(DataSync dataSync);
//
//    CategorySyncFromServer categorySyncFromServer();
//
//    CurrencySyncFromServer currencySyncFromServer();
//
//    UnitsSyncFromServer unitsSyncFromServer();
//
//    ListSyncFromServer shoppingListSyncFromServer();
//
//    ListItemsSyncFromServer shoppingListItemsSyncFromServer();
//
//    GoodsSyncFromServer goodsSyncFromServer();
//
//
//    CurrencySyncToServer currencySyncToServer();
//
//    UnitsSyncToServer unitsSyncToServer();
//
//    ListSyncToServer shoppingListSyncToServer();
//
//    ListItemsSyncToServer shoppingListItemsSyncToServer();
//
//    GoodsSyncToServer goodsSyncToServer();
//
//    CategorySyncToServer categorySyncToServer();
//
//
//    LocalCurrencyDataStore localCurrencyDataStore();
//
//    LocalCategoryDataStore localCategoryDataStore();
//
//    LocalGoodsDataStore localGoodsDataStore();
//
//    LocalNotificationDataStore localNotificationDataStore();
//
//    LocalListDataStore localShoppingListDataStore();
//
//    LocalUnitsDataStore localUnitsDataStore();
//
//    LocalListItemsDataStore localShoppingListItemsDataStore();
//}
