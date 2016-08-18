package com.justplay1.shoppist.adapters;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableDraggableItemAdapter;
import com.justplay1.shoppist.models.BaseModel;
import com.justplay1.shoppist.models.Header;

import java.util.List;

/**
 * Created by Mkhytar on 25.09.2015.
 */
public abstract class BaseExpandableDraggableAdapter<T extends BaseModel, GVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder>
        extends BaseExpandableAdapter<T, GVH, CVH>
        implements ExpandableDraggableItemAdapter<GVH, CVH> {

    public BaseExpandableDraggableAdapter(Context context, Cursor cursor, ContentObserver changeObserver) {
        super(context, cursor, changeObserver);
    }

    @Override
    public void onMoveGroupItem(int fromGroupPosition, int toGroupPosition) {
        if (fromGroupPosition == toGroupPosition) {
            return;
        }
        final Pair<Header, List<T>> item = mData.remove(fromGroupPosition);
        mData.add(toGroupPosition, item);
    }

    @Override
    public void onMoveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        if ((fromGroupPosition == toGroupPosition) && (fromChildPosition == toChildPosition)) {
            return;
        }
        final Pair<Header, List<T>> fromGroup = mData.get(fromGroupPosition);
        final Pair<Header, List<T>> toGroup = mData.get(toGroupPosition);

        final T item = fromGroup.second.remove(fromChildPosition);
        toGroup.second.add(toChildPosition, item);
    }
}
