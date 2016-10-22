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

package com.justplay1.shoppist.presenter;

import android.os.Bundle;

import com.justplay1.shoppist.interactor.category.GetCategory;
import com.justplay1.shoppist.interactor.currency.GetCurrency;
import com.justplay1.shoppist.interactor.listitems.DeleteListItems;
import com.justplay1.shoppist.interactor.listitems.GetListItems;
import com.justplay1.shoppist.interactor.listitems.UpdateListItems;
import com.justplay1.shoppist.interactor.units.GetUnit;
import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.CurrencyModel;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.ListItemModel;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.models.SortType;
import com.justplay1.shoppist.models.UnitModel;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.CategoryViewModelMapper;
import com.justplay1.shoppist.models.mappers.CurrencyViewModelMapper;
import com.justplay1.shoppist.models.mappers.ListItemsViewModelMapper;
import com.justplay1.shoppist.models.mappers.UnitsViewModelMapper;
import com.justplay1.shoppist.navigation.ListItemsRouter;
import com.justplay1.shoppist.preferences.AppPreferences;
import com.justplay1.shoppist.view.ListItemsView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.List;

import rx.Observable;

import static com.justplay1.shoppist.ViewModelUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeCategoryViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeCurrencyModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeCurrencyViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeListItemModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeListItemViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeListViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitViewModel;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GetCurrency.class,
        GetCategory.class,
        GetUnit.class,
        GetListItems.class,
        UpdateListItems.class,
        DeleteListItems.class,
        Bundle.class})
public class ListItemsPresenterTest {

    private UpdateListItems updateListItems;
    private DeleteListItems deleteListItems;

    @Mock
    private AppPreferences mockPreferences;
    @Mock
    private ListItemsRouter mockRouter;
    @Mock
    private ListItemsView mockView;

    private ListItemsPresenter presenter;
    private ListItemViewModel fakeListItemViewModel;
    private ListViewModel fakeListViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        GetCurrency getCurrency = PowerMockito.mock(GetCurrency.class);
        GetCategory getCategory = PowerMockito.mock(GetCategory.class);
        GetUnit getUnit = PowerMockito.mock(GetUnit.class);
        GetListItems getListItems = PowerMockito.mock(GetListItems.class);
        updateListItems = PowerMockito.mock(UpdateListItems.class);
        deleteListItems = PowerMockito.mock(DeleteListItems.class);

        CategoryViewModelMapper categoryMapper = new CategoryViewModelMapper();
        UnitsViewModelMapper unitsMapper = new UnitsViewModelMapper();
        CurrencyViewModelMapper currencyMapper = new CurrencyViewModelMapper();
        ListItemsViewModelMapper listItemsMapper = new ListItemsViewModelMapper(categoryMapper, currencyMapper, unitsMapper);

        CategoryModel fakeCategoryModel = createFakeCategoryModel();
        UnitModel fakeUnitModel = createFakeUnitModel();
        CurrencyModel fakeCurrencyModel = createFakeCurrencyModel();
        ListItemModel fakeListItemModel = createFakeListItemModel(fakeCategoryModel, fakeUnitModel, fakeCurrencyModel);

        CategoryViewModel fakeCategoryViewModel = createFakeCategoryViewModel();
        UnitViewModel fakeUnitViewModel = createFakeUnitViewModel();
        CurrencyViewModel fakeCurrencyViewModel = createFakeCurrencyViewModel();
        fakeListViewModel = createFakeListViewModel();
        fakeListItemViewModel = createFakeListItemViewModel(fakeCategoryViewModel, fakeUnitViewModel, fakeCurrencyViewModel);

        when(getListItems.get()).thenReturn(Observable.just(Collections.singletonList(fakeListItemModel)));
        when(getUnit.get()).thenReturn(Observable.just(fakeUnitModel));
        when(getCategory.get()).thenReturn(Observable.just(fakeCategoryModel));
        when(getCurrency.get()).thenReturn(Observable.just(fakeCurrencyModel));

        when(getListItems.init(anyString())).thenReturn(getListItems);
        when(getUnit.init(UnitViewModel.NO_UNIT_ID)).thenReturn(getUnit);
        when(getCategory.init(CategoryViewModel.NO_CATEGORY_ID)).thenReturn(getCategory);
        when(getCurrency.init(CurrencyViewModel.NO_CURRENCY_ID)).thenReturn(getCurrency);

