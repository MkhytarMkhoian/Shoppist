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

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.models.ListModel;
import com.justplay1.shoppist.models.ListViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
@PerActivity
public class ListModelDataMapper {

    @Inject
    public ListModelDataMapper() {
    }

    @SuppressWarnings("ResourceType")
    public ListViewModel transformToViewModel(ListModel list) {
        ListViewModel listModel = null;
        if (list != null) {
            listModel = new ListViewModel();
            listModel.setId(list.getId());
            listModel.setName(list.getName());
            listModel.setTimeCreated(list.getTimeCreated());
            listModel.setPriority(list.getPriority());
            listModel.setBoughtCount(list.getBoughtCount());
            listModel.setColor(list.getColor());
            listModel.setSize(list.getSize());
        }
        return listModel;
    }

    public List<ListViewModel> transformToViewModel(Collection<ListModel> lists) {
        List<ListViewModel> listModels = new ArrayList<>();
        ListViewModel listModel;
        for (ListModel list : lists) {
            listModel = transformToViewModel(list);
            if (listModel != null) {
                listModels.add(listModel);
            }
        }
        return listModels;
    }

    public ListModel transform(ListViewModel listModel) {
        ListModel list = null;
        if (listModel != null) {
            list = new ListModel();
            list.setId(listModel.getId());
            list.setName(listModel.getName());
            list.setTimeCreated(listModel.getTimeCreated());
            list.setPriority(listModel.getPriority());
            list.setBoughtCount(listModel.getBoughtCount());
            list.setColor(listModel.getColor());
            list.setSize(listModel.getSize());
        }
        return list;
    }

    public List<ListModel> transform(Collection<ListViewModel> listModels) {
        List<ListModel> lists = new ArrayList<>();
        ListModel list;
        for (ListViewModel listModel : listModels) {
            list = transform(listModel);
            if (list != null) {
                lists.add(list);
            }
        }
        return lists;
    }
}
