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

package com.justplay1.shoppist.di.components;

import android.content.Context;

import com.justplay1.shoppist.di.modules.AppModule;
import com.justplay1.shoppist.di.modules.DataDAOMapperModule;
import com.justplay1.shoppist.di.modules.DatabaseModule;
import com.justplay1.shoppist.di.modules.LocalDataStoreModule;
import com.justplay1.shoppist.di.modules.PreferenceModule;
import com.justplay1.shoppist.di.modules.RepositoryModule;
import com.justplay1.shoppist.di.modules.ThreadExecutorModule;
import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.preferences.AppPreferences;
import com.justplay1.shoppist.repository.CategoryRepository;
import com.justplay1.shoppist.repository.CurrencyRepository;
import com.justplay1.shoppist.repository.GoodsRepository;
import com.justplay1.shoppist.repository.ListItemsRepository;
import com.justplay1.shoppist.repository.ListRepository;
import com.justplay1.shoppist.repository.UnitsRepository;
import com.justplay1.shoppist.view.activities.BaseActivity;
import com.justplay1.shoppist.view.fragments.BaseFragment;
import com.justplay1.shoppist.view.fragments.dialog.BaseDialogFragment;
import com.justplay1.shoppist.view.fragments.settings.BaseSettingFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton
@Component(modules = {AppModule.class,
        ThreadExecutorModule.class,
        RepositoryModule.class,
        DatabaseModule.class,
        DataDAOMapperModule.class,
        PreferenceModule.class,
        LocalDataStoreModule.class})
public interface AppComponent {

    void inject(BaseActivity baseActivity);

    void inject(BaseFragment baseFragment);

    void inject(BaseDialogFragment baseFragment);

    void inject(BaseSettingFragment baseFragment);

    Context context();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    AppPreferences preferences();


    CategoryRepository categoryRepository();

    CurrencyRepository currencyRepository();

    GoodsRepository goodsRepository();

    UnitsRepository unitsRepository();

    ListRepository listRepository();

    ListItemsRepository listItemsRepository();
}
