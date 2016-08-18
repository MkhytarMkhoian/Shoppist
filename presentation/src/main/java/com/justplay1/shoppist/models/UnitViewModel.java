package com.justplay1.shoppist.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mkhitar on 15.05.2015.
 */
public class UnitViewModel extends BaseViewModel {

    public static final String NO_UNIT_ID = "no_unit";

    private String shortName;
    private boolean isChecked;

    public UnitViewModel() {
        name = "";
        shortName = "";
        timestamp = 0;
    }

    public UnitViewModel(Parcel parcel) {
        id = parcel.readString();
        name = parcel.readString();
        shortName = parcel.readString();
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof UnitViewModel)) return false;
        UnitViewModel item = (UnitViewModel) o;

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
        dest.writeString(shortName);
        dest.writeString(serverId);
        dest.writeByte((byte) (isDirty ? 1 : 0));
        dest.writeLong(timestamp);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeByte((byte) (isDelete ? 1 : 0));
    }

    public static final Parcelable.Creator<UnitViewModel> CREATOR = new Creator<UnitViewModel>() {

        @Override
        public UnitViewModel[] newArray(int size) {
            return new UnitViewModel[size];
        }

        @Override
        public UnitViewModel createFromParcel(Parcel source) {
            return new UnitViewModel(source);
        }
    };
}
