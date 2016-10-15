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
import com.justplay1.shoppist.models.ProductModel;
import com.justplay1.shoppist.models.ProductViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class GoodsModelDataMapper {

    private final CategoryModelDataMapper categoryModelDataMapper;
    private final UnitsDataModelMapper unitsDataModelMapper;

    @Inject
    public GoodsModelDataMapper(UnitsDataModelMapper unitsDataMapper, CategoryModelDataMapper categoryDataMapper) {
        this.unitsDataModelMapper = unitsDataMapper;
        this.categoryModelDataMapper = categoryDataMapper;
    }

    public ProductViewModel transformToViewModel(ProductModel model) {
        ProductViewModel viewModel = null;
        if (model != null) {
            viewModel = new ProductViewModel();
            viewModel.setId(model.getId());
            viewModel.setName(model.getName());
            viewModel.setCategory(categoryModelDataMapper.transformToViewModel(model.getCategory()));
            viewModel.setUnit(unitsDataModelMapper.transformToViewModel(model.getUnit()));
            viewModel.setTimeCreated(model.getTimeCreated());
            viewModel.setCreateByUser(model.isCreateByUser());
        }
        return viewModel;
    }

    public List<ProductViewModel> transformToViewModel(Collection<ProductModel> models) {
        List<ProductViewModel> viewModels = new ArrayList<>();
        ProductViewModel viewModel;
        for (ProductModel model : models) {
            viewModel = transformToViewModel(model);
            if (viewModel != null) {
                viewModels.add(viewModel);
            }
        }
        return viewModels;
    }

    public ProductModel transform(ProductViewModel viewModel) {
        ProductModel model = null;
        if (viewModel != null) {
            model = new ProductModel(viewModel.getId(),
                    viewModel.getName(),
                    categoryModelDataMapper.transform(viewModel.getCategory()),
                    viewModel.isCreateByUser(),
                    viewModel.getTimeCreated(),
                    unitsDataModelMapper.transform(viewModel.getUnit()));
        }
        return model;
    }

    public List<ProductModel> transform(Collection<ProductViewModel> viewModels) {
        List<ProductModel> models = new ArrayList<>();
        ProductModel model;
        for (ProductViewModel viewModel : viewModels) {
            model = transform(viewModel);
            if (model != null) {
                models.add(model);
            }
        }
        return models;
    }
}
