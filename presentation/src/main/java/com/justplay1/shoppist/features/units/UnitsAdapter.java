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
package com.justplay1.shoppist.features.units;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.shared.base.adapters.BaseListAdapter;
import com.justplay1.shoppist.shared.widget.actionmode.ActionModeInteractionListener;
import com.justplay1.shoppist.shared.widget.recyclerview.holders.BaseItemHolder;
import com.justplay1.shoppist.utils.ShoppistUtils;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mkhytar Mkhoian.
 */
public class UnitsAdapter extends BaseListAdapter<UnitViewModel> {

    public UnitsAdapter(Context context, ActionModeInteractionListener listener,
                        RecyclerView recyclerView) {
        super(context, listener, recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_currency, parent, false);
        UnitItemViewHolder holder = new UnitItemViewHolder(view);
        holder.setClickListener(itemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        UnitItemViewHolder holder = (UnitItemViewHolder) viewHolder;
        UnitViewModel item = getItem(position);
        if (!item.isChecked()) {
            item.setChecked(isItemChecked(item.getId()));
        }

        holder.name.setText(String.format("%s (%s)", item.getName(), item.getShortName()));

        holder.selectBox.setNormalStateColor(ContextCompat.getColor(context, R.color.blue_grey_500));
        holder.selectBox.setInnerText(ShoppistUtils.getFirstCharacter(item.getName()).toUpperCase(Locale.getDefault()));
        holder.selectBox.setEventListener(isChecked -> {
            onCheckItem(item, isChecked);
            holder.setActivated(isChecked);
        });
        holder.selectBox.setChecked(item.isChecked());
        holder.setActivated(item.isChecked());
    }

    static class UnitItemViewHolder extends BaseItemHolder {
        @Bind(R.id.item_name)
        TextView name;

        UnitItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
