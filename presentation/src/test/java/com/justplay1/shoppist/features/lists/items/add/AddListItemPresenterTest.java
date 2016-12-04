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

package com.justplay1.shoppist.features.lists.items.add;

import android.os.Bundle;

import com.justplay1.shoppist.features.lists.items.add.AddListItemPresenter;
import com.justplay1.shoppist.interactor.category.GetCategoryList;
import com.justplay1.shoppist.interactor.currency.GetCurrencyList;
import com.justplay1.shoppist.interactor.goods.GetGoodsList;
import com.justplay1.shoppist.interactor.goods.UpdateGoods;
import com.justplay1.shoppist.interactor.listitems.AddListItems;
import com.justplay1.shoppist.interactor.listitems.UpdateListItems;
import com.justplay1.shoppist.interactor.units.GetUnitsList;
import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.CurrencyModel;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.ListItemModel;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.models.ProductModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.UnitModel;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.CategoryViewModelMapper;
import com.justplay1.shoppist.models.mappers.CurrencyViewModelMapper;
import com.justplay1.shoppist.models.mappers.GoodsViewModelMapper;
import com.justplay1.shoppist.models.mappers.ListItemsViewModelMapper;
import com.justplay1.shoppist.models.mappers.UnitsViewModelMapper;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.features.lists.items.add.AddListItemView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;

import rx.Observable;

import static com.justplay1.shoppist.ViewModelUtil.FAKE_NAME;
import static com.justplay1.shoppist.ViewModelUtil.FAKE_PARENT_LIST_ID;
import static com.justplay1.shoppist.ViewModelUtil.FAKE_PRICE;
import static com.justplay1.shoppist.ViewModelUtil.FAKE_QUANTITY;
import static com.justplay1.shoppist.ViewModelUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeCategoryViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeCurrencyModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeCurrencyViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeListItemViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeProductModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeProductViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitViewModel;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({UpdateGoods.class,
        UpdateListItems.class,
        AddListItems.class,
        GetUnitsList.class,
        GetCategoryList.class,
        GetCurrencyList.class,
        GetGoodsList.class,
        Bundle.class})
public class AddListItemPresenterTest {

    private AddListItems addListItems;
    private UpdateListItems updateListItems;
    private UpdateGoods updateGoods;

    private AddListItemPresenter presenter;

    private ProductViewModel fakeProductViewModel;
    private ListItemViewModel fakeListItemViewModel;

    @Mock
    private AddListItemView mockView;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        updateListItems = PowerMockito.mock(UpdateListItems.class);
        GetCurrencyList getCurrencyList = PowerMockito.mock(GetCurrencyList.class);
        GetGoodsList getGoodsList = PowerMockito.mock(GetGoodsList.class);
        updateGoods = PowerMockito.mock(UpdateGoods.class);
        addListItems = PowerMockito.mock(AddListItems.class);
        GetUnitsList getUnitsList = PowerMockito.mock(GetUnitsList.class);
        GetCategoryList getCategoryList = PowerMockito.mock(GetCategoryList.class);

        CategoryViewModelMapper categoryMapper = new CategoryViewModelMapper();
        UnitsViewModelMapper unitsMapper = new UnitsViewModelMapper();
        CurrencyViewModelMapper currencyMapper = new CurrencyViewModelMapper();
        GoodsViewModelMapper goodsMapper = new GoodsViewModelMapper(unitsMapper, categoryMapper);
        ListItemsViewModelMapper listItemsMapper = new ListItemsViewModelMapper(categoryMapper, currencyMapper, unitsMapper);

        CategoryModel fakeCategoryModel = createFakeCategoryModel();
        UnitModel fakeUnitModel = createFakeUnitModel();
        CurrencyModel fakeCurrencyModel = createFakeCurrencyModel();
        ProductModel fakeProductModel = createFakeProductModel(fakeUnitModel, fakeCategoryModel);

        CategoryViewModel fakeCategoryViewModel = createFakeCategoryViewModel();
        UnitViewModel fakeUnitViewModel = createFakeUnitViewModel();
        CurrencyViewModel fakeCurrencyViewModel = createFakeCurrencyViewModel();
        fakeProductViewModel = createFakeProductViewModel(fakeCategoryViewModel, fakeUnitViewModel);
        fakeListItemViewModel = createFakeListItemViewModel(fakeCategoryViewModel, fakeUnitViewModel, fakeCurrencyViewModel);

