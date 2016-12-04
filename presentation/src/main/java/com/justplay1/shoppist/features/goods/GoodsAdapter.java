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

package com.justplay1.shoppist.features.goods;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemViewHolder;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.SortType;
import com.justplay1.shoppist.preferences.AppPreferences;
import com.justplay1.shoppist.shared.base.adapters.BaseExpandableAdapter;
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
public class GoodsAdapter extends
    BaseExpandableAdapter<ProductViewModel, BaseHeaderHolder, GoodsAdapter.GoodsViewHolder> {

    private AppPreferences preferences;

    public GoodsAdapter(Context context, ActionModeInteractionListener listener,
                        RecyclerView recyclerView, AppPreferences preferences) {
        super(context, listener, recyclerView);
        this.preferences = preferences;
    }

    @Override
    public void onBindGroupViewHolder(BaseHeaderHolder holder, int groupPosition, int viewType) {
        HeaderViewModel model = getGroupItem(groupPosition);
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        if (preferences.getSortForGoods() == SortType.SORT_BY_PRIORITY) {
            setPriorityTextColor(model.getPriority(), headerHolder.name);
        } else {
            headerHolder.name.setTextColor(preferences.getColorPrimary());
        }
        headerHolder.name.setText(model.getName());

        ExpandUtils.toggleIndicator(holder);
    }

    @Override
    public void onBindChildViewHolder(GoodsViewHolder holder, int groupPosition, int childPosition, int viewType) {
        ProductViewModel item = getChildItem(groupPosition, childPosition);
        if (!item.isChecked()) {
            item.setChecked(isItemChecked(item.getId()));
        }

        holder.childPosition = childPosition;
        holder.groupPosition = groupPosition;

        holder.name.setText(item.getName());

        if (preferences.getSortForGoods() == SortType.SORT_BY_CATEGORIES) {
            holder.categoryName.setVisibility(View.GONE);
        } else {
            holder.categoryName.setVisibility(View.VISIBLE);
            holder.categoryName.setText(item.getCategory().getName());
        }

        holder.selectBox.setNormalStateColor(item.getCategory().getColor());
        holder.selectBox.setInnerText(ShoppistUtils.getFirstCharacter(item.getName()).toUpperCase(Locale.getDefault()));
        holder.selectBox.setEventListener(isChecked -> {
            onCheckItem(item, isChecked);
            holder.setActivated(isChecked);
        });
        holder.selectBox.setChecked(item.isChecked());
        holder.setActivated(item.isChecked());
    }

    @Override
    public GoodsViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        GoodsViewHolder holder = new GoodsViewHolder(view);
        holder.setClickListener(itemClickListener);
        return holder;
    }

    @Override
    public BaseHeaderHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(BaseHeaderHolder holder, int groupPosition, int x, int y, boolean expand) {
        return holder.itemView.isEnabled();
    }

    static class HeaderViewHolder extends BaseHeaderHolder {
        @Bind(R.id.header_name)
        TextView name;

        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class GoodsViewHolder extends BaseItemHolder implements ExpandableItemViewHolder {
        @Bind(R.id.item_name)
        TextView name;
        @Bind(R.id.category_name)
        TextView categoryName;

        private int mExpandStateFlags;

        GoodsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
