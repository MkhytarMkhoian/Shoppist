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

package com.justplay1.shoppist.features.lists.items;

import android.os.Bundle;
import android.support.v4.util.Pair;

import com.justplay1.shoppist.eventbus.DataEventBus;
import com.justplay1.shoppist.eventbus.ListItemsDataUpdatedEvent;
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
import com.justplay1.shoppist.models.mappers.CategoryViewModelMapper;
import com.justplay1.shoppist.models.mappers.CurrencyViewModelMapper;
import com.justplay1.shoppist.models.mappers.ListItemsViewModelMapper;
import com.justplay1.shoppist.models.mappers.UnitsViewModelMapper;
import com.justplay1.shoppist.preferences.AppPreferences;

import com.justplay1.shoppist.shared.base.presenters.BaseSortablePresenter;
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
public class ListItemsPresenter extends
    BaseSortablePresenter<ListItemsView, ListItemViewModel, ListItemsRouter> {

    private final BehaviorSubject<List<Pair<HeaderViewModel, List<ListItemViewModel>>>> cache = BehaviorSubject.create();

    private final CategoryViewModelMapper categoryModelDataMapper;
    private final UnitsViewModelMapper unitsViewModelMapper;
    private final CurrencyViewModelMapper currencyViewModelMapper;
    private final ListItemsViewModelMapper listItemsViewModelMapper;

    private final GetCategory getCategory;
    private final GetUnit getUnit;
    private final GetCurrency getCurrency;
    private final GetListItems getListItems;
    private final UpdateListItems updateListItems;
    private final DeleteListItems deleteListItems;
    private final AppPreferences preferences;

    private Subscription dataBusSubscription;
    private ListViewModel parentList;

    @Inject
    ListItemsPresenter(AppPreferences preferences,
                       CategoryViewModelMapper categoryModelDataMapper,
                       UnitsViewModelMapper unitsViewModelMapper,
                       CurrencyViewModelMapper currencyViewModelMapper,
                       ListItemsViewModelMapper listItemsViewModelMapper,
                       GetCategory getCategory,
                       GetUnit getUnit,
                       GetCurrency getCurrency,
                       GetListItems getListItems,
                       UpdateListItems updateListItems,
                       DeleteListItems deleteListItems) {
        this.preferences = preferences;
        this.categoryModelDataMapper = categoryModelDataMapper;
        this.unitsViewModelMapper = unitsViewModelMapper;
        this.currencyViewModelMapper = currencyViewModelMapper;
        this.listItemsViewModelMapper = listItemsViewModelMapper;
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

        addSubscription(cache.subscribe(new DefaultSubscriber<List<Pair<HeaderViewModel, List<ListItemViewModel>>>>() {

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
        return getListItems.init(parentList.getId()).get()
                .map(listItemsViewModelMapper::transformToViewModel)
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
        return getCurrency.init(CurrencyViewModel.NO_CURRENCY_ID).get()
                .map(currencyViewModelMapper::transformToViewModel);
    }

    private Observable<UnitViewModel> loadDefaultUnit() {
        return getUnit.init(UnitViewModel.NO_UNIT_ID).get()
                .map(unitsViewModelMapper::transformToViewModel);
    }

    private Observable<CategoryViewModel> loadDefaultCategory() {
        return getCategory.init(CategoryViewModel.NO_CATEGORY_ID).get()
                .map(categoryModelDataMapper::transformToViewModel);
    }

    public void onChildItemMoved(final ListItemViewModel moveItem) {
        addSubscription(Observable.fromCallable(() -> {
            moveItem.setStatus(!moveItem.getStatus());
            return moveItem;
        }).map(listItemsViewModelMapper::transform)
                .flatMap(listItemViewModel -> updateListItems.init(Collections.singletonList(listItemViewModel)).get())
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
        addSubscription(Observable.fromCallable(() -> listItemsViewModelMapper.transform(data))
                .flatMap(listItemModels -> deleteListItems.init(listItemModels).get())
                .subscribe(new DefaultSubscriber<>()));
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
        if (preferences.getSortForShoppingListItems() != SortType.SORT_BY_NAME) {
            preferences.setSortForShoppingListItems(SortType.SORT_BY_NAME);
            showData(sort(data, SortType.SORT_BY_NAME));
        }
    }

    public void onSortByPriorityClick(List<ListItemViewModel> data) {
        if (preferences.getSortForShoppingListItems() != SortType.SORT_BY_PRIORITY) {
            preferences.setSortForShoppingListItems(SortType.SORT_BY_PRIORITY);
            showData(sort(data, SortType.SORT_BY_PRIORITY));
        }
    }

    public void onSortByCategoryClick(List<ListItemViewModel> data) {
        if (preferences.getSortForShoppingListItems() != SortType.SORT_BY_CATEGORIES) {
            preferences.setSortForShoppingListItems(SortType.SORT_BY_CATEGORIES);
            showData(sort(data, SortType.SORT_BY_CATEGORIES));
        }
    }

    public void onSortByTimeCreatedClick(List<ListItemViewModel> data) {
        if (preferences.getSortForShoppingListItems() != SortType.SORT_BY_TIME_CREATED) {
            preferences.setSortForShoppingListItems(SortType.SORT_BY_TIME_CREATED);
            showData(sort(data, SortType.SORT_BY_TIME_CREATED));
        }
    }

    private void strikeOut(List<ListItemViewModel> items, boolean toShoppingCart) {
        addSubscription(Observable.fromCallable(() -> {
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
                .map(listItemsViewModelMapper::transform)
                .flatMap(models -> updateListItems.init(models).get())
                .subscribe(new DefaultSubscriber<Boolean>()));
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
