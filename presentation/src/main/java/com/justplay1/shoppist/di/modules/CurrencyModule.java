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

import com.justplay1.shoppist.di.scope.NonConfigurationScope;
import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.currency.AddCurrency;
import com.justplay1.shoppist.interactor.currency.GetCurrencyList;
import com.justplay1.shoppist.interactor.currency.GetCurrency;
import com.justplay1.shoppist.interactor.currency.DeleteCurrency;
import com.justplay1.shoppist.interactor.currency.UpdateCurrency;
import com.justplay1.shoppist.repository.CurrencyRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar Mkhoian.
 */
@Module
public class CurrencyModule {

    @Provides
    @NonConfigurationScope
    UpdateCurrency provideUpdateCurrency(CurrencyRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new UpdateCurrency(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @NonConfigurationScope
    DeleteCurrency provideSoftDeleteCurrency(CurrencyRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new DeleteCurrency(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @NonConfigurationScope
    GetCurrencyList provideGetCurrencies(CurrencyRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new GetCurrencyList(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @NonConfigurationScope
    GetCurrency provideGetCurrency(CurrencyRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new GetCurrency(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @NonConfigurationScope
    AddCurrency provideAddCurrency(CurrencyRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new AddCurrency(repository, threadExecutor, postExecutionThread);
    }
}