        when(getCategoryList.get()).thenReturn(Observable.just(Collections.singletonList(fakeCategoryModel)));
        when(getUnitsList.get()).thenReturn(Observable.just(Collections.singletonList(fakeUnitModel)));
        when(getCurrencyList.get()).thenReturn(Observable.just(Collections.singletonList(fakeCurrencyModel)));
        when(getGoodsList.get()).thenReturn(Observable.just(Collections.singletonList(fakeProductModel)));

        presenter = new AddListItemPresenter(categoryMapper,
                unitsMapper,
                currencyMapper,
                goodsMapper,
                listItemsMapper,
                addListItems,
                updateListItems,
                getCategoryList,
                getCurrencyList,
                getGoodsList,
                getUnitsList,
                updateGoods);

        verify(getCategoryList).get();
        verify(getUnitsList).get();
        verify(getCurrencyList).get();
        verify(getGoodsList).get();
    }

    @Test
    public void detachView_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.detachView();

        assertEquals(presenter.getView(), isNull());
    }

    @Test
    public void attachView_EditListItem_HappyCase() throws Exception {
        callOnCreateInPresenter();

        presenter.attachView(mockView);

        verify(mockView).setName(fakeListItemViewModel.getName());
        verify(mockView).setToolbarTitle(fakeListItemViewModel.getName());
        verify(mockView).selectPriority(fakeListItemViewModel.getPriority());
        verify(mockView).setNote(fakeListItemViewModel.getNote());
        verify(mockView).setPrice(String.valueOf(fakeListItemViewModel.getPrice()));
        verify(mockView).setQuantity(String.valueOf(fakeListItemViewModel.getQuantity()));
    }

    @Test
    public void attachView_AddListItem_HappyCase() throws Exception {
        presenter.attachView(mockView);

        verify(mockView).setDefaultToolbarTitle();
        verify(mockView).setName(anyString());
        verify(mockView).selectPriority(Priority.NO_PRIORITY);
        verify(mockView).setNote(anyString());
        verify(mockView).setPrice(String.valueOf(0.0));
        verify(mockView).setQuantity(String.valueOf(0.0));
    }

    @Test
    public void onProductClick_Null_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onProductClick(null);

        verify(mockView).selectUnit(UnitViewModel.NO_UNIT_ID);
        verify(mockView).selectCategory(CategoryViewModel.NO_CATEGORY_ID);
    }

    @Test
    public void onProductClick_NotNull_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onProductClick(fakeProductViewModel);

        verify(mockView).selectUnit(fakeProductViewModel.getUnit().getId());
        verify(mockView).selectCategory(fakeProductViewModel.getCategory().getId());
    }

    @Test
    public void onIncrementPriceClick_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onIncrementPriceClick(String.valueOf(FAKE_PRICE));

        verify(mockView).setPrice(String.valueOf(FAKE_PRICE + 1));
    }

    @Test
    public void onDecrementPriceClick_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onDecrementPriceClick(String.valueOf(FAKE_PRICE));

        verify(mockView).setPrice(String.valueOf(FAKE_PRICE - 1));
    }

    @Test
    public void onIncrementQuantityClick_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onIncrementQuantityClick(String.valueOf(FAKE_QUANTITY));

        verify(mockView).setQuantity(String.valueOf(FAKE_QUANTITY + 1));
    }

    @Test
    public void onDecrementQuantityClick_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onDecrementQuantityClick(String.valueOf(FAKE_QUANTITY));

        verify(mockView).setQuantity(String.valueOf(FAKE_QUANTITY - 1));
    }

    @Test
    public void onDoneButtonClick_UpdateListItem_NullProduct_HappyCase() throws Exception {
        when(updateListItems.init(anyCollectionOf(ListItemModel.class))).thenReturn(updateListItems);
        when(updateListItems.get()).thenReturn(Observable.just(true));

        callOnCreateInPresenter();
        presenter.attachView(mockView);
        presenter.onDoneButtonClick();

        verify(updateListItems).init(anyCollectionOf(ListItemModel.class));
        verify(updateListItems).get();

        verify(mockView).closeScreen();

        verifyZeroInteractions(updateGoods);
    }

    @Test
    public void onDoneButtonClick_UpdateListItem_HappyCase() throws Exception {
        when(updateListItems.init(anyCollectionOf(ListItemModel.class))).thenReturn(updateListItems);
        when(updateListItems.get()).thenReturn(Observable.just(true));
        when(updateGoods.init(anyCollectionOf(ProductModel.class))).thenReturn(updateGoods);
        when(updateGoods.get()).thenReturn(Observable.just(true));

        callOnCreateInPresenter();
        presenter.attachView(mockView);
        presenter.setProduct(fakeProductViewModel);
        presenter.onDoneButtonClick();

        verify(updateListItems).init(anyCollectionOf(ListItemModel.class));
        verify(updateListItems).get();

        verify(updateGoods).init(anyCollectionOf(ProductModel.class));
        verify(updateGoods).get();

        verify(mockView).closeScreen();
    }

    @Test
    public void onDoneButtonClick_AddListItem_HappyCase() throws Exception {
        when(addListItems.init(anyCollectionOf(ListItemModel.class))).thenReturn(addListItems);
        when(addListItems.get()).thenReturn(Observable.just(true));

        presenter.attachView(mockView);
        presenter.selectName(FAKE_NAME);
        presenter.onDoneButtonClick();

        verify(addListItems).init(anyCollectionOf(ListItemModel.class));
        verify(addListItems).get();

        verify(mockView).closeScreen();
    }

    @Test
    public void onDoneButtonLongClick_AddListItem_HappyCase() throws Exception {
        when(addListItems.init(anyCollectionOf(ListItemModel.class))).thenReturn(addListItems);
        when(addListItems.get()).thenReturn(Observable.just(true));

        presenter.attachView(mockView);
        presenter.selectName(FAKE_NAME);
        presenter.onDoneButtonLongClick();

        verify(addListItems).init(anyCollectionOf(ListItemModel.class));
        verify(addListItems).get();

        verify(mockView).showKeyboard();
        verify(mockView).showNewElementAddedMessage();
        verify(mockView, times(2)).setDefaultToolbarTitle();
        verify(mockView, times(2)).setName("");
        verify(mockView, times(2)).selectPriority(Priority.NO_PRIORITY);
        verify(mockView, times(2)).setNote("");
        verify(mockView).setPrice(String.valueOf(0));
        verify(mockView).setQuantity(String.valueOf(0));
        verify(mockView).setDefaultUnit();
        verify(mockView).setDefaultCategory();
        verify(mockView).setDefaultCurrency();
    }

    @Test
    public void onDoneButtonLongClick_UpdateListItem_HappyCase() throws Exception {
        when(updateListItems.init(anyCollectionOf(ListItemModel.class))).thenReturn(updateListItems);
        when(updateListItems.get()).thenReturn(Observable.just(true));
        when(updateGoods.init(anyCollectionOf(ProductModel.class))).thenReturn(updateGoods);
        when(updateGoods.get()).thenReturn(Observable.just(true));

        callOnCreateInPresenter();
        presenter.attachView(mockView);
        presenter.setProduct(fakeProductViewModel);
        presenter.onDoneButtonLongClick();

        verify(updateListItems).init(anyCollectionOf(ListItemModel.class));
        verify(updateListItems).get();

        verify(updateGoods).init(anyCollectionOf(ProductModel.class));
        verify(updateGoods).get();

        verify(mockView).showKeyboard();
        verify(mockView).showElementUpdatedMessage();
        verify(mockView).setDefaultToolbarTitle();
        verify(mockView).setName("");
        verify(mockView).selectPriority(Priority.NO_PRIORITY);
        verify(mockView).setNote("");
        verify(mockView).setPrice(String.valueOf(0));
        verify(mockView).setQuantity(String.valueOf(0));
        verify(mockView).setDefaultUnit();
        verify(mockView).setDefaultCategory();
        verify(mockView).setDefaultCurrency();
    }

    private ListItemViewModel callOnCreateInPresenter() {
        Bundle bundle = PowerMockito.mock(Bundle.class);

        when(bundle.getParcelable(ListItemViewModel.class.getName())).thenReturn(fakeListItemViewModel);
        when(bundle.getString(Const.PARENT_LIST_ID)).thenReturn(FAKE_PARENT_LIST_ID);
        presenter.onCreate(bundle, null);
        return fakeListItemViewModel;
    }
}