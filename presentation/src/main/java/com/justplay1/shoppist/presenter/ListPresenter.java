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

import com.justplay1.shoppist.bus.DataEventBus;
import com.justplay1.shoppist.bus.ListsDataUpdatedEvent;
import com.justplay1.shoppist.di.scope.NonConfigurationScope;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.listitems.GetListItems;
import com.justplay1.shoppist.interactor.lists.DeleteLists;
import com.justplay1.shoppist.interactor.lists.GetLists;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.models.SortType;
import com.justplay1.shoppist.models.mappers.ListItemsModelDataMapper;
import com.justplay1.shoppist.models.mappers.ListModelDataMapper;
import com.justplay1.shoppist.navigation.ListRouter;
import com.justplay1.shoppist.preferences.AppPreferences;
import com.justplay1.shoppist.presenter.base.BaseSortablePresenter;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.ListView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.subjects.BehaviorSubject;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class ListPresenter extends BaseSortablePresenter<ListView, ListViewModel, ListRouter> {

    private final BehaviorSubject<List<Pair<HeaderViewModel, List<ListViewModel>>>> cache = BehaviorSubject.create();

    private final ListModelDataMapper mDataMapper;
    private final ListItemsModelDataMapper mListItemsModelDataMapper;
    private final GetLists mGetLists;
    private final DeleteLists mDeleteLists;
    private final GetListItems mGetListItems;

    private Subscription mDataBusSubscription;

    @Inject
    ListPresenter(AppPreferences preferences,
                  GetLists getLists,
                  DeleteLists deleteLists,
                  GetListItems getListItems,
                  ListModelDataMapper listModelDataMapper,
                  ListItemsModelDataMapper listItemsModelDataMapper) {
        super(preferences);
        this.mGetLists = getLists;
        this.mDeleteLists = deleteLists;
        this.mDataMapper = listModelDataMapper;
        this.mListItemsModelDataMapper = listItemsModelDataMapper;
        this.mGetListItems = getListItems;

        loadData();
    }

    @Override
    public void attachView(ListView view) {
        super.attachView(view);
        if (mPreferences.isNeedShowMessageDialog()) {
            showRateDialog();
        }

        DataEventBus.instanceOf().filteredObservable(ListsDataUpdatedEvent.class);
        mDataBusSubscription = DataEventBus.instanceOf().observable().subscribe(new DefaultSubscriber<Object>() {
            @Override
            public void onNext(Object o) {
                loadData();
            }
        });

        mSubscriptions.add(cache.subscribe(new DefaultSubscriber<List<Pair<HeaderViewModel, List<ListViewModel>>>>() {
            @Override
            public void onNext(List<Pair<HeaderViewModel, List<ListViewModel>>> data) {
                showData(data);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        }));
    }

    @Override
    public void detachView() {
        super.detachView();
        mDataBusSubscription.unsubscribe();
    }

    @SuppressWarnings("ResourceType")
    private void loadData() {
        mGetLists.get()
                .map(mDataMapper::transformToViewModel)
                .map(listViewModels -> sort(listViewModels, mPreferences.getSortForLists()))
                .subscribe(cache);
    }

    private void showRateDialog() {
        if (isViewAttached()) {
            getView().showMessageDialog();
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

    private void showLoadingDialog() {
        if (isViewAttached()) {
            getView().showLoadingDialog();
        }
    }

    private void hideLoadingDialog() {
        if (isViewAttached()) {
            getView().hideLoadingDialog();
        }
    }

    public void sortByName(final List<ListViewModel> data) {
        mPreferences.setSortForShoppingLists(SortType.SORT_BY_NAME);
        showData(sort(data, SortType.SORT_BY_NAME));
    }

    public void sortByPriority(final List<ListViewModel> data) {
        mPreferences.setSortForShoppingLists(SortType.SORT_BY_PRIORITY);
        showData(sort(data, SortType.SORT_BY_PRIORITY));
    }

    public void sortByTimeCreated(final List<ListViewModel> data) {
        mPreferences.setSortForShoppingLists(SortType.SORT_BY_TIME_CREATED);
        showData(sort(data, SortType.SORT_BY_TIME_CREATED));
    }

    public void onAddButtonClick() {
        if (hasRouter()) {
            getRouter().openEditScreen(null);
        }
    }

    public void onEditItemClick(ListViewModel list) {
        if (hasRouter()) {
            getRouter().openEditScreen(list);
        }
    }

    public void onItemClick(ListViewModel list) {
        if (hasRouter()) {
            getRouter().openListDetailScreen(list);
        }
    }

    public void deleteItems(Collection<ListViewModel> data) {
        mSubscriptions.add(Observable.fromCallable(() -> mDataMapper.transform(data))
                .flatMap(list -> {
                    mDeleteLists.setData(list);
                    return mDeleteLists.get();
                }).subscribe(new DefaultSubscriber<>()));
    }

    public void emailShare(List<ListViewModel> data) {
        showLoadingDialog();
        mSubscriptions.add(Observable.from(data)
                .flatMap(shoppingList -> {
                    mGetListItems.setParentId(shoppingList.getId());
                    return mGetListItems.get()
                            .map(mListItemsModelDataMapper::transformToViewModel)
                            .map(items -> shoppingList.getName() + "\n" + "\n" +
                                    ShoppistUtils.buildShareString(items) + "\n");
                })
                .buffer(data.size())
                .map(strings -> {
                    String result = "";
                    for (String s : strings) {
                        result = result + s;
                    }
                    return result;
                }).subscribe(new DefaultSubscriber<String>() {
                    @Override
                    public void onNext(String s) {
                        hideLoadingDialog();
                        if (isViewAttached()) {
                            getView().share(s);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoadingDialog();
                    }
                }));
    }

    private void showData(List<Pair<HeaderViewModel, List<ListViewModel>>> data) {
        if (isViewAttached()) {
            getView().showData(data);
        }
    }
}
