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

import android.support.v4.util.Pair;

import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.UnitViewModel;

import java.util.List;

/**
 * Created by Mkhytar Mkhoian.
 */
public interface GoodsView extends LoadDataView, ContextView {

    void showData(List<Pair<HeaderViewModel, List<ProductViewModel>>> data);

    void showChangeUnitDialog(List<ProductViewModel> editProducts);

    void showChangeCategoryDialog(List<ProductViewModel> editProducts);

    void showEditGoodsDialog(ProductViewModel editProduct);

    void showDeleteDialog();

    void openSearchScreen();
}
