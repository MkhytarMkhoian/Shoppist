package com.justplay1.shoppist.models;

import android.database.Cursor;
import android.os.Parcel;

import com.justplay1.shoppist.communication.network.ServerRequests;
import com.justplay1.shoppist.database.ShoppingListContact;
import com.parse.ParseObject;

/**
 * Created by Mkhitar on 21.07.2015.
 */
public class ShoppingListItem extends BaseModel implements ServerModel {

    private String parentListId;
    private String note;
    private Status status;
    private Category category;
    private Priority priority;
    private boolean isChecked;
    private double price;
    private double quantity;
    private Unit unit;
    private long timeCreated;
    private Currency currency;
    private int position;

    public ShoppingListItem() {
        status = Status.NOT_DONE;
        name = "";
        note = "";
        priority = Priority.NO_PRIORITY;
        quantity = 1;
        position = -1;
        timestamp = 0;
        isDelete = false;
    }

    public ShoppingListItem(ParseObject item, Category category, Unit unit, Currency currency) {
        this();
        setName(item.getString(ShoppingListContact.ShoppingListItems.LIST_ITEM_NAME));
        setId(item.getString(ShoppingListContact.ShoppingListItems.LIST_ITEM_ID));
        setParentListId(item.getString(ShoppingListContact.ShoppingListItems.PARENT_LIST_ID));
        setNote(item.getString(ShoppingListContact.ShoppingListItems.SHORT_DESCRIPTION));
        setUnit(unit);
        setCategory(category);
        setCurrency(currency);
        setPrice(item.getDouble(ShoppingListContact.ShoppingListItems.PRICE));
        setTimeCreated(item.getLong(ShoppingListContact.ShoppingListItems.TIME_CREATED));
        setQuantity(item.getDouble(ShoppingListContact.ShoppingListItems.QUANTITY));
        setServerId(item.getObjectId());
        setTimestamp(item.getLong(ServerRequests.TIMESTAMP));
        setDelete(item.getBoolean(ShoppingListContact.ShoppingListItems.IS_DELETED));

        switch (item.getInt(ShoppingListContact.ShoppingListItems.STATUS)) {
            case 0:
                setStatus(Status.NOT_DONE);
                break;
            case 1:
                setStatus(Status.DONE);
                break;
        }

        switch (item.getInt(ShoppingListContact.ShoppingListItems.PRIORITY)) {
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

    public ShoppingListItem(ShoppingListItem item) {
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
        setServerId(item.getServerId());
        setPosition(item.getPosition());
        setPinned(item.isPinned());
        setDelete(item.isDelete());
        setDirty(item.isDirty());
        setTimestamp(item.getTimestamp());
    }

    public ShoppingListItem(Cursor cursor) {
        this();
        setParentListId(cursor.getString(cursor.getColumnIndex(ShoppingListContact.ShoppingListItems.PARENT_LIST_ID)));
        setId(cursor.getString(cursor.getColumnIndex(ShoppingListContact.ShoppingListItems.LIST_ITEM_ID)));
        setName(cursor.getString(cursor.getColumnIndex(ShoppingListContact.ShoppingListItems.LIST_ITEM_NAME)));
        setNote(cursor.getString(cursor.getColumnIndex(ShoppingListContact.ShoppingListItems.SHORT_DESCRIPTION)));
        setUnit(new Unit(cursor));
        setCurrency(new Currency(cursor));
        setPrice(cursor.getDouble(cursor.getColumnIndex(ShoppingListContact.ShoppingListItems.PRICE)));
        setTimeCreated(cursor.getLong(cursor.getColumnIndex(ShoppingListContact.ShoppingListItems.TIME_CREATED)));
        setQuantity(cursor.getDouble(cursor.getColumnIndex(ShoppingListContact.ShoppingListItems.QUANTITY)));
        setServerId(cursor.getString(cursor.getColumnIndex(ShoppingListContact.ShoppingListItems.SERVER_ID)));
        setPosition(cursor.getInt(cursor.getColumnIndex(ShoppingListContact.ShoppingListItems.MANUAL_SORT_POSITION)));

        try {
            setDelete(cursor.getInt(cursor.getColumnIndexOrThrow(ShoppingListContact.ShoppingListItems.IS_DELETED)) != 0);
        } catch (IllegalArgumentException e) {
            setDelete(false);
        }
        try {
            setDirty(cursor.getInt(cursor.getColumnIndexOrThrow(ShoppingListContact.ShoppingListItems.IS_DIRTY)) != 0);
        } catch (IllegalArgumentException e) {
            setDirty(false);
        }
        try {
            setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(ShoppingListContact.ShoppingListItems.TIMESTAMP)));
        } catch (IllegalArgumentException e) {
            setTimestamp(0);
        }

        switch (cursor.getInt(cursor.getColumnIndex(ShoppingListContact.ShoppingListItems.STATUS))) {
            case 0:
                setStatus(Status.NOT_DONE);
                break;
            case 1:
                setStatus(Status.DONE);
                break;
        }
        switch (cursor.getInt(cursor.getColumnIndex(ShoppingListContact.ShoppingListItems.PRIORITY))) {
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
        setCategory(new Category(cursor, ShoppingListContact.ShoppingListItems.CATEGORY_ID));
    }

    public ShoppingListItem(Parcel parcel) {
        this();
        parentListId = parcel.readString();
        id = parcel.readString();
        name = parcel.readString();
        note = parcel.readString();
        status = (Status) parcel.readSerializable();
        category = parcel.readParcelable(ShoppingListItem.class.getClassLoader());
        priority = (Priority) parcel.readSerializable();
        isChecked = parcel.readByte() != 0;
        price = parcel.readDouble();
        quantity = parcel.readDouble();
        unit = parcel.readParcelable(ShoppingListItem.class.getClassLoader());
        timeCreated = parcel.readLong();
        currency = parcel.readParcelable(ShoppingListItem.class.getClassLoader());
        serverId = parcel.readString();
        position = parcel.readInt();
        mPinned = parcel.readByte() != 0;
        isDelete = parcel.readByte() != 0;
        isDirty = parcel.readByte() != 0;
        timestamp = parcel.readLong();
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isCategoryEmpty() {
        return category == null || category.getId() == null || category.getName() == null || !category.isEnable();
    }

    public boolean isCurrencyEmpty() {
        return currency == null || currency.getId() == null || currency.getName() == null;
    }

    public boolean isUnitEmpty() {
        return unit == null || unit.getId() == null || unit.getName() == null;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
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

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
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
        if (o == null || !(o instanceof ShoppingListItem)) return false;

        ShoppingListItem item = (ShoppingListItem) o;
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
        dest.writeSerializable(status);
        dest.writeParcelable(category, flags);
        dest.writeSerializable(priority);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeDouble(price);
        dest.writeDouble(quantity);
        dest.writeParcelable(unit, flags);
        dest.writeLong(timeCreated);
        dest.writeParcelable(currency, flags);
        dest.writeString(serverId);
        dest.writeInt(position);
        dest.writeByte((byte) (mPinned ? 1 : 0));
        dest.writeByte((byte) (isDelete ? 1 : 0));
        dest.writeByte((byte) (isDirty ? 1 : 0));
        dest.writeLong(timestamp);
    }

    public static final Creator<ShoppingListItem> CREATOR = new Creator<ShoppingListItem>() {

        @Override
        public ShoppingListItem[] newArray(int size) {
            return new ShoppingListItem[size];
        }

        @Override
        public ShoppingListItem createFromParcel(Parcel source) {
            return new ShoppingListItem(source);
        }
    };
}
