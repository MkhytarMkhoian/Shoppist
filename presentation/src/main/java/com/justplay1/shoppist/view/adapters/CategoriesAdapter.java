/*
 * Copyright 2016 Mkhytar Mkhoian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.justplay1.shoppist.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.utils.DraggableUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.component.actionmode.ActionModeInteractionListener;
import com.justplay1.shoppist.view.component.animboxes.SelectBoxView;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseDraggableItemViewHolder;

import java.util.Locale;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CategoriesAdapter extends BaseListAdapter<CategoryViewModel> {

    public CategoriesAdapter(Context context, ActionModeInteractionListener listener,
                             RecyclerView recyclerView) {
        super(context, listener, recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryItemViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CategoryItemViewHolder holder = (CategoryItemViewHolder) viewHolder;
        CategoryViewModel item = getItem(position);

        if (isManualSortModeEnable) {
            holder.dragHandle.setVisibility(View.VISIBLE);
        } else {
            holder.dragHandle.setVisibility(View.GONE);
        }

        holder.name.setText(item.getName());
        holder.selectBox.setNormalStateColor(item.getColor());
        holder.selectBox.setInnerText(ShoppistUtils.getFirstCharacter(item.getName()).toUpperCase(Locale.getDefault()));
        holder.selectBox.setEventListener(isChecked -> {
            onCheckItem(item, isChecked);
            holder.setActivated(isChecked);
        });
        holder.selectBox.setChecked(item.isChecked());
        holder.setActivated(item.isChecked());

        DraggableUtils.clearSelector(holder, holder.container);
    }

    public static class CategoryItemViewHolder extends BaseDraggableItemViewHolder {
        protected TextView name;

        public CategoryItemViewHolder(View itemView, ShoppistRecyclerView.OnItemClickListener clickListener) {
            super(itemView, clickListener);
        }

        @Override
        protected void init(View itemView) {
            dragHandle = itemView.findViewById(R.id.drag_handle);
            container = itemView.findViewById(R.id.swipe_container);
            selectBox = (SelectBoxView) itemView.findViewById(R.id.select_box);
            name = (TextView) itemView.findViewById(R.id.item_name);
        }
    }
}
