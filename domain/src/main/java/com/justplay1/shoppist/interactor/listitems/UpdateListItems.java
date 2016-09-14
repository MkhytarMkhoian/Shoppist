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

package com.justplay1.shoppist.interactor.listitems;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.ListItemModel;
import com.justplay1.shoppist.repository.ListItemsRepository;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar Mkhoian.
 */
public class UpdateListItems extends UseCase<Boolean> {

    private final ListItemsRepository mRepository;
    private Collection<ListItemModel> mData;

    @Inject
    public UpdateListItems(ListItemsRepository repository,
                           ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setData(Collection<ListItemModel> items) {
        this.mData = items;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        return Observable.fromCallable(() -> {
            mRepository.update(mData);
            return true;
        });
    }
}
