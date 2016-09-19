package com.justplay1.shoppist.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ItemType;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.models.SortType;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.preferences.AppPreferences;
import com.justplay1.shoppist.utils.ExpandUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.utils.ViewUtils;
import com.justplay1.shoppist.view.component.ExpandIndicator;
import com.justplay1.shoppist.view.component.actionmode.ActionModeInteractionListener;
import com.justplay1.shoppist.view.component.animboxes.SelectBoxView;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseHeaderHolder;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseSwipeableItemViewHolder;

import java.util.Locale;


/**
 * Created by Mkhytar Mkhoian.
 */
public class ListItemAdapter extends BaseGroupSwipeableItemAdapter<ListItemViewModel, BaseHeaderHolder, BaseSwipeableItemViewHolder> {

    private NoteClickListener mNoteClickListener;

    public ListItemAdapter(Context context, ActionModeInteractionListener listener,
                           RecyclerView recyclerView, AppPreferences preferences) {
        super(context, listener, recyclerView, preferences);
    }

    public void setNoteClickListener(NoteClickListener noteClickListener) {
        this.mNoteClickListener = noteClickListener;
    }

    @Override
    protected int getLeftSwipeActionType() {
        return mPreferences.getLeftShoppingListItemSwipeAction();
    }

    @Override
    protected int getRightSwipeActionType() {
        return mPreferences.getRightShoppingListItemSwipeAction();
    }

    @Override
    protected int getMoveToStatusNotDoneIcon() {
        return R.drawable.bg_swipe_item_move_from_cart_action;
    }

    @Override
    protected int getMoveToStatusDoneIcon() {
        return R.drawable.bg_swipe_item_move_to_cart_action;
    }

