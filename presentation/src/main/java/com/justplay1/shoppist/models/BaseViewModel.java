package com.justplay1.shoppist.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.annotation.SwipeableItemReactions;

/**
 * Created by Mkhitar on 05.02.2015.
 */
public abstract class BaseViewModel implements Parcelable {

    protected int mSwipeReaction = SwipeableItemConstants.REACTION_CAN_SWIPE_BOTH_H;
    protected boolean mPinned;

    protected String id;
    protected String serverId;
    protected String name;
    protected long timestamp;
    protected boolean isDelete = false;
    protected boolean isDirty = false;

    public BaseViewModel() {
    }

    public BaseViewModel(Parcel parcel) {
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String id) {
        serverId = id;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
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

    public int getItemType() {
        return ItemType.LIST_ITEM;
    }

    public CategoryViewModel getCategory() {
        throw new UnsupportedOperationException();
    }

    public boolean getStatus() {
        throw new UnsupportedOperationException();
    }

    public int getPosition() {
        throw new UnsupportedOperationException();
    }

    public void setPosition(int position) {
        throw new UnsupportedOperationException();
    }
}
