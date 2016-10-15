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

import com.justplay1.shoppist.entity.UnitDAO;
import com.justplay1.shoppist.models.UnitModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhytar Mkhoian.
 */
@Singleton
public class UnitsDAODataMapper {

    @Inject
    public UnitsDAODataMapper() {
    }

    public UnitModel transformFromDAO(UnitDAO dao) {
        UnitModel model = null;
        if (dao != null) {
            model = new UnitModel(dao.getId(),
                    dao.getName(),
                    dao.getShortName());
        }
        return model;
    }

    public List<UnitModel> transformFromDAO(Collection<UnitDAO> daos) {
        List<UnitModel> models = new ArrayList<>();
        UnitModel model;
        for (UnitDAO dao : daos) {
            model = transformFromDAO(dao);
            if (model != null) {
                models.add(model);
            }
        }
        return models;
    }

    public UnitDAO transformToDAO(UnitModel model) {
        UnitDAO dao = null;
        if (model != null) {
            dao = new UnitDAO(model.getId(),
                    model.getName(),
                    model.getShortName());
        }
        return dao;
    }

    public List<UnitDAO> transformToDAO(Collection<UnitModel> models) {
        List<UnitDAO> daos = new ArrayList<>();
        UnitDAO dao;
        for (UnitModel model : models) {
            dao = transformToDAO(model);
            if (dao != null) {
                daos.add(dao);
            }
        }
        return daos;
    }
}
