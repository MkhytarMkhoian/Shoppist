/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.justplay1.shoppist.models;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListModel extends BaseModel {

    private final int boughtCount;
    private final long timeCreated;
    private final int priority;
    private final int color;
    private final int size;

    public ListModel(String id, String name, int boughtCount, long timeCreated,
                     int priority, int color, int size) {
        super(id, name);
        this.boughtCount = boughtCount;
        this.timeCreated = timeCreated;
        this.priority = priority;
        this.color = color;
        this.size = size;
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

    public int getPriority() {
        return priority;
    }

    public int getColor() {
        return color;
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
