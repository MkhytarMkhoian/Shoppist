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

import com.justplay1.shoppist.interactor.category.GetCategoryList;
import com.justplay1.shoppist.interactor.goods.AddGoods;
import com.justplay1.shoppist.interactor.goods.UpdateGoods;
import com.justplay1.shoppist.interactor.units.GetUnitsList;
import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.ProductModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.UnitModel;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.CategoryViewModelMapper;
import com.justplay1.shoppist.models.mappers.GoodsViewModelMapper;
import com.justplay1.shoppist.models.mappers.UnitsViewModelMapper;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.view.AddGoodsView;

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
import static com.justplay1.shoppist.ViewModelUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeCategoryViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeProductViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitViewModel;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({UpdateGoods.class, AddGoods.class, GetUnitsList.class, GetCategoryList.class, Bundle.class})
public class AddGoodsPresenterTest {

    private UpdateGoods updateGoods;
    private AddGoods addGoods;
    private GetUnitsList getUnitsList;
    private GetCategoryList getCategoryList;

    @Mock
    private AddGoodsView mockView;

    private AddGoodsPresenter presenter;
    private CategoryModel fakeCategoryModel;
    private UnitModel fakeUnitModel;
    private CategoryViewModel fakeCategoryViewModel;
    private UnitViewModel fakeUnitViewModel;
    private ProductViewModel fakeProductViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        updateGoods = PowerMockito.mock(UpdateGoods.class);
        addGoods = PowerMockito.mock(AddGoods.class);
        getUnitsList = PowerMockito.mock(GetUnitsList.class);
        getCategoryList = PowerMockito.mock(GetCategoryList.class);

        CategoryViewModelMapper categoryMapper = new CategoryViewModelMapper();
        UnitsViewModelMapper unitsMapper = new UnitsViewModelMapper();

        fakeCategoryModel = createFakeCategoryModel();
        fakeUnitModel = createFakeUnitModel();

        fakeCategoryViewModel = createFakeCategoryViewModel();
        fakeUnitViewModel = createFakeUnitViewModel();
        fakeProductViewModel = createFakeProductViewModel(fakeCategoryViewModel, fakeUnitViewModel);

        when(getCategoryList.get()).thenReturn(Observable.just(Collections.singletonList(fakeCategoryModel)));
        when(getUnitsList.get()).thenReturn(Observable.just(Collections.singletonList(fakeUnitModel)));

        presenter = new AddGoodsPresenter(new GoodsViewModelMapper(unitsMapper, categoryMapper),
                updateGoods,
                addGoods,
                getUnitsList,
                getCategoryList,
                categoryMapper,
                unitsMapper);

        verify(getCategoryList).get();
        verify(getUnitsList).get();
    }

    @Test
    public void detachView_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.detachView();

        assertEquals(presenter.getView(), isNull());
    }

    @Test
    public void attachView_EditProduct_HappyCase() throws Exception {
        ProductViewModel viewModel = callOnCreateInPresenter();

        presenter.attachView(mockView);

        verify(mockView).setDefaultUpdateTitle();
        verify(mockView).setViewName(viewModel.getName());
    }

    @Test
    public void attachView_AddProduct_HappyCase() throws Exception {
        presenter.attachView(mockView);

        verify(mockView).setDefaultNewTitle();
        verify(mockView).setViewName(anyString());
    }

    @Test
    public void onPositiveButtonClick_NameIsRequiredError() throws Exception {
        presenter.attachView(mockView);
        presenter.onPositiveButtonClick();

        verify(mockView).showNameIsRequiredError();
        verifyZeroInteractions(addGoods);
        verifyZeroInteractions(updateGoods);
    }

    @Test
    public void onPositiveButtonClick_AddProduct_HappyCase() throws Exception {
        when(addGoods.init(anyCollectionOf(ProductModel.class))).thenReturn(addGoods);
        when(addGoods.get()).thenReturn(Observable.just(true));

        presenter.attachView(mockView);
        presenter.setName(FAKE_NAME);
        presenter.setCategory(fakeCategoryViewModel);
        presenter.setUnit(fakeUnitViewModel);
        presenter.onPositiveButtonClick();

        verify(addGoods).init(anyCollectionOf(ProductModel.class));
        verify(addGoods).get();

        verify(mockView).hideLoading();
        verify(mockView).closeDialog();
        verify(mockView).onComplete(false);

        verifyZeroInteractions(updateGoods);
    }

    @Test
    public void onPositiveButtonClick_UpdateProduct_HappyCase() throws Exception {
        when(updateGoods.init(anyCollectionOf(ProductModel.class))).thenReturn(updateGoods);
        when(updateGoods.get()).thenReturn(Observable.just(true));

        callOnCreateInPresenter();
        presenter.attachView(mockView);
        presenter.setName(FAKE_NAME);
        presenter.setCategory(fakeCategoryViewModel);
        presenter.setUnit(fakeUnitViewModel);
        presenter.onPositiveButtonClick();

        verify(updateGoods).init(anyCollectionOf(ProductModel.class));
        verify(updateGoods).get();

        verify(mockView).hideLoading();
        verify(mockView).closeDialog();
        verify(mockView).onComplete(true);

        verifyZeroInteractions(addGoods);
    }

    @Test
    public void onNegativeButtonClick_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onNegativeButtonClick();

        verify(mockView).closeDialog();
    }

    @Test
    public void onAddUnitClick_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onAddUnitClick();

        verify(mockView).showUnitDialog(null);
    }

    @Test
    public void onEditUnitClick_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onEditUnitClick(fakeUnitViewModel);

        verify(mockView).showUnitDialog(fakeUnitViewModel);
    }

    private ProductViewModel callOnCreateInPresenter() {
        Bundle bundle = PowerMockito.mock(Bundle.class);

        when(bundle.getParcelable(ProductViewModel.class.getName())).thenReturn(fakeProductViewModel);
        when(bundle.getString(Const.NEW_NAME)).thenReturn(FAKE_NAME);
        presenter.onCreate(bundle, null);
        return fakeProductViewModel;
    }
}