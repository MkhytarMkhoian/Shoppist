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

import com.justplay1.shoppist.entity.CurrencyDAO;
import com.justplay1.shoppist.models.CurrencyModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhytar Mkhoian.
 */
@Singleton
public class CurrencyDAODataMapper {

    @Inject
    public CurrencyDAODataMapper() {
    }

    public CurrencyModel transformFromDAO(CurrencyDAO dao) {
        CurrencyModel list = null;
        if (dao != null) {
            list = new CurrencyModel(dao.getId(),
                    dao.getName());
        }
        return list;
    }

    public List<CurrencyModel> transformFromDAO(Collection<CurrencyDAO> daos) {
        List<CurrencyModel> models = new ArrayList<>();
        CurrencyModel model;
        for (CurrencyDAO dao : daos) {
            model = transformFromDAO(dao);
            if (model != null) {
                models.add(model);
            }
        }
        return models;
    }

    public CurrencyDAO transformToDAO(CurrencyModel model) {
        CurrencyDAO dao = null;
        if (model != null) {
            dao = new CurrencyDAO(model.getId(),
                    model.getName());
        }
        return dao;
    }

    public List<CurrencyDAO> transformToDAO(Collection<CurrencyModel> models) {
        List<CurrencyDAO> daos = new ArrayList<>();
        CurrencyDAO dao;
        for (CurrencyModel currency : models) {
            dao = transformToDAO(currency);
            if (dao != null) {
                daos.add(dao);
            }
        }
        return daos;
    }
}
