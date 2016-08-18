package com.justplay1.shoppist.models;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mkhitar on 16.11.2014.
 */
public class CategoryViewModel extends BaseViewModel {

    public static final String NO_CATEGORY_ID = "1";

    private int color;
    private boolean isEnable;
    private boolean isChecked;
    private boolean isCreateByUser;
    private int position = -1;

    public CategoryViewModel() {
        color = Color.DKGRAY;
        isEnable = true;
        timestamp = 0;
    }

    public CategoryViewModel(CategoryViewModel category) {
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

    public CategoryViewModel(Parcel parcel) {
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
        if (o == null || !(o instanceof CategoryViewModel)) return false;
        CategoryViewModel item = (CategoryViewModel) o;

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

    public static final Parcelable.Creator<CategoryViewModel> CREATOR = new Creator<CategoryViewModel>() {

        @Override
        public CategoryViewModel[] newArray(int size) {
            return new CategoryViewModel[size];
        }

        @Override
        public CategoryViewModel createFromParcel(Parcel source) {
            return new CategoryViewModel(source);
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
