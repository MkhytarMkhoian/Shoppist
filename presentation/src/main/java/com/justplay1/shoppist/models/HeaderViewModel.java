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

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mkhytar Mkhoian.
 */
public class HeaderViewModel extends BaseViewModel {

    private int itemType;
    private int priority;
    private boolean showExpandIndicator;

    public HeaderViewModel() {
        priority = Priority.NO_PRIORITY;
        itemType = ItemType.HEADER_ITEM;
        showExpandIndicator = true;
    }

    public HeaderViewModel(Parcel parcel) {
        this();
        id = parcel.readString();
        name = parcel.readString();
        itemType = parcel.readInt();
        priority = parcel.readInt();
        showExpandIndicator = parcel.readByte() != 0;
    }

    public boolean isShowExpandIndicator() {
        return showExpandIndicator;
    }

    public void setShowExpandIndicator(boolean showExpandIndicator) {
        this.showExpandIndicator = showExpandIndicator;
    }

    @Priority
    @Override
    public int getPriority() {
        return priority;
    }

    public void setPriority(@Priority int priority) {
        this.priority = priority;
    }

    public void setItemType(@ItemType int mItemType) {
        this.itemType = mItemType;
    }

    @ItemType
    @Override
    public int getItemType() {
        return itemType;
    }

    public static final Parcelable.Creator<HeaderViewModel> CREATOR = new Creator<HeaderViewModel>() {

        @Override
        public HeaderViewModel[] newArray(int size) {
            return new HeaderViewModel[size];
        }

        @Override
        public HeaderViewModel createFromParcel(Parcel source) {
            return new HeaderViewModel(source);
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof HeaderViewModel)) return false;

        HeaderViewModel item = (HeaderViewModel) o;
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
        dest.writeSerializable(itemType);
        dest.writeSerializable(priority);
        dest.writeByte((byte) (showExpandIndicator ? 1 : 0));
    }
}
