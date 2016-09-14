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
import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.models.CategoryViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
@PerActivity
public class CategoryModelDataMapper {

    @Inject
    public CategoryModelDataMapper() {
    }

    public CategoryViewModel transformToViewModel(CategoryModel category) {
        CategoryViewModel categoryModel = null;
        if (category != null) {
            categoryModel = new CategoryViewModel();
            categoryModel.setId(category.getId());
            categoryModel.setName(category.getName());
            categoryModel.setColor(category.getColor());
            categoryModel.setCreateByUser(category.isCreateByUser());
            categoryModel.setPosition(category.getPosition());
        }
        return categoryModel;
    }

    public List<CategoryViewModel> transformToViewModel(Collection<CategoryModel> categories) {
        List<CategoryViewModel> categoriesModels = new ArrayList<>();
        CategoryViewModel categoryModel;
        for (CategoryModel category : categories) {
            categoryModel = transformToViewModel(category);
            if (categoryModel != null) {
                categoriesModels.add(categoryModel);
            }
        }
        return categoriesModels;
    }

    public CategoryModel transform(CategoryViewModel category) {
        CategoryModel entity = null;
        if (category != null) {
            entity = new CategoryModel();
            entity.setId(category.getId());
            entity.setName(category.getName());
            entity.setColor(category.getColor());
            entity.setCreateByUser(category.isCreateByUser());
            entity.setPosition(category.getPosition());
        }
        return entity;
    }

    public List<CategoryModel> transform(Collection<CategoryViewModel> categories) {
        List<CategoryModel> entities = new ArrayList<>();
        CategoryModel entity;
        for (CategoryViewModel category : categories) {
            entity = transform(category);
            if (entity != null) {
                entities.add(entity);
            }
        }
        return entities;
    }
}
