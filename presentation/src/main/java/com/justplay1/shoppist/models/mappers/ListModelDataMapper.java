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
import com.justplay1.shoppist.models.ListModel;
import com.justplay1.shoppist.models.ListViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class ListModelDataMapper {

    @Inject
    public ListModelDataMapper() {
    }

    @SuppressWarnings("ResourceType")
    public ListViewModel transformToViewModel(ListModel model) {
        ListViewModel viewModel = null;
        if (model != null) {
            viewModel = new ListViewModel();
            viewModel.setId(model.getId());
            viewModel.setName(model.getName());
            viewModel.setTimeCreated(model.getTimeCreated());
            viewModel.setPriority(model.getPriority());
            viewModel.setBoughtCount(model.getBoughtCount());
            viewModel.setColor(model.getColor());
            viewModel.setSize(model.getSize());
        }
        return viewModel;
    }

    public List<ListViewModel> transformToViewModel(Collection<ListModel> models) {
        List<ListViewModel> viewModels = new ArrayList<>();
        ListViewModel viewModel;
        for (ListModel model : models) {
            viewModel = transformToViewModel(model);
            if (viewModel != null) {
                viewModels.add(viewModel);
            }
        }
        return viewModels;
    }

    public ListModel transform(ListViewModel viewModel) {
        ListModel model = null;
        if (viewModel != null) {
            model = new ListModel(viewModel.getId(),
                    viewModel.getName(),
                    viewModel.getBoughtCount(),
                    viewModel.getTimeCreated(),
                    viewModel.getPriority(),
                    viewModel.getColor(),
                    viewModel.getSize());
        }
        return model;
    }

    public List<ListModel> transform(Collection<ListViewModel> viewModels) {
        List<ListModel> models = new ArrayList<>();
        ListModel model;
        for (ListViewModel viewModel : viewModels) {
            model = transform(viewModel);
            if (model != null) {
                models.add(model);
            }
        }
        return models;
    }
}
