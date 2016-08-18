package com.justplay1.shoppist.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mkhytar on 11.11.2015.
 */
public class NotificationModel {

    private String id;
    private String title;
    private List<String> itemNames;
    private long time;
    private int status;
    private int type;

    private boolean isExpand;

    public NotificationModel() {
        status = 2;
        itemNames = new ArrayList<>(1);
    }

    public int getType() {
        return type;
    }

    public void setType(int notificationType) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String itemNamesToSimpleString() {
        if (itemNames == null || itemNames.size() == 0) return "";
        return itemNames.toString().replace(" ", "").replace(",", "\n").replace("[", "").replace("]", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof NotificationModel)) return false;
        NotificationModel item = (NotificationModel) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
