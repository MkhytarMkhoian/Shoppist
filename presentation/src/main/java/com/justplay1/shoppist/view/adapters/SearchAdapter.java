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
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.view.SearchView;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mkhytar Mkhoian.
 */
public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private Map<String, ProductViewModel> mItems;
    private List<ProductViewModel> mItemsToDisplay;
    private ShoppistRecyclerView.OnItemClickListener mItemClickListener;
    private final int mContextType;
    private final Drawable iconEndDrawable;

    public SearchAdapter(Context context, final int contextType) {
        mContextType = contextType;
        setHasStableIds(true);
        mItems = new HashMap<>();
        mItemsToDisplay = new ArrayList<>();
        iconEndDrawable = ContextCompat.getDrawable(context, R.drawable.ic_add_black_24dp);
        iconEndDrawable.setColorFilter(ContextCompat.getColor(context, R.color.action_mode_toolbar_color), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_suggestion, parent, false);
        return new SearchViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        SearchViewHolder holder = (SearchViewHolder) viewHolder;
        ProductViewModel item = getItem(position);
        holder.text1.setText(item.getName());

        OvalShape ovalShape = new OvalShape();
        ShapeDrawable drawable = new ShapeDrawable(ovalShape);
        drawable.getPaint().setColor(item.getCategory().getColor());
        holder.typeIcon.setBackgroundDrawable(drawable);
        if (!item.getId().equals(SearchView.JUST_NAME)) {
            holder.text2.setText(item.getCategory().getName());
        } else {
            holder.text2.setVisibility(View.GONE);
        }

        switch (mContextType) {
            case Const.CONTEXT_QUICK_ADD_GOODS_TO_LIST:
                holder.iconEnd.setBackgroundDrawable(iconEndDrawable);
                break;
            case Const.CONTEXT_QUICK_SEARCH_IN_GOODS_LIST:
                holder.iconEnd.setVisibility(View.GONE);
                break;
        }
    }

    public void setData(Map<String, ProductViewModel> items) {
        mItems = items;
        mItemsToDisplay.clear();
        mItemsToDisplay.addAll(mItems.values());
        ;
    }

    public ProductViewModel getItem(int position) {
        return mItemsToDisplay.get(position);
    }

    @Override
    public int getItemCount() {
        return mItemsToDisplay.size();
    }

    @Override
    public long getItemId(int position) {
        return mItemsToDisplay.get(position).hashCode();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = mItems.values();
                    results.count = mItems.size();
                } else {
                    String prefixString = constraint.toString().toLowerCase();
                    final List<ProductViewModel> newValues = new ArrayList<>();

                    if (!mItems.containsKey(prefixString)) {
                        ProductViewModel product = new ProductViewModel();
                        product.setId(SearchView.JUST_NAME);
                        product.setName(constraint.toString());
                        UnitViewModel unit = new UnitViewModel();
                        unit.setId(UnitViewModel.NO_UNIT_ID);
                        product.setUnit(unit);
                        CategoryViewModel category = new CategoryViewModel();
                        category.setId(CategoryViewModel.NO_CATEGORY_ID);
                        product.setCategory(category);
                        newValues.add(product);
                    }

                    for (final ProductViewModel value : mItems.values()) {
                        final String valueText = value.getName().toLowerCase();

                        if (valueText.startsWith(prefixString)) {
                            newValues.add(value);
                        } else {
                            final String[] words = valueText.split(" ");

                            // Start at index 0, in case valueText starts with space(s)
                            for (String word : words) {
                                if (word.startsWith(prefixString)) {
                                    int start = valueText.length() - word.length();
                                    newValues.add(value);
                                    break;
                                }
                            }
                        }
                    }
                    results.values = newValues;
                    results.count = newValues.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                if (constraint == null) return;
                if (results.values == null) return;

                mItemsToDisplay.clear();
                mItemsToDisplay.addAll((Collection<ProductViewModel>) results.values);
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyItemRangeRemoved(0, getItemCount());
                }
            }
        };
    }

    public ShoppistRecyclerView.OnItemClickListener getClickListener() {
        return mItemClickListener;
    }

    public void setClickListener(ShoppistRecyclerView.OnItemClickListener clickListener) {
        this.mItemClickListener = clickListener;
    }

    public static class SearchViewHolder extends BaseItemHolder {
        @Bind(R.id.icon_end)
        ImageView iconEnd;
        @Bind(R.id.type_icon)
        ImageView typeIcon;
        @Bind(R.id.text2)
        TextView text2;
        @Bind(R.id.text1)
        TextView text1;

        public SearchViewHolder(View itemView, ShoppistRecyclerView.OnItemClickListener clickListener) {
            super(itemView, clickListener);
            ButterKnife.bind(this, itemView);
        }
    }
}
