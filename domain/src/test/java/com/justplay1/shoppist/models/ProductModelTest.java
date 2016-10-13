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

import static com.justplay1.shoppist.TestUtil.FAKE_CREATE_BY_USER;
import static com.justplay1.shoppist.TestUtil.FAKE_ID;
import static com.justplay1.shoppist.TestUtil.FAKE_NAME;
import static com.justplay1.shoppist.TestUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.TestUtil.createFakeProductModel;
import static com.justplay1.shoppist.TestUtil.createFakeUnitModel;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Mkhytar Mkhoian.
 */

public class ProductModelTest {

    @Test
    public void productConstructor_HappyCase() {
        UnitModel unitModel = createFakeUnitModel();
        CategoryModel categoryModel = createFakeCategoryModel();
        ProductModel model = createFakeProductModel(createFakeUnitModel(), createFakeCategoryModel());

        String id = model.getId();
        String name = model.getName();
        boolean isCreateByUser = model.isCreateByUser();
        CategoryModel category = model.getCategory();
        UnitModel unit = model.getUnit();

        assertThat(id, is(FAKE_ID));
        assertThat(name, is(FAKE_NAME));
        assertThat(isCreateByUser, is(FAKE_CREATE_BY_USER));

        assertEquals(categoryModel, category);
        assertEquals(unitModel, unit);
    }

    @Test
    public void productHashCode_HappyCase() {
        ProductModel model = createFakeProductModel(createFakeUnitModel(), createFakeCategoryModel());
        int hashCode = model.hashCode();

        assertThat(hashCode, is(FAKE_ID.hashCode()));
    }

    @Test
    public void productEquals_HappyCase() {
        ProductModel x = createFakeProductModel(createFakeUnitModel(), createFakeCategoryModel());
        ProductModel y = createFakeProductModel(createFakeUnitModel(), createFakeCategoryModel());
        ProductModel z = createFakeProductModel(createFakeUnitModel(), createFakeCategoryModel());

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
