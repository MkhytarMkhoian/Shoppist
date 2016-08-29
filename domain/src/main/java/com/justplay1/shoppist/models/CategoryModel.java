package com.justplay1.shoppist.models;

/**
 * Created by Mkhitar on 16.11.2014.
 */
public class CategoryModel extends BaseModel {

    public static final String NO_CATEGORY_ID = "1";

    private int color;
    private boolean isCreateByUser;
    private int position = -1;

    public CategoryModel() {
    }

    public CategoryModel(CategoryModel category) {
        this();
        setId(category.getId());
        setName(category.getName());
        setColor(category.getColor());
        setCreateByUser(category.isCreateByUser());
        setServerId(category.getServerId());
        setPosition(category.getPosition());
        setDelete(category.isDelete());
        setDirty(category.isDirty());
        setTimestamp(category.getTimestamp());
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isCreateByUser() {
        return isCreateByUser;
    }

    public void setCreateByUser(boolean isCreateByUser) {
        this.isCreateByUser = isCreateByUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof CategoryModel)) return false;
        CategoryModel item = (CategoryModel) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return 31 * id.hashCode();
    }
}
