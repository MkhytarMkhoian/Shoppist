package com.justplay1.shoppist.models;

/**
 * Created by Mkhitar on 18.05.2015.
 */
public class ProductModel extends BaseModel {

    private CategoryModel category;
    private boolean isCreateByUser;
    private long timeCreated;
    private UnitModel unit;

    public ProductModel() {
    }

    public ProductModel(ProductModel product) {
        this();
        setId(product.getId());
        setName(product.getName());
        setCreateByUser(product.isCreateByUser());
        setServerId(product.getServerId());
        setDelete(product.isDelete());
        setDirty(product.isDirty());
        setTimestamp(product.getTimestamp());
        setCategory(product.getCategory());
        setUnit(product.getUnit());
    }

    public UnitModel getUnit() {
        return unit;
    }

    public void setUnit(UnitModel unit) {
        this.unit = unit;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public boolean isCreateByUser() {
        return isCreateByUser;
    }

    public void setCreateByUser(boolean isCreateByUser) {
        this.isCreateByUser = isCreateByUser;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof ProductModel)) return false;
        ProductModel item = (ProductModel) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public boolean isCategoryEmpty() {
        return category == null || category.getId() == null || category.getName() == null;
    }

    public boolean isUnitEmpty() {
        return unit == null || unit.getId() == null || unit.getName() == null;
    }
}
