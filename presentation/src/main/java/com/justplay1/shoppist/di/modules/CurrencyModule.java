package com.justplay1.shoppist.di.modules;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.currency.AddCurrency;
import com.justplay1.shoppist.interactor.currency.GetCurrencies;
import com.justplay1.shoppist.interactor.currency.SoftDeleteCurrency;
import com.justplay1.shoppist.interactor.currency.UpdateCurrency;
import com.justplay1.shoppist.repository.CurrencyRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar on 14.05.2016.
 */
@Module
public class CurrencyModule {

    @Provides
    @PerActivity
    UpdateCurrency provideUpdateCurrency(CurrencyRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new UpdateCurrency(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    SoftDeleteCurrency provideSoftDeleteCurrency(CurrencyRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new SoftDeleteCurrency(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    GetCurrencies provideGetCurrencies(CurrencyRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new GetCurrencies(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    AddCurrency provideAddCurrency(CurrencyRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new AddCurrency(repository, threadExecutor, postExecutionThread);
    }
}
