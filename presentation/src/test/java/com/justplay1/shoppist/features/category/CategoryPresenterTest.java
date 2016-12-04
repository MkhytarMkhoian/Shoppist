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

package com.justplay1.shoppist.features.category;

import com.justplay1.shoppist.features.category.CategoryPresenter;
import com.justplay1.shoppist.interactor.category.DeleteCategory;
import com.justplay1.shoppist.interactor.category.GetCategoryList;
import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.mappers.CategoryViewModelMapper;
import com.justplay1.shoppist.features.category.CategoryRouter;
import com.justplay1.shoppist.features.category.CategoryView;

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
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DeleteCategory.class, GetCategoryList.class})
public class CategoryPresenterTest {

    private GetCategoryList getCategoryList;
    private DeleteCategory deleteCategory;

    @Mock
    private CategoryView mockView;
    @Mock
    private CategoryRouter mockRouter;

    private CategoryPresenter presenter;

    private CategoryViewModel fakeCategoryViewModel;
    private CategoryModel fakeCategoryModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        getCategoryList = PowerMockito.mock(GetCategoryList.class);
        deleteCategory = PowerMockito.mock(DeleteCategory.class);

        CategoryViewModelMapper mapper = new CategoryViewModelMapper();
        fakeCategoryModel = createFakeCategoryModel();
        fakeCategoryViewModel = mapper.transformToViewModel(fakeCategoryModel);

        when(getCategoryList.get()).thenReturn(Observable.just(Collections.singletonList(fakeCategoryModel)));

        presenter = new CategoryPresenter(getCategoryList, deleteCategory, mapper);
    }

    @Test
    public void detachView_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.detachView();

        assertEquals(presenter.getView(), isNull());
    }

    @Test
    public void onAddButtonClick_HappyCase() throws Exception {
        presenter.attachRouter(mockRouter);
        presenter.onAddButtonClick();

        verify(mockRouter).openEditCategoryScreen(null);
    }

    @Test
    public void onListItemClick_HappyCase() throws Exception {
        presenter.attachRouter(mockRouter);
        presenter.onListItemClick(fakeCategoryViewModel);

        verify(mockRouter).openEditCategoryScreen(fakeCategoryViewModel);
    }

    @Test
    public void deleteItems_HappyCase() throws Exception {
        when(deleteCategory.init(anyCollectionOf(CategoryModel.class))).thenReturn(deleteCategory);
        when(deleteCategory.get()).thenReturn(Observable.just(true));

        presenter.deleteItems(Collections.singletonList(fakeCategoryViewModel));

        verify(deleteCategory).init(anyCollectionOf(CategoryModel.class));
        verify(deleteCategory).get();
    }
}