package com.justplay1.shoppist.entity;

/**
 * Created by Mkhitar on 05.02.2015.
 */
public abstract class BaseDAO implements ServerModel {

    protected String id;
    protected String serverId;
    protected String name;
    protected long timestamp;
    protected boolean isDirty = false;
    protected boolean isDelete = false;

    public BaseDAO(String id, String serverId, String name, long timestamp, boolean isDirty, boolean isDelete) {
        this.id = id;
        this.serverId = serverId;
        this.name = name;
        this.timestamp = timestamp;
        this.isDirty = isDirty;
        this.isDelete = isDelete;
    }

    @Override
    public String getServerId() {
        return serverId;
    }

    @Override
    public void setServerId(String id) {
        serverId = id;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getId() {
        return id;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public String getName() {
        return name;
    }
}
