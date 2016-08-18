package com.justplay1.shoppist.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.models.Product;
import com.justplay1.shoppist.models.Unit;
import com.justplay1.shoppist.ui.activities.SearchActivity;
import com.justplay1.shoppist.ui.views.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseItemHolder;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mkhytar on 10.02.2016.
 */
public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private Context mContext;
    private Map<String, Product> mItems;
    private List<Product> mItemsToDisplay;
    private ShoppistRecyclerView.OnItemClickListener mItemClickListener;
    private final int mContextType;
    private final Drawable iconEndDrawable;

    public SearchAdapter(Context context, final int contextType) {
        mContextType = contextType;
        mContext = context;
        setHasStableIds(true);
        mItems = new HashMap<>();
        mItemsToDisplay = new ArrayList<>();
        iconEndDrawable = mContext.getResources().getDrawable(R.drawable.ic_add_black_24dp);
        iconEndDrawable.setColorFilter(mContext.getResources().getColor(R.color.action_mode_toolbar_color), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_search_item_suggestion, parent, false);
        return new SearchViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        SearchViewHolder holder = (SearchViewHolder) viewHolder;
        Product item = getItem(position);
        holder.text1.setText(item.getName());

        OvalShape ovalShape = new OvalShape();
        ShapeDrawable drawable = new ShapeDrawable(ovalShape);
        drawable.getPaint().setColor(item.getCategory().getColor());
        holder.typeIcon.setBackgroundDrawable(drawable);
        if (!item.getId().equals("000")) {
            holder.text2.setText(item.getCategory().getName());
        } else {
            holder.text2.setVisibility(View.GONE);
        }

        switch (mContextType) {
            case SearchActivity.CONTEXT_QUICK_ADD_GOODS_TO_LIST:
                holder.iconEnd.setBackgroundDrawable(iconEndDrawable);
                break;
            case SearchActivity.CONTEXT_QUICK_SEARCH_IN_GOODS_LIST:
                holder.iconEnd.setVisibility(View.GONE);
                break;
        }
    }

    public void setData(Map<String, Product> items) {
        mItems = items;
        mItemsToDisplay.clear();
        mItemsToDisplay.addAll(mItems.values());
        notifyDataSetChanged();
    }

    public Product getItem(int position) {
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
                    final List<Product> newValues = new ArrayList<>();

                    if (!mItems.containsKey(prefixString)) {
                        Product product = new Product();
                        product.setId("000");
                        product.setName(constraint.toString());
                        Unit unit = new Unit();
                        unit.setId(Unit.NO_UNIT_ID);
                        product.setUnit(unit);
                        Category category = new Category();
                        category.setId(Category.NO_CATEGORY_ID);
                        product.setCategory(category);
                        newValues.add(product);
                    }

                    for (final Product value : mItems.values()) {
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
                mItemsToDisplay.addAll((Collection<Product>) results.values);
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

        ImageView typeIcon, iconEnd;
        TextView text1, text2;

        public SearchViewHolder(final View itemView) {
            super(itemView);
        }

        public SearchViewHolder(View itemView, ShoppistRecyclerView.OnItemClickListener clickListener) {
            super(itemView, clickListener);
        }

        @Override
        protected void init(View itemView) {
            typeIcon = (ImageView) itemView.findViewById(R.id.type_icon);
            iconEnd = (ImageView) itemView.findViewById(R.id.icon_end);
            text1 = (TextView) itemView.findViewById(R.id.text1);
            text2 = (TextView) itemView.findViewById(R.id.text2);
        }
    }
}
