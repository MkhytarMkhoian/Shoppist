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
public class Currency extends BaseModel implements ServerModel {

    public static final String NO_CURRENCY_ID = "no_currency";

    private boolean isChecked;

    public Currency() {
        name = "";
        timestamp = 0;
    }

    public Currency(Parcel parcel) {
        this();
        id = parcel.readString();
        name = parcel.readString();
        serverId = parcel.readString();
        isDirty = parcel.readByte() != 0;
        timestamp = parcel.readLong();
        isChecked = parcel.readByte() != 0;
        isDelete = parcel.readByte() != 0;
    }

    public Currency(Cursor cursor) {
        this();
        setId(cursor.getString(cursor.getColumnIndex(ShoppingListContact.Currencies.CURRENCY_ID)));
        setName(cursor.getString(cursor.getColumnIndex(ShoppingListContact.Currencies.NAME)));
        setServerId(cursor.getString(cursor.getColumnIndex(ShoppingListContact.Currencies.SERVER_ID)));

        try {
            setDelete(cursor.getInt(cursor.getColumnIndexOrThrow(ShoppingListContact.Currencies.IS_DELETED)) != 0);
        } catch (IllegalArgumentException e) {
            setDelete(false);
        }
        try {
            setDirty(cursor.getInt(cursor.getColumnIndexOrThrow(ShoppingListContact.Currencies.IS_DIRTY)) != 0);
        } catch (IllegalArgumentException e) {
            setDirty(false);
        }
        try {
            setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(ShoppingListContact.Currencies.TIMESTAMP)));
        } catch (IllegalArgumentException e) {
            setTimestamp(0);
        }
    }

    public Currency(ParseObject object) {
        this();
        setId(object.getString(ShoppingListContact.Currencies.CURRENCY_ID));
        setName(object.getString(ShoppingListContact.Currencies.NAME));
        setServerId(object.getObjectId());
        setTimestamp(object.getLong(ServerConstants.TIMESTAMP));
        setDelete(object.getBoolean(ShoppingListContact.Currencies.IS_DELETED));
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
        if (o == null || !(o instanceof Currency)) return false;
        Currency item = (Currency) o;

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

    public static final Parcelable.Creator<Currency> CREATOR = new Creator<Currency>() {

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }

        @Override
        public Currency createFromParcel(Parcel source) {
            return new Currency(source);
        }
    };
}