    @Override
    public BaseHeaderHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ItemType.HEADER_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_header, parent, false);
                return new HeaderViewHolder(view, mHeaderClickListener);
            case ItemType.CART_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_header, parent, false);
                CartViewHolder holder = new CartViewHolder(view, mHeaderClickListener);
                holder.setColor(mPreferences.getColorPrimary());
                return holder;
        }
        return null;
    }

    @Override
    public BaseSwipeableItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_list_item, parent, false);
        return new ListItemViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindGroupViewHolder(BaseHeaderHolder viewHolder, int groupPosition, int viewType) {
        switch (viewType) {
            case ItemType.HEADER_ITEM:
                BaseViewModel model = getGroupItem(groupPosition);
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
                if (mPreferences.getSortForShoppingListItems() == SortType.SORT_BY_PRIORITY) {
                    if (model.getPriority() == Priority.NO_PRIORITY) {
                        headerViewHolder.name.setTextColor(ContextCompat.getColor(mContext, R.color.action_mode_toolbar_color));
                    } else {
                        setPriorityTextColor(model.getPriority(), headerViewHolder.name);
                    }
                } else {
                    headerViewHolder.name.setTextColor(mPreferences.getColorPrimary());
                }
                headerViewHolder.indicator.setBackgroundResource(R.drawable.ic_expand_less);
                headerViewHolder.name.setText(model.getName());
                break;
            case ItemType.CART_HEADER:
                HeaderViewModel header = getGroupItem(groupPosition);
                CartViewHolder cartViewHolder = (CartViewHolder) viewHolder;

                if (header.isShowExpandIndicator()) {
                    cartViewHolder.indicator.setVisibility(View.VISIBLE);
                    cartViewHolder.indicator.setBackgroundResource(R.drawable.ic_expand_more_white);
                } else {
                    cartViewHolder.indicator.setVisibility(View.INVISIBLE);
                }

                cartViewHolder.itemView.setBackgroundColor(mPreferences.getColorPrimary());
                cartViewHolder.cart.setTextColor(Color.WHITE);
                cartViewHolder.cart.setText(header.getName().toUpperCase(Locale.getDefault()));
                if (header.getTotalPrice() > 0) {
                    cartViewHolder.priceInfo.setText(String.format("%s %s", header.getTotalPrice(), "FFF"));
                    cartViewHolder.priceInfo.setTextColor(Color.WHITE);
                    cartViewHolder.priceInfo.setVisibility(View.VISIBLE);
                } else {
                    cartViewHolder.priceInfo.setVisibility(View.INVISIBLE);
                }
                break;
        }
        ExpandUtils.toggleIndicator(viewHolder);
    }

    @Override
    public void onBindChildViewHolder(BaseSwipeableItemViewHolder viewHolder, int groupPosition, int childPosition, int viewType) {
        ListItemViewHolder holder = (ListItemViewHolder) viewHolder;
        holder.childPosition = childPosition;
        holder.groupPosition = groupPosition;

        final ListItemViewModel item = getChildItem(groupPosition, childPosition);
        item.setChecked(isItemChecked(item.getId()));

        holder.name.setText(item.getName());
        if (item.getStatus()) {
            holder.note.setVisibility(View.GONE);
            holder.info2.setVisibility(View.INVISIBLE);
        } else {
            holder.info2.setVisibility(View.VISIBLE);

            if (!item.getNote().isEmpty()) {
                View.OnClickListener listener = v -> {
                    if (mNoteClickListener != null) {
                        mNoteClickListener.onNoteClick(item.getNote());
                    }
                };
                holder.note.setVisibility(View.VISIBLE);
                holder.note.setOnClickListener(listener);
                ViewUtils.setPaddingRight(holder.info2, 0);
            } else {
                ViewUtils.setPaddingRight(holder.info2, mContext.getResources().getDimensionPixelSize(R.dimen.content2x));
                holder.note.setVisibility(View.GONE);
            }
        }

        if (mPreferences.getSortForShoppingListItems() == SortType.SORT_BY_CATEGORIES) {
            holder.categoryName.setVisibility(View.GONE);
        } else {
            holder.categoryName.setVisibility(View.VISIBLE);
            holder.categoryName.setText(item.getCategory().getName());
        }

        if (item.getPrice() == 0 || item.getCurrency().getId().equals(CurrencyViewModel.NO_CURRENCY_ID)) {
            holder.priceAndCurrency.setVisibility(View.GONE);
        } else {
            holder.priceAndCurrency.setVisibility(View.VISIBLE);
            if (mPreferences.isCalculatePrice()) {
                holder.priceAndCurrency.setText(String.format("%s %s", ShoppistUtils.roundDouble(item.getPrice() * item.getQuantity(), 2),
                        item.getCurrency().getName()));
            } else {
                holder.priceAndCurrency.setText(String.format("%s %s", item.getPrice(), item.getCurrency().getName()));
            }
        }

        if (item.getQuantity() == 0 || item.getUnit().getId().equals(UnitViewModel.NO_UNIT_ID)) {
            holder.quantityAndUnit.setVisibility(View.GONE);
        } else {
            holder.quantityAndUnit.setVisibility(View.VISIBLE);
            holder.quantityAndUnit.setText(String.format("%s %s", item.getQuantity(), item.getUnit().getShortName()));
        }

        setPriorityBackgroundColor(item.getPriority(), holder.priorityIndicator);

        int normalStateColor = item.getCategory().getColor();
        if (item.getStatus()) {
            holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.categoryName.setPaintFlags(holder.categoryName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.name.setTextColor(ContextCompat.getColor(mContext, R.color.disable_text_color_black));
            normalStateColor = ContextCompat.getColor(mContext, R.color.grey_300);
            holder.selectBox.setInnerTextColor(ContextCompat.getColor(mContext, R.color.disable_text_color_black));
        } else {
            holder.name.setPaintFlags(holder.name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.categoryName.setPaintFlags(holder.categoryName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.name.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_black));
            holder.selectBox.setInnerTextColor(ContextCompat.getColor(mContext, R.color.white));
        }

        holder.selectBox.setNormalStateColor(normalStateColor);
        holder.selectBox.setInnerText(ShoppistUtils.getFirstCharacter(item.getName()).toUpperCase(Locale.getDefault()));
        holder.selectBox.setEventListener(isChecked -> {
            onCheckItem(item, isChecked);
            holder.setActivated(isChecked);
        });
        holder.selectBox.setChecked(item.isChecked());
        holder.setActivated(item.isChecked());

        if (item.isPinned()) {
            if (holder.getSwipeResult() == SwipeableItemConstants.RESULT_SWIPED_RIGHT) {
                holder.setSwipeItemHorizontalSlideAmount(SwipeableItemConstants.OUTSIDE_OF_THE_WINDOW_RIGHT);
            } else {
                holder.setSwipeItemHorizontalSlideAmount(SwipeableItemConstants.OUTSIDE_OF_THE_WINDOW_LEFT);
            }
        } else {
            holder.setSwipeItemHorizontalSlideAmount(0);
        }
    }

    public interface NoteClickListener {

        void onNoteClick(String note);
    }

    public static class CartViewHolder extends BaseHeaderHolder {
        public TextView cart;
        public TextView priceInfo;

        public CartViewHolder(View itemView, ShoppistRecyclerView.OnHeaderClickListener clickListener) {
            super(itemView, clickListener);
        }

        public CartViewHolder(View itemView) {
            super(itemView, null);
        }

        public void setColor(@ColorInt int color) {
            itemView.setBackgroundColor(color);
        }

        @Override
        protected void init(View itemView) {
            itemView.findViewById(R.id.line).setVisibility(View.GONE);
            cart = (TextView) itemView.findViewById(R.id.header_name);
            priceInfo = (TextView) itemView.findViewById(R.id.header_price_info);
            indicator = (ExpandIndicator) itemView.findViewById(R.id.indicator);
        }
    }

    public static class HeaderViewHolder extends BaseHeaderHolder {
        public TextView name;

        public HeaderViewHolder(View itemView, ShoppistRecyclerView.OnHeaderClickListener clickListener) {
            super(itemView, clickListener);
        }

        public HeaderViewHolder(View itemView) {
            super(itemView, null);
        }

        @Override
        protected void init(View itemView) {
            name = (TextView) itemView.findViewById(R.id.header_name);
            indicator = (ExpandIndicator) itemView.findViewById(R.id.indicator);
        }
    }

    public static class ListItemViewHolder extends BaseSwipeableItemViewHolder implements ExpandableItemViewHolder {
        public ImageView priorityIndicator;
        public ImageView note;
        public TextView name;
        public TextView quantityAndUnit;
        public TextView priceAndCurrency;
        public TextView categoryName;
        public LinearLayout info2;

        private int mExpandStateFlags;

        public ListItemViewHolder(View itemView, ShoppistRecyclerView.OnItemClickListener clickListener) {
            super(itemView, clickListener);
        }

        public ListItemViewHolder(View itemView) {
            super(itemView, null);
        }

        @Override
        protected void init(View itemView) {
            container = itemView.findViewById(R.id.swipe_container);
            info2 = (LinearLayout) itemView.findViewById(R.id.info2);
            note = (ImageView) itemView.findViewById(R.id.note_image);
            name = (TextView) itemView.findViewById(R.id.item_name);
            priceAndCurrency = (TextView) itemView.findViewById(R.id.price_currency);
            quantityAndUnit = (TextView) itemView.findViewById(R.id.quantity_unit);
            categoryName = (TextView) itemView.findViewById(R.id.category_name);
            priorityIndicator = (ImageView) itemView.findViewById(R.id.priority_indicator);
            selectBox = (SelectBoxView) itemView.findViewById(R.id.select_box);
        }

        @Override
        public void setExpandStateFlags(int flags) {
            mExpandStateFlags = flags;
        }

        @Override
        public int getExpandStateFlags() {
            return mExpandStateFlags;
        }

        @Override
        public View getSwipeableContainerView() {
            return container;
        }
    }
}
