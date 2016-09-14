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

package com.justplay1.shoppist;

import android.app.Application;

import com.justplay1.shoppist.di.components.AppComponent;
import com.justplay1.shoppist.di.components.DaggerAppComponent;
import com.justplay1.shoppist.di.modules.AppModule;
import com.justplay1.shoppist.di.modules.DataDAOMapperModule;
import com.justplay1.shoppist.di.modules.DatabaseModule;
import com.justplay1.shoppist.di.modules.LocalDataStoreModule;
import com.justplay1.shoppist.di.modules.PreferenceModule;
import com.justplay1.shoppist.di.modules.RepositoryModule;
import com.justplay1.shoppist.di.modules.ThreadExecutorModule;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Mkhytar Mkhoian.
 */
public class App extends Application {

    private AppComponent mAppComponent;

    private static App instance;

    public App() {
        instance = this;
        initializeInjector();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    private void initializeInjector() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .repositoryModule(new RepositoryModule())
                .threadExecutorModule(new ThreadExecutorModule())
                .databaseModule(new DatabaseModule())
                .dataDAOMapperModule(new DataDAOMapperModule())
                .localDataStoreModule(new LocalDataStoreModule())
                .preferenceModule(new PreferenceModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public static App get() {
        return instance;
    }
}
