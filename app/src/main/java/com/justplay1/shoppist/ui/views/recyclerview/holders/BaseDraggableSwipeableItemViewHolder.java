package com.justplay1.shoppist.ui.views.recyclerview.holders;

import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.annotation.SwipeableItemReactions;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.annotation.SwipeableItemResults;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.annotation.SwipeableItemStateFlags;
import com.justplay1.shoppist.ui.views.recyclerview.ShoppistRecyclerView;

/**
 * Created by Mkhytar on 10.10.2015.
 */
public abstract class BaseDraggableSwipeableItemViewHolder extends BaseDraggableItemViewHolder implements SwipeableItemViewHolder {
    @SwipeableItemStateFlags
    private int mSwipeStateFlags;
    @SwipeableItemResults
    private int mSwipeResult = RecyclerViewSwipeManager.RESULT_NONE;
    @SwipeableItemReactions
    private int mAfterSwipeReaction = RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_DEFAULT;
    private float mHorizontalSwipeAmount;
    private float mVerticalSwipeAmount;
    private float mMaxLeftSwipeAmount = RecyclerViewSwipeManager.OUTSIDE_OF_THE_WINDOW_LEFT;
    private float mMaxUpSwipeAmount = RecyclerViewSwipeManager.OUTSIDE_OF_THE_WINDOW_TOP;
    private float mMaxRightSwipeAmount = RecyclerViewSwipeManager.OUTSIDE_OF_THE_WINDOW_RIGHT;
    private float mMaxDownSwipeAmount = RecyclerViewSwipeManager.OUTSIDE_OF_THE_WINDOW_BOTTOM;

    public BaseDraggableSwipeableItemViewHolder(View itemView) {
        super(itemView);
    }

    public BaseDraggableSwipeableItemViewHolder(View itemView, ShoppistRecyclerView.OnItemClickListener clickListener) {
        super(itemView, clickListener);
    }

    @Override
    public void setSwipeStateFlags(@SwipeableItemStateFlags int flags) {
        mSwipeStateFlags = flags;
    }

    @Override
    @SwipeableItemStateFlags
    public int getSwipeStateFlags() {
        return mSwipeStateFlags;
    }

    @Override
    public void setSwipeResult(@SwipeableItemResults int result) {
        mSwipeResult = result;
    }

    @Override
    @SwipeableItemResults
    public int getSwipeResult() {
        return mSwipeResult;
    }

    @Override
    @SwipeableItemReactions
    public int getAfterSwipeReaction() {
        return mAfterSwipeReaction;
    }

    @Override
    public void setAfterSwipeReaction(@SwipeableItemReactions int reaction) {
        mAfterSwipeReaction = reaction;
    }

    /**
     * Deprecated. Use the {@link #getSwipeItemHorizontalSlideAmount()} instead.
     *
     * @return horizontal swipe amount
     */
    @Deprecated
    public float getSwipeItemSlideAmount() {
        return getSwipeItemHorizontalSlideAmount();
    }

    /**
     * Deprecated. Use the {@link #getSwipeItemHorizontalSlideAmount()} instead.
     *
     * @param amount horizontal swipe amount
     */
    @Deprecated
    public void setSwipeItemSlideAmount(float amount) {
        setSwipeItemHorizontalSlideAmount(amount);
    }

    @Override
    public void setSwipeItemVerticalSlideAmount(float amount) {
        mVerticalSwipeAmount = amount;
    }

    @Override
    public float getSwipeItemVerticalSlideAmount() {
        return mVerticalSwipeAmount;
    }

    @Override
    public void setSwipeItemHorizontalSlideAmount(float amount) {
        mHorizontalSwipeAmount = amount;
    }

    @Override
    public float getSwipeItemHorizontalSlideAmount() {
        return mHorizontalSwipeAmount;
    }

    @Override
    public View getSwipeableContainerView() {
        return container;
    }

    @Override
    public void setMaxLeftSwipeAmount(float amount) {
        mMaxLeftSwipeAmount = amount;
    }

    @Override
    public float getMaxLeftSwipeAmount() {
        return mMaxLeftSwipeAmount;
    }

    @Override
    public void setMaxUpSwipeAmount(float amount) {
        mMaxUpSwipeAmount = amount;
    }

    @Override
    public float getMaxUpSwipeAmount() {
        return mMaxUpSwipeAmount;
    }

    @Override
    public void setMaxRightSwipeAmount(float amount) {
        mMaxRightSwipeAmount = amount;
    }

    @Override
    public float getMaxRightSwipeAmount() {
        return mMaxRightSwipeAmount;
    }

    @Override
    public void setMaxDownSwipeAmount(float amount) {
        mMaxDownSwipeAmount = amount;
    }

    @Override
    public float getMaxDownSwipeAmount() {
        return mMaxDownSwipeAmount;
    }

    @Override
    public void onSlideAmountUpdated(float horizontalAmount, float verticalAmount, boolean isSwiping) {
    }
}