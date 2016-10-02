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
import android.support.v4.util.Pair;

import com.justplay1.shoppist.bus.DataEventBus;
import com.justplay1.shoppist.bus.ListItemsDataUpdatedEvent;
import com.justplay1.shoppist.di.scope.NonConfigurationScope;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.category.GetCategory;
import com.justplay1.shoppist.interactor.currency.GetCurrency;
import com.justplay1.shoppist.interactor.listitems.DeleteListItems;
import com.justplay1.shoppist.interactor.listitems.GetListItems;
import com.justplay1.shoppist.interactor.listitems.UpdateListItems;
import com.justplay1.shoppist.interactor.units.GetUnit;
import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.models.SortType;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.CategoryModelDataMapper;
import com.justplay1.shoppist.models.mappers.CurrencyModelDataMapper;
import com.justplay1.shoppist.models.mappers.ListItemsModelDataMapper;
import com.justplay1.shoppist.models.mappers.UnitsDataModelMapper;
import com.justplay1.shoppist.navigation.ListItemsRouter;
import com.justplay1.shoppist.preferences.AppPreferences;
import com.justplay1.shoppist.presenter.base.BaseSortablePresenter;
import com.justplay1.shoppist.view.ListItemsView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.subjects.BehaviorSubject;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class ListItemsPresenter extends BaseSortablePresenter<ListItemsView, ListItemViewModel, ListItemsRouter> {

    private final BehaviorSubject<List<Pair<HeaderViewModel, List<ListItemViewModel>>>> cache = BehaviorSubject.create();

    private final CategoryModelDataMapper mCategoryModelDataMapper;
    private final UnitsDataModelMapper mUnitsDataModelMapper;
    private final CurrencyModelDataMapper mCurrencyModelDataMapper;
    private final ListItemsModelDataMapper mListItemsModelDataMapper;

    private final GetCategory mGetCategory;
    private final GetUnit mGetUnit;
    private final GetCurrency mGetCurrency;
    private final GetListItems mGetListItems;
    private final UpdateListItems mUpdateListItems;
    private final DeleteListItems mDeleteListItems;

    private Subscription mDataBusSubscription;
    private ListViewModel mParentList;
    private ListItemViewModel mItem;

    @Inject
    public ListItemsPresenter(AppPreferences preferences,
                              CategoryModelDataMapper categoryModelDataMapper,
                              UnitsDataModelMapper unitsDataModelMapper,
                              CurrencyModelDataMapper currencyModelDataMapper,
                              ListItemsModelDataMapper listItemsModelDataMapper,
                              GetCategory getCategory,
                              GetUnit getUnit,
                              GetCurrency getCurrency,
                              GetListItems getListItems,
                              UpdateListItems updateListItems,
                              DeleteListItems deleteListItems) {
        super(preferences);
        this.mCategoryModelDataMapper = categoryModelDataMapper;
        this.mUnitsDataModelMapper = unitsDataModelMapper;
        this.mCurrencyModelDataMapper = currencyModelDataMapper;
        this.mListItemsModelDataMapper = listItemsModelDataMapper;
        this.mGetCategory = getCategory;
        this.mGetUnit = getUnit;
        this.mGetCurrency = getCurrency;
        this.mGetListItems = getListItems;
        this.mUpdateListItems = updateListItems;
        this.mDeleteListItems = deleteListItems;

        loadData();
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
        if (arguments != null) {
            mParentList = arguments.getParcelable(ListViewModel.class.getName());
        }
    }

    @Override
    public void attachView(ListItemsView view) {
        super.attachView(view);
        DataEventBus.instanceOf().filteredObservable(ListItemsDataUpdatedEvent.class);
        mDataBusSubscription = DataEventBus.instanceOf().observable().subscribe(new DefaultSubscriber<Object>() {
            @Override
            public void onNext(Object o) {
                loadData();
            }
        });

        mSubscriptions.add(cache.subscribe(new DefaultSubscriber<List<Pair<HeaderViewModel, List<ListItemViewModel>>>>() {
            @Override
            public void onNext(List<Pair<HeaderViewModel, List<ListItemViewModel>>> data) {
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
    private Observable<List<Pair<HeaderViewModel, List<ListItemViewModel>>>> loadListItems() {
        mGetListItems.setParentId(mParentList.getId());
        return mGetListItems.get()
                .map(mListItemsModelDataMapper::transformToViewModel)
                .map(listItems -> sort(listItems, mPreferences.getSortForShoppingListItems()));
    }

    private void loadData() {
        Observable.zip(loadDefaultUnit(), loadDefaultCategory(), loadDefaultCurrency(),
                (unit, category, currency) -> {
                    Map<String, BaseViewModel> map = new HashMap<>();
                    map.put(CategoryViewModel.NO_CATEGORY_ID, category);
                    map.put(CurrencyViewModel.NO_CURRENCY_ID, currency);
                    map.put(UnitViewModel.NO_UNIT_ID, unit);
                    return map;
                })
                .flatMap(map -> loadListItems()
                        .map(listItems -> {
                            CategoryViewModel category = (CategoryViewModel) map.get(CategoryViewModel.NO_CATEGORY_ID);
                            CurrencyViewModel currency = (CurrencyViewModel) map.get(CurrencyViewModel.NO_CURRENCY_ID);
                            UnitViewModel unit = (UnitViewModel) map.get(UnitViewModel.NO_UNIT_ID);
                            for (Pair<HeaderViewModel, List<ListItemViewModel>> pair : listItems) {
                                for (ListItemViewModel item : pair.second) {
                                    if (item.isCategoryEmpty()) {
                                        item.setCategory(category);
                                    }
                                    if (item.isCurrencyEmpty()) {
                                        item.setCurrency(currency);
                                    }
                                    if (item.isUnitEmpty()) {
                                        item.setUnit(unit);
                                    }
                                }
                            }
                            return listItems;
                        }))
                .subscribe(cache);
    }

    private Observable<CurrencyViewModel> loadDefaultCurrency() {
        mGetCurrency.setId(mPreferences.getDefaultCurrency());
        return mGetCurrency.get()
                .flatMap(currencyModel -> {
                    if (currencyModel == null) {
                        mGetCurrency.setId(CurrencyViewModel.NO_CURRENCY_ID);
                        return mGetCurrency.get();
                    }
                    return Observable.fromCallable(() -> currencyModel);
                })
                .map(mCurrencyModelDataMapper::transformToViewModel);
    }

    private Observable<UnitViewModel> loadDefaultUnit() {
        mGetUnit.setId(UnitViewModel.NO_UNIT_ID);
        return mGetUnit.get()
                .map(mUnitsDataModelMapper::transformToViewModel);
    }

    private Observable<CategoryViewModel> loadDefaultCategory() {
        mGetCategory.setId(CategoryViewModel.NO_CATEGORY_ID);
        return mGetCategory.get()
                .map(mCategoryModelDataMapper::transformToViewModel);
    }

    public void onChildItemMoved(final ListItemViewModel moveItem) {
        mSubscriptions.add(Observable.fromCallable(() -> {
            moveItem.setStatus(!moveItem.getStatus());
            return moveItem;
        }).map(mListItemsModelDataMapper::transform)
                .flatMap(listItemViewModel -> {
                    mUpdateListItems.setData(Collections.singletonList(listItemViewModel));
                    return mUpdateListItems.get();
                })
                .subscribe(new DefaultSubscriber<Boolean>()));
    }

    public void onEditItemClick(ListItemViewModel editItem) {
        openEditScreen(mParentList, editItem);
    }

    public void onChildItemEdit(ListItemViewModel editItem) {
        editItem.setPinned(false);
        openEditScreen(mParentList, editItem);
    }

    public void onListItemClick(ListItemViewModel item) {
        openEditScreen(mParentList, item);
    }

    public void onAddButtonClick() {
        clickAction(false);
    }

    public void onAddButtonLongClick() {
        clickAction(true);
    }

    private void clickAction(boolean isLongClick) {
        switch (mPreferences.getAddButtonClickAction()) {
            case 0:
                if (!isLongClick) {
                    openEditScreen(mParentList, null);
                } else {
                    openQuickMode(mParentList.getId());
                }
                break;
            case 1:
                if (!isLongClick) {
                    openQuickMode(mParentList.getId());
                } else {
                    openEditScreen(mParentList, null);
                }
                break;
        }
    }

    public void returnItemsToList(List<ListItemViewModel> items) {
        strikeOut(items, false);
    }

    public void strikeOutItems(List<ListItemViewModel> items) {
        strikeOut(items, true);
    }

    public void deleteItems(Collection<ListItemViewModel> data) {
        mSubscriptions.add(Observable.fromCallable(() -> mListItemsModelDataMapper.transform(data))
                .flatMap(listItemModels -> {
                    mDeleteListItems.setData(listItemModels);
                    return mDeleteListItems.get();
                }).subscribe(new DefaultSubscriber<Boolean>()));
    }

    public void onCopyItemsClick(List<ListItemViewModel> items) {
        if (isViewAttached()) {
            getView().openCopyGoodsDialog(mParentList, items);
        }
    }

    public void onMoveItemsClick(List<ListItemViewModel> items) {
        if (isViewAttached()) {
            getView().openMoveGoodsDialog(mParentList, items);
        }
    }

    public void onEmailShareClick() {
        showEmailShareDialog(mParentList.getName());
    }

    public void onSortByNameClick(List<ListItemViewModel> data) {
        mPreferences.setSortForShoppingListItems(SortType.SORT_BY_NAME);
        showData(sort(data, SortType.SORT_BY_NAME));
    }

    public void onSortByPriorityClick(List<ListItemViewModel> data) {
        mPreferences.setSortForShoppingListItems(SortType.SORT_BY_PRIORITY);
        showData(sort(data, SortType.SORT_BY_PRIORITY));
    }

    public void onSortByCategoryClick(List<ListItemViewModel> data) {
        mPreferences.setSortForShoppingListItems(SortType.SORT_BY_CATEGORIES);
        showData(sort(data, SortType.SORT_BY_CATEGORIES));
    }

    public void onSortByTimeCreatedClick(List<ListItemViewModel> data) {
        mPreferences.setSortForShoppingListItems(SortType.SORT_BY_TIME_CREATED);
        showData(sort(data, SortType.SORT_BY_TIME_CREATED));
    }

    private void strikeOut(List<ListItemViewModel> items, boolean toShoppingCart) {
        mSubscriptions.add(Observable.fromCallable(() -> {
            List<ListItemViewModel> itemsToMove = new ArrayList<>();
            for (ListItemViewModel item : items) {
                if (toShoppingCart) {
                    if (!item.getStatus()) {
                        item.setStatus(true);
                        itemsToMove.add(item);
                    }
                } else {
                    if (item.getStatus()) {
                        item.setStatus(false);
                        itemsToMove.add(item);
                    }
                }
            }
            return itemsToMove;
        }).filter(itemsToMove -> itemsToMove.size() > 0)
                .map(mListItemsModelDataMapper::transform)
                .flatMap(listItemViewModels -> {
                    mUpdateListItems.setData(listItemViewModels);
                    return mUpdateListItems.get();
                })
                .subscribe(new DefaultSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                }));
    }

    private void showData(List<Pair<HeaderViewModel, List<ListItemViewModel>>> data) {
        if (isViewAttached()) {
            getView().showData(data);
        }
    }

    private void showEmailShareDialog(String listName) {
        if (isViewAttached()) {
            getView().showEmailShareDialog(listName);
        }
    }

    private void openQuickMode(String parentListId) {
        if (hasRouter()) {
            getRouter().openQuickMode(parentListId);
        }
    }

    private void openEditScreen(ListViewModel list, ListItemViewModel item) {
        if (hasRouter()) {
            getRouter().openEditScreen(list, item);
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
}
