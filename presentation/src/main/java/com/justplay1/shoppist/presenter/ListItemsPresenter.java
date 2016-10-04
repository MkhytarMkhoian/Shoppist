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

    private final CategoryModelDataMapper categoryModelDataMapper;
    private final UnitsDataModelMapper unitsDataModelMapper;
    private final CurrencyModelDataMapper currencyModelDataMapper;
    private final ListItemsModelDataMapper listItemsModelDataMapper;

    private final GetCategory getCategory;
    private final GetUnit getUnit;
    private final GetCurrency getCurrency;
    private final GetListItems getListItems;
    private final UpdateListItems updateListItems;
    private final DeleteListItems deleteListItems;

    private Subscription dataBusSubscription;
    private ListViewModel parentList;

    @Inject
    ListItemsPresenter(AppPreferences preferences,
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
        this.categoryModelDataMapper = categoryModelDataMapper;
        this.unitsDataModelMapper = unitsDataModelMapper;
        this.currencyModelDataMapper = currencyModelDataMapper;
        this.listItemsModelDataMapper = listItemsModelDataMapper;
        this.getCategory = getCategory;
        this.getUnit = getUnit;
        this.getCurrency = getCurrency;
        this.getListItems = getListItems;
        this.updateListItems = updateListItems;
        this.deleteListItems = deleteListItems;

        loadData();
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
        if (arguments != null) {
            parentList = arguments.getParcelable(ListViewModel.class.getName());
        }
    }

    @Override
    public void attachView(ListItemsView view) {
        super.attachView(view);
        DataEventBus.instanceOf().filteredObservable(ListItemsDataUpdatedEvent.class);
        dataBusSubscription = DataEventBus.instanceOf().observable().subscribe(new DefaultSubscriber<Object>() {
            @Override
            public void onNext(Object o) {
                loadData();
            }
        });

        subscriptions.add(cache.subscribe(new DefaultSubscriber<List<Pair<HeaderViewModel, List<ListItemViewModel>>>>() {

            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onNext(List<Pair<HeaderViewModel, List<ListItemViewModel>>> data) {
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

    @Override
    public void detachView() {
        super.detachView();
        dataBusSubscription.unsubscribe();
    }

    @SuppressWarnings("ResourceType")
    private Observable<List<Pair<HeaderViewModel, List<ListItemViewModel>>>> loadListItems() {
        getListItems.setParentId(parentList.getId());
        return getListItems.get()
                .map(listItemsModelDataMapper::transformToViewModel)
                .map(listItems -> sort(listItems, preferences.getSortForShoppingListItems()));
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
        getCurrency.setId(CurrencyViewModel.NO_CURRENCY_ID);
        return getCurrency.get()
                .flatMap(currencyModel -> {
                    if (currencyModel == null) {
                        getCurrency.setId(CurrencyViewModel.NO_CURRENCY_ID);
                        return getCurrency.get();
                    }
                    return Observable.fromCallable(() -> currencyModel);
                })
                .map(currencyModelDataMapper::transformToViewModel);
    }

    private Observable<UnitViewModel> loadDefaultUnit() {
        getUnit.setId(UnitViewModel.NO_UNIT_ID);
        return getUnit.get()
                .map(unitsDataModelMapper::transformToViewModel);
    }

    private Observable<CategoryViewModel> loadDefaultCategory() {
        getCategory.setId(CategoryViewModel.NO_CATEGORY_ID);
        return getCategory.get()
                .map(categoryModelDataMapper::transformToViewModel);
    }

    public void onChildItemMoved(final ListItemViewModel moveItem) {
        subscriptions.add(Observable.fromCallable(() -> {
            moveItem.setStatus(!moveItem.getStatus());
            return moveItem;
        }).map(listItemsModelDataMapper::transform)
                .flatMap(listItemViewModel -> {
                    updateListItems.setData(Collections.singletonList(listItemViewModel));
                    return updateListItems.get();
                })
                .subscribe(new DefaultSubscriber<>()));
    }

    public void onEditItemClick(ListItemViewModel editItem) {
        openEditScreen(parentList, editItem);
    }

    public void onChildItemEdit(ListItemViewModel editItem) {
        editItem.setPinned(false);
        openEditScreen(parentList, editItem);
    }

    public void onListItemClick(ListItemViewModel item) {
        openEditScreen(parentList, item);
    }

    public void onAddButtonClick() {
        clickAction(false);
    }

    public void onAddButtonLongClick() {
        clickAction(true);
    }

    private void clickAction(boolean isLongClick) {
        switch (preferences.getAddButtonClickAction()) {
            case 0:
                if (!isLongClick) {
                    openEditScreen(parentList, null);
                } else {
                    openQuickMode(parentList.getId());
                }
                break;
            case 1:
                if (!isLongClick) {
                    openQuickMode(parentList.getId());
                } else {
                    openEditScreen(parentList, null);
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
        subscriptions.add(Observable.fromCallable(() -> listItemsModelDataMapper.transform(data))
                .flatMap(listItemModels -> {
                    deleteListItems.setData(listItemModels);
                    return deleteListItems.get();
                }).subscribe(new DefaultSubscriber<>()));
    }

    public void onCopyItemsClick(List<ListItemViewModel> items) {
        if (isViewAttached()) {
            getView().openCopyGoodsDialog(parentList, items);
        }
    }

    public void onMoveItemsClick(List<ListItemViewModel> items) {
        if (isViewAttached()) {
            getView().openMoveGoodsDialog(parentList, items);
        }
    }

    public void onEmailShareClick() {
        showEmailShareDialog(parentList.getName());
    }

    public void onSortByNameClick(List<ListItemViewModel> data) {
        preferences.setSortForShoppingListItems(SortType.SORT_BY_NAME);
        showData(sort(data, SortType.SORT_BY_NAME));
    }

    public void onSortByPriorityClick(List<ListItemViewModel> data) {
        preferences.setSortForShoppingListItems(SortType.SORT_BY_PRIORITY);
        showData(sort(data, SortType.SORT_BY_PRIORITY));
    }

    public void onSortByCategoryClick(List<ListItemViewModel> data) {
        preferences.setSortForShoppingListItems(SortType.SORT_BY_CATEGORIES);
        showData(sort(data, SortType.SORT_BY_CATEGORIES));
    }

    public void onSortByTimeCreatedClick(List<ListItemViewModel> data) {
        preferences.setSortForShoppingListItems(SortType.SORT_BY_TIME_CREATED);
        showData(sort(data, SortType.SORT_BY_TIME_CREATED));
    }

    private void strikeOut(List<ListItemViewModel> items, boolean toShoppingCart) {
        subscriptions.add(Observable.fromCallable(() -> {
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
                .map(listItemsModelDataMapper::transform)
                .flatMap(listItemViewModels -> {
                    updateListItems.setData(listItemViewModels);
                    return updateListItems.get();
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
