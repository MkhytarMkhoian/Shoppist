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

package com.justplay1.shoppist.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.annotation.SwipeableItemReactions;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseViewModel implements Parcelable {

    protected int mSwipeReaction = SwipeableItemConstants.REACTION_CAN_SWIPE_BOTH_H;
    protected boolean mPinned;

    protected String id;
    protected String name;

    public BaseViewModel() {
    }

    public BaseViewModel(Parcel parcel) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPinned(boolean pinnedToSwipeLeft) {
        mPinned = pinnedToSwipeLeft;
    }

    public boolean isPinned() {
        return mPinned;
    }

    public void setSwipeReaction(int swipeReaction) {
        this.mSwipeReaction = swipeReaction;
    }

    @SwipeableItemReactions
    public int getSwipeReactionType() {
        return mSwipeReaction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeCreated() {
        throw new UnsupportedOperationException();
    }

    @Priority
    public int getPriority() {
        throw new UnsupportedOperationException();
    }

    public void setChecked(boolean checked) {
        throw new UnsupportedOperationException();
    }

    public boolean isChecked() {
        throw new UnsupportedOperationException();
    }

    public
    @ItemType
    int getItemType() {
        return ItemType.LIST_ITEM;
    }

    public CategoryViewModel getCategory() {
        throw new UnsupportedOperationException();
    }

    public boolean getStatus() {
        throw new UnsupportedOperationException();
    }
}
