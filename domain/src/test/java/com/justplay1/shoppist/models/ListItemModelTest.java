/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.justplay1.shoppist.models;

import org.junit.Test;

import static com.justplay1.shoppist.ModelUtil.FAKE_ID;
import static com.justplay1.shoppist.ModelUtil.FAKE_NAME;
import static com.justplay1.shoppist.ModelUtil.FAKE_NOTE;
import static com.justplay1.shoppist.ModelUtil.FAKE_PARENT_LIST_ID;
import static com.justplay1.shoppist.ModelUtil.FAKE_PRICE;
import static com.justplay1.shoppist.ModelUtil.FAKE_PRIORITY;
import static com.justplay1.shoppist.ModelUtil.FAKE_QUANTITY;
import static com.justplay1.shoppist.ModelUtil.FAKE_STATUS;
import static com.justplay1.shoppist.ModelUtil.FAKE_TIME_CREATED;
import static com.justplay1.shoppist.ModelUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.ModelUtil.createFakeCurrencyModel;
import static com.justplay1.shoppist.ModelUtil.createFakeListItemModel;
import static com.justplay1.shoppist.ModelUtil.createFakeUnitModel;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Mkhytar Mkhoian.
 */

public class ListItemModelTest {

    @Test
    public void listItemConstructor_HappyCase() {
        CurrencyModel currencyModel = createFakeCurrencyModel();
        UnitModel unitModel = createFakeUnitModel();
        CategoryModel categoryModel = createFakeCategoryModel();
        ListItemModel model = createFakeListItemModel(categoryModel, unitModel, currencyModel);

        assertThat(model.getId(), is(FAKE_ID));
        assertThat(model.getNote(), is(FAKE_NOTE));
        assertThat(model.getName(), is(FAKE_NAME));
        assertThat(model.getParentListId(), is(FAKE_PARENT_LIST_ID));
        assertThat(model.getPriority(), is(FAKE_PRIORITY));
        assertThat(model.getTimeCreated(), is(FAKE_TIME_CREATED));
        assertThat(model.getStatus(), is(FAKE_STATUS));
        assertThat(model.getPrice(), is(FAKE_PRICE));
        assertThat(model.getQuantity(), is(FAKE_QUANTITY));

        assertEquals(categoryModel, model.getCategory());
        assertEquals(currencyModel, model.getCurrency());
        assertEquals(unitModel, model.getUnit());
    }

    @Test
    public void listItemHashCode_HappyCase() {
        ListItemModel model = createFakeListItemModel(createFakeCategoryModel(), createFakeUnitModel(), createFakeCurrencyModel());
        int hashCode = model.hashCode();

        assertThat(hashCode, is(FAKE_ID.hashCode() + FAKE_PARENT_LIST_ID.hashCode()));
    }

    @Test
    public void listItemEquals_HappyCase() {
        ListItemModel x = createFakeListItemModel(createFakeCategoryModel(), createFakeUnitModel(), createFakeCurrencyModel());
        ListItemModel y = createFakeListItemModel(createFakeCategoryModel(), createFakeUnitModel(), createFakeCurrencyModel());
        ListItemModel z = createFakeListItemModel(createFakeCategoryModel(), createFakeUnitModel(), createFakeCurrencyModel());

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
