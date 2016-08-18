package com.justplay1.shoppist.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mkhitar on 05.02.2015.
 */
public class HeaderViewModel extends BaseViewModel {

    @ItemType
    private int itemType;
    @Priority
    private int priority;
    private double totalPrice;
    private boolean showExpandIndicator;

    public HeaderViewModel() {
        priority = Priority.NO_PRIORITY;
        totalPrice = 0;
        itemType = ItemType.HEADER_ITEM;
        showExpandIndicator = true;
    }

    @SuppressWarnings("ResourceType")
    public HeaderViewModel(Parcel parcel) {
        this();
        name = parcel.readString();
        itemType = parcel.readInt();
        priority = parcel.readInt();
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
