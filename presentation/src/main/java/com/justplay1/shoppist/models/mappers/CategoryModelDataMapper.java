package com.justplay1.shoppist.models.mappers;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.models.CategoryViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 28.04.2016.
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
            categoryModel.setServerId(category.getServerId());
            categoryModel.setPosition(category.getPosition());
            categoryModel.setDelete(category.isDelete());
            categoryModel.setTimestamp(category.getTimestamp());
            categoryModel.setDirty(category.isDirty());
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
            entity.setServerId(category.getServerId());
            entity.setPosition(category.getPosition());
            entity.setDelete(category.isDelete());
            entity.setTimestamp(category.getTimestamp());
            entity.setDirty(category.isDirty());
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
