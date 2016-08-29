package com.justplay1.shoppist.view.adapters;

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
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.DraggableUtils;
import com.justplay1.shoppist.utils.ExpandUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.component.ExpandIndicator;
import com.justplay1.shoppist.view.component.actionmode.ActionModeOpenCloseListener;
import com.justplay1.shoppist.view.component.animboxes.SelectBoxView;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseDraggableItemViewHolder;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseHeaderHolder;

import java.util.Locale;

/**
 * Created by Mkhitar on 18.08.2015.
 */
public class ListAdapter extends BaseListGroupAdapter<ListViewModel, BaseHeaderHolder, BaseDraggableItemViewHolder> {

    public ListAdapter(Context context, ActionModeOpenCloseListener listener,
                       RecyclerView recyclerView, ShoppistPreferences preferences) {
        super(context, listener, recyclerView, preferences);
    }

    @Override
    public void onMoveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        super.onMoveChildItem(fromGroupPosition, fromChildPosition, toGroupPosition, toChildPosition);
        if (!mPreferences.isManualSortEnableForLists()) {
            mPreferences.setManualSortEnableForShoppingLists(true);
        }
    }

    @Override
    public BaseHeaderHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_header, parent, false);
        return new HeaderViewHolder(view, mHeaderClickListener);
    }

    @Override
    public BaseDraggableItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_list, parent, false);
        ListViewHolder holder = new ListViewHolder(view, mItemClickListener);
        holder.setColor(mPreferences.getColorPrimaryDark());
        return holder;
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
    public void onBindChildViewHolder(BaseDraggableItemViewHolder viewHolder, int groupPosition, int childPosition, int viewType) {
        ListViewHolder holder = (ListViewHolder) viewHolder;
        ListViewModel list = getChildItem(groupPosition, childPosition);
        list.setChecked(isItemChecked(list.getId()));

        holder.childPosition = childPosition;
        holder.groupPosition = groupPosition;

        if (isManualSortModeEnable) {
            holder.dragHandle.setVisibility(View.VISIBLE);
            holder.size.setVisibility(View.GONE);
        } else {
            holder.dragHandle.setVisibility(View.GONE);
            holder.size.setVisibility(View.VISIBLE);
        }

        holder.name.setText(list.getName());
        holder.size.setText(String.format(Locale.getDefault(), "%d/%d", list.getBoughtCount(), list.getSize()));
        setPriorityBackgroundColor(list.getPriority(), holder.priorityIndicator);

        holder.selectBox.setNormalStateColor(list.getColor());
        holder.selectBox.setHolder(holder);
        holder.selectBox.setInnerText(ShoppistUtils.getFirstCharacter(list.getName()).toUpperCase(Locale.getDefault()));
        holder.selectBox.setEventListener(this);
        holder.selectBox.refresh(list.isChecked());

        DraggableUtils.clearSelector(holder, holder.container);
    }

    public static class HeaderViewHolder extends BaseHeaderHolder {
        protected TextView name;

        public HeaderViewHolder(View itemView, ShoppistRecyclerView.OnHeaderClickListener clickListener) {
            super(itemView, clickListener);
        }

        @Override
        protected void init(View itemView) {
            name = (TextView) itemView.findViewById(R.id.header_name);
            indicator = (ExpandIndicator) itemView.findViewById(R.id.indicator);
        }
    }

    public static class ListViewHolder extends BaseDraggableItemViewHolder implements ExpandableItemViewHolder {
        public ImageView priorityIndicator;
        public TextView name;
        public TextView size;

        private int mExpandStateFlags;

        public ListViewHolder(View itemView, ShoppistRecyclerView.OnItemClickListener clickListener) {
            super(itemView, clickListener);
        }

        public void setColor(@ColorInt int color) {
            itemView.setBackgroundColor(color);
        }

        @Override
        protected void init(View itemView) {

            dragHandle = itemView.findViewById(R.id.drag_handle);
            container = itemView.findViewById(R.id.swipe_container);

            selectBox = (SelectBoxView) itemView.findViewById(R.id.select_box);
            priorityIndicator = (ImageView) itemView.findViewById(R.id.priority_indicator);
            name = (TextView) itemView.findViewById(R.id.item_name);
            size = (TextView) itemView.findViewById(R.id.list_count);
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
