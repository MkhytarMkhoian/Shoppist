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
    public ListItemViewModel transformToViewModel(ListItemModel model) {
        ListItemViewModel viewModel = null;
        if (model != null) {
            viewModel = new ListItemViewModel();
            viewModel.setId(model.getId());
            viewModel.setName(model.getName());
            viewModel.setNote(model.getNote());
            viewModel.setParentListId(model.getParentListId());
            viewModel.setPrice(model.getPrice());
            viewModel.setPriority(model.getPriority());
            viewModel.setStatus(model.getStatus());
            viewModel.setCategory(categoryModelDataMapper.transformToViewModel(model.getCategory()));
            viewModel.setCurrency(currencyModelDataMapper.transformToViewModel(model.getCurrency()));
            viewModel.setUnit(unitsDataModelMapper.transformToViewModel(model.getUnit()));
            viewModel.setQuantity(model.getQuantity());
            viewModel.setTimeCreated(model.getTimeCreated());
        }
        return viewModel;
    }

    public List<ListItemViewModel> transformToViewModel(Collection<ListItemModel> models) {
        List<ListItemViewModel> viewModels = new ArrayList<>();
        ListItemViewModel viewModel;
        for (ListItemModel item : models) {
            viewModel = transformToViewModel(item);
            if (viewModel != null) {
                viewModels.add(viewModel);
            }
        }
        return viewModels;
    }

    public ListItemModel transform(ListItemViewModel viewModel) {
        ListItemModel model = null;
        if (viewModel != null) {
            model = new ListItemModel(viewModel.getId(),
                    viewModel.getName(),
                    viewModel.getParentListId(),
                    viewModel.getNote(),
                    viewModel.getStatus(),
                    categoryModelDataMapper.transform(viewModel.getCategory()),
                    viewModel.getPriority(),
                    viewModel.getPrice(),
                    viewModel.getQuantity(),
                    unitsDataModelMapper.transform(viewModel.getUnit()),
                    viewModel.getTimeCreated(),
                    currencyModelDataMapper.transform(viewModel.getCurrency()));
        }
        return model;
    }

    public List<ListItemModel> transform(Collection<ListItemViewModel> viewModels) {
        List<ListItemModel> models = new ArrayList<>();
        ListItemModel model;
        for (ListItemViewModel viewModel : viewModels) {
            model = transform(viewModel);
            if (model != null) {
                models.add(model);
            }
        }
        return models;
    }
}
