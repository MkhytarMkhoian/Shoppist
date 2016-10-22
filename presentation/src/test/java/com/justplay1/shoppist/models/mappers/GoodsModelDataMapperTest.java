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
import com.justplay1.shoppist.models.ProductModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.UnitModel;
import com.justplay1.shoppist.models.UnitViewModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.justplay1.shoppist.ViewModelUtil.FAKE_CREATE_BY_USER;
import static com.justplay1.shoppist.ViewModelUtil.FAKE_ID;
import static com.justplay1.shoppist.ViewModelUtil.FAKE_NAME;
import static com.justplay1.shoppist.ViewModelUtil.FAKE_TIME_CREATED;
import static com.justplay1.shoppist.ViewModelUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeCategoryViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeProductModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeProductViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitViewModel;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by Mkhytar Mkhoian.
 */
public class GoodsModelDataMapperTest {

    private UnitsViewModelMapper unitsMapper;
    private CategoryViewModelMapper categoryMapper;
    private GoodsViewModelMapper mapper;

    @Before
    public void setUp() throws Exception {
        unitsMapper = new UnitsViewModelMapper();
        categoryMapper = new CategoryViewModelMapper();
        mapper = new GoodsViewModelMapper(unitsMapper, categoryMapper);
    }

    @Test
    public void transformGoodsViewModel() {
        CategoryViewModel categoryViewModel = createFakeCategoryViewModel();
        CategoryModel categoryModel = categoryMapper.transform(categoryViewModel);

        UnitViewModel unitViewModel = createFakeUnitViewModel();
        UnitModel unitModel = unitsMapper.transform(unitViewModel);

        ProductViewModel productViewModel = createFakeProductViewModel(categoryViewModel, unitViewModel);

        ProductModel productModel = mapper.transform(productViewModel);

        assertThat(productModel, is(instanceOf(ProductModel.class)));
        assertThat(productModel.getId(), is(FAKE_ID));
        assertThat(productModel.getName(), is(FAKE_NAME));
        assertThat(productModel.getTimeCreated(), is(FAKE_TIME_CREATED));
        assertThat(productModel.isCreateByUser(), is(FAKE_CREATE_BY_USER));
        assertThat(productModel.getCategory(), is(categoryModel));
        assertThat(productModel.getUnit(), is(unitModel));
    }

    @Test
    public void transformGoodsViewModelCollection() {
        ProductViewModel mockDAOOne = mock(ProductViewModel.class);
        ProductViewModel mockDAOTwo = mock(ProductViewModel.class);

        List<ProductViewModel> list = new ArrayList<>(5);
        list.add(mockDAOOne);
        list.add(mockDAOTwo);

        Collection<ProductModel> collection = mapper.transform(list);

        assertThat(collection.toArray()[0], is(instanceOf(ProductModel.class)));
        assertThat(collection.toArray()[1], is(instanceOf(ProductModel.class)));
        assertThat(collection.size(), is(2));
    }

    @Test
    public void transformGoodsModel() {

        CategoryModel categoryModel = createFakeCategoryModel();
        CategoryViewModel categoryDAO = categoryMapper.transformToViewModel(categoryModel);

        UnitModel unitModel = createFakeUnitModel();
        UnitViewModel unitDAO = unitsMapper.transformToViewModel(unitModel);

        ProductModel productModel = createFakeProductModel(unitModel, categoryModel);

        ProductViewModel dao = mapper.transformToViewModel(productModel);

        assertThat(dao, is(instanceOf(ProductViewModel.class)));
        assertThat(dao.getId(), is(FAKE_ID));
        assertThat(dao.getName(), is(FAKE_NAME));
        assertThat(dao.getTimeCreated(), is(FAKE_TIME_CREATED));
        assertThat(dao.isCreateByUser(), is(FAKE_CREATE_BY_USER));
        assertThat(dao.getCategory(), is(categoryDAO));
        assertThat(dao.getUnit(), is(unitDAO));
    }

    @Test
    public void transformGoodsModelCollection() {
        ProductModel mockModelOne = mock(ProductModel.class);
        ProductModel mockModelTwo = mock(ProductModel.class);

        List<ProductModel> list = new ArrayList<>(5);
        list.add(mockModelOne);
        list.add(mockModelTwo);

        Collection<ProductViewModel> collection = mapper.transformToViewModel(list);

        assertThat(collection.toArray()[0], is(instanceOf(ProductViewModel.class)));
        assertThat(collection.toArray()[1], is(instanceOf(ProductViewModel.class)));
        assertThat(collection.size(), is(2));
    }
}