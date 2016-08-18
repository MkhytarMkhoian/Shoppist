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
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.repository.AccountRepository;
import com.justplay1.shoppist.repository.CategoryRepository;
import com.justplay1.shoppist.repository.CurrencyRepository;
import com.justplay1.shoppist.repository.GoodsRepository;
import com.justplay1.shoppist.repository.ListItemsRepository;
import com.justplay1.shoppist.repository.ListRepository;
import com.justplay1.shoppist.repository.NotificationRepository;
import com.justplay1.shoppist.repository.UnitsRepository;
import com.justplay1.shoppist.view.activities.BaseActivity;
import com.justplay1.shoppist.view.fragments.BaseFragment;

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

    Context context();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    ShoppistPreferences preferences();


    CategoryRepository categoryRepository();

    CurrencyRepository currencyRepository();

    GoodsRepository goodsRepository();

    NotificationRepository notificationRepository();

    UnitsRepository unitsRepository();

    ListRepository listRepository();

    AccountRepository accountRepository();

    ListItemsRepository listItemsRepository();
}
