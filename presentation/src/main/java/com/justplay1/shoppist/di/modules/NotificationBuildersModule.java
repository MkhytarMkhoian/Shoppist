//package com.justplay1.shoppist.di.modules;
//
//import android.content.Context;
//
//import com.justplay1.shoppist.di.scopes.PerSync;
//import com.justplay1.shoppist.preferences.ShoppistPreferences;
//import com.justplay1.shoppist.repository.datasource.local.LocalNotificationDataStore;
//import com.justplay1.shoppist.repository.sync.alarm.builders.CategoriesNotificationBuilder;
//import com.justplay1.shoppist.repository.sync.alarm.builders.CurrenciesNotificationBuilder;
//import com.justplay1.shoppist.repository.sync.alarm.builders.GoodsNotificationBuilder;
//import com.justplay1.shoppist.repository.sync.alarm.builders.ListItemsNotificationBuilder;
//import com.justplay1.shoppist.repository.sync.alarm.builders.ListsNotificationBuilder;
//import com.justplay1.shoppist.repository.sync.alarm.builders.UnitsNotificationBuilder;
//
//import dagger.Module;
//import dagger.Provides;
//
///**
// * Created by Mkhytar on 29.06.2016.
// */
//@Module
//public class NotificationBuildersModule {
//
//    @Provides
//    @PerSync
//    CategoriesNotificationBuilder provideCategoriesNotificationBuilder(Context context,
//                                                                       ShoppistPreferences preferences,
//                                                                       LocalNotificationDataStore dataStore) {
//        return new CategoriesNotificationBuilder(context, preferences, dataStore);
//    }
//
//    @Provides
//    @PerSync
//    CurrenciesNotificationBuilder provideCurrenciesNotificationBuilder(Context context,
//                                                                       ShoppistPreferences preferences,
//                                                                       LocalNotificationDataStore dataStore) {
//        return new CurrenciesNotificationBuilder(context, preferences, dataStore);
//    }
//
//    @Provides
//    @PerSync
//    UnitsNotificationBuilder provideUnitsNotificationBuilder(Context context,
//                                                             ShoppistPreferences preferences,
//                                                             LocalNotificationDataStore dataStore) {
//        return new UnitsNotificationBuilder(context, preferences, dataStore);
//    }
//
//    @Provides
//    @PerSync
//    ListsNotificationBuilder provideShoppingListsNotificationBuilder(Context context,
//                                                                     ShoppistPreferences preferences,
//                                                                     LocalNotificationDataStore dataStore) {
//        return new ListsNotificationBuilder(context, preferences, dataStore);
//    }
//
//    @Provides
//    @PerSync
//    ListItemsNotificationBuilder provideShoppingListItemsNotificationBuilder(Context context,
//                                                                             ShoppistPreferences preferences,
//                                                                             LocalNotificationDataStore dataStore) {
//        return new ListItemsNotificationBuilder(context, preferences, dataStore);
//    }
//
//    @Provides
//    @PerSync
//    GoodsNotificationBuilder provideGoodsNotificationBuilder(Context context,
//                                                             ShoppistPreferences preferences,
//                                                             LocalNotificationDataStore dataStore) {
//        return new GoodsNotificationBuilder(context, preferences, dataStore);
//    }
//}
