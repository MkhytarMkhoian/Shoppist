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

import org.junit.Before;
import org.junit.Test;

import static com.justplay1.shoppist.TestUtil.FAKE_ID;
import static com.justplay1.shoppist.TestUtil.FAKE_NAME;
import static com.justplay1.shoppist.TestUtil.FAKE_NOTE;
import static com.justplay1.shoppist.TestUtil.FAKE_PARENT_LIST_ID;
import static com.justplay1.shoppist.TestUtil.FAKE_PRICE;
import static com.justplay1.shoppist.TestUtil.FAKE_PRIORITY;
import static com.justplay1.shoppist.TestUtil.FAKE_QUANTITY;
import static com.justplay1.shoppist.TestUtil.FAKE_STATUS;
import static com.justplay1.shoppist.TestUtil.FAKE_TIME_CREATED;
import static com.justplay1.shoppist.TestUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.TestUtil.createFakeCurrencyModel;
import static com.justplay1.shoppist.TestUtil.createFakeListItemModel;
import static com.justplay1.shoppist.TestUtil.createFakeUnitModel;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Mkhytar Mkhoian.
 */

public class ListItemModelTest {

    private ListItemModel model;
    private CategoryModel categoryModel;
    private UnitModel unitModel;
    private CurrencyModel currencyModel;

    @Before
    public void setUp() {
        currencyModel = createFakeCurrencyModel();
        unitModel = createFakeUnitModel();
        categoryModel = createFakeCategoryModel();

        model = createFakeListItemModel(categoryModel, unitModel, currencyModel);
    }

    @Test
    public void listItemConstructor_HappyCase() {
        String id = model.getId();
        String name = model.getName();
        String note = model.getNote();
        String parentListId = model.getParentListId();
        int priority = model.getPriority();
        boolean status = model.getStatus();
        long timeCreated = model.getTimeCreated();
        double price = model.getPrice();
        double quantity = model.getQuantity();
        CategoryModel category = model.getCategory();
        CurrencyModel currency = model.getCurrency();
        UnitModel unit = model.getUnit();

        assertThat(id, is(FAKE_ID));
        assertThat(note, is(FAKE_NOTE));
        assertThat(name, is(FAKE_NAME));
        assertThat(parentListId, is(FAKE_PARENT_LIST_ID));
        assertThat(priority, is(FAKE_PRIORITY));
        assertThat(timeCreated, is(FAKE_TIME_CREATED));
        assertThat(status, is(FAKE_STATUS));
        assertThat(price, is(FAKE_PRICE));
        assertThat(quantity, is(FAKE_QUANTITY));

        assertEquals(categoryModel, category);
        assertEquals(currencyModel, currency);
        assertEquals(unitModel, unit);
    }
}
