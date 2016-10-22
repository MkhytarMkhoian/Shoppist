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
import com.justplay1.shoppist.interactor.goods.DeleteGoods;
import com.justplay1.shoppist.interactor.goods.GetGoodsList;
import com.justplay1.shoppist.interactor.goods.UpdateGoods;
import com.justplay1.shoppist.interactor.units.GetUnit;
import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.ProductModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.SortType;
import com.justplay1.shoppist.models.UnitModel;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.CategoryViewModelMapper;
import com.justplay1.shoppist.models.mappers.GoodsViewModelMapper;
import com.justplay1.shoppist.models.mappers.UnitsViewModelMapper;
import com.justplay1.shoppist.navigation.GoodsRouter;
import com.justplay1.shoppist.preferences.AppPreferences;
import com.justplay1.shoppist.view.GoodsView;

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
import static com.justplay1.shoppist.ViewModelUtil.createFakeProductModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeProductViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitViewModel;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({UpdateGoods.class,
        GetCategory.class,
        GetUnit.class,
        GetGoodsList.class,
        DeleteGoods.class,
        Bundle.class})
public class GoodsPresenterTest {

    private DeleteGoods deleteGoods;
    private UpdateGoods updateGoods;

    @Mock
    private AppPreferences mockPreferences;
    @Mock
    private GoodsRouter mockRouter;
    @Mock
    private GoodsView mockView;

    private GoodsPresenter presenter;
    private CategoryViewModel fakeCategoryViewModel;
    private UnitViewModel fakeUnitViewModel;
    private ProductViewModel fakeProductViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        updateGoods = PowerMockito.mock(UpdateGoods.class);
        GetCategory getCategory = PowerMockito.mock(GetCategory.class);
        GetUnit getUnit = PowerMockito.mock(GetUnit.class);
        GetGoodsList getGoodsList = PowerMockito.mock(GetGoodsList.class);
        deleteGoods = PowerMockito.mock(DeleteGoods.class);

        CategoryViewModelMapper categoryMapper = new CategoryViewModelMapper();
        UnitsViewModelMapper unitsMapper = new UnitsViewModelMapper();
        GoodsViewModelMapper goodsMapper = new GoodsViewModelMapper(unitsMapper, categoryMapper);

        CategoryModel fakeCategoryModel = createFakeCategoryModel();
        UnitModel fakeUnitModel = createFakeUnitModel();
        ProductModel fakeProductModel = createFakeProductModel(fakeUnitModel, fakeCategoryModel);

        fakeCategoryViewModel = createFakeCategoryViewModel();
        fakeUnitViewModel = createFakeUnitViewModel();
        fakeProductViewModel = createFakeProductViewModel(fakeCategoryViewModel, fakeUnitViewModel);

        when(getGoodsList.get()).thenReturn(Observable.just(Collections.singletonList(fakeProductModel)));
        when(getUnit.get()).thenReturn(Observable.just(fakeUnitModel));
        when(getCategory.get()).thenReturn(Observable.just(fakeCategoryModel));

        when(getUnit.init(UnitViewModel.NO_UNIT_ID)).thenReturn(getUnit);
        when(getCategory.init(CategoryViewModel.NO_CATEGORY_ID)).thenReturn(getCategory);

        presenter = new GoodsPresenter(mockPreferences,
                goodsMapper,
                getGoodsList,
                deleteGoods,
                updateGoods,
                getCategory,
                getUnit,
                categoryMapper,
                unitsMapper);

        verify(getCategory).get();
        verify(getUnit).get();
        verify(getGoodsList).get();

        verify(getCategory).init(CategoryViewModel.NO_CATEGORY_ID);
        verify(getUnit).init(UnitViewModel.NO_UNIT_ID);
    }

    @Test
    public void onAddButtonClick_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onAddButtonClick();

        verify(mockView).showEditGoodsDialog(null);
    }

    @Test
    public void onListItemClick_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onListItemClick(fakeProductViewModel);

        verify(mockView).showEditGoodsDialog(fakeProductViewModel);
    }

    @Test
    public void onChangeCategoryClick_HappyCase() throws Exception {
        List<ProductViewModel> data = Collections.singletonList(fakeProductViewModel);

        presenter.attachView(mockView);
        presenter.onChangeCategoryClick(data);

        verify(mockView).showChangeCategoryDialog(data);
    }

    @Test
    public void onChangeUnitClick_HappyCase() throws Exception {
        List<ProductViewModel> data = Collections.singletonList(fakeProductViewModel);

        presenter.attachView(mockView);
        presenter.onChangeUnitClick(data);

        verify(mockView).showChangeUnitDialog(data);
    }

    @Test
    public void onSortByNameClick_HappyCase() throws Exception {
        List<ProductViewModel> data = Collections.singletonList(fakeProductViewModel);

        presenter.attachView(mockView);
        presenter.onSortByNameClick(data);

        verify(mockPreferences).setSortForGoods(SortType.SORT_BY_NAME);
        verify(mockView).showData(anyList());
    }

    @Test
    public void onSortByTimeCreatedClick_HappyCase() throws Exception {
        List<ProductViewModel> data = Collections.singletonList(fakeProductViewModel);

        presenter.attachView(mockView);
        presenter.onSortByTimeCreatedClick(data);

        verify(mockPreferences).setSortForGoods(SortType.SORT_BY_TIME_CREATED);
        verify(mockView).showData(anyList());
    }

    @Test
    public void onSortByCategoryClick_HappyCase() throws Exception {
        List<ProductViewModel> data = Collections.singletonList(fakeProductViewModel);

        presenter.attachView(mockView);
        presenter.onSortByCategoryClick(data);

        verify(mockPreferences).setSortForGoods(SortType.SORT_BY_CATEGORIES);
        verify(mockView).showData(anyList());
    }

    @Test
    public void onSearchClick_HappyCase() throws Exception {
        presenter.attachRouter(mockRouter);
        presenter.onSearchClick();

        verify(mockRouter).openSearchScreen();
    }

    @Test
    public void deleteItems_HappyCase() throws Exception {
        when(deleteGoods.init(anyCollectionOf(ProductModel.class))).thenReturn(deleteGoods);
        when(deleteGoods.get()).thenReturn(Observable.just(true));

        presenter.deleteItems(Collections.singletonList(fakeProductViewModel));

        verify(deleteGoods).init(anyCollectionOf(ProductModel.class));
        verify(deleteGoods).get();
    }

    @Test
    public void changeUnit_HappyCase() throws Exception {
        List<ProductViewModel> data = Collections.singletonList(fakeProductViewModel);

        when(updateGoods.init(anyCollectionOf(ProductModel.class))).thenReturn(updateGoods);
        when(updateGoods.get()).thenReturn(Observable.just(true));

        presenter.changeUnit(fakeUnitViewModel, data);

        verify(updateGoods).init(anyCollectionOf(ProductModel.class));
        verify(updateGoods).get();
    }

    @Test
    public void changeCategory_HappyCase() throws Exception {
        List<ProductViewModel> data = Collections.singletonList(fakeProductViewModel);

        when(updateGoods.init(anyCollectionOf(ProductModel.class))).thenReturn(updateGoods);
        when(updateGoods.get()).thenReturn(Observable.just(true));

        presenter.changeCategory(fakeCategoryViewModel, data);

        verify(updateGoods).init(anyCollectionOf(ProductModel.class));
        verify(updateGoods).get();
    }

}