/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.justplay1.shoppist.features.lists;

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
import com.justplay1.shoppist.shared.base.adapters.BaseListGroupAdapter;
import com.justplay1.shoppist.shared.widget.actionmode.ActionModeInteractionListener;
import com.justplay1.shoppist.shared.widget.recyclerview.holders.BaseHeaderHolder;
import com.justplay1.shoppist.shared.widget.recyclerview.holders.BaseItemHolder;
import com.justplay1.shoppist.utils.ExpandUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListAdapter extends
    BaseListGroupAdapter<ListViewModel, BaseHeaderHolder, BaseItemHolder> {

    public ListAdapter(Context context, ActionModeInteractionListener listener,
                       RecyclerView recyclerView, AppPreferences preferences) {
        super(context, listener, recyclerView, preferences);
    }

    @Override
    public BaseHeaderHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public BaseItemHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_list, parent, false);
        ListViewHolder holder = new ListViewHolder(view);
        holder.setClickListener(itemClickListener);
        return holder;
    }

    @Override
    public void onBindGroupViewHolder(BaseHeaderHolder holder, int groupPosition, int viewType) {
        HeaderViewModel model = getGroupItem(groupPosition);
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        if (preferences.getSortForShoppingLists() == SortType.SORT_BY_PRIORITY) {
            setPriorityTextColor(model.getPriority(), headerHolder.name);
        } else {
            headerHolder.name.setTextColor(preferences.getColorPrimary());
        }
        headerHolder.name.setText(model.getName());
        ExpandUtils.toggleIndicator(holder);
    }

    @Override
    public void onBindChildViewHolder(BaseItemHolder viewHolder, int groupPosition, int childPosition, int viewType) {
        ListViewHolder holder = (ListViewHolder) viewHolder;
        ListViewModel item = getChildItem(groupPosition, childPosition);
        if (!item.isChecked()) {
            item.setChecked(isItemChecked(item.getId()));
        }
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

    static class HeaderViewHolder extends BaseHeaderHolder {
        @Bind(R.id.header_name)
        TextView name;

        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ListViewHolder extends BaseItemHolder implements ExpandableItemViewHolder {
        @Bind(R.id.priority_indicator)
        ImageView priorityIndicator;
        @Bind(R.id.item_name)
        TextView name;
        @Bind(R.id.list_count)
        TextView size;

        private int mExpandStateFlags;

        ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setColor(@ColorInt int color) {
            itemView.setBackgroundColor(color);
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
