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

package com.justplay1.shoppist.presenter;

import android.os.Bundle;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.listitems.MoveToList;
import com.justplay1.shoppist.interactor.lists.GetLists;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.models.mappers.ListItemsModelDataMapper;
import com.justplay1.shoppist.models.mappers.ListModelDataMapper;
import com.justplay1.shoppist.navigation.Router;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.MoveListItemsView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar Mkhoian.
 */
@PerActivity
public class MoveListItemsPresenter extends BaseRxPresenter<MoveListItemsView, Router> {

    private final ListModelDataMapper mDataMapper;
    private final ListItemsModelDataMapper mListItemsModelDataMapper;
    private final GetLists mGetLists;
    private final MoveToList mMoveToList;

    private boolean isCopy;
    private ListViewModel mCurrentList;
    private ArrayList<ListItemViewModel> mListItems;

    @Inject
    public MoveListItemsPresenter(ListModelDataMapper dataMapper,
                                  ListItemsModelDataMapper listItemsModelDataMapper,
                                  GetLists getLists,
                                  MoveToList moveToList) {
        this.mDataMapper = dataMapper;
        this.mListItemsModelDataMapper = listItemsModelDataMapper;
        this.mGetLists = getLists;
        this.mMoveToList = moveToList;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        mCurrentList = arguments.getParcelable(ListViewModel.class.getName());
        mListItems = arguments.getParcelableArrayList(ListItemViewModel.class.getName());
        isCopy = arguments.getBoolean("isCopy");
    }

    public void init() {
        loadData();
    }

    public void loadData() {
        showLoading();
        mSubscriptions.add(mGetLists.get()
                .map(mDataMapper::transformToViewModel)
                .map(lists -> {
                    ArrayList<Map<String, Object>> data = new ArrayList<>(lists.size());
                    Map<String, Object> m;
                    for (ListViewModel item : lists) {
                        if (item.getId().equals(mCurrentList.getId())) continue;
                        m = new HashMap<>();
                        m.put("name", item.getName());
                        m.put("object", item);
                        data.add(m);
                    }
                    return data;
                })
                .subscribe(new DefaultSubscriber<ArrayList<Map<String, Object>>>() {

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

    public void onPositiveButtonClick(ListViewModel newList) {
        showLoading();
        mMoveToList.setCopy(isCopy);
        mMoveToList.setNewParentListId(newList.getId());
        Observable.fromCallable(() -> mListItemsModelDataMapper.transform(mListItems))
                .flatMap(listItemModels -> {
                    mMoveToList.setData(listItemModels);
                    return mMoveToList.get();
                }).subscribe(new DefaultSubscriber<Boolean>() {

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
