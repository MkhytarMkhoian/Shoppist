package com.justplay1.shoppist.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mkhitar on 15.05.2015.
 */
public class CurrencyViewModel extends BaseViewModel {

    public static final String NO_CURRENCY_ID = "no_currency";

    private boolean isChecked;

    public CurrencyViewModel() {
        name = "";
        timestamp = 0;
    }

    public CurrencyViewModel(Parcel parcel) {
        this();
        id = parcel.readString();
        name = parcel.readString();
        serverId = parcel.readString();
        isDirty = parcel.readByte() != 0;
        timestamp = parcel.readLong();
        isChecked = parcel.readByte() != 0;
        isDelete = parcel.readByte() != 0;
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
        if (o == null || !(o instanceof CurrencyViewModel)) return false;
        CurrencyViewModel item = (CurrencyViewModel) o;

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
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(serverId);
        dest.writeByte((byte) (isDirty ? 1 : 0));
        dest.writeLong(timestamp);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeByte((byte) (isDelete ? 1 : 0));
    }

    public static final Parcelable.Creator<CurrencyViewModel> CREATOR = new Creator<CurrencyViewModel>() {

        @Override
        public CurrencyViewModel[] newArray(int size) {
            return new CurrencyViewModel[size];
        }

        @Override
        public CurrencyViewModel createFromParcel(Parcel source) {
            return new CurrencyViewModel(source);
        }
    };
}
