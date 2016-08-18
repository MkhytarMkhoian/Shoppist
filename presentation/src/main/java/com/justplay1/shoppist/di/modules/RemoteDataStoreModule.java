package com.justplay1.shoppist.di.modules;

import com.justplay1.shoppist.di.scopes.PerSync;
import com.justplay1.shoppist.repository.datasource.local.LocalCategoryDataStore;
import com.justplay1.shoppist.repository.datasource.local.LocalCurrencyDataStore;
import com.justplay1.shoppist.repository.datasource.local.LocalUnitsDataStore;
import com.justplay1.shoppist.repository.datasource.remote.RemoteCategoryDataStore;
import com.justplay1.shoppist.repository.datasource.remote.RemoteCurrencyDataStore;
import com.justplay1.shoppist.repository.datasource.remote.RemoteGoodsDataStore;
import com.justplay1.shoppist.repository.datasource.remote.RemoteListDataStore;
import com.justplay1.shoppist.repository.datasource.remote.RemoteListItemsDataStore;
import com.justplay1.shoppist.repository.datasource.remote.RemoteUnitsDataStore;
import com.justplay1.shoppist.repository.datasource.remote.parse.RemoteCategoryDataStoreImpl;
import com.justplay1.shoppist.repository.datasource.remote.parse.RemoteCurrencyDataStoreImpl;
import com.justplay1.shoppist.repository.datasource.remote.parse.RemoteGoodsDataStoreImpl;
import com.justplay1.shoppist.repository.datasource.remote.parse.RemoteListDataStoreImpl;
import com.justplay1.shoppist.repository.datasource.remote.parse.RemoteListItemsDataStoreImpl;
import com.justplay1.shoppist.repository.datasource.remote.parse.RemoteUnitsDataStoreImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar on 03.06.2016.
 */
@Module
public class RemoteDataStoreModule {

    @Provides
    @PerSync
    RemoteListDataStore provideRemoteShoppingListDataStore() {
        return new RemoteListDataStoreImpl();
    }

    @Provides
    @PerSync
    RemoteListItemsDataStore provideRemoteShoppingListItemsDataStore(LocalCategoryDataStore localCategoryDataStore,
                                                                     LocalUnitsDataStore localUnitsDataStore,
                                                                     LocalCurrencyDataStore localCurrencyDataStore) {
        return new RemoteListItemsDataStoreImpl(localCategoryDataStore,
                localUnitsDataStore, localCurrencyDataStore);
    }

    @Provides
    @PerSync
    RemoteCurrencyDataStore provideRemoteCurrencyDataStore() {
        return new RemoteCurrencyDataStoreImpl();
    }

    @Provides
    @PerSync
    RemoteCategoryDataStore provideRemoteCategoryDataStore() {
        return new RemoteCategoryDataStoreImpl();
    }

    @Provides
    @PerSync
    RemoteGoodsDataStore provideRemoteGoodsDataStore(LocalCategoryDataStore localCategoryDataStore,
                                                     LocalUnitsDataStore unitsDataStore) {
        return new RemoteGoodsDataStoreImpl(localCategoryDataStore, unitsDataStore);
    }

    @Provides
    @PerSync
    RemoteUnitsDataStore provideRemoteUnitsDataStore() {
        return new RemoteUnitsDataStoreImpl();
    }
}
