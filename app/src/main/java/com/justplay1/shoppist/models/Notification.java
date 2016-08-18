package com.justplay1.shoppist.models;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.justplay1.shoppist.database.ShoppingListContact;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mkhytar on 11.11.2015.
 */
public class Notification implements Parcelable {

    private String id;
    private String title;
    private List<String> itemNames;
    private long time;
    private NotificationStatus status;
    private int type;

    private boolean isExpand;

    public Notification() {
        status = NotificationStatus.NEW;
        itemNames = new ArrayList<>(1);
    }

    public Notification(Cursor cursor) {
        setId(cursor.getString(cursor.getColumnIndex(ShoppingListContact.Notifications.NOTIFICATION_ID)));
        setTitle(cursor.getString(cursor.getColumnIndex(ShoppingListContact.Notifications.TITLE)));
        setItemNames(fromJson(cursor.getString(cursor.getColumnIndex(ShoppingListContact.Notifications.ITEM_NAMES))));
        setTime(cursor.getLong(cursor.getColumnIndex(ShoppingListContact.Notifications.TIME)));

        int status = cursor.getInt(cursor.getColumnIndex(ShoppingListContact.Notifications.STATUS));
        switch (status) {
            case 0:
                setStatus(NotificationStatus.UPDATE);
                break;
            case 1:
                setStatus(NotificationStatus.DELETE);
                break;
            case 2:
                setStatus(NotificationStatus.NEW);
                break;
            case 3:
                setStatus(NotificationStatus.BOUGHT);
                break;
            case 4:
                setStatus(NotificationStatus.NOT_BOUGHT);
                break;
        }
    }

    protected Notification(Parcel in) {
        this();
        id = in.readString();
        title = in.readString();
        in.readStringList(itemNames);
        time = in.readLong();
        status = (NotificationStatus) in.readSerializable();
        type = in.readInt();
        isExpand = in.readByte() != 0;
    }

    @NotificationType
    public int getType() {
        return type;
    }

    public void setType(@NotificationType int notificationType) {
        this.type = notificationType;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public List<String> getItemNames() {
        return itemNames;
    }

    public void setItemNames(List<String> itemNames) {
        this.itemNames = itemNames;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public List<String> fromJson(String arrayJson) {
        List<String> result = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(arrayJson);
            for (int i = 0; i < array.length(); i++) {
                result.add(array.optString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String toJson(List<String> array) {
        return new JSONArray(array).toString();
    }

    public String itemNamesToSimpleString() {
        return itemNames.toString().replace(" ", "").replace(",", "\n").replace("[", "").replace("]", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Notification)) return false;
        Notification item = (Notification) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeStringList(itemNames);
        dest.writeLong(time);
        dest.writeSerializable(status);
        dest.writeInt(type);
        dest.writeByte((byte) (isExpand ? 1 : 0));
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
}
