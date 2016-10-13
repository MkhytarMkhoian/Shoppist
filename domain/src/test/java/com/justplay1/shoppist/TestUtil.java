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

package com.justplay1.shoppist;

import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.models.CurrencyModel;
import com.justplay1.shoppist.models.ListItemModel;
import com.justplay1.shoppist.models.ListModel;
import com.justplay1.shoppist.models.ProductModel;
import com.justplay1.shoppist.models.UnitModel;

import java.util.Collections;
import java.util.List;

/**
 * Created by Mkhytar Mkhoian.
 */

public class TestUtil {

    public static final String FAKE_ID = "id";
    public static final String FAKE_PARENT_LIST_ID = "parent_list_id";
    public static final String FAKE_NAME = "name";
    public static final double FAKE_PRICE = 5.56;
    public static final double FAKE_QUANTITY = 2.333;
    public static final String FAKE_NOTE = "note";
    public static final int FAKE_PRIORITY = 2;
    public static final long FAKE_TIME_CREATED = 55555L;
    public static final boolean FAKE_STATUS = true;

    public static final int FAKE_COLOR = 22222;
    public static final boolean FAKE_CREATE_BY_USER = true;

    public static final String FAKE_SHORT_NAME = "short_name";

    public static final int FAKE_BOUGHT_COUNT = 5;
    public static final int FAKE_SIZE = 10;

    public static List<ListItemModel> createFakeListItemModelList(CategoryModel categoryModel,
                                                                  UnitModel unitModel,
                                                                  CurrencyModel currencyModel) {
        return Collections.singletonList(createFakeListItemModel(categoryModel, unitModel, currencyModel));
    }

    public static ListItemModel createFakeListItemModel(CategoryModel categoryModel,
                                                        UnitModel unitModel,
                                                        CurrencyModel currencyModel) {
        ListItemModel model = new ListItemModel();
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
        return model;
    }

    public static CategoryModel createFakeCategoryModel() {
        CategoryModel model = new CategoryModel();
        model.setId(FAKE_ID);
        model.setColor(FAKE_COLOR);
        model.setCreateByUser(FAKE_CREATE_BY_USER);
        model.setName(FAKE_NAME);
        return model;
    }

    public static CurrencyModel createFakeCurrencyModel() {
        CurrencyModel model = new CurrencyModel();
        model.setId(FAKE_ID);
        model.setName(FAKE_NAME);
        return model;
    }

    public static ListModel createFakeListModel() {
        ListModel model = new ListModel();
        model.setId(FAKE_ID);
        model.setColor(FAKE_COLOR);
        model.setBoughtCount(FAKE_BOUGHT_COUNT);
        model.setName(FAKE_NAME);
        model.setPriority(FAKE_PRIORITY);
        model.setTimeCreated(FAKE_TIME_CREATED);
        model.setSize(FAKE_SIZE);
        return model;
    }

    public static UnitModel createFakeUnitModel() {
        UnitModel model = new UnitModel();
        model.setId(FAKE_ID);
        model.setName(FAKE_NAME);
        model.setShortName(FAKE_SHORT_NAME);
        return model;
    }

    public static ProductModel createFakeProductModel(UnitModel unitModel, CategoryModel categoryModel) {
        ProductModel model = new ProductModel();
        model.setId(FAKE_ID);
        model.setCreateByUser(FAKE_CREATE_BY_USER);
        model.setName(FAKE_NAME);
        model.setCategory(categoryModel);
        model.setUnit(unitModel);
        return model;
    }


}
