/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.justplay1.shoppist.shared.widget.recyclerview.holders;

import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.annotation.SwipeableItemReactions;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.annotation.SwipeableItemResults;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.annotation.SwipeableItemStateFlags;
import com.justplay1.shoppist.R;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseSwipeableItemViewHolder extends BaseItemHolder implements SwipeableItemViewHolder {

    @SwipeableItemStateFlags
    private int swipeStateFlags;
    @SwipeableItemResults
    private int swipeResult = RecyclerViewSwipeManager.RESULT_NONE;
    @SwipeableItemReactions
    private int afterSwipeReaction = RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_DEFAULT;
    private float horizontalSwipeAmount;
    private float verticalSwipeAmount;
    private float maxLeftSwipeAmount = RecyclerViewSwipeManager.OUTSIDE_OF_THE_WINDOW_LEFT;
    private float maxUpSwipeAmount = RecyclerViewSwipeManager.OUTSIDE_OF_THE_WINDOW_TOP;
    private float maxRightSwipeAmount = RecyclerViewSwipeManager.OUTSIDE_OF_THE_WINDOW_RIGHT;
    private float maxDownSwipeAmount = RecyclerViewSwipeManager.OUTSIDE_OF_THE_WINDOW_BOTTOM;

    @Bind(R.id.swipe_container)
    public View container;

    public BaseSwipeableItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setActivated(boolean isChecked) {
        container.setActivated(isChecked);
    }

    @Override
    public void setSwipeStateFlags(@SwipeableItemStateFlags int flags) {
        swipeStateFlags = flags;
    }

    @Override
    @SwipeableItemStateFlags
    public int getSwipeStateFlags() {
        return swipeStateFlags;
    }

    @Override
    public void setSwipeResult(@SwipeableItemResults int result) {
        swipeResult = result;
    }

    @Override
    @SwipeableItemResults
    public int getSwipeResult() {
        return swipeResult;
    }

    @Override
    @SwipeableItemReactions
    public int getAfterSwipeReaction() {
        return afterSwipeReaction;
    }

    @Override
    public void setAfterSwipeReaction(@SwipeableItemReactions int reaction) {
        afterSwipeReaction = reaction;
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
        verticalSwipeAmount = amount;
    }

    @Override
    public float getSwipeItemVerticalSlideAmount() {
        return verticalSwipeAmount;
    }

    @Override
    public void setSwipeItemHorizontalSlideAmount(float amount) {
        horizontalSwipeAmount = amount;
    }

    @Override
    public float getSwipeItemHorizontalSlideAmount() {
        return horizontalSwipeAmount;
    }

    @Override
    public View getSwipeableContainerView() {
        return container;
    }

    @Override
    public void setMaxLeftSwipeAmount(float amount) {
        maxLeftSwipeAmount = amount;
    }

    @Override
    public float getMaxLeftSwipeAmount() {
        return maxLeftSwipeAmount;
    }

    @Override
    public void setMaxUpSwipeAmount(float amount) {
        maxUpSwipeAmount = amount;
    }

    @Override
    public float getMaxUpSwipeAmount() {
        return maxUpSwipeAmount;
    }

    @Override
    public void setMaxRightSwipeAmount(float amount) {
        maxRightSwipeAmount = amount;
    }

    @Override
    public float getMaxRightSwipeAmount() {
        return maxRightSwipeAmount;
    }

    @Override
    public void setMaxDownSwipeAmount(float amount) {
        maxDownSwipeAmount = amount;
    }

    @Override
    public float getMaxDownSwipeAmount() {
        return maxDownSwipeAmount;
    }

    @Override
    public void onSlideAmountUpdated(float horizontalAmount, float verticalAmount, boolean isSwiping) {
    }
}