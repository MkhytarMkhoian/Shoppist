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
import com.justplay1.shoppist.models.UnitViewModel;

import java.util.List;

/**
 * Created by Mkhytar Mkhoian.
 */
public interface AddGoodsView extends LoadDataView {

    void setViewName(String name);

    void setDefaultUpdateTitle();

    void setDefaultNewTitle();

    void closeDialog();

    void showNameIsRequiredError();

    void onComplete(boolean isUpdate);

    void showUnitDialog(UnitViewModel editUnit);

    void selectCategory(String id);

    void setCategory(List<CategoryViewModel> category);

    void selectUnit(String id);

    void setUnits(List<UnitViewModel> unit);
}
