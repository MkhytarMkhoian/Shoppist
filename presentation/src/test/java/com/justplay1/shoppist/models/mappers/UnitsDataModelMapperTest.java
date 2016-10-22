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

import com.justplay1.shoppist.models.UnitModel;
import com.justplay1.shoppist.models.UnitViewModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.justplay1.shoppist.ViewModelUtil.FAKE_ID;
import static com.justplay1.shoppist.ViewModelUtil.FAKE_NAME;
import static com.justplay1.shoppist.ViewModelUtil.FAKE_SHORT_NAME;
import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitViewModel;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by Mkhytar Mkhoian.
 */
public class UnitsDataModelMapperTest {

    private UnitsViewModelMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new UnitsViewModelMapper();
    }

    @Test
    public void transformUnitViewModel() {
        UnitViewModel viewModel = createFakeUnitViewModel();

        UnitModel model = mapper.transform(viewModel);

        assertThat(model, is(instanceOf(UnitModel.class)));
        assertThat(model.getId(), is(FAKE_ID));
        assertThat(model.getName(), is(FAKE_NAME));
        assertThat(model.getShortName(), is(FAKE_SHORT_NAME));
    }

    @Test
    public void transformUnitViewModelCollection() {
        UnitViewModel mockViewModelOne = mock(UnitViewModel.class);
        UnitViewModel mockViewModelTwo = mock(UnitViewModel.class);

        List<UnitViewModel> list = new ArrayList<>(5);
        list.add(mockViewModelOne);
        list.add(mockViewModelTwo);

        Collection<UnitModel> collection = mapper.transform(list);

        assertThat(collection.toArray()[0], is(instanceOf(UnitModel.class)));
        assertThat(collection.toArray()[1], is(instanceOf(UnitModel.class)));
        assertThat(collection.size(), is(2));
    }

    @Test
    public void transformUnitModel() {
        UnitModel model = createFakeUnitModel();

        UnitViewModel viewModel = mapper.transformToViewModel(model);

        assertThat(viewModel, is(instanceOf(UnitViewModel.class)));
        assertThat(viewModel.getId(), is(FAKE_ID));
        assertThat(viewModel.getName(), is(FAKE_NAME));
        assertThat(viewModel.getShortName(), is(FAKE_SHORT_NAME));
    }

    @Test
    public void transformUnitModelCollection() {
        UnitModel mockModelOne = mock(UnitModel.class);
        UnitModel mockModelTwo = mock(UnitModel.class);

        List<UnitModel> list = new ArrayList<>(5);
        list.add(mockModelOne);
        list.add(mockModelTwo);

        Collection<UnitViewModel> collection = mapper.transformToViewModel(list);

        assertThat(collection.toArray()[0], is(instanceOf(UnitViewModel.class)));
        assertThat(collection.toArray()[1], is(instanceOf(UnitViewModel.class)));
        assertThat(collection.size(), is(2));
    }
}