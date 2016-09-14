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

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListItemViewModel extends BaseViewModel {

    private String parentListId;
    private String note;
    private boolean status;
    private CategoryViewModel category;
    @Priority
    private int priority = Priority.NO_PRIORITY;
    private boolean isChecked;
    private double price;
    private double quantity = 1;
    private UnitViewModel unit;
    private long timeCreated;
    private CurrencyViewModel currency;
    private int position = -1;

    public ListItemViewModel() {

    }

    public ListItemViewModel(ListItemViewModel item) {
        this();
        setParentListId(item.getParentListId());
        setId(item.getId());
        setName(item.getName());
        setNote(item.getNote());
        setUnit(item.getUnit());
        setCurrency(item.getCurrency());
        setPrice(item.getPrice());
        setTimeCreated(item.getTimeCreated());
        setQuantity(item.getQuantity());
        setStatus(item.getStatus());
        setPriority(item.getPriority());
        setCategory(item.getCategory());
        setPosition(item.getPosition());
        setPinned(item.isPinned());
    }

    @SuppressWarnings("ResourceType")
    public ListItemViewModel(Parcel parcel) {
        this();
        parentListId = parcel.readString();
        id = parcel.readString();
        name = parcel.readString();
        note = parcel.readString();
        status = parcel.readByte() != 0;
        category = parcel.readParcelable(ListItemViewModel.class.getClassLoader());
        priority = parcel.readInt();
        isChecked = parcel.readByte() != 0;
        price = parcel.readDouble();
        quantity = parcel.readDouble();
        unit = parcel.readParcelable(ListItemViewModel.class.getClassLoader());
        timeCreated = parcel.readLong();
        currency = parcel.readParcelable(ListItemViewModel.class.getClassLoader());
        position = parcel.readInt();
        mPinned = parcel.readByte() != 0;
    }

    public String getParentListId() {
        return parentListId;
    }

    public void setParentListId(String parentListId) {
        this.parentListId = parentListId;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String shortMessage) {
        this.note = shortMessage;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public CategoryViewModel getCategory() {
        return category;
    }

    public boolean isCategoryEmpty() {
        return category == null || category.getId() == null || category.getName() == null;
    }

    public boolean isCurrencyEmpty() {
        return currency == null || currency.getId() == null || currency.getName() == null;
    }

    public boolean isUnitEmpty() {
        return unit == null || unit.getId() == null || unit.getName() == null;
    }

    public void setCategory(CategoryViewModel category) {
        this.category = category;
    }

    @Priority
    public int getPriority() {
        return priority;
    }

    public void setPriority(@Priority int priority) {
        this.priority = priority;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public UnitViewModel getUnit() {
        return unit;
    }

    public void setUnit(UnitViewModel unit) {
        this.unit = unit;
    }

    public CurrencyViewModel getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyViewModel currency) {
        this.currency = currency;
    }

    @Override
    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof ListItemViewModel)) return false;

        ListItemViewModel item = (ListItemViewModel) o;
        return item.getId().equals(this.getId())
                && (item.getParentListId().equals(this.getParentListId()));
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        return result + parentListId.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(parentListId);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(note);
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeParcelable(category, flags);
        dest.writeInt(priority);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeDouble(price);
        dest.writeDouble(quantity);
        dest.writeParcelable(unit, flags);
        dest.writeLong(timeCreated);
        dest.writeParcelable(currency, flags);
        dest.writeInt(position);
        dest.writeByte((byte) (mPinned ? 1 : 0));
    }

    public static final Creator<ListItemViewModel> CREATOR = new Creator<ListItemViewModel>() {

        @Override
        public ListItemViewModel[] newArray(int size) {
            return new ListItemViewModel[size];
        }

        @Override
        public ListItemViewModel createFromParcel(Parcel source) {
            return new ListItemViewModel(source);
        }
    };
}
