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

import com.justplay1.shoppist.entity.mappers.CategoryDAODataMapper;
import com.justplay1.shoppist.entity.mappers.CurrencyDAODataMapper;
import com.justplay1.shoppist.entity.mappers.GoodsDAODataMapper;
import com.justplay1.shoppist.entity.mappers.ListDAODataMapper;
import com.justplay1.shoppist.entity.mappers.ListItemsDAODataMapper;
import com.justplay1.shoppist.entity.mappers.UnitsDAODataMapper;
import com.justplay1.shoppist.repository.CategoryDataRepository;
import com.justplay1.shoppist.repository.CategoryRepository;
import com.justplay1.shoppist.repository.CurrencyDataRepository;
import com.justplay1.shoppist.repository.CurrencyRepository;
import com.justplay1.shoppist.repository.GoodsDataRepository;
import com.justplay1.shoppist.repository.GoodsRepository;
import com.justplay1.shoppist.repository.ListDataRepository;
import com.justplay1.shoppist.repository.ListItemsDataRepository;
import com.justplay1.shoppist.repository.ListItemsRepository;
import com.justplay1.shoppist.repository.ListRepository;
import com.justplay1.shoppist.repository.UnitsDataRepository;
import com.justplay1.shoppist.repository.UnitsRepository;
import com.justplay1.shoppist.repository.datasource.local.LocalCategoryDataStore;
import com.justplay1.shoppist.repository.datasource.local.LocalListItemsDataStore;
import com.justplay1.shoppist.repository.datasource.local.database.LocalCurrencyDataStoreImpl;
import com.justplay1.shoppist.repository.datasource.local.database.LocalGoodsDataStoreImpl;
import com.justplay1.shoppist.repository.datasource.local.database.LocalListDataStoreImpl;
import com.justplay1.shoppist.repository.datasource.local.database.LocalUnitsDataStoreImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar Mkhoian.
 */
@Module
public class RepositoryModule {

    @Provides
    @Singleton
    CategoryRepository provideCategoryRepository(CategoryDAODataMapper dataMapper, LocalCategoryDataStore store) {
        return new CategoryDataRepository(dataMapper, store);
    }

    @Provides
    @Singleton
    CurrencyRepository provideCurrencyRepository(CurrencyDAODataMapper dataMapper, LocalCurrencyDataStoreImpl store) {
        return new CurrencyDataRepository(dataMapper, store);
    }

    @Provides
    @Singleton
    GoodsRepository provideGoodsRepository(LocalGoodsDataStoreImpl dataStore, GoodsDAODataMapper dataMapper) {
        return new GoodsDataRepository(dataStore, dataMapper);
    }

    @Provides
    @Singleton
    UnitsRepository provideUnitsRepository(UnitsDAODataMapper dataMapper, LocalUnitsDataStoreImpl store) {
        return new UnitsDataRepository(dataMapper, store);
    }

    @Provides
    @Singleton
    ListRepository provideShoppingListRepository(ListDAODataMapper dataMapper, LocalListDataStoreImpl store) {
        return new ListDataRepository(dataMapper, store);
    }

    @Provides
    @Singleton
    ListItemsRepository provideShoppingListItemsRepository(ListItemsDAODataMapper dataMapper, LocalListItemsDataStore store) {
        return new ListItemsDataRepository(dataMapper, store);
    }
}
