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

package com.justplay1.shoppist.features.search;

import android.support.annotation.ColorInt;

import com.justplay1.shoppist.models.ProductViewModel;

import com.justplay1.shoppist.shared.view.BaseMvpView;
import java.util.Map;

/**
 * Created by Mkhytar Mkhoian.
 */
public interface SearchView extends BaseMvpView {

    String JUST_NAME = "just_name";

    void fadeInSignal(@ColorInt int color);

    void closeSearch();

    void showEditGoodsDialog(String name);

    void showEditGoodsDialog(ProductViewModel product);

    void showData(Map<String, ProductViewModel> data);
}
