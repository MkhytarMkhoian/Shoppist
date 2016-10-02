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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Mkhytar Mkhoian.
 */
public class MoveToList extends UseCase<Boolean> {

    private final DeleteListItems mDeleteListItems;
    private final AddListItems mAddListItems;

    private Collection<ListItemModel> mData;
    private String mNewParentListId;
    private boolean mCopy;

    @Inject
    public MoveToList(DeleteListItems deleteListItems, AddListItems addListItems,
                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mDeleteListItems = deleteListItems;
        mAddListItems = addListItems;
    }

    public void setData(Collection<ListItemModel> data) {
        this.mData = data;
    }

    public void setNewParentListId(String newParentListId) {
        this.mNewParentListId = newParentListId;
    }

    public void setCopy(boolean copy) {
        this.mCopy = copy;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        return Observable.just(getListItemModels(mData))
                .flatMap(new Func1<List<ListItemModel>, Observable<?>>() {
                    @Override
                    public Observable<Boolean> call(List<ListItemModel> needAdd) {
                        if (needAdd.size() > 0) {
                            mAddListItems.setData(needAdd);
                            return mAddListItems.buildUseCaseObservable();
                        }
                        return Observable.just(false);
                    }
                }).flatMap(new Func1<Object, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Object o) {
                        if (!mCopy) {
                            mDeleteListItems.setData(mData);
                            return mDeleteListItems.buildUseCaseObservable();
                        }
                        return Observable.just(false);
                    }
                });
    }

    private List<ListItemModel> getListItemModels(Collection<ListItemModel> data) {
        List<ListItemModel> needAdd = new ArrayList<>();
        for (ListItemModel item : data) {
            ListItemModel newItem = new ListItemModel(item);
            newItem.setId(UUID.nameUUIDFromBytes((System.nanoTime() + "").getBytes()).toString());
            newItem.setParentListId(mNewParentListId);
            needAdd.add(newItem);
        }
        return needAdd;
    }
}
