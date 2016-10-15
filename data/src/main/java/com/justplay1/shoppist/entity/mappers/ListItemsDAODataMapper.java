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

package com.justplay1.shoppist.entity.mappers;

import com.justplay1.shoppist.entity.ListItemDAO;
import com.justplay1.shoppist.models.ListItemModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhytar Mkhoian.
 */
@Singleton
public class ListItemsDAODataMapper {

    private final CategoryDAODataMapper categoryDAODataMapper;
    private final CurrencyDAODataMapper currencyDAODataMapper;
    private final UnitsDAODataMapper unitsDAODataMapper;

    @Inject
    public ListItemsDAODataMapper(CategoryDAODataMapper categoryDAODataMapper, CurrencyDAODataMapper currencyDAODataMapper, UnitsDAODataMapper unitsDAODataMapper) {
        this.categoryDAODataMapper = categoryDAODataMapper;
        this.currencyDAODataMapper = currencyDAODataMapper;
        this.unitsDAODataMapper = unitsDAODataMapper;
    }

    public ListItemModel transformFromDAO(ListItemDAO dao) {
        ListItemModel model = null;
        if (dao != null) {
            model = new ListItemModel(dao.getId(),
                    dao.getName(),
                    dao.getParentListId(),
                    dao.getNote(),
                    dao.getStatus(),
                    categoryDAODataMapper.transformFromDAO(dao.getCategory()),
                    dao.getPriority(),
                    dao.getPrice(),
                    dao.getQuantity(),
                    unitsDAODataMapper.transformFromDAO(dao.getUnit()),
                    dao.getTimeCreated(),
                    currencyDAODataMapper.transformFromDAO(dao.getCurrency()));
        }
        return model;
    }

    public List<ListItemModel> transformFromDAO(Collection<ListItemDAO> daos) {
        List<ListItemModel> models = new ArrayList<>();
        ListItemModel model;
        for (ListItemDAO dao : daos) {
            model = transformFromDAO(dao);
            if (model != null) {
                models.add(model);
            }
        }
        return models;
    }

    public ListItemDAO transformToDAO(ListItemModel model) {
        ListItemDAO dao = null;
        if (model != null) {
            dao = new ListItemDAO(model.getId(),
                    model.getName(),
                    model.getParentListId(),
                    model.getNote(),
                    model.getStatus(),
                    categoryDAODataMapper.transformToDAO(model.getCategory()),
                    model.getPriority(),
                    model.getPrice(),
                    model.getQuantity(),
                    unitsDAODataMapper.transformToDAO(model.getUnit()),
                    model.getTimeCreated(),
                    currencyDAODataMapper.transformToDAO(model.getCurrency()));
        }
        return dao;
    }

    public List<ListItemDAO> transformToDAO(Collection<ListItemModel> models) {
        List<ListItemDAO> daos = new ArrayList<>();
        ListItemDAO dao;
        for (ListItemModel model : models) {
            dao = transformToDAO(model);
            if (dao != null) {
                daos.add(dao);
            }
        }
        return daos;
    }
}
