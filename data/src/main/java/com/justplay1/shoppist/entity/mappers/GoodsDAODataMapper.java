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

    public ProductModel transformFromDAO(ProductDAO itemEntity) {
        ProductModel item = null;
        if (itemEntity != null) {
            item = new ProductModel();
            item.setId(itemEntity.getId());
            item.setName(itemEntity.getName());
            item.setCategory(categoryDAODataMapper.transformFromDAO(itemEntity.getCategory()));
            item.setUnit(unitsDAODataMapper.transformFromDAO(itemEntity.getUnit()));
            item.setTimeCreated(itemEntity.getTimeCreated());
            item.setCreateByUser(itemEntity.isCreateByUser());
        }
        return item;
    }

    public List<ProductModel> transformFromDAO(Collection<ProductDAO> entities) {
        List<ProductModel> items = new ArrayList<>();
        ProductModel item;
        for (ProductDAO entity : entities) {
            item = transformFromDAO(entity);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    public ProductDAO transform(ProductModel product) {
        ProductDAO item = null;
        if (product != null) {
            item = new ProductDAO(product.getId(),
                    product.getName(),
                    categoryDAODataMapper.transformToDAO(product.getCategory()),
                    product.isCreateByUser(),
                    product.getTimeCreated(),
                    unitsDAODataMapper.transformToDAO(product.getUnit()));
        }
        return item;
    }

    public List<ProductDAO> transformToDAO(Collection<ProductModel> products) {
        List<ProductDAO> items = new ArrayList<>();
        ProductDAO item;
        for (ProductModel entity : products) {
            item = transform(entity);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }
}
