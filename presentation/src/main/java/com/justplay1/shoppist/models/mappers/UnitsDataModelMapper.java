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
import com.justplay1.shoppist.models.UnitModel;
import com.justplay1.shoppist.models.UnitViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class UnitsDataModelMapper {

    @Inject
    public UnitsDataModelMapper() {
    }

    public UnitViewModel transformToViewModel(UnitModel model) {
        UnitViewModel viewModel = null;
        if (model != null) {
            viewModel = new UnitViewModel();
            viewModel.setId(model.getId());
            viewModel.setName(model.getName());
            viewModel.setShortName(model.getShortName());
        }
        return viewModel;
    }

    public List<UnitViewModel> transformToViewModel(Collection<UnitModel> models) {
        List<UnitViewModel> viewModels = new ArrayList<>();
        UnitViewModel viewModel;
        for (UnitModel model : models) {
            viewModel = transformToViewModel(model);
            if (viewModel != null) {
                viewModels.add(viewModel);
            }
        }
        return viewModels;
    }

    public UnitModel transform(UnitViewModel viewModel) {
        UnitModel model = null;
        if (viewModel != null) {
            model = new UnitModel(model.getId(),
                    model.getName(),
                    model.getShortName());
        }
        return model;
    }

    public List<UnitModel> transform(Collection<UnitViewModel> viewModels) {
        List<UnitModel> models = new ArrayList<>();
        UnitModel model;
        for (UnitViewModel viewModel : viewModels) {
            model = transform(viewModel);
            if (model != null) {
                models.add(model);
            }
        }
        return models;
    }

}