        when(mockView.getString(anyInt())).thenReturn(anyString());

        presenter = new ListItemsPresenter(mockPreferences,
                categoryMapper,
                unitsMapper,
                currencyMapper,
                listItemsMapper,
                getCategory,
                getUnit,
                getCurrency,
                getListItems,
                updateListItems,
                deleteListItems);

        verify(getCategory).get();
        verify(getUnit).get();
        verify(getCurrency).get();

        verify(getCategory).init(CategoryViewModel.NO_CATEGORY_ID);
        verify(getUnit).init(UnitViewModel.NO_UNIT_ID);
        verify(getCurrency).init(CurrencyViewModel.NO_CURRENCY_ID);
    }

    @Test
    public void onChildItemMoved_HappyCase() throws Exception {
        when(updateListItems.init(anyCollectionOf(ListItemModel.class))).thenReturn(updateListItems);
        when(updateListItems.get()).thenReturn(Observable.just(true));

        presenter.onChildItemMoved(fakeListItemViewModel);

        verify(updateListItems).init(anyCollectionOf(ListItemModel.class));
        verify(updateListItems).get();
    }

    @Test
    public void onEditItemClick_HappyCase() throws Exception {
        ListViewModel listViewModel = callOnCreateInPresenter();

        presenter.attachRouter(mockRouter);
        presenter.onEditItemClick(fakeListItemViewModel);

        verify(mockRouter).openEditScreen(listViewModel, fakeListItemViewModel);
    }

    @Test
    public void onChildItemEdit_HappyCase() throws Exception {
        ListViewModel listViewModel = callOnCreateInPresenter();

        presenter.attachRouter(mockRouter);
        presenter.onChildItemEdit(fakeListItemViewModel);

        verify(mockRouter).openEditScreen(listViewModel, fakeListItemViewModel);
        assertEquals(fakeListItemViewModel.isPinned(), false);
    }

    @Test
    public void onListItemClick_HappyCase() throws Exception {
        ListViewModel listViewModel = callOnCreateInPresenter();

        presenter.attachRouter(mockRouter);
        presenter.onListItemClick(fakeListItemViewModel);

        verify(mockRouter).openEditScreen(listViewModel, fakeListItemViewModel);
    }

    @Test
    public void onAddButtonClick_ClickAction_0_HappyCase() throws Exception {
        ListViewModel listViewModel = callOnCreateInPresenter();
        when(mockPreferences.getAddButtonClickAction()).thenReturn(0);

        presenter.attachRouter(mockRouter);
        presenter.onAddButtonClick();

        verify(mockRouter).openEditScreen(listViewModel, null);
    }

    @Test
    public void onAddButtonLongClick_ClickAction_0_HappyCase() throws Exception {
        ListViewModel listViewModel = callOnCreateInPresenter();
        when(mockPreferences.getAddButtonClickAction()).thenReturn(0);

        presenter.attachRouter(mockRouter);
        presenter.onAddButtonLongClick();

        verify(mockRouter).openQuickMode(listViewModel.getId());
    }

    @Test
    public void onAddButtonClick_ClickAction_1_HappyCase() throws Exception {
        ListViewModel listViewModel = callOnCreateInPresenter();
        when(mockPreferences.getAddButtonClickAction()).thenReturn(1);

        presenter.attachRouter(mockRouter);
        presenter.onAddButtonClick();

        verify(mockRouter).openQuickMode(listViewModel.getId());
    }

    @Test
    public void onAddButtonLongClick_ClickAction_1_HappyCase() throws Exception {
        ListViewModel listViewModel = callOnCreateInPresenter();
        when(mockPreferences.getAddButtonClickAction()).thenReturn(1);

        presenter.attachRouter(mockRouter);
        presenter.onAddButtonLongClick();

        verify(mockRouter).openEditScreen(listViewModel, null);
    }

    @Test
    public void returnItemsToList_HappyCase() throws Exception {
        when(updateListItems.init(anyCollectionOf(ListItemModel.class))).thenReturn(updateListItems);
        when(updateListItems.get()).thenReturn(Observable.just(true));

        presenter.returnItemsToList(Collections.singletonList(fakeListItemViewModel));

        verify(updateListItems).init(anyCollectionOf(ListItemModel.class));
        verify(updateListItems).get();
    }

    @Test
    public void strikeOutItems_HappyCase() throws Exception {
        when(updateListItems.init(anyCollectionOf(ListItemModel.class))).thenReturn(updateListItems);
        when(updateListItems.get()).thenReturn(Observable.just(true));

        fakeListItemViewModel.setStatus(false);
        presenter.strikeOutItems(Collections.singletonList(fakeListItemViewModel));

        verify(updateListItems).init(anyCollectionOf(ListItemModel.class));
        verify(updateListItems).get();
    }

    @Test
    public void deleteItems_HappyCase() throws Exception {
        when(deleteListItems.init(anyCollectionOf(ListItemModel.class))).thenReturn(deleteListItems);
        when(deleteListItems.get()).thenReturn(Observable.just(true));

        presenter.deleteItems(Collections.singletonList(fakeListItemViewModel));

        verify(deleteListItems).init(anyCollectionOf(ListItemModel.class));
        verify(deleteListItems).get();
    }

    @Test
    public void onCopyItemsClick_HappyCase() throws Exception {
        List<ListItemViewModel> data = Collections.singletonList(fakeListItemViewModel);
        ListViewModel listViewModel = callOnCreateInPresenter();

        presenter.attachView(mockView);
        presenter.onCopyItemsClick(data);

        verify(mockView).openCopyGoodsDialog(listViewModel, data);
    }

    @Test
    public void onMoveItemsClick_HappyCase() throws Exception {
        List<ListItemViewModel> data = Collections.singletonList(fakeListItemViewModel);
        ListViewModel listViewModel = callOnCreateInPresenter();

        presenter.attachView(mockView);
        presenter.onMoveItemsClick(data);

        verify(mockView).openMoveGoodsDialog(listViewModel, data);
    }

    @Test
    public void onEmailShareClick_HappyCase() throws Exception {
        ListViewModel listViewModel = callOnCreateInPresenter();
        when(mockPreferences.getAddButtonClickAction()).thenReturn(1);

        presenter.attachView(mockView);
        presenter.onEmailShareClick();

        verify(mockView).showEmailShareDialog(listViewModel.getName());
    }

    @Test
    public void onSortByNameClick_HappyCase() throws Exception {
        List<ListItemViewModel> data = Collections.singletonList(fakeListItemViewModel);

        presenter.attachView(mockView);
        presenter.onSortByNameClick(data);

        verify(mockPreferences).setSortForShoppingListItems(SortType.SORT_BY_NAME);
        verify(mockView).showData(anyList());
    }

    @Test
    public void onSortByPriorityClick_HappyCase() throws Exception {
        List<ListItemViewModel> data = Collections.singletonList(fakeListItemViewModel);

        presenter.attachView(mockView);
        presenter.onSortByPriorityClick(data);

        verify(mockPreferences).setSortForShoppingListItems(SortType.SORT_BY_PRIORITY);
        verify(mockView).showData(anyList());
    }

    @Test
    public void onSortByCategoryClick_HappyCase() throws Exception {
        List<ListItemViewModel> data = Collections.singletonList(fakeListItemViewModel);

        presenter.attachView(mockView);
        presenter.onSortByCategoryClick(data);

        verify(mockPreferences).setSortForShoppingListItems(SortType.SORT_BY_CATEGORIES);
        verify(mockView).showData(anyList());
    }

    @Test
    public void onSortByTimeCreatedClick_HappyCase() throws Exception {
        List<ListItemViewModel> data = Collections.singletonList(fakeListItemViewModel);

        presenter.attachView(mockView);
        presenter.onSortByTimeCreatedClick(data);

        verify(mockPreferences).setSortForShoppingListItems(SortType.SORT_BY_TIME_CREATED);
        verify(mockView).showData(anyList());
    }

    private ListViewModel callOnCreateInPresenter() {
        Bundle bundle = PowerMockito.mock(Bundle.class);

        when(bundle.getParcelable(ListViewModel.class.getName())).thenReturn(fakeListViewModel);
        presenter.onCreate(bundle, null);
        return fakeListViewModel;
    }
}