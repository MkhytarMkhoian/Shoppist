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

package com.justplay1.shoppist.models.mappers;

import com.justplay1.shoppist.di.scope.NonConfigurationScope;
import com.justplay1.shoppist.models.ListItemModel;
import com.justplay1.shoppist.models.ListItemViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class ListItemsModelDataMapper {

    private final CategoryModelDataMapper categoryModelDataMapper;
    private final CurrencyModelDataMapper currencyModelDataMapper;
    private final UnitsDataModelMapper unitsDataModelMapper;

    @Inject
    public ListItemsModelDataMapper(CategoryModelDataMapper categoryDataMapper, CurrencyModelDataMapper currencyDataMapper, UnitsDataModelMapper unitsDataMapper) {
        this.categoryModelDataMapper = categoryDataMapper;
        this.currencyModelDataMapper = currencyDataMapper;
        this.unitsDataModelMapper = unitsDataMapper;
    }

    @SuppressWarnings("ResourceType")
    public ListItemViewModel transformToViewModel(ListItemModel item) {
        ListItemViewModel itemModel = null;
        if (item != null) {
            itemModel = new ListItemViewModel();
            itemModel.setId(item.getId());
            itemModel.setName(item.getName());
            itemModel.setNote(item.getNote());
            itemModel.setParentListId(item.getParentListId());
            itemModel.setPrice(item.getPrice());
            itemModel.setPriority(item.getPriority());
            itemModel.setStatus(item.getStatus());
            itemModel.setCategory(categoryModelDataMapper.transformToViewModel(item.getCategory()));
            itemModel.setCurrency(currencyModelDataMapper.transformToViewModel(item.getCurrency()));
            itemModel.setUnit(unitsDataModelMapper.transformToViewModel(item.getUnit()));
            itemModel.setQuantity(item.getQuantity());
            itemModel.setTimeCreated(item.getTimeCreated());
        }
        return itemModel;
    }

    public List<ListItemViewModel> transformToViewModel(Collection<ListItemModel> listItems) {
        List<ListItemViewModel> itemModels = new ArrayList<>();
        ListItemViewModel itemModel;
        for (ListItemModel item : listItems) {
            itemModel = transformToViewModel(item);
            if (itemModel != null) {
                itemModels.add(itemModel);
            }
        }
        return itemModels;
    }

    public ListItemModel transform(ListItemViewModel itemModel) {
        ListItemModel item = null;
        if (itemModel != null) {
            item = new ListItemModel();
            item.setId(itemModel.getId());
            item.setName(itemModel.getName());
            item.setNote(itemModel.getNote());
            item.setParentListId(itemModel.getParentListId());
            item.setPrice(itemModel.getPrice());
            item.setPriority(itemModel.getPriority());
            item.setStatus(itemModel.getStatus());
            item.setCategory(categoryModelDataMapper.transform(itemModel.getCategory()));
            item.setCurrency(currencyModelDataMapper.transform(itemModel.getCurrency()));
            item.setUnit(unitsDataModelMapper.transform(itemModel.getUnit()));
            item.setQuantity(itemModel.getQuantity());
            item.setTimeCreated(itemModel.getTimeCreated());
        }
        return item;
    }

    public List<ListItemModel> transform(Collection<ListItemViewModel> itemModels) {
        List<ListItemModel> items = new ArrayList<>();
        ListItemModel item;
        for (ListItemViewModel itemModel : itemModels) {
            item = transform(itemModel);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }
}
