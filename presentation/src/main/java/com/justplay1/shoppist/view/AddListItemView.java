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

package com.justplay1.shoppist.view;

import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.UnitViewModel;

import java.util.List;
import java.util.Map;

/**
 * Created by Mkhytar Mkhoian.
 */
public interface AddListItemView extends AddElementView {

    void selectPriority(@Priority int priority);

    void setPrice(String price);

    void setNote(String note);

    void setQuantity(String quantity);

    void selectCategory(String id);

    void setCategory(List<CategoryViewModel> category);

    void selectUnit(String id);

    void setUnits(List<UnitViewModel> unit);

    void selectCurrency(String id);

    void setCurrency(List<CurrencyViewModel> currency);

    void setGoods(Map<String, ProductViewModel> goods);

    void setDefaultCategory();

    void setDefaultUnit();

    void setDefaultCurrency();
}
