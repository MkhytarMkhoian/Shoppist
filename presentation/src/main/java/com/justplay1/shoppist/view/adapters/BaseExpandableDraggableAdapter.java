package com.justplay1.shoppist.view.adapters;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableDraggableItemAdapter;
import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.view.component.actionmode.ActionModeOpenCloseListener;

import java.util.List;

/**
 * Created by Mkhytar on 25.09.2015.
 */
public abstract class BaseExpandableDraggableAdapter<T extends BaseViewModel, GVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder>
        extends BaseExpandableAdapter<T, GVH, CVH>
        implements ExpandableDraggableItemAdapter<GVH, CVH> {

    public BaseExpandableDraggableAdapter(Context context, ActionModeOpenCloseListener listener,
                                          RecyclerView recyclerView) {
        super(context, listener, recyclerView);
    }

    @Override
    public void onMoveGroupItem(int fromGroupPosition, int toGroupPosition) {
        if (fromGroupPosition == toGroupPosition) {
            return;
        }
        final Pair<HeaderViewModel, List<T>> item = mData.remove(fromGroupPosition);
        mData.add(toGroupPosition, item);
    }

    @Override
    public void onMoveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        if ((fromGroupPosition == toGroupPosition) && (fromChildPosition == toChildPosition)) {
            return;
        }
        final Pair<HeaderViewModel, List<T>> fromGroup = mData.get(fromGroupPosition);
        final Pair<HeaderViewModel, List<T>> toGroup = mData.get(toGroupPosition);

        final T item = fromGroup.second.remove(fromChildPosition);
        toGroup.second.add(toChildPosition, item);
    }
}
