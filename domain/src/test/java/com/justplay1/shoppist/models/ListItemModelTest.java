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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Mkhytar Mkhoian.
 */

public class ListItemModelTest {

    private static final String FAKE_ID = "id";
    private static final String FAKE_PARENT_LIST_ID = "parent_list_id";
    private static final String FAKE_NAME = "name";
    private static final double FAKE_PRICE = 5.56;
    private static final double FAKE_QUANTITY = 2.333;
    private static final String FAKE_NOTE = "note";
    private static final int FAKE_PRIORITY = 2;
    private static final long FAKE_TIME_CREATED = 55555L;
    private static final boolean FAKE_STATUS = true;

    private ListItemModel model;
    private CategoryModel categoryModel;
    private UnitModel unitModel;
    private CurrencyModel currencyModel;

    @Before
    public void setUp() {
        currencyModel = new CurrencyModel();
        unitModel = new UnitModel();
        categoryModel = new CategoryModel();

        model = new ListItemModel();
        model.setId(FAKE_ID);
        model.setNote(FAKE_NOTE);
        model.setPrice(FAKE_PRICE);
        model.setName(FAKE_NAME);
        model.setPriority(FAKE_PRIORITY);
        model.setTimeCreated(FAKE_TIME_CREATED);
        model.setQuantity(FAKE_QUANTITY);
        model.setParentListId(FAKE_PARENT_LIST_ID);
        model.setStatus(FAKE_STATUS);
        model.setUnit(unitModel);
        model.setCurrency(currencyModel);
        model.setCategory(categoryModel);
    }

    @Test
    public void testUserConstructorHappyCase() {
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
