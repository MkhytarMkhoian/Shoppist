package com.justplay1.shoppist.adapters;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.BaseModel;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.models.Currency;
import com.justplay1.shoppist.models.Header;
import com.justplay1.shoppist.models.ItemType;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.models.ShoppingListItem;
import com.justplay1.shoppist.models.Status;
import com.justplay1.shoppist.models.Unit;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.ui.views.ExpandIndicator;
import com.justplay1.shoppist.ui.views.animboxes.SelectBoxView;
import com.justplay1.shoppist.ui.views.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseDraggableSwipeableItemViewHolder;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseHeaderHolder;
import com.justplay1.shoppist.utils.DialogUtils;
import com.justplay1.shoppist.utils.DraggableUtils;
import com.justplay1.shoppist.utils.ExpandUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.utils.ViewUtils;

import java.util.Locale;


/**
 * Created by Mkhitar on 22.08.2015.
 */
public class ShoppingListItemAdapter extends BaseListItemGroupAdapter<ShoppingListItem, BaseHeaderHolder, BaseDraggableSwipeableItemViewHolder> {

    private Currency mDefaultCurrency;
    private Category mDefaultCategory;
    private Unit mDefaultUnit;

    public ShoppingListItemAdapter(Context context, Cursor cursor, ContentObserver changeObserver) {
        super(context, cursor, changeObserver);
        mDefaultSort = ShoppistPreferences.getSortForShoppingListItems();
    }

    @Override
    protected int getLeftSwipeActionType() {
        return ShoppistPreferences.getLeftShoppingListItemSwipeAction();
    }

    @Override
    protected int getRightSwipeActionType() {
        return ShoppistPreferences.getRightShoppingListItemSwipeAction();
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
    public void onMoveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        super.onMoveChildItem(fromGroupPosition, fromChildPosition, toGroupPosition, toChildPosition);
        if (!ShoppistPreferences.isManualSortEnableForShoppingListItems()) {
            ShoppistPreferences.setManualSortEnableForShoppingListItems(true);
        }
    }

    @Override
    public boolean isManualSortEnable() {
        return ShoppistPreferences.isManualSortEnableForShoppingListItems();
    }

    public void setDefaultUnit(Unit defaultUnit) {
        mDefaultUnit = defaultUnit;
    }

    public void setDefaultCategory(Category defaultCategory) {
        mDefaultCategory = defaultCategory;
    }

    public void setDefaultCurrency(Currency defaultCurrency) {
        mDefaultCurrency = defaultCurrency;
    }

    @Override
    protected ShoppingListItem getModelItem(Cursor cursor) {
        ShoppingListItem item = new ShoppingListItem(cursor);
        if (item.isCategoryEmpty()) {
            item.setCategory(mDefaultCategory);
        }
        if (item.isCurrencyEmpty()) {
            item.setCurrency(mDefaultCurrency);
        }
        if (item.isUnitEmpty()) {
            item.setUnit(mDefaultUnit);
        }
        return item;
    }

