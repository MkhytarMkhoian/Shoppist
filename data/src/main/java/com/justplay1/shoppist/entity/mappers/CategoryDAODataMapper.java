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

    public CategoryModel transformFromDAO(CategoryDAO categoryEntity) {
        CategoryModel category = null;
        if (categoryEntity != null) {
            category = new CategoryModel();
            category.setId(categoryEntity.getId());
            category.setName(categoryEntity.getName());
            category.setColor(categoryEntity.getColor());
            category.setCreateByUser(categoryEntity.isCreateByUser());
            category.setPosition(categoryEntity.getPosition());
        }
        return category;
    }

    public List<CategoryModel> transformFromDAO(Collection<CategoryDAO> categoryEntities) {
        List<CategoryModel> categories = new ArrayList<>();
        CategoryModel category;
        for (CategoryDAO categoryEntity : categoryEntities) {
            category = transformFromDAO(categoryEntity);
            if (category != null) {
                categories.add(category);
            }
        }
        return categories;
    }

    public CategoryDAO transformToDAO(CategoryModel category) {
        CategoryDAO entity = null;
        if (category != null) {
            entity = new CategoryDAO(category.getId(),
                    category.getName(),
                    category.getColor(),
                    category.isCreateByUser(),
                    category.getPosition());
        }
        return entity;
    }

    public List<CategoryDAO> transformToDAO(Collection<CategoryModel> categories) {
        List<CategoryDAO> entities = new ArrayList<>();
        CategoryDAO entity;
        for (CategoryModel category : categories) {
            entity = transformToDAO(category);
            if (entity != null) {
                entities.add(entity);
            }
        }
        return entities;
    }
}
