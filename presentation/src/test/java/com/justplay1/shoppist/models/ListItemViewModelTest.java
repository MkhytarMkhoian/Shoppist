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

package com.justplay1.shoppist.models;

import org.junit.Test;

import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_ID;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_NAME;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_NOTE;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_PARENT_LIST_ID;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_PRICE;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_PRIORITY;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_QUANTITY;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_STATUS;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_TIME_CREATED;
import static com.justplay1.shoppist.presenter.ViewModelUtil.createFakeCategoryViewModel;
import static com.justplay1.shoppist.presenter.ViewModelUtil.createFakeCurrencyViewModel;
import static com.justplay1.shoppist.presenter.ViewModelUtil.createFakeListItemViewModel;
import static com.justplay1.shoppist.presenter.ViewModelUtil.createFakeUnitViewModel;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListItemViewModelTest {

    @Test
    public void listItemConstructor_HappyCase() {
        CurrencyViewModel currencyViewModel = createFakeCurrencyViewModel();
        UnitViewModel unitViewModel = createFakeUnitViewModel();
        CategoryViewModel categoryViewModel = createFakeCategoryViewModel();
        ListItemViewModel listItemViewModel = createFakeListItemViewModel(categoryViewModel, unitViewModel, currencyViewModel);

        assertThat(listItemViewModel.getId(), is(FAKE_ID));
        assertThat(listItemViewModel.getNote(), is(FAKE_NOTE));
        assertThat(listItemViewModel.getName(), is(FAKE_NAME));
        assertThat(listItemViewModel.getParentListId(), is(FAKE_PARENT_LIST_ID));
        assertThat(listItemViewModel.getPriority(), is(FAKE_PRIORITY));
        assertThat(listItemViewModel.getTimeCreated(), is(FAKE_TIME_CREATED));
        assertThat(listItemViewModel.getStatus(), is(FAKE_STATUS));
        assertThat(listItemViewModel.getPrice(), is(FAKE_PRICE));
        assertThat(listItemViewModel.getQuantity(), is(FAKE_QUANTITY));

        assertEquals(categoryViewModel, listItemViewModel.getCategory());
        assertEquals(currencyViewModel, listItemViewModel.getCurrency());
        assertEquals(unitViewModel, listItemViewModel.getUnit());
    }

    @Test
    public void listItemHashCode_HappyCase() {
        ListItemViewModel model = createFakeListItemViewModel(createFakeCategoryViewModel(), createFakeUnitViewModel(), createFakeCurrencyViewModel());
        int hashCode = model.hashCode();

        assertThat(hashCode, is(FAKE_ID.hashCode()));
    }

    @Test
    public void listItemEquals_HappyCase() {
        ListItemViewModel x = createFakeListItemViewModel(createFakeCategoryViewModel(), createFakeUnitViewModel(), createFakeCurrencyViewModel());
        ListItemViewModel y = createFakeListItemViewModel(createFakeCategoryViewModel(), createFakeUnitViewModel(), createFakeCurrencyViewModel());
        ListItemViewModel z = createFakeListItemViewModel(createFakeCategoryViewModel(), createFakeUnitViewModel(), createFakeCurrencyViewModel());

        // reflection rule
        assertEquals(x, x);

        // symmetry rule
        assertEquals(x, y);
        assertEquals(y, x);

        // transitivity rule
        assertEquals(x, y);
        assertEquals(y, z);
        assertEquals(x, z);

        assertNotEquals(x, null);
    }
}