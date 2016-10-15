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

import com.justplay1.shoppist.entity.CategoryDAO;
import com.justplay1.shoppist.models.CategoryModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhytar Mkhoian.
 */
@Singleton
public class CategoryDAODataMapper {

    @Inject
    public CategoryDAODataMapper() {
    }

    public CategoryModel transformFromDAO(CategoryDAO dao) {
        CategoryModel category = null;
        if (dao != null) {
            category = new CategoryModel(dao.getId(),
                    dao.getName(),
                    dao.getColor(),
                    dao.isCreateByUser());
        }
        return category;
    }

    public List<CategoryModel> transformFromDAO(Collection<CategoryDAO> daos) {
        List<CategoryModel> models = new ArrayList<>();
        CategoryModel model;
        for (CategoryDAO dao : daos) {
            model = transformFromDAO(dao);
            if (model != null) {
                models.add(model);
            }
        }
        return models;
    }

    public CategoryDAO transformToDAO(CategoryModel model) {
        CategoryDAO dao = null;
        if (model != null) {
            dao = new CategoryDAO(model.getId(),
                    model.getName(),
                    model.getColor(),
                    model.isCreateByUser());
        }
        return dao;
    }

    public List<CategoryDAO> transformToDAO(Collection<CategoryModel> models) {
        List<CategoryDAO> daos = new ArrayList<>();
        CategoryDAO dao;
        for (CategoryModel model : models) {
            dao = transformToDAO(model);
            if (dao != null) {
                daos.add(dao);
            }
        }
        return daos;
    }
}
