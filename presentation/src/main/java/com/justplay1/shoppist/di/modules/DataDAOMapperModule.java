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

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar Mkhoian.
 */
@Module
public class DataDAOMapperModule {

    @Provides
    @Singleton
    CategoryDAODataMapper provideCategoryDataMapper() {
        return new CategoryDAODataMapper();
    }

    @Provides
    @Singleton
    CurrencyDAODataMapper provideCurrencyDataMapper() {
        return new CurrencyDAODataMapper();
    }

    @Provides
    @Singleton
    UnitsDAODataMapper provideUnitsDataMapper() {
        return new UnitsDAODataMapper();
    }

    @Provides
    @Singleton
    GoodsDAODataMapper provideGoodsDataMapper(UnitsDAODataMapper unitsDAODataMapper, CategoryDAODataMapper categoryDAODataMapper) {
        return new GoodsDAODataMapper(unitsDAODataMapper, categoryDAODataMapper);
    }

    @Provides
    @Singleton
    ListItemsDAODataMapper provideShoppingListItemsDataMapper(CategoryDAODataMapper categoryDAODataMapper, CurrencyDAODataMapper currencyDAODataMapper,
                                                              UnitsDAODataMapper unitsDAODataMapper) {
        return new ListItemsDAODataMapper(categoryDAODataMapper, currencyDAODataMapper, unitsDAODataMapper);
    }

    @Provides
    @Singleton
    ListDAODataMapper provideShoppingListDataMapper() {
        return new ListDAODataMapper();
    }
}
