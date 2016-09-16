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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemViewHolder;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.models.SortType;
import com.justplay1.shoppist.preferences.AppPreferences;
import com.justplay1.shoppist.utils.ExpandUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.component.ExpandIndicator;
import com.justplay1.shoppist.view.component.actionmode.ActionModeInteractionListener;
import com.justplay1.shoppist.view.component.animboxes.SelectBoxView;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseHeaderHolder;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;

import java.util.Locale;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListAdapter extends BaseListGroupAdapter<ListViewModel, BaseHeaderHolder, BaseItemHolder> {

    public ListAdapter(Context context, ActionModeInteractionListener listener,
                       RecyclerView recyclerView, AppPreferences preferences) {
        super(context, listener, recyclerView, preferences);
    }

    @Override
    public BaseHeaderHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_header, parent, false);
        return new HeaderViewHolder(view, mHeaderClickListener);
    }

    @Override
    public BaseItemHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_list, parent, false);
        ListViewHolder holder = new ListViewHolder(view, mItemClickListener);
        return holder;
    }

    @Override
    public void onBindGroupViewHolder(BaseHeaderHolder holder, int groupPosition, int viewType) {
        if (holder == null) return;
        HeaderViewModel model = getGroupItem(groupPosition);
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        if (mSort == SortType.SORT_BY_PRIORITY) {
            setPriorityTextColor(model.getPriority(), headerHolder.name);
        } else {
            headerHolder.name.setTextColor(mPreferences.getColorPrimary());
        }
        headerHolder.name.setText(model.getName());
        ExpandUtils.toggleIndicator(holder);
    }

    @Override
    public void onBindChildViewHolder(BaseItemHolder viewHolder, int groupPosition, int childPosition, int viewType) {
        ListViewHolder holder = (ListViewHolder) viewHolder;
        ListViewModel item = getChildItem(groupPosition, childPosition);

        holder.childPosition = childPosition;
        holder.groupPosition = groupPosition;

        holder.size.setVisibility(View.VISIBLE);
        holder.name.setText(item.getName());
        holder.size.setText(String.format(Locale.getDefault(), "%d/%d", item.getBoughtCount(), item.getSize()));
        setPriorityBackgroundColor(item.getPriority(), holder.priorityIndicator);

        holder.selectBox.setNormalStateColor(item.getColor());
        holder.selectBox.setInnerText(ShoppistUtils.getFirstCharacter(item.getName()).toUpperCase(Locale.getDefault()));
        holder.selectBox.setEventListener(isChecked -> {
            onCheckItem(item, isChecked);
            holder.setActivated(isChecked);
        });
        holder.selectBox.setChecked(item.isChecked());
        holder.setActivated(item.isChecked());
    }

    public static class HeaderViewHolder extends BaseHeaderHolder {
        protected TextView name;

        public HeaderViewHolder(View itemView, ShoppistRecyclerView.OnHeaderClickListener clickListener) {
            super(itemView, clickListener);
        }

        @Override
        protected void init(View itemView) {
            name = (TextView) itemView.findViewById(R.id.header_name);
            indicator = (ExpandIndicator) itemView.findViewById(R.id.indicator);
        }
    }

    public static class ListViewHolder extends BaseItemHolder implements ExpandableItemViewHolder {
        public ImageView priorityIndicator;
        public TextView name;
        public TextView size;

        private int mExpandStateFlags;

        public ListViewHolder(View itemView, ShoppistRecyclerView.OnItemClickListener clickListener) {
            super(itemView, clickListener);
        }

        public void setColor(@ColorInt int color) {
            itemView.setBackgroundColor(color);
        }

        @Override
        protected void init(View itemView) {
            container = itemView.findViewById(R.id.swipe_container);
            selectBox = (SelectBoxView) itemView.findViewById(R.id.select_box);
            priorityIndicator = (ImageView) itemView.findViewById(R.id.priority_indicator);
            name = (TextView) itemView.findViewById(R.id.item_name);
            size = (TextView) itemView.findViewById(R.id.list_count);
        }

        @Override
        public void setExpandStateFlags(int flags) {
            mExpandStateFlags = flags;
        }

        @Override
        public int getExpandStateFlags() {
            return mExpandStateFlags;
        }
    }
}
