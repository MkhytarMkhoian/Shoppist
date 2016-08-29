package com.justplay1.shoppist.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemViewHolder;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.SortType;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ExpandUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.component.ExpandIndicator;
import com.justplay1.shoppist.view.component.actionmode.ActionModeOpenCloseListener;
import com.justplay1.shoppist.view.component.animboxes.SelectBoxView;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseHeaderHolder;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;

import java.util.Locale;

/**
 * Created by Mkhytar on 25.09.2015.
 */
public class GoodsAdapter extends BaseExpandableAdapter<ProductViewModel, BaseHeaderHolder, GoodsAdapter.GoodsViewHolder> {

    private ShoppistPreferences mPreferences;

    public GoodsAdapter(Context context, ActionModeOpenCloseListener listener,
                        RecyclerView recyclerView, ShoppistPreferences preferences) {
        super(context, listener, recyclerView);
        setHasStableIds(true);
        mPreferences = preferences;
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
    public void onBindChildViewHolder(GoodsViewHolder holder, int groupPosition, int childPosition, int viewType) {
        ProductViewModel product = getChildItem(groupPosition, childPosition);
        product.setChecked(isItemChecked(product.getId()));

        holder.childPosition = childPosition;
        holder.groupPosition = groupPosition;

        holder.name.setText(product.getName());

        if (mSort == SortType.SORT_BY_CATEGORIES) {
            holder.categoryName.setVisibility(View.GONE);
        } else {
            holder.categoryName.setVisibility(View.VISIBLE);
            holder.categoryName.setText(product.getCategory().getName());
        }

        holder.selectBox.setNormalStateColor(product.getCategory().getColor());
        holder.selectBox.setHolder(holder);
        holder.selectBox.setInnerText(ShoppistUtils.getFirstCharacter(product.getName()).toUpperCase(Locale.getDefault()));
        holder.selectBox.setEventListener(this);
        holder.selectBox.refresh(product.isChecked());
    }

    @Override
    public GoodsViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new GoodsViewHolder(view, mItemClickListener);
    }

    @Override
    public BaseHeaderHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_header, parent, false);
        return new HeaderViewHolder(view, mHeaderClickListener);
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

    public static class HeaderViewHolder extends BaseHeaderHolder {
        protected TextView name;

        public HeaderViewHolder(View itemView, ShoppistRecyclerView.OnHeaderClickListener clickListener) {
            super(itemView, clickListener);
        }

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void init(View itemView) {
            name = (TextView) itemView.findViewById(R.id.header_name);
            indicator = (ExpandIndicator) itemView.findViewById(R.id.indicator);
        }
    }

    public static class GoodsViewHolder extends BaseItemHolder implements ExpandableItemViewHolder {
        public TextView name;
        public TextView categoryName;

        private int mExpandStateFlags;

        public GoodsViewHolder(View itemView, ShoppistRecyclerView.OnItemClickListener clickListener) {
            super(itemView, clickListener);
        }

        public GoodsViewHolder(View itemView) {
            super(itemView, null);
        }

        @Override
        protected void init(View itemView) {
            selectBox = (SelectBoxView) itemView.findViewById(R.id.select_box);
            name = (TextView) itemView.findViewById(R.id.item_name);
            categoryName = (TextView) itemView.findViewById(R.id.category_name);
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
