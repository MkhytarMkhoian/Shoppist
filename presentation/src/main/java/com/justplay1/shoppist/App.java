package com.justplay1.shoppist;

import android.app.Application;
import android.graphics.Typeface;

import com.justplay1.shoppist.di.components.AppComponent;
import com.justplay1.shoppist.di.components.DaggerAppComponent;
import com.justplay1.shoppist.di.modules.AppModule;
import com.justplay1.shoppist.di.modules.DataDAOMapperModule;
import com.justplay1.shoppist.di.modules.DatabaseModule;
import com.justplay1.shoppist.di.modules.LocalDataStoreModule;
import com.justplay1.shoppist.di.modules.PreferenceModule;
import com.justplay1.shoppist.di.modules.RepositoryModule;
import com.justplay1.shoppist.di.modules.ThreadExecutorModule;
import com.parse.Parse;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Mkhytar on 20.10.2015.
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
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

//        mSyncLimitFilter.requestSync(false);
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
