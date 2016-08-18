package com.justplay1.shoppist.entity;

import android.content.ContentValues;
import android.database.Cursor;

import com.justplay1.shoppist.repository.datasource.local.database.DbUtil;

import rx.functions.Func1;

/**
 * Created by Mkhitar on 15.05.2015.
 */
public class UnitDAO extends BaseDAO {

    public static final String NO_UNIT_ID = "no_unit";

    public static final String TABLE = "units";

    public static final String SERVER_ID = "unit_parse_id";
    public static final String UNIT_ID = "main_unit_id";
    public static final String FULL_NAME = "unit_full_name";
    public static final String SHORT_NAME = "unit_short_name";
    public static final String TIMESTAMP = "unit_timestamp";
    public static final String IS_DIRTY = "unit_is_dirty";
    public static final String IS_DELETED = "unit_is_deleted";

    public static final String WHERE_STRING = UNIT_ID + " IN(?)";

    private String shortName;

    public UnitDAO(String id) {
        super(id, null, null, 0, false, false);
    }

    public UnitDAO(String id, String serverId, String name, long timestamp,
                   boolean isDirty, boolean isDelete, String shortName) {
        super(id, serverId, name, timestamp, isDirty, isDelete);
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof UnitDAO)) return false;
        UnitDAO item = (UnitDAO) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static final Func1<Cursor, UnitDAO> MAPPER = (Func1<Cursor, UnitDAO>) cursor -> {
        String id = DbUtil.getString(cursor, UNIT_ID);
        String fullName = DbUtil.getString(cursor, FULL_NAME);
        String shortName = DbUtil.getString(cursor, SHORT_NAME);
        String serverId = DbUtil.getString(cursor, SERVER_ID);
        boolean isDelete = DbUtil.getBoolean(cursor, IS_DELETED);
        boolean isDirty = DbUtil.getBoolean(cursor, IS_DIRTY);
        long timestamp = DbUtil.getLong(cursor, TIMESTAMP);
        return new UnitDAO(id, serverId, fullName, timestamp, isDirty, isDelete, shortName);
    };

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(String id) {
            values.put(UNIT_ID, id);
            return this;
        }

        public Builder fullName(String fullName) {
            values.put(FULL_NAME, fullName);
            return this;
        }

        public Builder shortName(String shortName) {
            values.put(SHORT_NAME, shortName);
            return this;
        }

        public Builder serverId(String serverId) {
            values.put(SERVER_ID, serverId);
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

        public ContentValues build() {
            return values;
        }
    }
}
