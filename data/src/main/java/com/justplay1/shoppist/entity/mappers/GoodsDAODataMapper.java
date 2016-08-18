package com.justplay1.shoppist.entity.mappers;

import com.justplay1.shoppist.entity.ProductDAO;
import com.justplay1.shoppist.models.ProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhytar on 29.04.2016.
 */
@Singleton
public class GoodsDAODataMapper {

    private final CategoryDAODataMapper mCategoryDAODataMapper;
    private final UnitsDAODataMapper mUnitsDAODataMapper;

    @Inject
    public GoodsDAODataMapper(UnitsDAODataMapper unitsDAODataMapper, CategoryDAODataMapper categoryDAODataMapper) {
        this.mUnitsDAODataMapper = unitsDAODataMapper;
        this.mCategoryDAODataMapper = categoryDAODataMapper;
    }

    public ProductModel transformFromDAO(ProductDAO itemEntity) {
        ProductModel item = null;
        if (itemEntity != null) {
            item = new ProductModel();
            item.setId(itemEntity.getId());
            item.setName(itemEntity.getName());
            item.setServerId(itemEntity.getServerId());
            item.setDirty(itemEntity.isDirty());
            item.setDelete(itemEntity.isDelete());
            item.setTimestamp(itemEntity.getTimestamp());
            item.setCategory(mCategoryDAODataMapper.transformFromDAO(itemEntity.getCategory()));
            item.setUnit(mUnitsDAODataMapper.transformFromDAO(itemEntity.getUnit()));
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
                    product.getServerId(),
                    product.getName(),
                    product.getTimestamp(),
                    product.isDirty(),
                    product.isDelete(),
                    mCategoryDAODataMapper.transformToDAO(product.getCategory()),
                    product.isCreateByUser(),
                    product.getTimeCreated(),
                    mUnitsDAODataMapper.transformToDAO(product.getUnit()));
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