    @Override
    public BaseHeaderHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ItemType.HEADER_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_header, parent, false);
                return new HeaderViewHolder(view, mHeaderClickListener);
            case ItemType.CART_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_header, parent, false);
                return new CartViewHolder(view, mHeaderClickListener);
        }
        return null;
    }

    @Override
    public BaseDraggableSwipeableItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_shopping_list_item, parent, false);
        return new ShoppingListItemViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindGroupViewHolder(BaseHeaderHolder viewHolder, int groupPosition, int viewType) {
        switch (viewType) {
            case ItemType.HEADER_ITEM:
                BaseModel model = getGroupItem(groupPosition);
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
                if (mDefaultSort == SORT_BY_PRIORITY) {
                    if (model.getPriority() == Priority.NO_PRIORITY) {
                        headerViewHolder.name.setTextColor(mContext.getResources().getColor(R.color.action_mode_toolbar_color));
                    } else {
                        headerViewHolder.name.setTextColor(mContext.getResources().getColor(model.getPriority().mColorRes));
                    }
                } else {
                    headerViewHolder.name.setTextColor(ShoppistPreferences.getColorPrimary());
                }
                headerViewHolder.indicator.setBackgroundResource(R.drawable.ic_expand_less);
                headerViewHolder.name.setText(model.getName());
                break;
            case ItemType.CART_HEADER:
                Header header = getGroupItem(groupPosition);
                CartViewHolder cartViewHolder = (CartViewHolder) viewHolder;

                if (header.isShowExpandIndicator()) {
                    cartViewHolder.indicator.setVisibility(View.VISIBLE);
                    cartViewHolder.indicator.setBackgroundResource(R.drawable.ic_expand_more_white);
                } else {
                    cartViewHolder.indicator.setVisibility(View.INVISIBLE);
                }

                cartViewHolder.itemView.setBackgroundColor(ShoppistPreferences.getColorPrimary());
                cartViewHolder.cart.setTextColor(Color.WHITE);
                cartViewHolder.cart.setText(header.getName().toUpperCase(Locale.getDefault()));
                if (header.getTotalPrice() > 0) {
                    cartViewHolder.priceInfo.setText(String.format("%s %s", header.getTotalPrice(), mDefaultCurrency.getName()));
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
    public void onBindChildViewHolder(BaseDraggableSwipeableItemViewHolder viewHolder, int groupPosition, int childPosition, int viewType) {
        ShoppingListItemViewHolder holder = (ShoppingListItemViewHolder) viewHolder;

        holder.childPosition = childPosition;
        holder.groupPosition = groupPosition;

        final ShoppingListItem item = getChildItem(groupPosition, childPosition);
        item.setChecked(mSelectItemsManager.isItemChecked(item.getId()));

        holder.name.setText(item.getName());

        if (isManualSortModeEnable && item.getStatus() == Status.NOT_DONE) {
            holder.dragHandle.setVisibility(View.VISIBLE);
            holder.note.setVisibility(View.GONE);
            holder.info2.setVisibility(View.INVISIBLE);
        } else {
            holder.dragHandle.setVisibility(View.GONE);
            holder.info2.setVisibility(View.VISIBLE);

            if (!item.getNote().isEmpty()) {
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtils.showNoteDialog(mContext, item.getNote());
                    }
                };
                holder.note.setVisibility(View.VISIBLE);
                holder.note.setOnClickListener(listener);
                ViewUtils.setPaddingRight(holder.info2, 0);
            } else {
                ViewUtils.setPaddingRight(holder.info2, mContext.getResources().getDimensionPixelSize(R.dimen.content_margin2x));
                holder.note.setVisibility(View.GONE);
            }
        }

        if (mDefaultSort == SORT_BY_CATEGORIES) {
            holder.categoryName.setVisibility(View.GONE);
        } else {
            holder.categoryName.setVisibility(View.VISIBLE);
            holder.categoryName.setText(item.getCategory().getName());
        }

        if (item.getPrice() == 0 || item.getCurrency().getId().equals(Currency.NO_CURRENCY_ID)) {
            holder.priceAndCurrency.setVisibility(View.GONE);
        } else {
            holder.priceAndCurrency.setVisibility(View.VISIBLE);
            if (ShoppistPreferences.isCalculatePrice()) {
                holder.priceAndCurrency.setText(String.format("%s %s", ShoppistUtils.roundDouble(item.getPrice() * item.getQuantity(), 2),
                        item.getCurrency().getName()));
            } else {
                holder.priceAndCurrency.setText(String.format("%s %s", item.getPrice(), item.getCurrency().getName()));
            }
        }

        if (item.getQuantity() == 0 || item.getUnit().getId().equals(Unit.NO_UNIT_ID)) {
            holder.quantityAndUnit.setVisibility(View.GONE);
        } else {
            holder.quantityAndUnit.setVisibility(View.VISIBLE);
            holder.quantityAndUnit.setText(String.format("%s %s", item.getQuantity(), item.getUnit().getShortName()));
        }

        holder.priorityIndicator.setBackgroundColor(mContext.getResources().getColor(item.getPriority().mColorRes));

        int normalStateColor = item.getCategory().getColor();
        if (item.getStatus() == Status.DONE) {
            holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.categoryName.setPaintFlags(holder.categoryName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            if (ShoppistPreferences.isDiscolorPurchasedGoods()) {
                holder.name.setTextColor(mContext.getResources().getColor(R.color.disable_text_color_black));
                normalStateColor = mContext.getResources().getColor(R.color.grey_300);
                holder.selectBox.setInnerTextColor(mContext.getResources().getColor(R.color.disable_text_color_black));
            }
        } else {
            holder.name.setPaintFlags(holder.name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.categoryName.setPaintFlags(holder.categoryName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

            if (ShoppistPreferences.isDiscolorPurchasedGoods()) {
                holder.name.setTextColor(mContext.getResources().getColor(R.color.text_color_black));
                holder.selectBox.setInnerTextColor(mContext.getResources().getColor(R.color.white));
            }
        }

        holder.selectBox.setNormalStateColor(normalStateColor);
        holder.selectBox.setHolder(holder);
        holder.selectBox.setInnerText(ShoppistUtils.getFirstCharacter(item.getName()).toUpperCase(Locale.getDefault()));
        holder.selectBox.setEventListener(this);
        holder.selectBox.refresh(item.isChecked());

        DraggableUtils.clearSelector(holder, holder.container);

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

    public static class CartViewHolder extends BaseHeaderHolder {
        public TextView cart;
        public TextView priceInfo;

        public CartViewHolder(View itemView, ShoppistRecyclerView.OnHeaderClickListener clickListener) {
            super(itemView, clickListener);
        }

        public CartViewHolder(View itemView) {
            super(itemView, null);
        }

        @Override
        protected void init(View itemView) {
            itemView.setBackgroundColor(ShoppistPreferences.getColorPrimary());
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

    public static class ShoppingListItemViewHolder extends BaseDraggableSwipeableItemViewHolder implements ExpandableItemViewHolder {
        public ImageView priorityIndicator;
        public ImageView note;
        public TextView name;
        public TextView quantityAndUnit;
        public TextView priceAndCurrency;
        public TextView categoryName;
        public LinearLayout info2;

        private int mExpandStateFlags;

        public ShoppingListItemViewHolder(View itemView, ShoppistRecyclerView.OnItemClickListener clickListener) {
            super(itemView, clickListener);
        }

        public ShoppingListItemViewHolder(View itemView) {
            super(itemView, null);
        }

        @Override
        protected void init(View itemView) {
            dragHandle = itemView.findViewById(R.id.drag_handle);
            container = itemView.findViewById(R.id.swipe_container);
            info2 = (LinearLayout) itemView.findViewById(R.id.info2);
            note = (ImageView) itemView.findViewById(R.id.note_image);
            name = (TextView) itemView.findViewById(R.id.item_name);
            priceAndCurrency = (TextView) itemView.findViewById(R.id.price_currency);
            quantityAndUnit = (TextView) itemView.findViewById(R.id.quantity_unit);
            categoryName = (TextView) itemView.findViewById(R.id.category_name);
            priorityIndicator = (ImageView) itemView.findViewById(R.id.priority_indicator);
            selectBox = (SelectBoxView) itemView.findViewById(R.id.select_box);

            selectBox.setInnerTextTypeface(App.fontRobotoLight);
            categoryName.setTypeface(App.fontRobotoRegular);
            name.setTypeface(App.fontRobotoMedium);
            priceAndCurrency.setTypeface(App.fontRobotoRegular);
            quantityAndUnit.setTypeface(App.fontRobotoRegular);
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
