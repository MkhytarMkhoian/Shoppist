package com.justplay1.shoppist.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mkhytar on 11.11.2015.
 */
public class NotificationViewModel implements Parcelable {

    private String id;
    private String title;
    private List<String> itemNames;
    private long time;
    private NotificationStatus status;
    @NotificationType
    private int type;

    private boolean isExpand;

    public NotificationViewModel() {
        status = NotificationStatus.NEW;
        itemNames = new ArrayList<>(1);
    }

    @SuppressWarnings("ResourceType")
    protected NotificationViewModel(Parcel in) {
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
        if (itemNames == null || itemNames.size() == 0) return "";
        return itemNames.toString().replace(" ", "").replace(",", "\n").replace("[", "").replace("]", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof NotificationViewModel)) return false;
        NotificationViewModel item = (NotificationViewModel) o;

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

    public static final Creator<NotificationViewModel> CREATOR = new Creator<NotificationViewModel>() {
        @Override
        public NotificationViewModel createFromParcel(Parcel in) {
            return new NotificationViewModel(in);
        }

        @Override
        public NotificationViewModel[] newArray(int size) {
            return new NotificationViewModel[size];
        }
    };
}
