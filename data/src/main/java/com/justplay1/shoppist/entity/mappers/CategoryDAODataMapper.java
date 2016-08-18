package com.justplay1.shoppist.entity.mappers;

import com.justplay1.shoppist.entity.CategoryDAO;
import com.justplay1.shoppist.models.CategoryModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhytar on 28.04.2016.
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
            category.setServerId(categoryEntity.getServerId());
            category.setPosition(categoryEntity.getPosition());
            category.setDelete(categoryEntity.isDelete());
            category.setTimestamp(categoryEntity.getTimestamp());
            category.setDirty(categoryEntity.isDirty());
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
                    category.getServerId(),
                    category.getName(),
                    category.getTimestamp(),
                    category.isDirty(),
                    category.isDelete(),
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
