package com.justplay1.shoppist.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.justplay1.shoppist.communication.network.ServerConstants;
import com.justplay1.shoppist.database.ShoppingListContact;
import com.parse.ParseObject;

/**
 * Created by Mkhitar on 15.05.2015.
 */
public class Unit extends BaseModel {

    public static final String NO_UNIT_ID = "no_unit";

    private String shortName;
    private boolean isChecked;

    public Unit() {
        name = "";
        shortName = "";
        timestamp = 0;
    }

    public Unit(Parcel parcel) {
        id = parcel.readString();
        name = parcel.readString();
        shortName = parcel.readString();
        serverId = parcel.readString();
        isDirty = parcel.readByte() != 0;
        timestamp = parcel.readLong();
        isChecked = parcel.readByte() != 0;
        isDelete = parcel.readByte() != 0;
    }

    public Unit(Cursor cursor) {
        this();
        setId(cursor.getString(cursor.getColumnIndex(ShoppingListContact.Units.UNIT_ID)));
        setName(cursor.getString(cursor.getColumnIndex(ShoppingListContact.Units.FULL_NAME)));
        setShortName(cursor.getString(cursor.getColumnIndex(ShoppingListContact.Units.SHORT_NAME)));
        setServerId(cursor.getString(cursor.getColumnIndex(ShoppingListContact.Units.SERVER_ID)));

        try {
            setDelete(cursor.getInt(cursor.getColumnIndexOrThrow(ShoppingListContact.Units.IS_DELETED)) != 0);
        } catch (IllegalArgumentException e) {
            setDelete(false);
        }
        try {
            setDirty(cursor.getInt(cursor.getColumnIndexOrThrow(ShoppingListContact.Units.IS_DIRTY)) != 0);
        } catch (IllegalArgumentException e) {
            setDirty(false);
        }
        try {
            setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(ShoppingListContact.Units.TIMESTAMP)));
        } catch (IllegalArgumentException e) {
            setTimestamp(0);
        }
    }

    public Unit(ParseObject object) {
        this();
        setId(object.getString(ShoppingListContact.Units.UNIT_ID));
        setName(object.getString(ShoppingListContact.Units.FULL_NAME));
        setShortName(object.getString(ShoppingListContact.Units.SHORT_NAME));
        setServerId(object.getObjectId());
        setTimestamp(object.getLong(ServerConstants.TIMESTAMP));
        setDelete(object.getBoolean(ShoppingListContact.Units.IS_DELETED));
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
        if (o == null || !(o instanceof Unit)) return false;
        Unit item = (Unit) o;

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

    public static final Parcelable.Creator<Unit> CREATOR = new Creator<Unit>() {

        @Override
        public Unit[] newArray(int size) {
            return new Unit[size];
        }

        @Override
        public Unit createFromParcel(Parcel source) {
            return new Unit(source);
        }
    };
}
