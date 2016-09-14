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
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.ItemType;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.component.actionmode.ActionModeInteractionListener;
import com.justplay1.shoppist.view.component.animboxes.SelectBoxView;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;

import java.util.Locale;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CurrencyAdapter extends BaseListAdapter<CurrencyViewModel> {

    public CurrencyAdapter(Context context, ActionModeInteractionListener listener,
                           RecyclerView recyclerView) {
        super(context, listener, recyclerView);
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ItemType.LIST_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_currency, parent, false);
                return new CurrencyItemViewHolder(view, mItemClickListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case ItemType.LIST_ITEM:
                CurrencyItemViewHolder holder = (CurrencyItemViewHolder) viewHolder;
                CurrencyViewModel item = getItem(position);

                holder.name.setText(item.getName());

                holder.selectBox.setNormalStateColor(ContextCompat.getColor(mContext, R.color.blue_grey_500));
                holder.selectBox.setInnerText(ShoppistUtils.getFirstCharacter(item.getName()).toUpperCase(Locale.getDefault()));
                holder.selectBox.setEventListener(isChecked -> {
                    onCheckItem(item, isChecked);
                    holder.setActivated(isChecked);
                });
                holder.selectBox.setChecked(item.isChecked());
                break;
        }
    }

    public static class CurrencyItemViewHolder extends BaseItemHolder {
        protected TextView name;

        public CurrencyItemViewHolder(View itemView, ShoppistRecyclerView.OnItemClickListener clickListener) {
            super(itemView, clickListener);
        }

        public CurrencyItemViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void init(View itemView) {
            container = itemView.findViewById(R.id.swipe_container);
            selectBox = (SelectBoxView) itemView.findViewById(R.id.select_box);
            name = (TextView) itemView.findViewById(R.id.item_name);
        }
    }
}
