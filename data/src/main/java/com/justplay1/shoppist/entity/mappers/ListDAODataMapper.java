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

import com.justplay1.shoppist.entity.ListDAO;
import com.justplay1.shoppist.models.ListModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhytar Mkhoian.
 */
@Singleton
public class ListDAODataMapper {

    @Inject
    public ListDAODataMapper() {
    }

    public ListModel transformFromDAO(ListDAO dao) {
        ListModel model = null;
        if (dao != null) {
            model = new ListModel(dao.getId(),
                    dao.getName(),
                    dao.getBoughtCount(),
                    dao.getTimeCreated(),
                    dao.getPriority(),
                    dao.getColor(),
                    dao.getSize());
        }
        return model;
    }

    public List<ListModel> transformFromDAO(Collection<ListDAO> daos) {
        List<ListModel> models = new ArrayList<>();
        ListModel model;
        for (ListDAO dao : daos) {
            model = transformFromDAO(dao);
            if (model != null) {
                models.add(model);
            }
        }
        return models;
    }

    public ListDAO transformToDAO(ListModel model) {
        ListDAO dao = null;
        if (model != null) {
            dao = new ListDAO(model.getId(),
                    model.getName(),
                    model.getBoughtCount(),
                    model.getTimeCreated(),
                    model.getPriority(),
                    model.getColor(),
                    model.getSize());
        }
        return dao;
    }

    public List<ListDAO> transformToDAO(Collection<ListModel> models) {
        List<ListDAO> daos = new ArrayList<>();
        ListDAO dao;
        for (ListModel model : models) {
            dao = transformToDAO(model);
            if (dao != null) {
                daos.add(dao);
            }
        }
        return daos;
    }
}
