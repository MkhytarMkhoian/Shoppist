package com.justplay1.shoppist.models.mappers;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.models.ProductModel;
import com.justplay1.shoppist.models.ProductViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 29.04.2016.
 */
@PerActivity
public class GoodsModelDataMapper {

    private final CategoryModelDataMapper mCategoryDataMapper;
    private final UnitsDataModelMapper mUnitsDataMapper;

    @Inject
    public GoodsModelDataMapper(UnitsDataModelMapper unitsDataMapper, CategoryModelDataMapper categoryDataMapper) {
        this.mUnitsDataMapper = unitsDataMapper;
        this.mCategoryDataMapper = categoryDataMapper;
    }

    public ProductViewModel transformToViewModel(ProductModel product) {
        ProductViewModel productModel = null;
        if (product != null) {
            productModel = new ProductViewModel();
            productModel.setId(product.getId());
            productModel.setName(product.getName());
            productModel.setServerId(product.getServerId());
            productModel.setDirty(product.isDirty());
            productModel.setDelete(product.isDelete());
            productModel.setTimestamp(product.getTimestamp());
            productModel.setCategory(mCategoryDataMapper.transformToViewModel(product.getCategory()));
            productModel.setUnit(mUnitsDataMapper.transformToViewModel(product.getUnit()));
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
            item.setServerId(product.getServerId());
            item.setDirty(product.isDirty());
            item.setDelete(product.isDelete());
            item.setTimestamp(product.getTimestamp());
            item.setCategory(mCategoryDataMapper.transform(product.getCategory()));
            item.setUnit(mUnitsDataMapper.transform(product.getUnit()));
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
