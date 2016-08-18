package com.justplay1.shoppist.adapters;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemViewHolder;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.models.Header;
import com.justplay1.shoppist.models.Product;
import com.justplay1.shoppist.models.Unit;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ExpandUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.ui.views.ExpandIndicator;
import com.justplay1.shoppist.ui.views.animboxes.SelectBoxView;
import com.justplay1.shoppist.ui.views.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseHeaderHolder;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseItemHolder;

import java.util.Locale;

/**
 * Created by Mkhytar on 25.09.2015.
 */
public class GoodsAdapter extends BaseExpandableAdapter<Product, BaseHeaderHolder, GoodsAdapter.GoodsViewHolder> {

    private Category mDefaultCategory;
    private Unit mDefaultUnit;

    public GoodsAdapter(Context context, Cursor cursor, ContentObserver changeObserver) {
        super(context, cursor, changeObserver);
        setHasStableIds(true);
        mDefaultSort = ShoppistPreferences.getSortForGoods();
    }

    @Override
    public boolean isManualSortEnable() {
        return false;
    }

    @Override
    protected Product getModelItem(Cursor cursor) {
        Product product = new Product(cursor);
        if (product.isCategoryEmpty()) {
            product.setCategory(mDefaultCategory);
        }
        if (product.isUnitEmpty()) {
            product.setUnit(mDefaultUnit);
        }
        return product;
    }

    public void setDefaultCategory(Category defaultCategory) {
        mDefaultCategory = defaultCategory;
    }

    public void setDefaultUnit(Unit defaultUnit) {
        mDefaultUnit = defaultUnit;
    }

    @Override
    public void onBindGroupViewHolder(BaseHeaderHolder holder, int groupPosition, int viewType) {
        if (holder == null) return;
        Header model = getGroupItem(groupPosition);
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        if (mDefaultSort == SORT_BY_PRIORITY) {
            headerHolder.name.setTextColor(mContext.getResources().getColor(model.getPriority().mColorRes));
        } else {
            headerHolder.name.setTextColor(ShoppistPreferences.getColorPrimary());
        }
        headerHolder.name.setText(model.getName());

        ExpandUtils.toggleIndicator(holder);
    }

    @Override
    public void onBindChildViewHolder(GoodsViewHolder holder, int groupPosition, int childPosition, int viewType) {
        Product product = getChildItem(groupPosition, childPosition);
        product.setChecked(mSelectItemsManager.isItemChecked(product.getId()));

        holder.childPosition = childPosition;
        holder.groupPosition = groupPosition;

        holder.name.setText(product.getName());

        if (mDefaultSort == SORT_BY_CATEGORIES) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_product_item, parent, false);
        return new GoodsViewHolder(view, mItemClickListener);
    }

    @Override
    public BaseHeaderHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_header, parent, false);
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

            selectBox.setInnerTextTypeface(App.fontRobotoLight);
            name.setTypeface(App.fontRobotoMedium);
            categoryName.setTypeface(App.fontRobotoMedium);
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
