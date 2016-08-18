package com.justplay1.shoppist.models;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.justplay1.shoppist.communication.network.ServerConstants;
import com.justplay1.shoppist.database.ShoppingListContact.ShoppingLists;
import com.parse.ParseObject;

/**
 * Created by Mkhitar on 28.10.2014.
 */
public class ShoppingList extends BaseModel {

    private int boughtCount;
    private long timeCreated;
    private Priority priority;
    private int color;
    private boolean isChecked;
    private int size;
    private int position = -1;

    public ShoppingList() {
        name = "";
        priority = Priority.NO_PRIORITY;
        boughtCount = 0;
        size = 0;
        color = Color.DKGRAY;
        timestamp = 0;
    }

    public ShoppingList(Cursor cursor) {
        this();
        setId(cursor.getString(cursor.getColumnIndex(ShoppingLists.LIST_ID)));
        setName(cursor.getString(cursor.getColumnIndex(ShoppingLists.LIST_NAME)));
        setColor(cursor.getInt(cursor.getColumnIndex(ShoppingLists.COLOR)));
        setBoughtCount(cursor.getInt(cursor.getColumnIndex(ShoppingLists.BOUGHT_COUNT)));
        setSize(cursor.getInt(cursor.getColumnIndex(ShoppingLists.SIZE)));
        setTimeCreated(cursor.getLong(cursor.getColumnIndex(ShoppingLists.TIME_CREATED)));
        setServerId(cursor.getString(cursor.getColumnIndex(ShoppingLists.SERVER_ID)));
        setPosition(cursor.getInt(cursor.getColumnIndex(ShoppingLists.MANUAL_SORT_POSITION)));

        try {
            setDelete(cursor.getInt(cursor.getColumnIndexOrThrow(ShoppingLists.IS_DELETED)) != 0);
        } catch (IllegalArgumentException e) {
            setDelete(false);
        }
        try {
            setDirty(cursor.getInt(cursor.getColumnIndexOrThrow(ShoppingLists.IS_DIRTY)) != 0);
        } catch (IllegalArgumentException e) {
            setDirty(false);
        }
        try {
            setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(ShoppingLists.TIMESTAMP)));
        } catch (IllegalArgumentException e) {
            setTimestamp(0);
        }

        switch (cursor.getInt(cursor.getColumnIndex(ShoppingLists.PRIORITY))) {
            case 0:
                setPriority(Priority.NO_PRIORITY);
                break;
            case 1:
                setPriority(Priority.LOW);
                break;
            case 2:
                setPriority(Priority.MEDIUM);
                break;
            case 3:
                setPriority(Priority.HIGH);
                break;
        }
    }

    public ShoppingList(ShoppingList shoppingList) {
        this();
        setId(shoppingList.getId());
        setName(shoppingList.getName());
        setColor(shoppingList.getColor());
        setTimeCreated(shoppingList.getTimeCreated());
        setPriority(shoppingList.getPriority());
        setSize(shoppingList.getSize());
        setServerId(shoppingList.getServerId());
        setBoughtCount(shoppingList.getBoughtCount());
        setPosition(shoppingList.getPosition());
        setPinned(shoppingList.isPinned());
        setDelete(shoppingList.isDelete());
        setDirty(shoppingList.isDirty());
        setTimestamp(shoppingList.getTimestamp());
    }

    public ShoppingList(ParseObject object) {
        this();
        setId(object.getString(ShoppingLists.LIST_ID));
        setName(object.getString(ShoppingLists.LIST_NAME));
        setColor(object.getInt(ShoppingLists.COLOR));
        setTimeCreated(object.getLong(ShoppingLists.TIME_CREATED));
        setSize(object.getInt(ShoppingLists.SIZE));
        setServerId(object.getObjectId());
        setBoughtCount(object.getInt(ShoppingLists.BOUGHT_COUNT));
        setPosition(object.getInt(ShoppingLists.MANUAL_SORT_POSITION));
        setDelete(object.getBoolean(ShoppingLists.IS_DELETED));
        setTimestamp(object.getLong(ServerConstants.TIMESTAMP));

        switch (object.getInt(ShoppingLists.PRIORITY)) {
            case 0:
                setPriority(Priority.NO_PRIORITY);
                break;
            case 1:
                setPriority(Priority.LOW);
                break;
            case 2:
                setPriority(Priority.MEDIUM);
                break;
            case 3:
                setPriority(Priority.HIGH);
                break;
        }
    }

    public ShoppingList(Parcel parcel) {
        this();
        id = parcel.readString();
        name = parcel.readString();
        boughtCount = parcel.readInt();
        timeCreated = parcel.readLong();
        priority = (Priority) parcel.readSerializable();
        color = parcel.readInt();
        isChecked = parcel.readByte() != 0;
        size = parcel.readInt();
        serverId = parcel.readString();
        position = parcel.readInt();
        mPinned = parcel.readByte() != 0;
        isDelete = parcel.readByte() != 0;
        isDirty = parcel.readByte() != 0;
        timestamp = parcel.readLong();
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
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

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
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
        if (o == null || !(o instanceof ShoppingList)) return false;
        ShoppingList item = (ShoppingList) o;

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
        dest.writeSerializable(priority);
        dest.writeInt(color);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeInt(size);
        dest.writeString(serverId);
        dest.writeInt(position);
        dest.writeByte((byte) (mPinned ? 1 : 0));
        dest.writeByte((byte) (isDelete ? 1 : 0));
        dest.writeByte((byte) (isDirty ? 1 : 0));
        dest.writeLong(timestamp);
    }

    public static final Parcelable.Creator<ShoppingList> CREATOR = new Creator<ShoppingList>() {

        @Override
        public ShoppingList[] newArray(int size) {
            return new ShoppingList[size];
        }

        @Override
        public ShoppingList createFromParcel(Parcel source) {
            return new ShoppingList(source);
        }
    };
}
