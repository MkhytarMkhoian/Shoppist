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
import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.models.CategoryViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class CategoryViewModelMapper {

    @Inject
    public CategoryViewModelMapper() {
    }

    public CategoryViewModel transformToViewModel(CategoryModel model) {
        CategoryViewModel viewModel = null;
        if (model != null) {
            viewModel = new CategoryViewModel();
            viewModel.setId(model.getId());
            viewModel.setName(model.getName());
            viewModel.setColor(model.getColor());
            viewModel.setCreateByUser(model.isCreateByUser());
        }
        return viewModel;
    }

    public List<CategoryViewModel> transformToViewModel(Collection<CategoryModel> models) {
        List<CategoryViewModel> viewModels = new ArrayList<>();
        CategoryViewModel viewModel;
        for (CategoryModel model : models) {
            viewModel = transformToViewModel(model);
            if (viewModel != null) {
                viewModels.add(viewModel);
            }
        }
        return viewModels;
    }

    public CategoryModel transform(CategoryViewModel viewModel) {
        CategoryModel model = null;
        if (viewModel != null) {
            model = new CategoryModel(viewModel.getId(),
                    viewModel.getName(),
                    viewModel.getColor(),
                    viewModel.isCreateByUser());
        }
        return model;
    }

    public List<CategoryModel> transform(Collection<CategoryViewModel> viewModels) {
        List<CategoryModel> models = new ArrayList<>();
        CategoryModel model;
        for (CategoryViewModel viewModel : viewModels) {
            model = transform(viewModel);
            if (model != null) {
                models.add(model);
            }
        }
        return models;
    }
}
