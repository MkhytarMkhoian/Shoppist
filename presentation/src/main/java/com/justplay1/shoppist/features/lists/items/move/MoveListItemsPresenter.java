/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.justplay1.shoppist.features.lists.items.move;

import android.os.Bundle;

import com.justplay1.shoppist.di.scope.NonConfigurationScope;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.listitems.MoveToList;
import com.justplay1.shoppist.interactor.lists.GetLists;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.models.mappers.ListItemsViewModelMapper;
import com.justplay1.shoppist.models.mappers.ListViewModelMapper;
import com.justplay1.shoppist.shared.base.presenters.BaseRouterPresenter;
import com.justplay1.shoppist.shared.navigation.Router;
import com.justplay1.shoppist.utils.Const;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class MoveListItemsPresenter extends BaseRouterPresenter<MoveListItemsView, Router> {

    private final BehaviorSubject<ArrayList<Map<String, Object>>> cache = BehaviorSubject.create();

    private final ListViewModelMapper dataMapper;
    private final ListItemsViewModelMapper listItemsViewModelMapper;
    private final GetLists getLists;
    private final MoveToList moveToList;

    private boolean isCopy;
    private ListViewModel currentList;
    private ArrayList<ListItemViewModel> listItems;

    @Inject
    MoveListItemsPresenter(ListViewModelMapper dataMapper,
                           ListItemsViewModelMapper listItemsViewModelMapper,
                           GetLists getLists,
                           MoveToList moveToList) {
        this.dataMapper = dataMapper;
        this.listItemsViewModelMapper = listItemsViewModelMapper;
        this.getLists = getLists;
        this.moveToList = moveToList;

        loadData();
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        currentList = arguments.getParcelable(ListViewModel.class.getName());
        listItems = arguments.getParcelableArrayList(ListItemViewModel.class.getName());
        isCopy = arguments.getBoolean(Const.IS_COPY);
    }

    @Override
    public void attachView(MoveListItemsView view) {
        super.attachView(view);
        addSubscription(cache.subscribe(new DefaultSubscriber<ArrayList<Map<String, Object>>>() {

            @Override
            public void onError(Throwable e) {
                hideLoading();
            }

            @Override
            public void onNext(ArrayList<Map<String, Object>> data) {
                hideLoading();
                showData(data);
            }
        }));
    }

    private void loadData() {
        getLists.get()
                .map(dataMapper::transformToViewModel)
                .map(lists -> {
                    ArrayList<Map<String, Object>> data = new ArrayList<>(lists.size());
                    Map<String, Object> m;
                    for (ListViewModel item : lists) {
                        if (item.getId().equals(currentList.getId())) continue;
                        m = new HashMap<>();
                        m.put("name", item.getName());
                        m.put("object", item);
                        data.add(m);
                    }
                    return data;
                })
                .subscribe(cache);
    }

    public void onPositiveButtonClick(ListViewModel newList) {
        showLoading();
        Observable.fromCallable(() -> listItemsViewModelMapper.transform(listItems))
                .flatMap(listItemModels -> moveToList.init(listItemModels, newList.getId(), isCopy).get())
                .subscribe(new DefaultSubscriber<Boolean>() {

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                hideLoading();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                hideLoading();
                if (isViewAttached()) {
                    getView().closeDialog();
                }
            }
        });
    }

    private void showLoading() {
        if (isViewAttached()) {
            getView().showLoading();
        }
    }

    private void hideLoading() {
        if (isViewAttached()) {
            getView().hideLoading();
        }
    }

    private void showData(ArrayList<Map<String, Object>> data) {
        if (isViewAttached()) {
            getView().showData(data);
        }
    }
}
