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

package com.justplay1.shoppist.models.mappers;

import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.models.CategoryViewModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_COLOR;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_CREATE_BY_USER;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_ID;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_NAME;
import static com.justplay1.shoppist.presenter.ViewModelUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.presenter.ViewModelUtil.createFakeCategoryViewModel;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CategoryViewModelMapperTest {

    private CategoryViewModelMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new CategoryViewModelMapper();
    }

    @Test
    public void transformCategoryViewModel() {
        CategoryViewModel viewModel = createFakeCategoryViewModel();

        CategoryModel model = mapper.transform(viewModel);

        assertThat(model, is(instanceOf(CategoryModel.class)));
        assertThat(model.getId(), is(FAKE_ID));
        assertThat(model.getName(), is(FAKE_NAME));
        assertThat(model.getColor(), is(FAKE_COLOR));
        assertThat(model.isCreateByUser(), is(FAKE_CREATE_BY_USER));
    }

    @Test
    public void transformCategoryViewModelCollection() {
        CategoryViewModel mockViewModelOne = mock(CategoryViewModel.class);
        CategoryViewModel mockViewModelTwo = mock(CategoryViewModel.class);

        List<CategoryViewModel> list = new ArrayList<>(5);
        list.add(mockViewModelOne);
        list.add(mockViewModelTwo);

        Collection<CategoryModel> collection = mapper.transform(list);

        assertThat(collection.toArray()[0], is(instanceOf(CategoryModel.class)));
        assertThat(collection.toArray()[1], is(instanceOf(CategoryModel.class)));
        assertThat(collection.size(), is(2));
    }

    @Test
    public void transformCategoryModel() {
        CategoryModel model = createFakeCategoryModel();

        CategoryViewModel viewModel = mapper.transformToViewModel(model);

        assertThat(viewModel, is(instanceOf(CategoryViewModel.class)));
        assertThat(viewModel.getId(), is(FAKE_ID));
        assertThat(viewModel.getName(), is(FAKE_NAME));
        assertThat(viewModel.getColor(), is(FAKE_COLOR));
        assertThat(viewModel.isCreateByUser(), is(FAKE_CREATE_BY_USER));
    }

    @Test
    public void transformCategoryModelCollection() {
        CategoryModel mockModelOne = mock(CategoryModel.class);
        CategoryModel mockModelTwo = mock(CategoryModel.class);

        List<CategoryModel> list = new ArrayList<>(5);
        list.add(mockModelOne);
        list.add(mockModelTwo);

        Collection<CategoryViewModel> collection = mapper.transformToViewModel(list);

        assertThat(collection.toArray()[0], is(instanceOf(CategoryViewModel.class)));
        assertThat(collection.toArray()[1], is(instanceOf(CategoryViewModel.class)));
        assertThat(collection.size(), is(2));
    }
}