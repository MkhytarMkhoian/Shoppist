package com.justplay1.shoppist.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.justplay1.shoppist.repository.datasource.local.database.DbUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by Mkhytar on 11.11.2015.
 */
public class NotificationDAO{

    public static final String TABLE = "notifications";

    public static final String NOTIFICATION_ID = "notification_id";
    public static final String TITLE = "notifications_title";
    public static final String TIME = "notifications_time";
    public static final String ITEM_NAMES = "notifications_item_names";
    public static final String STATUS = "notifications_status";
    public static final String TYPE = "notifications_type";

    public static final String WHERE_STRING = NOTIFICATION_ID + "=?";

    private String id;
    private String title;
    private List<String> itemNames;
    private long time;
    @NotificationStatus
    private int status = NotificationStatus.NEW;
    @NotificationType
    private int type;

    public NotificationDAO(String id, String title, List<String> itemNames, long time, int status, int type) {
        this.id = id;
        this.title = title;
        this.itemNames = itemNames;
        this.time = time;
        this.status = status;
        this.type = type;
    }

    @NotificationType
    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getItemNames() {
        return itemNames;
    }

    public long getTime() {
        return time;
    }

    @NotificationStatus
    public int getStatus() {
        return status;
    }

    public static List<String> fromJson(String arrayJson) {
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

    public static String toJson(List<String> array) {
        return new JSONArray(array).toString();
    }

    public String itemNamesToSimpleString() {
        if (itemNames == null || itemNames.size() == 0) return "";
        return itemNames.toString().replace(" ", "").replace(",", "\n").replace("[", "").replace("]", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof NotificationDAO)) return false;
        NotificationDAO item = (NotificationDAO) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }


    public static final Func1<Cursor, NotificationDAO> MAPPER = (Func1<Cursor, NotificationDAO>) cursor -> {
        String id = DbUtil.getString(cursor, NOTIFICATION_ID);
        String title = DbUtil.getString(cursor, TITLE);
        int status = DbUtil.getInt(cursor, STATUS);
        List<String> itemNames = NotificationDAO.fromJson(DbUtil.getString(cursor, ITEM_NAMES));
        long time = DbUtil.getLong(cursor, TIME);
        int type = DbUtil.getInt(cursor, TYPE);
        return new NotificationDAO(id, title, itemNames, time, status, type);
    };

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(String id) {
            values.put(NOTIFICATION_ID, id);
            return this;
        }

        public Builder title(String title) {
            values.put(TITLE, title);
            return this;
        }

        public Builder time(long time) {
            values.put(TIME, time);
            return this;
        }

        public Builder status(int status) {
            values.put(STATUS, status);
            return this;
        }

        public Builder type(int type) {
            values.put(TYPE, type);
            return this;
        }

        public Builder itemNames(String itemNames) {
            values.put(ITEM_NAMES, itemNames);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
