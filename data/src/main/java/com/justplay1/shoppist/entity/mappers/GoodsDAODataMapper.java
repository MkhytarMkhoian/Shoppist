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

import com.justplay1.shoppist.entity.ProductDAO;
import com.justplay1.shoppist.models.ProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhytar Mkhoian.
 */
@Singleton
public class GoodsDAODataMapper {

    private final CategoryDAODataMapper categoryDAODataMapper;
    private final UnitsDAODataMapper unitsDAODataMapper;

    @Inject
    public GoodsDAODataMapper(UnitsDAODataMapper unitsDAODataMapper, CategoryDAODataMapper categoryDAODataMapper) {
        this.unitsDAODataMapper = unitsDAODataMapper;
        this.categoryDAODataMapper = categoryDAODataMapper;
    }

    public ProductModel transformFromDAO(ProductDAO dao) {
        ProductModel model = null;
        if (dao != null) {
            model = new ProductModel(dao.getId(),
                    dao.getName(),
                    categoryDAODataMapper.transformFromDAO(dao.getCategory()),
                    dao.isCreateByUser(),
                    dao.getTimeCreated(),
                    unitsDAODataMapper.transformFromDAO(dao.getUnit()));
        }
        return model;
    }

    public List<ProductModel> transformFromDAO(Collection<ProductDAO> daos) {
        List<ProductModel> models = new ArrayList<>();
        ProductModel model;
        for (ProductDAO dao : daos) {
            model = transformFromDAO(dao);
            if (model != null) {
                models.add(model);
            }
        }
        return models;
    }

    public ProductDAO transformToDAO(ProductModel model) {
        ProductDAO dao = null;
        if (model != null) {
            dao = new ProductDAO(model.getId(),
                    model.getName(),
                    categoryDAODataMapper.transformToDAO(model.getCategory()),
                    model.isCreateByUser(),
                    model.getTimeCreated(),
                    unitsDAODataMapper.transformToDAO(model.getUnit()));
        }
        return dao;
    }

    public List<ProductDAO> transformToDAO(Collection<ProductModel> models) {
        List<ProductDAO> daos = new ArrayList<>();
        ProductDAO dao;
        for (ProductModel model : models) {
            dao = transformToDAO(model);
            if (dao != null) {
                daos.add(dao);
            }
        }
        return daos;
    }
}
