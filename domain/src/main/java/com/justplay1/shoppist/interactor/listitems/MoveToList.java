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

    private Collection<ListItemModel> data;
    private String newParentListId;
    private boolean copy;

    private final DeleteListItems deleteListItems;
    private final AddListItems addListItems;

    @Inject
    public MoveToList(DeleteListItems deleteListItems, AddListItems addListItems,
                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.deleteListItems = deleteListItems;
        this.addListItems = addListItems;
    }

    public void setData(Collection<ListItemModel> data) {
        this.data = data;
    }

    public void setNewParentListId(String newParentListId) {
        this.newParentListId = newParentListId;
    }

    public void setCopy(boolean copy) {
        this.copy = copy;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        return Observable.just(getListItems(data))
                .flatMap(new Func1<List<ListItemModel>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(List<ListItemModel> needAdd) {
                        if (needAdd.size() > 0) {
                            addListItems.setData(needAdd);
                            return addListItems.buildUseCaseObservable();
                        }
                        return Observable.just(false);
                    }
                }).flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean o) {
                        if (!copy) {
                            deleteListItems.setData(data);
                            return deleteListItems.buildUseCaseObservable();
                        }
                        return Observable.just(false);
                    }
                });
    }

    private List<ListItemModel> getListItems(Collection<ListItemModel> data) {
        List<ListItemModel> needAdd = new ArrayList<>();
        for (ListItemModel item : data) {
            ListItemModel newItem = new ListItemModel(item);
            newItem.setId(UUID.nameUUIDFromBytes((String.valueOf(System.nanoTime())).getBytes()).toString());
            newItem.setParentListId(newParentListId);
            needAdd.add(newItem);
        }
        return needAdd;
    }
}
