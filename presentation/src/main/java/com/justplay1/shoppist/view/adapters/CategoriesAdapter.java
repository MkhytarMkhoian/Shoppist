package com.justplay1.shoppist.view.adapters;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.ItemType;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.DraggableUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.utils.ViewUtils;
import com.justplay1.shoppist.view.component.actionmode.ActionModeOpenCloseListener;
import com.justplay1.shoppist.view.component.animboxes.SelectBoxView;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseDraggableItemViewHolder;

import java.util.Locale;

/**
 * Created by Mkhitar on 23.08.2015.
 */
public class CategoriesAdapter extends BaseListAdapter<CategoryViewModel>
        implements DraggableItemAdapter<BaseDraggableItemViewHolder> {

    public CategoriesAdapter(Context context, ActionModeOpenCloseListener listener,
                             RecyclerView recyclerView) {
        super(context, listener, recyclerView);
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ItemType.LIST_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_category_item, parent, false);
                return new CategoryItemViewHolder(view, mItemClickListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case ItemType.LIST_ITEM:
                CategoryItemViewHolder holder = (CategoryItemViewHolder) viewHolder;
                CategoryViewModel category = getItem(position);
                category.setChecked(isItemChecked(category.getId()));

                if (isManualSortModeEnable) {
                    holder.dragHandle.setVisibility(View.VISIBLE);
                } else {
                    holder.dragHandle.setVisibility(View.GONE);
                }

                holder.name.setText(category.getName());

                int normalStateColor = category.getColor();
                if (!category.isEnable()) {
                    holder.name.setTextColor(mContext.getResources().getColor(R.color.disable_text_color_black));
                    normalStateColor = mContext.getResources().getColor(R.color.grey_300);
                    holder.selectBox.setInnerTextColor(mContext.getResources().getColor(R.color.disable_text_color_black));
                } else {
                    holder.name.setTextColor(mContext.getResources().getColor(R.color.text_color_black));
                    holder.selectBox.setInnerTextColor(mContext.getResources().getColor(R.color.white));
                }

                holder.selectBox.setNormalStateColor(normalStateColor);
                holder.selectBox.setHolder(holder);
                holder.selectBox.setInnerText(ShoppistUtils.getFirstCharacter(category.getName()).toUpperCase(Locale.getDefault()));
                holder.selectBox.setEventListener(this);
                holder.selectBox.refresh(category.isChecked());

                DraggableUtils.clearSelector(holder, holder.container);
                break;
        }
    }

    @Override
    public boolean onCheckCanStartDrag(BaseDraggableItemViewHolder holder, int position, int x, int y) {
        if (holder.dragHandle == null
                || holder.dragHandle.getVisibility() == View.GONE) return false;

        // x, y --- relative from the itemView's top-left
        final View containerView = holder.container;
        final View dragHandleView = holder.dragHandle;

        final int offsetX = containerView.getLeft() + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
        final int offsetY = containerView.getTop() + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);

        return ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }

        final CategoryViewModel item = mData.remove(fromPosition);
        mData.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
        Log.d("onMoveItem", "fromPosition " + fromPosition + " toPosition " + toPosition);
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(BaseDraggableItemViewHolder holder, int position) {
        return null;
    }

    public static class CategoryItemViewHolder extends BaseDraggableItemViewHolder {
        protected TextView name;

        public CategoryItemViewHolder(View itemView, ShoppistRecyclerView.OnItemClickListener clickListener) {
            super(itemView, clickListener);
        }

        public CategoryItemViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void init(View itemView) {
            dragHandle = itemView.findViewById(R.id.drag_handle);
            container = itemView.findViewById(R.id.swipe_container);
            selectBox = (SelectBoxView) itemView.findViewById(R.id.select_box);
            name = (TextView) itemView.findViewById(R.id.item_name);
            selectBox.setInnerTextTypeface(App.fontRobotoLight);
        }
    }
}
