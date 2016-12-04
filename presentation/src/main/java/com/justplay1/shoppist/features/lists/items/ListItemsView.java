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

package com.justplay1.shoppist.features.lists.items;

import android.support.v4.util.Pair;

import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.ListViewModel;

import com.justplay1.shoppist.shared.view.LoadDataView;
import com.justplay1.shoppist.shared.view.StringView;
import java.util.List;

/**
 * Created by Mkhytar Mkhoian.
 */
public interface ListItemsView extends LoadDataView, StringView {

    void showData(List<Pair<HeaderViewModel, List<ListItemViewModel>>> data);

    void showEmailShareDialog(String listName);

    void openMoveGoodsDialog(ListViewModel currentList, List<ListItemViewModel> data);

    void openCopyGoodsDialog(ListViewModel currentList, List<ListItemViewModel> data);
}
