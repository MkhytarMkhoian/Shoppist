package com.justplay1.shoppist.models;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.justplay1.shoppist.communication.network.ServerConstants;
import com.justplay1.shoppist.database.ShoppingListContact.Categories;
import com.parse.ParseObject;

/**
 * Created by Mkhitar on 16.11.2014.
 */
public class Category extends BaseModel {

    public static final String NO_CATEGORY_ID = "1";

    private int color;
    private boolean isEnable;
    private boolean isChecked;
    private boolean isCreateByUser;
    private int position = -1;

    public Category() {
        color = Color.DKGRAY;
        isEnable = true;
        timestamp = 0;
    }

    public Category(Cursor cursor, String idColumnName) {
        this();
        setId(cursor.getString(cursor.getColumnIndex(idColumnName)));
        setName(cursor.getString(cursor.getColumnIndex(Categories.NAME)));
        setColor(cursor.getInt(cursor.getColumnIndex(Categories.COLOR)));
        setServerId(cursor.getString(cursor.getColumnIndex(Categories.SERVER_ID)));
        setPosition(cursor.getInt(cursor.getColumnIndex(Categories.MANUAL_SORT_POSITION)));
        setEnable(cursor.getInt(cursor.getColumnIndex(Categories.ENABLE)) != 0);
        setCreateByUser(cursor.getInt(cursor.getColumnIndex(Categories.CREATE_BY_USER)) != 0);

        try {
            setDelete(cursor.getInt(cursor.getColumnIndexOrThrow(Categories.IS_DELETED)) != 0);
        } catch (IllegalArgumentException e) {
            setDelete(false);
        }
        try {
            setDirty(cursor.getInt(cursor.getColumnIndexOrThrow(Categories.IS_DIRTY)) != 0);
        } catch (IllegalArgumentException e) {
            setDirty(false);
        }
        try {
            setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(Categories.TIMESTAMP)));
        } catch (IllegalArgumentException e) {
            setTimestamp(0);
        }
    }

    public Category(Category category) {
        this();
        setId(category.getId());
        setName(category.getName());
        setColor(category.getColor());
        setEnable(category.isEnable());
        setCreateByUser(category.isCreateByUser());
        setServerId(category.getServerId());
        setPosition(category.getPosition());
        setDelete(category.isDelete());
        setDirty(category.isDirty());
        setTimestamp(category.getTimestamp());
    }

    public Category(ParseObject object) {
        this();
        setId(object.getString(Categories.CATEGORY_ID));
        setName(object.getString(Categories.NAME));
        setColor(object.getInt(Categories.COLOR));
        setEnable(object.getBoolean(Categories.ENABLE));
        setCreateByUser(object.getBoolean(Categories.CREATE_BY_USER));
        setServerId(object.getObjectId());
        setPosition(object.getInt(Categories.MANUAL_SORT_POSITION));
        setDelete(object.getBoolean(Categories.IS_DELETED));
        setTimestamp(object.getLong(ServerConstants.TIMESTAMP));
    }

    public Category(Parcel parcel) {
        this();
        name = parcel.readString();
        color = parcel.readInt();
        isEnable = parcel.readByte() != 0;
        isChecked = parcel.readByte() != 0;
        id = parcel.readString();
        isCreateByUser = parcel.readByte() != 0;
        serverId = parcel.readString();
        position = parcel.readInt();
        isDelete = parcel.readByte() != 0;
        isDirty = parcel.readByte() != 0;
        timestamp = parcel.readLong();
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
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
        if (o == null || !(o instanceof Category)) return false;
        Category item = (Category) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return 31 * id.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(color);
        dest.writeByte((byte) (isEnable ? 1 : 0));
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeString(id);
        dest.writeByte((byte) (isCreateByUser ? 1 : 0));
        dest.writeString(serverId);
        dest.writeInt(position);
        dest.writeByte((byte) (isDelete ? 1 : 0));
        dest.writeByte((byte) (isDirty ? 1 : 0));
        dest.writeLong(timestamp);
    }

    public static final Parcelable.Creator<Category> CREATOR = new Creator<Category>() {

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }

        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }
    };

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public int getItemType() {
        return ItemType.LIST_ITEM;
    }
}
