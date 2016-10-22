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
import com.justplay1.shoppist.interactor.units.GetUnitsList;
import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.mappers.CategoryViewModelMapper;
import com.justplay1.shoppist.view.SelectCategoryView;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GetUnitsList.class, Bundle.class})
public class SelectCategoryPresenterTest {

    private GetCategoryList getCategoryList;

    @Mock
    private SelectCategoryView mockView;

    private SelectCategoryPresenter presenter;

    private CategoryViewModel fakeCategoryViewModel;
    private CategoryModel fakeCategoryModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        getCategoryList = PowerMockito.mock(GetCategoryList.class);

        CategoryViewModelMapper mapper = new CategoryViewModelMapper();
        fakeCategoryModel = createFakeCategoryModel();
        fakeCategoryViewModel = mapper.transformToViewModel(fakeCategoryModel);

        when(getCategoryList.get()).thenReturn(Observable.just(Collections.singletonList(fakeCategoryModel)));

        presenter = new SelectCategoryPresenter(mapper, getCategoryList);
    }

    @Test
    public void onNegativeButtonClick_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onNegativeButtonClick();

        verify(mockView).closeDialog();
    }

    @Test
    public void onPositiveButtonClick_Null_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onPositiveButtonClick(null);

        verify(mockView).closeDialog();
    }

    @Test
    public void onPositiveButtonClick_NotNull_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onPositiveButtonClick(fakeCategoryViewModel);

        verify(mockView).closeDialog();
        verify(mockView).onComplete(fakeCategoryViewModel, false);
    }

    private CategoryViewModel callOnCreateInPresenter() {
        Bundle bundle = PowerMockito.mock(Bundle.class);

        when(bundle.getParcelable(CategoryViewModel.class.getName())).thenReturn(fakeCategoryViewModel);
        presenter.onCreate(bundle, null);
        return fakeCategoryViewModel;
    }
}