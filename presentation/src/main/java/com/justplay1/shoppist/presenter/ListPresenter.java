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

import android.support.v4.util.Pair;

import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.lists.DeleteLists;
import com.justplay1.shoppist.interactor.lists.GetLists;
import com.justplay1.shoppist.interactor.lists.UpdateLists;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ListModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.models.SortType;
import com.justplay1.shoppist.models.mappers.ListModelDataMapper;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.presenter.base.BaseSortablePresenter;
import com.justplay1.shoppist.view.ListView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListPresenter extends BaseSortablePresenter<ListView, ListViewModel> {

    private final ListModelDataMapper mDataMapper;
    private final GetLists mGetLists;
    private final DeleteLists mDeleteLists;
    private final UpdateLists mUpdateLists;

    @Inject
    public ListPresenter(ShoppistPreferences preferences,
                         GetLists getLists,
                         DeleteLists deleteLists,
                         UpdateLists updateLists,
                         ListModelDataMapper listModelDataMapper) {
        super(preferences);
        this.mGetLists = getLists;
        this.mDeleteLists = deleteLists;
        this.mUpdateLists = updateLists;
        this.mDataMapper = listModelDataMapper;
    }

    @Override
    public boolean isManualSortEnable() {
        return mPreferences.isManualSortEnableForCategories();
    }

    public void init() {
        if (mPreferences.isNeedShowRateDialog()) {
            showRateDialog();
        }
        loadData();
    }

    @SuppressWarnings("ResourceType")
    public void loadData() {
        showLoading();
        mSubscriptions.add(mGetLists.get()
                .map(mDataMapper::transformToViewModel)
                .map(listViewModels -> sort(listViewModels, mPreferences.getSortForLists()))
                .subscribe(new DefaultSubscriber<List<Pair<HeaderViewModel, List<ListViewModel>>>>() {
                    @Override
                    public void onNext(List<Pair<HeaderViewModel, List<ListViewModel>>> data) {
                        hideLoading();
                        showData(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                        e.printStackTrace();
                    }
                }));
    }

    public void savePosition(final List<ListViewModel> data) {
        mSubscriptions.add(Observable.fromCallable(() -> {
            final int size = data.size();
            for (int i = 0; i < size; i++) {
                data.get(i).setPosition(i);
            }

            return data;
        }).map((Func1<List<ListViewModel>, List<ListModel>>) mDataMapper::transform)
                .flatMap(lists -> {
                    mUpdateLists.setData(lists);
                    return mUpdateLists.get();
                }).subscribe());
    }

    private void showRateDialog() {
        if (isViewAttached()) {
            getView().showRateDialog();
        }
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

    public void sortByName(final List<ListViewModel> data) {
        mPreferences.setManualSortEnableForShoppingLists(false);
        mPreferences.setSortForShoppingLists(SortType.SORT_BY_NAME);
        showData(sort(data, SortType.SORT_BY_NAME));
    }

    public void sortByPriority(final List<ListViewModel> data) {
        mPreferences.setManualSortEnableForShoppingLists(false);
        mPreferences.setSortForShoppingLists(SortType.SORT_BY_PRIORITY);
        showData(sort(data, SortType.SORT_BY_PRIORITY));
    }

    public void sortByTimeCreated(final List<ListViewModel> data) {
        mPreferences.setManualSortEnableForShoppingLists(false);
        mPreferences.setSortForShoppingLists(SortType.SORT_BY_TIME_CREATED);
        showData(sort(data, SortType.SORT_BY_TIME_CREATED));
    }

    public void onSortByManualClick() {
        if (isManualSortEnable()) {
            setManualSortModeEnable(false);
        } else {
            setManualSortModeEnable(true);
        }
    }

    public void onListItemLongClick(ListViewModel item) {

    }

    public void onAddButtonClick() {
        openEditListScreen(null);
    }

    public void onEditItemClick(ListViewModel list) {

    }

    public void deleteItems(Collection<ListViewModel> data) {
        mSubscriptions.add(Observable.fromCallable(() -> mDataMapper.transform(data))
                .flatMap(list -> {
                    mDeleteLists.setData(list);
                    return mDeleteLists.get();
                }).subscribe(new DefaultSubscriber<Boolean>()));
    }

    private void showData(List<Pair<HeaderViewModel, List<ListViewModel>>> data) {
        if (isViewAttached()) {
            getView().showData(data);
        }
    }

    private void showEmailShareDialog(String listName) {
        if (isViewAttached()) {
            getView().showEmailShareDialog(listName);
        }
    }

    private void setManualSortModeEnable(boolean enable) {
        if (isViewAttached()) {
            getView().setManualSortModeEnable(enable);
        }
    }

    private void openEditListScreen(ListViewModel list) {
        if (isViewAttached()) {
            getView().openEditListScreen(list);
        }
    }
}
