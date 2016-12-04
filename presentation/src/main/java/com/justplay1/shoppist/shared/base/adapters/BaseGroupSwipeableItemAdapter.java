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

package com.justplay1.shoppist.shared.base.adapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableSwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.models.SwipeActionType;
import com.justplay1.shoppist.preferences.AppPreferences;
import com.justplay1.shoppist.shared.widget.actionmode.ActionModeInteractionListener;
import com.justplay1.shoppist.shared.widget.recyclerview.holders.BaseHeaderHolder;
import com.justplay1.shoppist.shared.widget.recyclerview.holders.BaseSwipeableItemViewHolder;
import com.justplay1.shoppist.utils.DrawableUtils;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseGroupSwipeableItemAdapter<T extends BaseViewModel, GVH extends BaseHeaderHolder, CVH extends BaseSwipeableItemViewHolder>
        extends BaseListGroupAdapter<T, GVH, CVH>
        implements ExpandableSwipeableItemAdapter<GVH, CVH> {

    private SwipeEventListener<T> swipeEventListener;
    private RecyclerViewExpandableItemManager expandableItemManager;

    public BaseGroupSwipeableItemAdapter(Context context, ActionModeInteractionListener listener,
                                         RecyclerView recyclerView, AppPreferences preferences) {
        super(context, listener, recyclerView, preferences);
    }

    public void setExpandableItemManager(RecyclerViewExpandableItemManager expandableItemManager) {
        this.expandableItemManager = expandableItemManager;
    }

    public RecyclerViewExpandableItemManager getExpandableItemManager() {
        return expandableItemManager;
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return getGroupItem(groupPosition).getItemType();
    }

    @Override
    public int onGetGroupItemSwipeReactionType(GVH holder, int groupPosition, int x, int y) {
        return SwipeableItemConstants.REACTION_CAN_NOT_SWIPE_BOTH_H;
    }

    @Override
    public int onGetChildItemSwipeReactionType(CVH holder, int groupPosition, int childPosition, int x, int y) {
        return getChildItem(groupPosition, childPosition).getSwipeReactionType();
    }

    @Override
    public void onSetGroupItemSwipeBackground(GVH holder, int groupPosition, int type) {
        int bgResId = 0;
        switch (type) {
            case SwipeableItemConstants.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
                bgResId = R.drawable.bg_swipe_item_neutral;
                break;
            case SwipeableItemConstants.DRAWABLE_SWIPE_LEFT_BACKGROUND:
                bgResId = R.drawable.bg_swipe_group_item_right;
                break;
            case SwipeableItemConstants.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                bgResId = R.drawable.bg_swipe_group_item_right;
                break;
        }
        holder.itemView.setBackgroundResource(bgResId);
    }

    @Override
    public void onSetChildItemSwipeBackground(CVH holder, int groupPosition, int childPosition, int type) {
        int bgResId = R.drawable.bg_swipe_item_neutral;
        LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(context, bgResId);
        switch (type) {
            case SwipeableItemConstants.DRAWABLE_SWIPE_LEFT_BACKGROUND:
                switch (getLeftSwipeActionType()) {
                    case SwipeActionType.MOVE_ITEM:
                        if (getChildItem(groupPosition, childPosition).getStatus()) {
                            bgResId = getMoveToStatusNotDoneIcon();
                        } else {
                            bgResId = getMoveToStatusDoneIcon();
                        }
                        break;
                    case SwipeActionType.DELETE_ITEM:
                        bgResId = R.drawable.bg_swipe_item_delete_action;
                        break;
                    case SwipeActionType.EDIT_ITEM:
                        bgResId = R.drawable.bg_swipe_item_edit_action;
                        break;
                }
                drawable = (LayerDrawable) ContextCompat.getDrawable(context, bgResId);
                ((BitmapDrawable) drawable.findDrawableByLayerId(R.id.image)).setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                break;
            case SwipeableItemConstants.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                bgResId = R.drawable.bg_swipe_item_move_to_cart_action;
                switch (getRightSwipeActionType()) {
                    case SwipeActionType.MOVE_ITEM:
                        if (getChildItem(groupPosition, childPosition).getStatus()) {
                            bgResId = getMoveToStatusNotDoneIcon();
                        } else {
                            bgResId = getMoveToStatusDoneIcon();
                        }
                        break;
                    case SwipeActionType.DELETE_ITEM:
                        bgResId = R.drawable.bg_swipe_item_delete_action;
                        break;
                    case SwipeActionType.EDIT_ITEM:
                        bgResId = R.drawable.bg_swipe_item_edit_action;
                        break;
                }
                drawable = (LayerDrawable) ContextCompat.getDrawable(context, bgResId);
                ((BitmapDrawable) drawable.findDrawableByLayerId(R.id.image)).setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                break;
        }
        DrawableUtils.setViewDrawable(holder.itemView, drawable);
    }

    @Override
    public SwipeResultAction onSwipeGroupItem(GVH holder, int groupPosition, int result) {
        return new GroupUnpinResultAction();
    }

    @Override
    public SwipeResultAction onSwipeChildItem(CVH holder, int groupPosition, int childPosition, int result) {
        switch (result) {
            case SwipeableItemConstants.RESULT_SWIPED_RIGHT:
                switch (getRightSwipeActionType()) {
                    case SwipeActionType.MOVE_ITEM:
                        return new ChildSwipeMoveAction(this, groupPosition, childPosition);
                    case SwipeActionType.DELETE_ITEM:
                        return new ChildSwipeRemoveAction(this, groupPosition, childPosition);
                    case SwipeActionType.EDIT_ITEM:
                        if (getChildItem(groupPosition, childPosition).isPinned()) {
                            // pinned --- back to default position
                            return new ChildUnpinResultAction(this, groupPosition, childPosition);
                        } else {
                            // not pinned --- remove
                            return new ChildSwipeEditAction(this, groupPosition, childPosition);
                        }
                }
            case SwipeableItemConstants.RESULT_SWIPED_LEFT:
                switch (getLeftSwipeActionType()) {
                    case SwipeActionType.MOVE_ITEM:
                        return new ChildSwipeMoveAction(this, groupPosition, childPosition);
                    case SwipeActionType.DELETE_ITEM:
                        return new ChildSwipeRemoveAction(this, groupPosition, childPosition);
                    case SwipeActionType.EDIT_ITEM:
                        if (getChildItem(groupPosition, childPosition).isPinned()) {
                            // pinned --- back to default position
                            return new ChildUnpinResultAction(this, groupPosition, childPosition);
                        } else {
                            // not pinned --- remove
                            return new ChildSwipeEditAction(this, groupPosition, childPosition);
                        }
                }
            case SwipeableItemConstants.RESULT_CANCELED:
            default:
                return new ChildUnpinResultAction(this, groupPosition, childPosition);
        }
    }

    public void removeChildItem(int groupPosition, int childPosition) {
        data.get(groupPosition).second.remove(childPosition);
    }

    public void removeGroupItem(int groupPosition) {
        data.remove(groupPosition);
    }

    public interface SwipeEventListener<T extends BaseViewModel> {
        void onChildItemMoved(T moveItem);

        void onChildItemRemoved(T removeItem, int groupPosition, int childPosition);

        void onChildItemEdit(T editItem, int groupPosition, int childPosition);
    }

    public void setSwipeEventListener(SwipeEventListener<T> swipeEventListener) {
        this.swipeEventListener = swipeEventListener;
    }

    protected abstract int getMoveToStatusNotDoneIcon();

    protected abstract int getMoveToStatusDoneIcon();

    protected abstract int getLeftSwipeActionType();

    protected abstract int getRightSwipeActionType();

    private class GroupUnpinResultAction extends SwipeResultActionDefault {
        GroupUnpinResultAction() {

        }
    }

    private class ChildSwipeEditAction extends BaseSwipeAction {
        private boolean mSetPinned;

        ChildSwipeEditAction(BaseGroupSwipeableItemAdapter<T, GVH, CVH> adapter, int groupPosition, int childPosition) {
            super(RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_MOVE_TO_SWIPED_DIRECTION, adapter, groupPosition, childPosition);
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();
            mCurrentItem = getChildItem(mGroupPosition, mChildPosition);
            if (!mCurrentItem.isPinned()) {
                mCurrentItem.setPinned(true);
                mAdapter.getExpandableItemManager().notifyGroupItemChanged(mGroupPosition);
                mSetPinned = true;
            }
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();
            if (mSetPinned && swipeEventListener != null) {
                swipeEventListener.onChildItemEdit(mCurrentItem, mGroupPosition, mChildPosition);
            }
        }
    }

    private abstract class BaseChildSwipeMoveAction extends BaseSwipeAction {

        BaseChildSwipeMoveAction(int resultAction, BaseGroupSwipeableItemAdapter<T, GVH, CVH> adapter, int groupPosition, int childPosition) {
            super(resultAction, adapter, groupPosition, childPosition);
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            mCurrentItem = getChildItem(mGroupPosition, mChildPosition);
            mAdapter.removeChildItem(mGroupPosition, mChildPosition);
            mAdapter.getExpandableItemManager().notifyChildItemRemoved(mGroupPosition, mChildPosition);

            if (mAdapter.data.get(mGroupPosition).second.size() == 0) {
                mAdapter.removeGroupItem(mGroupPosition);
                mAdapter.getExpandableItemManager().notifyGroupItemRemoved(mGroupPosition);
            }
        }
    }

    private class ChildSwipeMoveAction extends BaseChildSwipeMoveAction {

        ChildSwipeMoveAction(BaseGroupSwipeableItemAdapter<T, GVH, CVH> adapter, int groupPosition, int childPosition) {
            super(RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM, adapter, groupPosition, childPosition);
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();
            if (swipeEventListener != null) {
                swipeEventListener.onChildItemMoved(mCurrentItem);
            }
        }
    }

    private class ChildSwipeRemoveAction extends BaseChildSwipeMoveAction {

        ChildSwipeRemoveAction(BaseGroupSwipeableItemAdapter<T, GVH, CVH> adapter, int groupPosition, int childPosition) {
            super(RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_MOVE_TO_SWIPED_DIRECTION, adapter, groupPosition, childPosition);
        }

        @Override
        protected void onPerformAction() {
            if (!preferences.isNeedShowConfirmDeleteDialog()) {
                super.onPerformAction();
            } else {
                mCurrentItem = getChildItem(mGroupPosition, mChildPosition);
                if (!mCurrentItem.isPinned()) {
                    mCurrentItem.setPinned(true);
                    mAdapter.expandableItemManager.notifyGroupItemChanged(mGroupPosition);
                }
            }
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();
            if (swipeEventListener != null) {
                swipeEventListener.onChildItemRemoved(mCurrentItem, mGroupPosition, mChildPosition);
            }
        }
    }

    private class ChildUnpinResultAction extends SwipeResultActionDefault {
        protected BaseGroupSwipeableItemAdapter<T, GVH, CVH> mAdapter;
        private final int mGroupPosition;
        private final int mChildPosition;

        ChildUnpinResultAction(BaseGroupSwipeableItemAdapter<T, GVH, CVH> adapter, int groupPosition, int childPosition) {
            mAdapter = adapter;
            mGroupPosition = groupPosition;
            mChildPosition = childPosition;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            T item = mAdapter.getChildItem(mGroupPosition, mChildPosition);
            if (item.isPinned()) {
                item.setPinned(false);
                mAdapter.getExpandableItemManager().notifyChildItemChanged(mGroupPosition, mChildPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            mAdapter = null;
        }
    }

    private abstract class BaseSwipeAction extends SwipeResultAction {

        protected BaseGroupSwipeableItemAdapter<T, GVH, CVH> mAdapter;
        protected final int mGroupPosition;
        protected final int mChildPosition;
        protected T mCurrentItem;

        public BaseSwipeAction(int resultAction, BaseGroupSwipeableItemAdapter<T, GVH, CVH> adapter, int groupPosition, int childPosition) {
            super(resultAction);
            mAdapter = adapter;
            mGroupPosition = groupPosition;
            mChildPosition = childPosition;
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            mAdapter = null;
            mCurrentItem = null;
        }
    }
}
