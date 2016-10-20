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
import com.justplay1.shoppist.models.CurrencyModel;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.ListItemModel;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.UnitModel;
import com.justplay1.shoppist.models.UnitViewModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_ID;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_NAME;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_NOTE;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_PARENT_LIST_ID;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_PRICE;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_PRIORITY;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_QUANTITY;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_STATUS;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_TIME_CREATED;
import static com.justplay1.shoppist.presenter.ViewModelUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.presenter.ViewModelUtil.createFakeCategoryViewModel;
import static com.justplay1.shoppist.presenter.ViewModelUtil.createFakeCurrencyModel;
import static com.justplay1.shoppist.presenter.ViewModelUtil.createFakeCurrencyViewModel;
import static com.justplay1.shoppist.presenter.ViewModelUtil.createFakeListItemModel;
import static com.justplay1.shoppist.presenter.ViewModelUtil.createFakeListItemViewModel;
import static com.justplay1.shoppist.presenter.ViewModelUtil.createFakeUnitModel;
import static com.justplay1.shoppist.presenter.ViewModelUtil.createFakeUnitViewModel;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListItemsModelDataMapperTest {

    private UnitsViewModelMapper unitsMapper;
    private CategoryViewModelMapper categoryMapper;
    private CurrencyViewModelMapper currencyMapper;
    private ListItemsViewModelMapper mapper;

    @Before
    public void setUp() throws Exception {
        unitsMapper = new UnitsViewModelMapper();
        categoryMapper = new CategoryViewModelMapper();
        currencyMapper = new CurrencyViewModelMapper();
        mapper = new ListItemsViewModelMapper(categoryMapper, currencyMapper, unitsMapper);
    }

    @Test
    public void transformListItemsDAO() {
        CategoryViewModel categoryViewModel = createFakeCategoryViewModel();
        UnitViewModel unitViewModel = createFakeUnitViewModel();
        CurrencyViewModel currencyViewModel = createFakeCurrencyViewModel();

        CategoryModel categoryModel = categoryMapper.transform(categoryViewModel);
        UnitModel unitModel = unitsMapper.transform(unitViewModel);
        CurrencyModel currencyModel = currencyMapper.transform(currencyViewModel);
        ListItemViewModel listItemDAO = createFakeListItemViewModel(categoryViewModel, unitViewModel, currencyViewModel);

        ListItemModel listItemModel = mapper.transform(listItemDAO);

        assertThat(listItemModel, is(instanceOf(ListItemModel.class)));
        assertThat(listItemModel.getId(), is(FAKE_ID));
        assertThat(listItemModel.getName(), is(FAKE_NAME));
        assertThat(listItemModel.getTimeCreated(), is(FAKE_TIME_CREATED));
        assertThat(listItemModel.getNote(), is(FAKE_NOTE));
        assertThat(listItemModel.getParentListId(), is(FAKE_PARENT_LIST_ID));
        assertThat(listItemModel.getPrice(), is(FAKE_PRICE));
        assertThat(listItemModel.getPriority(), is(FAKE_PRIORITY));
        assertThat(listItemModel.getQuantity(), is(FAKE_QUANTITY));
        assertThat(listItemModel.getStatus(), is(FAKE_STATUS));
        assertThat(listItemModel.getCategory(), is(categoryModel));
        assertThat(listItemModel.getUnit(), is(unitModel));
        assertThat(listItemModel.getCurrency(), is(currencyModel));
    }

    @Test
    public void transformListItemsDAOCollection() {
        ListItemViewModel mockViewModelOne = mock(ListItemViewModel.class);
        ListItemViewModel mockViewModelTwo = mock(ListItemViewModel.class);
        List<ListItemViewModel> list = new ArrayList<>(5);
        list.add(mockViewModelOne);
        list.add(mockViewModelTwo);

        Collection<ListItemModel> collection = mapper.transform(list);

        assertThat(collection.toArray()[0], is(instanceOf(ListItemModel.class)));
        assertThat(collection.toArray()[1], is(instanceOf(ListItemModel.class)));
        assertThat(collection.size(), is(2));
    }

    @Test
    public void transformListItemModel() {
        CategoryModel categoryModel = createFakeCategoryModel();
        UnitModel unitModel = createFakeUnitModel();
        CurrencyModel currencyModel = createFakeCurrencyModel();

        CategoryViewModel categoryViewModel = categoryMapper.transformToViewModel(categoryModel);
        UnitViewModel unitViewModel = unitsMapper.transformToViewModel(unitModel);
        CurrencyViewModel currencyViewModel = currencyMapper.transformToViewModel(currencyModel);

        ListItemModel listItemModel = createFakeListItemModel(categoryModel, unitModel, currencyModel);

        ListItemViewModel listItemViewModel = mapper.transformToViewModel(listItemModel);

        assertThat(listItemViewModel, is(instanceOf(ListItemViewModel.class)));
        assertThat(listItemViewModel.getId(), is(FAKE_ID));
        assertThat(listItemViewModel.getName(), is(FAKE_NAME));
        assertThat(listItemViewModel.getTimeCreated(), is(FAKE_TIME_CREATED));
        assertThat(listItemViewModel.getNote(), is(FAKE_NOTE));
        assertThat(listItemViewModel.getParentListId(), is(FAKE_PARENT_LIST_ID));
        assertThat(listItemViewModel.getPrice(), is(FAKE_PRICE));
        assertThat(listItemViewModel.getPriority(), is(FAKE_PRIORITY));
        assertThat(listItemViewModel.getQuantity(), is(FAKE_QUANTITY));
        assertThat(listItemViewModel.getStatus(), is(FAKE_STATUS));
        assertThat(listItemViewModel.getCategory(), is(categoryViewModel));
        assertThat(listItemViewModel.getUnit(), is(unitViewModel));
        assertThat(listItemViewModel.getCurrency(), is(currencyViewModel));
    }

    @Test
    public void transformListItemModelCollection() {
        ListItemModel mockModelOne = mock(ListItemModel.class);
        ListItemModel mockModelTwo = mock(ListItemModel.class);

        List<ListItemModel> list = new ArrayList<>(5);
        list.add(mockModelOne);
        list.add(mockModelTwo);

        Collection<ListItemViewModel> collection = mapper.transformToViewModel(list);

        assertThat(collection.toArray()[0], is(instanceOf(ListItemViewModel.class)));
        assertThat(collection.toArray()[1], is(instanceOf(ListItemViewModel.class)));
        assertThat(collection.size(), is(2));
    }
}