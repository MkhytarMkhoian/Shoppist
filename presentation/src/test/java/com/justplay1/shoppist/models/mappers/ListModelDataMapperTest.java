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

import com.justplay1.shoppist.models.ListModel;
import com.justplay1.shoppist.models.ListViewModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.justplay1.shoppist.ViewModelUtil.FAKE_BOUGHT_COUNT;
import static com.justplay1.shoppist.ViewModelUtil.FAKE_COLOR;
import static com.justplay1.shoppist.ViewModelUtil.FAKE_ID;
import static com.justplay1.shoppist.ViewModelUtil.FAKE_NAME;
import static com.justplay1.shoppist.ViewModelUtil.FAKE_PRIORITY;
import static com.justplay1.shoppist.ViewModelUtil.FAKE_SIZE;
import static com.justplay1.shoppist.ViewModelUtil.FAKE_TIME_CREATED;
import static com.justplay1.shoppist.ViewModelUtil.createFakeListModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeListViewModel;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListModelDataMapperTest {

    private ListViewModelMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ListViewModelMapper();
    }

    @Test
    public void transformListViewModel() {
        ListViewModel viewModel = createFakeListViewModel();

        ListModel model = mapper.transform(viewModel);

        assertThat(model, is(instanceOf(ListModel.class)));
        assertThat(model.getId(), is(FAKE_ID));
        assertThat(model.getName(), is(FAKE_NAME));
        assertThat(model.getColor(), is(FAKE_COLOR));
        assertThat(model.getSize(), is(FAKE_SIZE));
        assertThat(model.getBoughtCount(), is(FAKE_BOUGHT_COUNT));
        assertThat(model.getTimeCreated(), is(FAKE_TIME_CREATED));
        assertThat(model.getPriority(), is(FAKE_PRIORITY));
    }

    @Test
    public void transformListViewModelCollection() {
        ListViewModel mockViewModelOne = mock(ListViewModel.class);
        ListViewModel mockViewModelTwo = mock(ListViewModel.class);

        List<ListViewModel> list = new ArrayList<>(5);
        list.add(mockViewModelOne);
        list.add(mockViewModelTwo);

        Collection<ListModel> collection = mapper.transform(list);

        assertThat(collection.toArray()[0], is(instanceOf(ListModel.class)));
        assertThat(collection.toArray()[1], is(instanceOf(ListModel.class)));
        assertThat(collection.size(), is(2));
    }

    @Test
    public void transformListModel() {
        ListModel model = createFakeListModel();

        ListViewModel viewModel = mapper.transformToViewModel(model);

        assertThat(viewModel, is(instanceOf(ListViewModel.class)));
        assertThat(viewModel.getId(), is(FAKE_ID));
        assertThat(viewModel.getName(), is(FAKE_NAME));
        assertThat(viewModel.getColor(), is(FAKE_COLOR));
        assertThat(viewModel.getSize(), is(FAKE_SIZE));
        assertThat(viewModel.getBoughtCount(), is(FAKE_BOUGHT_COUNT));
        assertThat(viewModel.getTimeCreated(), is(FAKE_TIME_CREATED));
        assertThat(viewModel.getPriority(), is(FAKE_PRIORITY));
    }

    @Test
    public void transformListModelCollection() {
        ListModel mockModelOne = mock(ListModel.class);
        ListModel mockModelTwo = mock(ListModel.class);

        List<ListModel> list = new ArrayList<>(5);
        list.add(mockModelOne);
        list.add(mockModelTwo);

        Collection<ListViewModel> collection = mapper.transformToViewModel(list);

        assertThat(collection.toArray()[0], is(instanceOf(ListViewModel.class)));
        assertThat(collection.toArray()[1], is(instanceOf(ListViewModel.class)));
        assertThat(collection.size(), is(2));
    }
}