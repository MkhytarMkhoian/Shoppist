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

import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_COLOR;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_CREATE_BY_USER;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_ID;
import static com.justplay1.shoppist.presenter.ViewModelUtil.FAKE_NAME;
import static com.justplay1.shoppist.presenter.ViewModelUtil.createFakeCategoryViewModel;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CategoryViewModelTest {

    @Test
    public void categoryConstructor_HappyCase() {
        CategoryViewModel viewModel = createFakeCategoryViewModel();

        assertThat(viewModel.getId(), is(FAKE_ID));
        assertThat(viewModel.getColor(), is(FAKE_COLOR));
        assertThat(viewModel.getName(), is(FAKE_NAME));
        assertThat(viewModel.isCreateByUser(), is(FAKE_CREATE_BY_USER));
    }

    @Test
    public void categoryHashCode_HappyCase() {
        CategoryViewModel viewModel = createFakeCategoryViewModel();
        int hashCode = viewModel.hashCode();

        assertThat(hashCode, is(FAKE_ID.hashCode()));
    }

    @Test
    public void categoryEquals_HappyCase() {
        CategoryViewModel x = createFakeCategoryViewModel();
        CategoryViewModel y = createFakeCategoryViewModel();
        CategoryViewModel z = createFakeCategoryViewModel();

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