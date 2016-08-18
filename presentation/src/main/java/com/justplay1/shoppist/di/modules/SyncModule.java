//package com.justplay1.shoppist.di.modules;
//
//import com.justplay1.shoppist.di.scopes.PerSync;
//import com.justplay1.shoppist.repository.datasource.local.LocalCategoryDataStore;
//import com.justplay1.shoppist.repository.datasource.local.LocalCurrencyDataStore;
//import com.justplay1.shoppist.repository.datasource.local.LocalGoodsDataStore;
//import com.justplay1.shoppist.repository.datasource.local.LocalListDataStore;
//import com.justplay1.shoppist.repository.datasource.local.LocalListItemsDataStore;
//import com.justplay1.shoppist.repository.datasource.local.LocalUnitsDataStore;
//import com.justplay1.shoppist.repository.datasource.remote.RemoteCategoryDataStore;
//import com.justplay1.shoppist.repository.datasource.remote.RemoteCurrencyDataStore;
//import com.justplay1.shoppist.repository.datasource.remote.RemoteGoodsDataStore;
//import com.justplay1.shoppist.repository.datasource.remote.RemoteListDataStore;
//import com.justplay1.shoppist.repository.datasource.remote.RemoteListItemsDataStore;
//import com.justplay1.shoppist.repository.datasource.remote.RemoteUnitsDataStore;
//import com.justplay1.shoppist.repository.sync.alarm.builders.CategoriesNotificationBuilder;
//import com.justplay1.shoppist.repository.sync.alarm.builders.CurrenciesNotificationBuilder;
//import com.justplay1.shoppist.repository.sync.alarm.builders.GoodsNotificationBuilder;
//import com.justplay1.shoppist.repository.sync.alarm.builders.ListItemsNotificationBuilder;
//import com.justplay1.shoppist.repository.sync.alarm.builders.ListsNotificationBuilder;
//import com.justplay1.shoppist.repository.sync.alarm.builders.UnitsNotificationBuilder;
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
//import dagger.Module;
//import dagger.Provides;
//
///**
// * Created by Mkhytar on 03.06.2016.
// */
//@Module
//public class SyncModule {
//
//    @Provides
//    @PerSync
//    CategorySyncFromServer provideCategorySyncFromServer(LocalCategoryDataStore dataStore, RemoteCategoryDataStore remoteDataStore,
//                                                         CategoriesNotificationBuilder builder) {
//        return new CategorySyncFromServer(dataStore, remoteDataStore, builder);
//    }
//
//    @Provides
//    @PerSync
//    CurrencySyncFromServer provideCurrencySyncFromServer(LocalCurrencyDataStore dataStore, RemoteCurrencyDataStore remoteDataStore,
//                                                         CurrenciesNotificationBuilder builder) {
//        return new CurrencySyncFromServer(dataStore, remoteDataStore, builder);
//    }
//
//    @Provides
//    @PerSync
//    UnitsSyncFromServer provideUnitsSyncFromServer(LocalUnitsDataStore dataStore, RemoteUnitsDataStore remoteDataStore,
//                                                   UnitsNotificationBuilder builder) {
//        return new UnitsSyncFromServer(dataStore, remoteDataStore, builder);
//    }
//
//    @Provides
//    @PerSync
//    ListSyncFromServer provideShoppingListSyncFromServer(LocalListDataStore dataStore,
//                                                         RemoteListDataStore remoteDataStore,
//                                                         ListsNotificationBuilder builder) {
//        return new ListSyncFromServer(dataStore, remoteDataStore, builder);
//    }
//
//    @Provides
//    @PerSync
//    ListItemsSyncFromServer provideShoppingListItemsSyncFromServer(LocalListItemsDataStore dataStore,
//                                                                   RemoteListItemsDataStore remoteDataStore,
//                                                                   ListItemsNotificationBuilder builder) {
//        return new ListItemsSyncFromServer(dataStore, remoteDataStore, builder);
//    }
//
//    @Provides
//    @PerSync
//    GoodsSyncFromServer provideGoodsSyncFromServer(LocalGoodsDataStore dataStore, RemoteGoodsDataStore remoteDataStore,
//                                                   GoodsNotificationBuilder builder) {
//        return new GoodsSyncFromServer(dataStore, remoteDataStore, builder);
//    }
//
//    @Provides
//    @PerSync
//    CurrencySyncToServer provideCurrencySyncToServer(LocalCurrencyDataStore dataStore, RemoteCurrencyDataStore remoteDataStore) {
//        return new CurrencySyncToServer(dataStore, remoteDataStore);
//    }
//
//    @Provides
//    @PerSync
//    UnitsSyncToServer provideUnitsSyncToServer(LocalUnitsDataStore dataStore, RemoteUnitsDataStore remoteDataStore) {
//        return new UnitsSyncToServer(dataStore, remoteDataStore);
//    }
//
//    @Provides
//    @PerSync
//    ListSyncToServer provideShoppingListSyncToServer(LocalListDataStore dataStore,
//                                                     RemoteListDataStore remoteDataStore) {
//        return new ListSyncToServer(dataStore, remoteDataStore);
//    }
//
//    @Provides
//    @PerSync
//    ListItemsSyncToServer provideShoppingListItemsSyncToServer(LocalListItemsDataStore dataStore,
//                                                               RemoteListItemsDataStore remoteDataStore) {
//        return new ListItemsSyncToServer(dataStore, remoteDataStore);
//    }
//
//    @Provides
//    @PerSync
//    GoodsSyncToServer provideGoodsSyncToServer(LocalGoodsDataStore dataStore, RemoteGoodsDataStore remoteDataStore) {
//        return new GoodsSyncToServer(dataStore, remoteDataStore);
//    }
//
//    @Provides
//    @PerSync
//    CategorySyncToServer provideCategorySyncToServer(LocalCategoryDataStore dataStore, RemoteCategoryDataStore remoteDataStore) {
//        return new CategorySyncToServer(dataStore, remoteDataStore);
//    }
//}
