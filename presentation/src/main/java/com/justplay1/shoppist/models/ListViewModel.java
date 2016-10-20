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
public class ListViewModel extends BaseViewModel {

    private int boughtCount;
    private long timeCreated;
    @Priority
    private int priority;
    private int color;
    private boolean isChecked;
    private int size;

    public ListViewModel() {
        name = "";
        priority = Priority.NO_PRIORITY;
        color = Color.DKGRAY;
    }

    @SuppressWarnings("ResourceType")
    public ListViewModel(Parcel parcel) {
        this();
        id = parcel.readString();
        name = parcel.readString();
        boughtCount = parcel.readInt();
        timeCreated = parcel.readLong();
        priority = parcel.readInt();
        color = parcel.readInt();
        isChecked = parcel.readByte() != 0;
        size = parcel.readInt();
        pinned = parcel.readByte() != 0;
    }

    @Override
    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public int getItemType() {
        return ItemType.LIST_ITEM;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getBoughtCount() {
        return boughtCount;
    }

    public void setBoughtCount(int boughtCount) {
        this.boughtCount = boughtCount;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    @Priority
    public int getPriority() {
        return priority;
    }

    public void setPriority(@Priority int priority) {
        this.priority = priority;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof ListViewModel)) return false;
        ListViewModel item = (ListViewModel) o;

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
        dest.writeInt(boughtCount);
        dest.writeLong(timeCreated);
        dest.writeInt(priority);
        dest.writeInt(color);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeInt(size);
        dest.writeByte((byte) (pinned ? 1 : 0));
    }

    public static final Parcelable.Creator<ListViewModel> CREATOR = new Creator<ListViewModel>() {

        @Override
        public ListViewModel[] newArray(int size) {
            return new ListViewModel[size];
        }

        @Override
        public ListViewModel createFromParcel(Parcel source) {
            return new ListViewModel(source);
        }
    };
}
