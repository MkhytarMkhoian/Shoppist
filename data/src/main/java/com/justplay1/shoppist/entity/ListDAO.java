package com.justplay1.shoppist.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;

import com.justplay1.shoppist.repository.datasource.local.database.DbUtil;

import rx.functions.Func1;

/**
 * Created by Mkhitar on 28.10.2014.
 */
public class ListDAO extends BaseDAO {

    public static final String TABLE = "shopping_lists";

    public static final String LIST_ID = "shopping_list_id";
    public static final String SERVER_ID = "shopping_list_parse_id";
    public static final String LIST_NAME = "shopping_list_name";
    public static final String COLOR = "shopping_list_color";
    public static final String PRIORITY = "shopping_list_priority";
    public static final String TIME_CREATED = "shopping_list_time_created";
    public static final String BOUGHT_COUNT = "shopping_list_bought_count";
    public static final String SIZE = "shopping_list_size";
    public static final String MANUAL_SORT_POSITION = "shopping_list_manual_sort_position";
    public static final String IS_DELETED = "shopping_list_is_deleted";
    public static final String TIMESTAMP = "shopping_list_timestamp";
    public static final String IS_DIRTY = "shopping_list_is_dirty";

    public static final String WHERE_STRING = LIST_ID + " IN(?)";

    private int boughtCount;
    private long timeCreated;
    @PriorityDAO
    private int priority = PriorityDAO.NO_PRIORITY;
    private int color = Color.DKGRAY;
    private int size;
    private int position = -1;

    public ListDAO(String id, String serverId, String name, long timestamp, boolean isDirty,
                   boolean isDelete, int boughtCount, long timeCreated,
                   int priority, int color, int size, int position) {
        super(id, serverId, name, timestamp, isDirty, isDelete);
        this.boughtCount = boughtCount;
        this.timeCreated = timeCreated;
        this.priority = priority;
        this.color = color;
        this.size = size;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public int getSize() {
        return size;
    }

    public int getBoughtCount() {
        return boughtCount;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    @PriorityDAO
    public int getPriority() {
        return priority;
    }

    public int getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof ListDAO)) return false;
        ListDAO item = (ListDAO) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


    public static final Func1<Cursor, ListDAO> MAPPER = (Func1<Cursor, ListDAO>) cursor -> {
        String id = DbUtil.getString(cursor, LIST_ID);
        String name = DbUtil.getString(cursor, LIST_NAME);
        int color = DbUtil.getInt(cursor, COLOR);
        int position = DbUtil.getInt(cursor, MANUAL_SORT_POSITION);
        String serverId = DbUtil.getString(cursor, SERVER_ID);
        boolean isDelete = DbUtil.getBoolean(cursor, IS_DELETED);
        boolean isDirty = DbUtil.getBoolean(cursor, IS_DIRTY);
        long timestamp = DbUtil.getLong(cursor, TIMESTAMP);
        long timeCreated = DbUtil.getLong(cursor, TIME_CREATED);
        int size = DbUtil.getInt(cursor, SIZE);
        int boughtCount = DbUtil.getInt(cursor, BOUGHT_COUNT);
        int priority = DbUtil.getInt(cursor, PRIORITY);
        return new ListDAO(id, serverId, name, timestamp, isDirty, isDelete,
                boughtCount, timeCreated, priority, color, size, position);
    };

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(String id) {
            values.put(LIST_ID, id);
            return this;
        }

        public Builder name(String name) {
            values.put(LIST_NAME, name);
            return this;
        }

        public Builder color(int color) {
            values.put(COLOR, color);
            return this;
        }

        public Builder priority(int priority) {
            values.put(PRIORITY, priority);
            return this;
        }

        public Builder serverId(String serverId) {
            values.put(SERVER_ID, serverId);
            return this;
        }

        public Builder position(int position) {
            values.put(MANUAL_SORT_POSITION, position);
            return this;
        }

        public Builder isDelete(boolean delete) {
            values.put(IS_DELETED, delete ? 1 : 0);
            return this;
        }

        public Builder isDirty(boolean dirty) {
            values.put(IS_DIRTY, dirty ? 1 : 0);
            return this;
        }

        public Builder timestamp(long timestamp) {
            values.put(TIMESTAMP, timestamp);
            return this;
        }

        public Builder timeCreated(long timeCreated) {
            values.put(TIME_CREATED, timeCreated);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
