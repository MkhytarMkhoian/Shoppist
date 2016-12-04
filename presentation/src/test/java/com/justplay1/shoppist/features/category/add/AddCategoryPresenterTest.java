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

package com.justplay1.shoppist.features.category.add;

import android.graphics.Color;
import android.os.Bundle;

import com.justplay1.shoppist.features.category.add.AddCategoryPresenter;
import com.justplay1.shoppist.interactor.category.AddCategory;
import com.justplay1.shoppist.interactor.category.UpdateCategory;
import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.mappers.CategoryViewModelMapper;
import com.justplay1.shoppist.features.category.add.AddCategoryView;

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
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AddCategory.class, UpdateCategory.class, Bundle.class})
public class AddCategoryPresenterTest {

    private AddCategory addCategory;
    private UpdateCategory updateCategory;
    @Mock
    private AddCategoryView mockView;

    private AddCategoryPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        addCategory = PowerMockito.mock(AddCategory.class);
        updateCategory = PowerMockito.mock(UpdateCategory.class);

        presenter = new AddCategoryPresenter(new CategoryViewModelMapper(), addCategory, updateCategory);
    }

    @Test
    public void detachView_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.detachView();

        assertEquals(presenter.getView(), isNull());
    }

    @Test
    public void attachView_EditCategory_HappyCase() throws Exception {
        CategoryViewModel viewModel = callOnCreateInPresenter();

        presenter.attachView(mockView);

        verify(mockView).setToolbarTitle(viewModel.getName());
        verify(mockView).setColorToButton(viewModel.getColor());
        verify(mockView).setName(viewModel.getName());
    }

    @Test
    public void attachView_AddNewCategory_HappyCase() throws Exception {

        presenter.attachView(mockView);

        verify(mockView).setDefaultToolbarTitle();
        verify(mockView).setColorToButton(Color.DKGRAY);
        verify(mockView).setName("");
    }

    @Test
    public void onColorButtonClick_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onColorButtonClick();

        verify(mockView).showSelectColorDialog(Color.DKGRAY);
    }

    @Test
    public void onDoneButtonClick_NameIsRequiredError() throws Exception {
        when(addCategory.init(Collections.singletonList(createFakeCategoryModel()))).thenReturn(addCategory);
        when(addCategory.get()).thenReturn(Observable.just(true));

        presenter.attachView(mockView);
        presenter.onDoneButtonClick();

        verify(mockView).showNameIsRequiredError();
    }

    @Test
    public void onDoneButtonClick_AddCategory_HappyCase() throws Exception {
        when(addCategory.init(anyCollectionOf(CategoryModel.class))).thenReturn(addCategory);
        when(addCategory.get()).thenReturn(Observable.just(true));

        presenter.attachView(mockView);
        presenter.selectName(FAKE_NAME);
        presenter.onDoneButtonClick();

        verify(addCategory).init(anyCollectionOf(CategoryModel.class));
        verify(addCategory).get();
        verify(mockView).closeScreen();
        verifyZeroInteractions(updateCategory);
    }

    @Test
    public void onDoneButtonClick_UpdateCategory_HappyCase() throws Exception {
        when(updateCategory.init(anyCollectionOf(CategoryModel.class))).thenReturn(updateCategory);
        when(updateCategory.get()).thenReturn(Observable.just(true));

       callOnCreateInPresenter();
        presenter.attachView(mockView);

        presenter.onDoneButtonClick();

        verify(updateCategory).init(anyCollectionOf(CategoryModel.class));
        verify(updateCategory).get();
        verify(mockView).closeScreen();
        verifyZeroInteractions(addCategory);
    }

    @Test
    public void onDoneButtonLongClick_UpdateCategory_HappyCase() throws Exception {
        when(updateCategory.init(anyCollectionOf(CategoryModel.class))).thenReturn(updateCategory);
        when(updateCategory.get()).thenReturn(Observable.just(true));

        callOnCreateInPresenter();
        presenter.attachView(mockView);

        presenter.onDoneButtonLongClick();

        verify(updateCategory).init(anyCollectionOf(CategoryModel.class));
        verify(updateCategory).get();

        verify(mockView).showKeyboard();
        verify(mockView).showElementUpdatedMessage();
        verify(mockView).setDefaultToolbarTitle();
        verify(mockView).setName("");
        verify(mockView).setColorToButton(Color.DKGRAY);

        verifyZeroInteractions(addCategory);
    }

    @Test
    public void onDoneButtonLongClick_AddCategory_HappyCase() throws Exception {
        when(addCategory.init(anyCollectionOf(CategoryModel.class))).thenReturn(addCategory);
        when(addCategory.get()).thenReturn(Observable.just(true));

        presenter.attachView(mockView);
        presenter.selectName(FAKE_NAME);
        presenter.onDoneButtonLongClick();

        verify(addCategory).init(anyCollectionOf(CategoryModel.class));
        verify(addCategory).get();

        verify(mockView).showKeyboard();
        verify(mockView).showNewElementAddedMessage();
        verify(mockView, times(2)).setDefaultToolbarTitle();
        verify(mockView, times(2)).setName("");
        verify(mockView, times(2)).setColorToButton(Color.DKGRAY);

        verifyZeroInteractions(updateCategory);
    }

    private CategoryViewModel callOnCreateInPresenter() {
        Bundle bundle = PowerMockito.mock(Bundle.class);
        CategoryViewModel viewModel = createFakeCategoryViewModel();

        when(bundle.getParcelable(CategoryViewModel.class.getName())).thenReturn(viewModel);
        presenter.onCreate(bundle, null);
        return viewModel;
    }
}