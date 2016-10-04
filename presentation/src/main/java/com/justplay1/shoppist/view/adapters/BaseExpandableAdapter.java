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

package com.justplay1.shoppist.view.adapters;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemAdapter;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.view.component.actionmode.ActionModeInteractionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseExpandableAdapter<T extends BaseViewModel, GVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder>
        extends BaseAdapter<T>
        implements ExpandableItemAdapter<GVH, CVH> {

    protected List<Pair<HeaderViewModel, List<T>>> data;

    @ColorInt private int noPriority;
    @ColorInt private int noPriorityBackground;
    @ColorInt private int highPriority;
    @ColorInt private int lowPriority;
    @ColorInt private int mediumPriority;

    BaseExpandableAdapter(Context context, ActionModeInteractionListener listener,
                                 RecyclerView recyclerView) {
        super(context, listener, recyclerView);

        noPriority = ContextCompat.getColor(context, R.color.action_mode_toolbar_color);
        noPriorityBackground = ContextCompat.getColor(context, android.R.color.transparent);
        highPriority = ContextCompat.getColor(context, R.color.red_color);
        lowPriority = ContextCompat.getColor(context, R.color.green_500);
        mediumPriority = ContextCompat.getColor(context, R.color.orange_500);

        data = new ArrayList<>(1);
    }

    public List<Pair<HeaderViewModel, List<T>>> getData() {
        return data;
    }

    public void setData(List<Pair<HeaderViewModel, List<T>>> data) {
        this.data = data;
    }

    void setPriorityTextColor(@Priority int priority, TextView view) {
        switch (priority) {
            case Priority.HIGH:
                view.setTextColor(highPriority);
                break;
            case Priority.LOW:
                view.setTextColor(lowPriority);
                break;
            case Priority.MEDIUM:
                view.setTextColor(mediumPriority);
                break;
            case Priority.NO_PRIORITY:
                view.setTextColor(noPriority);
                break;
        }
    }

    void setPriorityBackgroundColor(@Priority int priority, View view) {
        switch (priority) {
            case Priority.HIGH:
                view.setBackgroundColor(highPriority);
                break;
            case Priority.LOW:
                view.setBackgroundColor(lowPriority);
                break;
            case Priority.MEDIUM:
                view.setBackgroundColor(mediumPriority);
                break;
            case Priority.NO_PRIORITY:
                view.setBackgroundColor(noPriorityBackground);
                break;
        }
    }

    @Override
    protected void refreshInvisibleItems() {
        if (linearLayoutManager.findFirstVisibleItemPosition() > 0) {
            notifyItemRangeChanged(0, linearLayoutManager.findFirstVisibleItemPosition());
        }
        notifyItemRangeChanged(linearLayoutManager.findLastVisibleItemPosition() + 1,
                (getGroupCount() + getChildItemsCount()) - linearLayoutManager.findLastVisibleItemPosition());
    }

    private List<T> getItems(boolean checked) {
        List<T> items = new ArrayList<>();
        for (Pair<HeaderViewModel, List<T>> pair : data) {
            for (T item : pair.second) {
                if (checked) {
                    if (item.isChecked()) {
                        items.add(item);
                    }
                } else {
                    items.add(item);
                }
            }
        }
        return items;
    }

    public int getChildItemsCount() {
        if (data == null) return 0;
        int count = 0;
        for (Pair<HeaderViewModel, List<T>> pair : data) {
            count += pair.second.size();
        }
        return count;
    }

    public List<T> getItems() {
        return getItems(false);
    }

    public List<T> getCheckedItems() {
        return getItems(true);
    }

    public int getGroupItemCount(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }
        return data.get(groupPosition).second.size();
    }

    public HeaderViewModel getGroupItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition + " getGroupCount()" + getGroupCount());
        }
        return data.get(groupPosition).first;
    }

    public T getChildItem(int groupPosition, int childPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        final List<T> children = data.get(groupPosition).second;

        if (childPosition < 0 || childPosition >= children.size()) {
            throw new IndexOutOfBoundsException("childPosition = " + childPosition);
        }
        return children.get(childPosition);
    }

    @Override
    public int getGroupCount() {
        if (data == null) return 0;
        return data.size();
    }

    @Override
    public void onBindGroupViewHolder(GVH holder, int groupPosition, int viewType, List<Object> payloads) {
        onBindGroupViewHolder(holder, groupPosition, viewType);
    }

    @Override
    public void onBindChildViewHolder(CVH holder, int groupPosition, int childPosition, int viewType, List<Object> payloads) {
        onBindChildViewHolder(holder, groupPosition, childPosition, viewType);
    }

    @Override
    public int getChildCount(int groupPosition) {
        return data.get(groupPosition).second.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return getGroupItem(groupPosition).hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getChildItem(groupPosition, childPosition).hashCode();
    }

    /**
     * This method will not be called.
     * Override {@link #onCreateGroupViewHolder(android.view.ViewGroup, int)} and
     * {@link #onCreateChildViewHolder(android.view.ViewGroup, int)} instead.
     *
     * @param parent   not used
     * @param viewType not used
     * @return null
     */
    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    /**
     * This method will not be called.
     * Override {@link #getGroupId(int)} and {@link #getChildId(int, int)} instead.
     *
     * @param position not used
     * @return {@link RecyclerView#NO_ID}
     */
    @Override
    public final long getItemId(int position) {
        return RecyclerView.NO_ID;
    }

    /**
     * This method will not be called.
     * Override {@link #getGroupItemViewType(int)} and {@link #getChildItemViewType(int, int)} instead.
     *
     * @param position not used
     * @return 0
     */
    @Override
    public final int getItemViewType(int position) {
        return 0;
    }

    /**
     * This method will not be called.
     * Override {@link #getGroupCount()} and {@link #getChildCount(int)} instead.
     *
     * @return 0
     */
    @Override
    public final int getItemCount() {
        return getChildItemsCount();
    }

    /**
     * This method will not be called.
     * Override {@link #onBindGroupViewHolder(RecyclerView.ViewHolder, int, int)} ()} and
     * {@link #onBindChildViewHolder(RecyclerView.ViewHolder, int, int, int)} instead.
     *
     * @param holder   not used
     * @param position not used
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    /**
     * Override this method if need to customize the behavior.
     * {@inheritDoc}
     */
    @Override
    public boolean onHookGroupExpand(int groupPosition, boolean fromUser) {
        return true;
    }

    /**
     * Override this method if need to customize the behavior.
     * {@inheritDoc}
     */
    @Override
    public boolean onHookGroupCollapse(int groupPosition, boolean fromUser) {
        return true;
    }
}
