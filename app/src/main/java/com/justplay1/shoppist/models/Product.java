package com.justplay1.shoppist.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.justplay1.shoppist.communication.network.ServerConstants;
import com.justplay1.shoppist.database.ShoppingListContact;
import com.parse.ParseObject;

/**
 * Created by Mkhitar on 18.05.2015.
 */
public class Product extends BaseModel {

    private Category category;
    private boolean isCreateByUser;
    private boolean isChecked;
    private long timeCreated;
    private Unit unit;

    public Product() {
        timestamp = 0;
    }

    public Product(Parcel parcel) {
        this();
        id = parcel.readString();
        name = parcel.readString();
        category = parcel.readParcelable(Product.class.getClassLoader());
        isCreateByUser = parcel.readByte() != 0;
        serverId = parcel.readString();
        isChecked = parcel.readByte() != 0;
        timeCreated = parcel.readLong();
        isDelete = parcel.readByte() != 0;
        isDirty = parcel.readByte() != 0;
        timestamp = parcel.readLong();
        unit = parcel.readParcelable(Product.class.getClassLoader());
    }

    public Product(Product product) {
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

    public Product(ParseObject object, Category category, Unit unit) {
        this();
        setServerId(object.getObjectId());
        setId(object.getString(ShoppingListContact.Products.PRODUCT_ID));
        setName(object.getString(ShoppingListContact.Products.NAME));
        setCreateByUser(object.getBoolean(ShoppingListContact.Products.IS_CREATE_BY_USER));
        setDelete(object.getBoolean(ShoppingListContact.Products.IS_DELETED));
        setTimestamp(object.getLong(ServerConstants.TIMESTAMP));
        setCategory(category);
        setUnit(unit);
    }

    public Product(Cursor cursor) {
        this();
        setId(cursor.getString(cursor.getColumnIndex(ShoppingListContact.Products.PRODUCT_ID)));
        setName(cursor.getString(cursor.getColumnIndex(ShoppingListContact.Products.NAME)));
        setCategory(new Category(cursor, ShoppingListContact.Products.CATEGORY_ID));
        setCreateByUser(cursor.getInt(cursor.getColumnIndex(ShoppingListContact.Products.IS_CREATE_BY_USER)) != 0);
        setServerId(cursor.getString(cursor.getColumnIndex(ShoppingListContact.Products.SERVER_ID)));
        setTimeCreated(cursor.getLong(cursor.getColumnIndex(ShoppingListContact.Products.TIME_CREATED)));
        setUnit(new Unit(cursor));

        try {
            setDelete(cursor.getInt(cursor.getColumnIndexOrThrow(ShoppingListContact.Products.IS_DELETED)) != 0);
        } catch (IllegalArgumentException e) {
            setDelete(false);
        }
        try {
            setDirty(cursor.getInt(cursor.getColumnIndexOrThrow(ShoppingListContact.Products.IS_DIRTY)) != 0);
        } catch (IllegalArgumentException e) {
            setDirty(false);
        }
        try {
            setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(ShoppingListContact.Products.TIMESTAMP)));
        } catch (IllegalArgumentException e) {
            setTimestamp(0);
        }
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
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

    @Override
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Product)) return false;
        Product item = (Product) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeParcelable(category, flags);
        dest.writeByte((byte) (isCreateByUser ? 1 : 0));
        dest.writeString(serverId);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeLong(timeCreated);
        dest.writeByte((byte) (isDelete ? 1 : 0));
        dest.writeByte((byte) (isDirty ? 1 : 0));
        dest.writeLong(timestamp);
        dest.writeParcelable(unit, flags);
    }

    public boolean isCategoryEmpty() {
        return category == null || category.getId() == null || category.getName() == null;
    }

    public boolean isUnitEmpty() {
        return unit == null || unit.getId() == null || unit.getName() == null;
    }

    public static final Parcelable.Creator<Product> CREATOR = new Creator<Product>() {

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }

        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }
    };
}
