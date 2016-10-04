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
import com.justplay1.shoppist.models.ProductModel;
import com.justplay1.shoppist.models.ProductViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class GoodsModelDataMapper {

    private final CategoryModelDataMapper categoryModelDataMapper;
    private final UnitsDataModelMapper unitsDataModelMapper;

    @Inject
    public GoodsModelDataMapper(UnitsDataModelMapper unitsDataMapper, CategoryModelDataMapper categoryDataMapper) {
        this.unitsDataModelMapper = unitsDataMapper;
        this.categoryModelDataMapper = categoryDataMapper;
    }

    public ProductViewModel transformToViewModel(ProductModel product) {
        ProductViewModel productModel = null;
        if (product != null) {
            productModel = new ProductViewModel();
            productModel.setId(product.getId());
            productModel.setName(product.getName());
            productModel.setCategory(categoryModelDataMapper.transformToViewModel(product.getCategory()));
            productModel.setUnit(unitsDataModelMapper.transformToViewModel(product.getUnit()));
            productModel.setTimeCreated(product.getTimeCreated());
            productModel.setCreateByUser(product.isCreateByUser());
        }
        return productModel;
    }

    public List<ProductViewModel> transformToViewModel(Collection<ProductModel> entities) {
        List<ProductViewModel> items = new ArrayList<>();
        ProductViewModel item;
        for (ProductModel product : entities) {
            item = transformToViewModel(product);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    public ProductModel transform(ProductViewModel product) {
        ProductModel item = null;
        if (product != null) {
            item = new ProductModel();
            item.setId(product.getId());
            item.setName(product.getName());
            item.setCategory(categoryModelDataMapper.transform(product.getCategory()));
            item.setUnit(unitsDataModelMapper.transform(product.getUnit()));
            item.setTimeCreated(product.getTimeCreated());
            item.setCreateByUser(product.isCreateByUser());
        }
        return item;
    }

    public List<ProductModel> transform(Collection<ProductViewModel> products) {
        List<ProductModel> items = new ArrayList<>();
        ProductModel item;
        for (ProductViewModel productModel : products) {
            item = transform(productModel);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }
}
