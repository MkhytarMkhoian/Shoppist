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
 * Created by Mkhytar on 29.06.2016.
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
