package com.justplay1.shoppist.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mkhitar on 05.02.2015.
 */
public class Header extends BaseModel {

    private int itemType;
    private Priority priority;
    private double totalPrice;
    private boolean showExpandIndicator;

    public Header() {
        priority = Priority.NO_PRIORITY;
        totalPrice = 0;
        itemType = ItemType.HEADER_ITEM;
        showExpandIndicator = true;
    }

    public Header(Parcel parcel) {
        this();
        name = parcel.readString();
        itemType = parcel.readInt();
        priority = (Priority) parcel.readSerializable();
        totalPrice = parcel.readDouble();
        showExpandIndicator = parcel.readByte() != 0;
    }

    public boolean isShowExpandIndicator() {
        return showExpandIndicator;
    }

    public void setShowExpandIndicator(boolean showExpandIndicator) {
        this.showExpandIndicator = showExpandIndicator;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setItemType(int mItemType) {
        this.itemType = mItemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public static final Parcelable.Creator<Header> CREATOR = new Creator<Header>() {

        @Override
        public Header[] newArray(int size) {
            return new Header[size];
        }

        @Override
        public Header createFromParcel(Parcel source) {
            return new Header(source);
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Header)) return false;

        Header item = (Header) o;
        return item.getName().equals(this.getName());
    }

    @Override
    public int hashCode() {
        return 31 * name.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeSerializable(itemType);
        dest.writeSerializable(priority);
        dest.writeDouble(totalPrice);
        dest.writeByte((byte) (showExpandIndicator ? 1 : 0));
    }

    @Override
    public String getServerId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setServerId(String id) {
        throw new UnsupportedOperationException();
    }
}
