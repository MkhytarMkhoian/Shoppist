package com.justplay1.shoppist.view.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.NotificationViewModel;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.view.component.animboxes.SelectBoxView;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseSwipeableHolder;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mkhytar on 11.11.2015.
 */
public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements SwipeableItemAdapter<NotificationAdapter.NotificationViewHolder> {

    protected Context mContext;
    protected ShoppistPreferences mPreferences;
    protected ShoppistRecyclerView.OnItemClickListener mItemClickListener;
    private List<NotificationViewModel> mData;
    private SwipeEventListener mSwipeEventListener;
    private Calendar mCalendar;
    private int mBlueColor;
    private int mBlackColor;

    public NotificationAdapter(ShoppistRecyclerView.OnItemClickListener onItemClickListener,
                               ShoppistPreferences mPreferences,
                               Context context) {
        this.mItemClickListener = onItemClickListener;
        this.mPreferences = mPreferences;
        this.mContext = context;
        setHasStableIds(true);

        mCalendar = Calendar.getInstance();
        mBlueColor = mContext.getResources().getColor(R.color.blue_700);
        mBlackColor = mContext.getResources().getColor(R.color.text_color_black);
    }

    protected List<NotificationViewModel> getItems() {
        return mData;
    }

    public void setData(List<NotificationViewModel> data) {
        mData = data;
    }

    public void remove(NotificationViewModel item) {
        if (mData != null) {
            mData.remove(item);
        }
    }

    public NotificationViewModel getItem(int position) {
        if (mData == null) return null;
        return mData.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        NotificationViewHolder holder = (NotificationViewHolder) viewHolder;
        NotificationViewModel notification = getItem(position);

        holder.childPosition = position;

        holder.fullMessage.setText(notification.itemNamesToSimpleString());
        holder.name.setText(notification.getTitle());

        mCalendar.setTimeInMillis(notification.getTime());
        holder.time.setText(String.format(Locale.getDefault(), "%s %d", mCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()), mCalendar.get(Calendar.DAY_OF_MONTH)));

        if (notification.getTime() > mPreferences.getLastUserSeenNotificationsTime()) {
            holder.time.setTypeface(App.fontRobotoMedium);
            holder.name.setTypeface(App.fontRobotoMedium);
            holder.name.setTypeface(holder.name.getTypeface(), Typeface.BOLD);
            holder.time.setTypeface(holder.time.getTypeface(), Typeface.BOLD);
            holder.time.setTextColor(mBlueColor);
        } else {
            holder.time.setTypeface(App.fontRobotoRegular);
            holder.name.setTypeface(App.fontRobotoRegular);
            holder.name.setTypeface(holder.name.getTypeface(), Typeface.NORMAL);
            holder.time.setTypeface(holder.time.getTypeface(), Typeface.NORMAL);
            holder.time.setTextColor(mBlackColor);
        }

        holder.selectBox.setNormalStateColor(mContext.getResources().getColor(notification.getStatus().mColorRes));
        holder.selectBox.setInnerImage(notification.getStatus().mImageRes);
        holder.selectBox.setEnabled(false);

        if (notification.isExpand()) {
            holder.fullMessage.setVisibility(View.VISIBLE);
        } else {
            holder.fullMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        if (mData == null) return 0;
        return mData.get(position).hashCode();
    }

    @Override
    public SwipeResultAction onSwipeItem(NotificationViewHolder holder, int position, int result) {
        switch (result) {
            case SwipeableItemConstants.RESULT_SWIPED_RIGHT:
                return new SwipeRemoveAction(this, position);
            case SwipeableItemConstants.RESULT_SWIPED_LEFT:
                return new SwipeRemoveAction(this, position);
        }
        return null;
    }

    @Override
    public int onGetSwipeReactionType(NotificationViewHolder holder, int position, int x, int y) {
        return SwipeableItemConstants.REACTION_CAN_SWIPE_BOTH_H;
    }

    @Override
    public void onSetSwipeBackground(NotificationViewHolder holder, int position, int type) {
        int bgRes = 0;
        switch (type) {
            case SwipeableItemConstants.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_neutral;
                break;
            case SwipeableItemConstants.DRAWABLE_SWIPE_LEFT_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_delete_without_icon;
                break;
            case SwipeableItemConstants.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_delete_without_icon;
                break;
        }
        holder.itemView.setBackgroundResource(bgRes);
    }

    private class SwipeRemoveAction extends SwipeResultAction {
        private NotificationAdapter mAdapter;
        private final int mPosition;
        private NotificationViewModel mCurrentItem;

        SwipeRemoveAction(NotificationAdapter adapter, int position) {
            super(RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM);
            mAdapter = adapter;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            mCurrentItem = getItem(mPosition);
            mAdapter.remove(mCurrentItem);
            mAdapter.notifyItemRemoved(mPosition);

        }

        @Override
        protected void onSlideAnimationEnd() {
            if (mSwipeEventListener != null) {
                mSwipeEventListener.onItemRemoved(mCurrentItem, mPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            // clear the references
            mAdapter = null;
            mCurrentItem = null;
        }
    }

    public static class NotificationViewHolder extends BaseSwipeableHolder {
        public TextView name;
        public TextView time;
        public TextView fullMessage;

        public NotificationViewHolder(View itemView, ShoppistRecyclerView.OnItemClickListener clickListener) {
            super(itemView, clickListener);
        }

        public NotificationViewHolder(View itemView) {
            super(itemView, null);
        }

        @Override
        protected void init(View itemView) {
            container = itemView.findViewById(R.id.swipe_container);
            fullMessage = (TextView) itemView.findViewById(R.id.full_message);
            time = (TextView) itemView.findViewById(R.id.time);
            name = (TextView) itemView.findViewById(R.id.title);
            selectBox = (SelectBoxView) itemView.findViewById(R.id.select_box);
            selectBox.setClickable(false);

            fullMessage.setTypeface(App.fontRobotoRegular);
            name.setTypeface(App.fontRobotoRegular);
            time.setTypeface(App.fontRobotoRegular);
        }
    }

    public SwipeEventListener getSwipeEventListener() {
        return mSwipeEventListener;
    }

    public void setSwipeEventListener(SwipeEventListener swipeEventListener) {
        mSwipeEventListener = swipeEventListener;
    }

    public void setClickListener(ShoppistRecyclerView.OnItemClickListener clickListener) {
        this.mItemClickListener = clickListener;
    }

    public interface SwipeEventListener {
        void onItemRemoved(NotificationViewModel removeItem, int position);
    }
}
