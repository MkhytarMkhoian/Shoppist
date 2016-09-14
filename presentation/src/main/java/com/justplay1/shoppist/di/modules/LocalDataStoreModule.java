/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.justplay1.shoppist.di.modules;

import android.content.Context;

import com.justplay1.shoppist.repository.datasource.local.LocalCategoryDataStore;
import com.justplay1.shoppist.repository.datasource.local.LocalCurrencyDataStore;
import com.justplay1.shoppist.repository.datasource.local.LocalGoodsDataStore;
import com.justplay1.shoppist.repository.datasource.local.LocalListDataStore;
import com.justplay1.shoppist.repository.datasource.local.LocalListItemsDataStore;
import com.justplay1.shoppist.repository.datasource.local.LocalUnitsDataStore;
import com.justplay1.shoppist.repository.datasource.local.database.LocalCategoryDataStoreImpl;
import com.justplay1.shoppist.repository.datasource.local.database.LocalCurrencyDataStoreImpl;
import com.justplay1.shoppist.repository.datasource.local.database.LocalGoodsDataStoreImpl;
import com.justplay1.shoppist.repository.datasource.local.database.LocalListDataStoreImpl;
import com.justplay1.shoppist.repository.datasource.local.database.LocalListItemsDataStoreImpl;
import com.justplay1.shoppist.repository.datasource.local.database.LocalUnitsDataStoreImpl;
import com.squareup.sqlbrite.BriteDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar Mkhoian.
 */
@Module
public class LocalDataStoreModule {

    @Provides
    @Singleton
    LocalCurrencyDataStore provideLocalCurrencyDataStore(BriteDatabase db, Context context) {
        return new LocalCurrencyDataStoreImpl(db, context);
    }

    @Provides
    @Singleton
    LocalCategoryDataStore provideLocalCategoryDataStore(BriteDatabase db, Context context) {
        return new LocalCategoryDataStoreImpl(db, context);
    }

    @Provides
    @Singleton
    LocalGoodsDataStore provideLocalGoodsDataStore(BriteDatabase db, Context context) {
        return new LocalGoodsDataStoreImpl(db, context);
    }

    @Provides
    @Singleton
    LocalListDataStore provideLocalShoppingListDataStore(BriteDatabase db) {
        return new LocalListDataStoreImpl(db);
    }

    @Provides
    @Singleton
    LocalUnitsDataStore provideLocalUnitsDataStore(BriteDatabase db, Context context) {
        return new LocalUnitsDataStoreImpl(db, context);
    }

    @Provides
    @Singleton
    LocalListItemsDataStore provideLocalShoppingListItemsDataStore(BriteDatabase db) {
        return new LocalListItemsDataStoreImpl(db);
    }
}
