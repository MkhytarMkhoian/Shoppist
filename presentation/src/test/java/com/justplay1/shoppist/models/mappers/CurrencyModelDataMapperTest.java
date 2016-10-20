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

import com.justplay1.shoppist.models.CurrencyModel;
import com.justplay1.shoppist.models.CurrencyViewModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_ID;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_NAME;
import static com.justplay1.shoppist.presenter.ViewModelUtil.createFakeCurrencyModel;
import static com.justplay1.shoppist.presenter.ViewModelUtil.createFakeCurrencyViewModel;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CurrencyModelDataMapperTest {

    private CurrencyViewModelMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new CurrencyViewModelMapper();
    }

    @Test
    public void transformCurrencyViewModel() {
        CurrencyViewModel viewModel = createFakeCurrencyViewModel();

        CurrencyModel model = mapper.transform(viewModel);

        assertThat(model, is(instanceOf(CurrencyModel.class)));
        assertThat(model.getId(), is(FAKE_ID));
        assertThat(model.getName(), is(FAKE_NAME));
    }

    @Test
    public void transformCurrencyViewModelCollection() {
        CurrencyViewModel mockViewModelOne = mock(CurrencyViewModel.class);
        CurrencyViewModel mockViewModelTwo = mock(CurrencyViewModel.class);

        List<CurrencyViewModel> list = new ArrayList<>(5);
        list.add(mockViewModelOne);
        list.add(mockViewModelTwo);

        Collection<CurrencyModel> collection = mapper.transform(list);

        assertThat(collection.toArray()[0], is(instanceOf(CurrencyModel.class)));
        assertThat(collection.toArray()[1], is(instanceOf(CurrencyModel.class)));
        assertThat(collection.size(), is(2));
    }

    @Test
    public void transformCurrencyModel() {
        CurrencyModel model = createFakeCurrencyModel();

        CurrencyViewModel viewModel = mapper.transformToViewModel(model);

        assertThat(viewModel, is(instanceOf(CurrencyViewModel.class)));
        assertThat(viewModel.getId(), is(FAKE_ID));
        assertThat(viewModel.getName(), is(FAKE_NAME));
    }

    @Test
    public void transformCurrencyModelCollection() {
        CurrencyModel mockModelOne = mock(CurrencyModel.class);
        CurrencyModel mockModelTwo = mock(CurrencyModel.class);

        List<CurrencyModel> list = new ArrayList<>(5);
        list.add(mockModelOne);
        list.add(mockModelTwo);

        Collection<CurrencyViewModel> collection = mapper.transformToViewModel(list);

        assertThat(collection.toArray()[0], is(instanceOf(CurrencyViewModel.class)));
        assertThat(collection.toArray()[1], is(instanceOf(CurrencyViewModel.class)));
        assertThat(collection.size(), is(2));
    }
}