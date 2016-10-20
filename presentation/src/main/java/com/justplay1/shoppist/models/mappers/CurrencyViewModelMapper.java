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
import com.justplay1.shoppist.models.CurrencyModel;
import com.justplay1.shoppist.models.CurrencyViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class CurrencyViewModelMapper {

    @Inject
    public CurrencyViewModelMapper() {
    }

    public CurrencyViewModel transformToViewModel(CurrencyModel model) {
        CurrencyViewModel viewModel = null;
        if (model != null) {
            viewModel = new CurrencyViewModel();
            viewModel.setId(model.getId());
            viewModel.setName(model.getName());
        }
        return viewModel;
    }

    public List<CurrencyViewModel> transformToViewModel(Collection<CurrencyModel> models) {
        List<CurrencyViewModel> viewModels = new ArrayList<>();
        CurrencyViewModel viewModel;
        for (CurrencyModel model : models) {
            viewModel = transformToViewModel(model);
            if (viewModel != null) {
                viewModels.add(viewModel);
            }
        }
        return viewModels;
    }

    public CurrencyModel transform(CurrencyViewModel viewModel) {
        CurrencyModel model = null;
        if (viewModel != null) {
            model = new CurrencyModel(viewModel.getId(), viewModel.getName());
        }
        return model;
    }

    public List<CurrencyModel> transform(Collection<CurrencyViewModel> viewModels) {
        List<CurrencyModel> models = new ArrayList<>();
        CurrencyModel model;
        for (CurrencyViewModel viewModel : viewModels) {
            model = transform(viewModel);
            if (model != null) {
                models.add(model);
            }
        }
        return models;
    }
}
