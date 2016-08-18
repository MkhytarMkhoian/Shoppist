package com.justplay1.shoppist.models;

/**
 * Created by Mkhitar on 28.10.2014.
 */
public class ListModel extends BaseModel {

    private int boughtCount;
    private long timeCreated;
    private int priority;
    private int color;
    private int size;
    private int position = -1;

    public ListModel() {
        priority = 0;
        boughtCount = 0;
        size = 0;
    }

    public ListModel(ListModel list) {
        this();
        setId(list.getId());
        setName(list.getName());
        setColor(list.getColor());
        setTimeCreated(list.getTimeCreated());
        setPriority(list.getPriority());
        setSize(list.getSize());
        setServerId(list.getServerId());
        setBoughtCount(list.getBoughtCount());
        setPosition(list.getPosition());
        setDelete(list.isDelete());
        setDirty(list.isDirty());
        setTimestamp(list.getTimestamp());
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
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
        if (o == null || !(o instanceof ListModel)) return false;
        ListModel item = (ListModel) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
