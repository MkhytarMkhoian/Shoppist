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

import static com.justplay1.shoppist.ViewModelUtil.FAKE_ID;
import static com.justplay1.shoppist.ViewModelUtil.FAKE_NAME;
import static com.justplay1.shoppist.ViewModelUtil.createFakeCurrencyViewModel;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CurrencyViewModelTest {

    @Test
    public void currencyConstructor_HappyCase() {
        CurrencyViewModel viewModel = createFakeCurrencyViewModel();

        assertThat(viewModel.getId(), is(FAKE_ID));
        assertThat(viewModel.getName(), is(FAKE_NAME));
    }

    @Test
    public void currencyHashCode_HappyCase() {
        CurrencyViewModel viewModel = createFakeCurrencyViewModel();
        int hashCode = viewModel.hashCode();

        assertThat(hashCode, is(FAKE_ID.hashCode()));
    }

    @Test
    public void currencyEquals_HappyCase() {
        CurrencyViewModel x = createFakeCurrencyViewModel();
        CurrencyViewModel y = createFakeCurrencyViewModel();
        CurrencyViewModel z = createFakeCurrencyViewModel();

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