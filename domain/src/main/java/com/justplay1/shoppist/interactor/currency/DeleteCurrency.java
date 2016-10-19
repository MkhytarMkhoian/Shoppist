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

package com.justplay1.shoppist.interactor.currency;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.CurrencyModel;
import com.justplay1.shoppist.repository.CurrencyRepository;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar Mkhoian.
 */
public class DeleteCurrency extends UseCase<Boolean> {

    private final CurrencyRepository repository;
    private Collection<CurrencyModel> data;

    @Inject
    public DeleteCurrency(CurrencyRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    public DeleteCurrency init(Collection<CurrencyModel> data) {
        this.data = data;
        return this;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        if (this.data == null) {
            throw new IllegalArgumentException("init(data) not called, or called with null argument.");
        }
        return Observable.fromCallable(() -> {
            repository.delete(data);
            return true;
        });
    }
}
