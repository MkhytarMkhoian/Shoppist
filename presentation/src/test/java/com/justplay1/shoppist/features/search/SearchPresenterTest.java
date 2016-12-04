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

package com.justplay1.shoppist.features.search;

import android.os.Bundle;

import com.justplay1.shoppist.features.search.SearchPresenter;
import com.justplay1.shoppist.interactor.category.GetCategory;
import com.justplay1.shoppist.interactor.goods.GetGoodsList;
import com.justplay1.shoppist.interactor.listitems.AddListItems;
import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.ListItemModel;
import com.justplay1.shoppist.models.ProductModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.mappers.CategoryViewModelMapper;
import com.justplay1.shoppist.models.mappers.CurrencyViewModelMapper;
import com.justplay1.shoppist.models.mappers.GoodsViewModelMapper;
import com.justplay1.shoppist.models.mappers.ListItemsViewModelMapper;
import com.justplay1.shoppist.models.mappers.UnitsViewModelMapper;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.features.search.SearchView;

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

import static com.justplay1.shoppist.ViewModelUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeCategoryViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeProductModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeProductViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitViewModel;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GetCategory.class, GetGoodsList.class, AddListItems.class, Bundle.class})
public class SearchPresenterTest {

    private GetCategory getCategory;
    private GetGoodsList getGoodsList;
    private AddListItems addListItems;

    private SearchPresenter presenter;
    private ProductViewModel fakeProductViewModel;
    private ProductModel fakeProductModel;
    private CategoryModel fakeCategoryModel;

    @Mock
    private SearchView mockView;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        getCategory = PowerMockito.mock(GetCategory.class);
        getGoodsList = PowerMockito.mock(GetGoodsList.class);
        addListItems = PowerMockito.mock(AddListItems.class);

        CategoryViewModelMapper categoryMapper = new CategoryViewModelMapper();
        GoodsViewModelMapper goodsMapper = new GoodsViewModelMapper(new UnitsViewModelMapper(), categoryMapper);
        ListItemsViewModelMapper listItemsMapper = new ListItemsViewModelMapper(categoryMapper,
                new CurrencyViewModelMapper(), new UnitsViewModelMapper());

        fakeCategoryModel = createFakeCategoryModel();
        fakeProductModel = createFakeProductModel(createFakeUnitModel(), fakeCategoryModel);
        fakeProductViewModel = createFakeProductViewModel(createFakeCategoryViewModel(), createFakeUnitViewModel());

        when(getGoodsList.get()).thenReturn(Observable.just(Collections.singletonList(fakeProductModel)));
        when(getCategory.init(CategoryViewModel.NO_CATEGORY_ID)).thenReturn(getCategory);
        when(getCategory.get()).thenReturn(Observable.just(fakeCategoryModel));


        presenter = new SearchPresenter(goodsMapper,
                getGoodsList,
                addListItems,
                getCategory,
                categoryMapper,
                listItemsMapper);

        verify(getGoodsList).get();
        verify(getCategory).get();
        verify(getCategory).init(CategoryViewModel.NO_CATEGORY_ID);
    }

    @Test
    public void onListItemClick_QuickSearchInGoodsList_HappyCase() throws Exception {
        callOnCreateInPresenter(Const.CONTEXT_QUICK_SEARCH_IN_GOODS_LIST);
        presenter.attachView(mockView);
        presenter.onListItemClick(fakeProductViewModel);

        verify(mockView).showEditGoodsDialog(fakeProductViewModel);
        verify(mockView, never()).showEditGoodsDialog(anyString());
    }

    @Test
    public void onListItemClick_QuickSearchInGoodsList_JustName_HappyCase() throws Exception {
        callOnCreateInPresenter(Const.CONTEXT_QUICK_SEARCH_IN_GOODS_LIST);
        presenter.attachView(mockView);
        fakeProductViewModel.setId(SearchView.JUST_NAME);
        presenter.onListItemClick(fakeProductViewModel);

        verify(mockView).showEditGoodsDialog(fakeProductViewModel.getName());
        verify(mockView, never()).showEditGoodsDialog(fakeProductViewModel);
    }

    @Test
    public void onListItemClick_QuickAddGoodsToList_HappyCase() throws Exception {
        when(addListItems.init(anyCollectionOf(ListItemModel.class))).thenReturn(addListItems);
        when(addListItems.get()).thenReturn(Observable.just(true));

        callOnCreateInPresenter(Const.CONTEXT_QUICK_ADD_GOODS_TO_LIST);
        presenter.attachView(mockView);
        presenter.onListItemClick(fakeProductViewModel);

        verify(addListItems).get();
        verify(addListItems).init(anyCollectionOf(ListItemModel.class));
        verify(mockView).fadeInSignal(anyInt());
        verify(mockView, never()).showEditGoodsDialog(fakeProductViewModel);
        verify(mockView, never()).showEditGoodsDialog(anyString());
    }

    @Test
    public void onNavigationClick_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onNavigationClick();

        verify(mockView).closeSearch();
    }

    @Test
    public void onTouch_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onTouch();

        verify(mockView).closeSearch();
    }

    private void callOnCreateInPresenter(int contextType) {
        Bundle bundle = PowerMockito.mock(Bundle.class);

        when(bundle.getString(Const.PARENT_LIST_ID)).thenReturn(fakeProductViewModel.getId());
        when(bundle.getInt(Const.SEARCH_CONTEXT_TYPE, Const.CONTEXT_QUICK_SEARCH_IN_GOODS_LIST))
                .thenReturn(contextType);
        presenter.onCreate(bundle, null);
    }
}