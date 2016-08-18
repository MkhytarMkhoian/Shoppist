package com.justplay1.shoppist.adapters;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemViewHolder;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Header;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.models.ShoppingList;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.ui.views.ExpandIndicator;
import com.justplay1.shoppist.ui.views.animboxes.SelectBoxView;
import com.justplay1.shoppist.utils.DraggableUtils;
import com.justplay1.shoppist.utils.ExpandUtils;
import com.justplay1.shoppist.ui.views.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseDraggableItemViewHolder;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseHeaderHolder;

import java.util.Locale;

/**
 * Created by Mkhitar on 18.08.2015.
 */
public class ShoppingListAdapter extends BaseListGroupAdapter<ShoppingList, BaseHeaderHolder, BaseDraggableItemViewHolder> {

    public ShoppingListAdapter(Context context, Cursor cursor, ContentObserver changeObserver) {
        super(context, cursor, changeObserver);
        mDefaultSort = ShoppistPreferences.getSortForShoppingLists();
    }

    @Override
    public void onMoveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        super.onMoveChildItem(fromGroupPosition, fromChildPosition, toGroupPosition, toChildPosition);
        if (!ShoppistPreferences.isManualSortEnableForShoppingLists()) {
            ShoppistPreferences.setManualSortEnableForShoppingLists(true);
        }
    }

    @Override
    public BaseHeaderHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_header, parent, false);
        return new HeaderViewHolder(view, mHeaderClickListener);
    }

    @Override
    public BaseDraggableItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_shopping_list, parent, false);
        return new ShoppingListViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindGroupViewHolder(BaseHeaderHolder holder, int groupPosition, int viewType) {
        if (holder == null) return;
        Header model = getGroupItem(groupPosition);
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        if (mDefaultSort == SORT_BY_PRIORITY) {
            if (model.getPriority() == Priority.NO_PRIORITY) {
                headerHolder.mName.setTextColor(mContext.getResources().getColor(R.color.action_mode_toolbar_color));
            } else {
                headerHolder.mName.setTextColor(mContext.getResources().getColor(model.getPriority().mColorRes));
            }
        } else {
            headerHolder.mName.setTextColor(ShoppistPreferences.getColorPrimary());
        }
        headerHolder.mName.setText(model.getName());
        ExpandUtils.toggleIndicator(holder);
    }

    @Override
    public void onBindChildViewHolder(BaseDraggableItemViewHolder viewHolder, int groupPosition, int childPosition, int viewType) {
        ShoppingListViewHolder holder = (ShoppingListViewHolder) viewHolder;
        ShoppingList shoppingList = getChildItem(groupPosition, childPosition);
        shoppingList.setChecked(mSelectItemsManager.isItemChecked(shoppingList.getId()));

        holder.childPosition = childPosition;
        holder.groupPosition = groupPosition;

        if (isManualSortModeEnable) {
            holder.dragHandle.setVisibility(View.VISIBLE);
            holder.size.setVisibility(View.GONE);
        } else {
            holder.dragHandle.setVisibility(View.GONE);
            holder.size.setVisibility(View.VISIBLE);
        }

        holder.name.setText(shoppingList.getName());
        holder.size.setText(String.format("%d/%d", shoppingList.getBoughtCount(), shoppingList.getSize()));
        holder.priorityIndicator.setBackgroundColor(mContext.getResources().getColor(shoppingList.getPriority().mColorRes));

        holder.selectBox.setNormalStateColor(shoppingList.getColor());
        holder.selectBox.setHolder(holder);
        holder.selectBox.setInnerText(ShoppistUtils.getFirstCharacter(shoppingList.getName()).toUpperCase(Locale.getDefault()));
        holder.selectBox.setEventListener(this);
        holder.selectBox.refresh(shoppingList.isChecked());

        DraggableUtils.clearSelector(holder, holder.container);
    }

    @Override
    public boolean isManualSortEnable() {
        return ShoppistPreferences.isManualSortEnableForShoppingLists();
    }

    @Override
    protected ShoppingList getModelItem(Cursor cursor) {
        return new ShoppingList(cursor);
    }

    public static class HeaderViewHolder extends BaseHeaderHolder {
        protected TextView mName;

        public HeaderViewHolder(View itemView, ShoppistRecyclerView.OnHeaderClickListener clickListener) {
            super(itemView, clickListener);
        }

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void init(View itemView) {
            mName = (TextView) itemView.findViewById(R.id.header_name);
            indicator = (ExpandIndicator) itemView.findViewById(R.id.indicator);
        }
    }

    public static class ShoppingListViewHolder extends BaseDraggableItemViewHolder implements ExpandableItemViewHolder {
        public ImageView priorityIndicator;
        public TextView name;
        public TextView size;

        private int mExpandStateFlags;

        public ShoppingListViewHolder(View itemView, ShoppistRecyclerView.OnItemClickListener clickListener) {
            super(itemView, clickListener);
        }

        public ShoppingListViewHolder(View itemView) {
            super(itemView, null);
        }

        @Override
        protected void init(View itemView) {
            itemView.setBackgroundColor(ShoppistPreferences.getColorPrimaryDark());
            dragHandle = itemView.findViewById(R.id.drag_handle);
            container = itemView.findViewById(R.id.swipe_container);

            selectBox = (SelectBoxView) itemView.findViewById(R.id.select_box);
            priorityIndicator = (ImageView) itemView.findViewById(R.id.priority_indicator);
            name = (TextView) itemView.findViewById(R.id.item_name);
            size = (TextView) itemView.findViewById(R.id.list_count);

            selectBox.setInnerTextTypeface(App.fontRobotoLight);
            name.setTypeface(App.fontRobotoMedium);
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
