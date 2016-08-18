package com.justplay1.shoppist.entity;

import android.content.ContentValues;
import android.database.Cursor;

import com.justplay1.shoppist.repository.datasource.local.database.DbUtil;

import rx.functions.Func1;

/**
 * Created by Mkhitar on 15.05.2015.
 */
public class CurrencyDAO extends BaseDAO {

    public static final String NO_CURRENCY_ID = "no_currency";

    public static final String TABLE = "currency";

    public static final String SERVER_ID = "currency_parse_id";
    public static final String CURRENCY_ID = "main_currency_id";
    public static final String NAME = "currency_name";
    public static final String TIMESTAMP = "currency_timestamp";
    public static final String IS_DIRTY = "currency_is_dirty";
    public static final String IS_DELETED = "currency_is_deleted";

    public static final String WHERE_STRING = CURRENCY_ID + " IN(?)";

    public CurrencyDAO(String id, String serverId, String name, long timestamp, boolean isDirty, boolean isDelete) {
        super(id, serverId, name, timestamp, isDirty, isDelete);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof CurrencyDAO)) return false;
        CurrencyDAO item = (CurrencyDAO) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return 31 * id.hashCode();
    }

    public static final Func1<Cursor, CurrencyDAO> MAPPER = (Func1<Cursor, CurrencyDAO>) cursor -> {
        String id = DbUtil.getString(cursor, CURRENCY_ID);
        String name = DbUtil.getString(cursor, NAME);
        String serverId = DbUtil.getString(cursor, SERVER_ID);
        boolean isDelete = DbUtil.getBoolean(cursor, IS_DELETED);
        boolean isDirty = DbUtil.getBoolean(cursor, IS_DIRTY);
        long timestamp = DbUtil.getLong(cursor, TIMESTAMP);
        return new CurrencyDAO(id, serverId, name, timestamp, isDirty, isDelete);
    };

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(String id) {
            values.put(CURRENCY_ID, id);
            return this;
        }

        public Builder name(String name) {
            values.put(NAME, name);
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
