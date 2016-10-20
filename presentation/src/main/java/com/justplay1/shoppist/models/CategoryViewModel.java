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

package com.justplay1.shoppist.models;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CategoryViewModel extends BaseViewModel {

    public static final String NO_CATEGORY_ID = "1";

    private int color;
    private boolean isChecked;
    private boolean isCreateByUser;

    public CategoryViewModel() {
        color = Color.DKGRAY;
    }

    public CategoryViewModel(CategoryViewModel category) {
        this();
        setId(category.getId());
        setName(category.getName());
        setColor(category.getColor());
        setCreateByUser(category.isCreateByUser());
    }

    public CategoryViewModel(Parcel parcel) {
        this();
        name = parcel.readString();
        color = parcel.readInt();
        isChecked = parcel.readByte() != 0;
        id = parcel.readString();
        isCreateByUser = parcel.readByte() != 0;
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
        if (o == null || !(o instanceof CategoryViewModel)) return false;
        CategoryViewModel item = (CategoryViewModel) o;

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
        dest.writeString(name);
        dest.writeInt(color);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeString(id);
        dest.writeByte((byte) (isCreateByUser ? 1 : 0));
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
}
