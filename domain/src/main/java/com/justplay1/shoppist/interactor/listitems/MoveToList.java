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
    private boolean isCopy;

    private final DeleteListItems deleteListItems;
    private final AddListItems addListItems;

    @Inject
    public MoveToList(DeleteListItems deleteListItems, AddListItems addListItems,
                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.deleteListItems = deleteListItems;
        this.addListItems = addListItems;
    }

    public MoveToList init(Collection<ListItemModel> data, String newParentListId, boolean isCopy) {
        this.data = data;
        this.newParentListId = newParentListId;
        this.isCopy = isCopy;
        return this;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        if (data == null || newParentListId == null) {
            throw new IllegalArgumentException("init(data) not called, or called with null argument.");
        }
        return Observable.just(getListItems(data))
                .flatMap(new Func1<List<ListItemModel>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(List<ListItemModel> needAdd) {
                        if (needAdd.size() > 0) {
                            return addListItems.init(needAdd).buildUseCaseObservable();
                        }
                        return Observable.just(false);
                    }
                }).flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean o) {
                        if (!isCopy) {
                            return deleteListItems.init(data).buildUseCaseObservable();
                        }
                        return Observable.just(false);
                    }
                });
    }

    private List<ListItemModel> getListItems(Collection<ListItemModel> data) {
        List<ListItemModel> result = new ArrayList<>();
        for (ListItemModel item : data) {
            ListItemModel newItem = new ListItemModel(UUID.nameUUIDFromBytes((String.valueOf(System.nanoTime())).getBytes()).toString(),
                    item.getName(),
                    newParentListId,
                    item.getNote(),
                    item.getStatus(),
                    item.getCategory(),
                    item.getPriority(),
                    item.getPrice(),
                    item.getQuantity(),
                    item.getUnit(),
                    item.getTimeCreated(),
                    item.getCurrency());
            result.add(newItem);
        }
        return result;
    }
}
