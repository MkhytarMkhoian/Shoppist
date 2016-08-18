package com.justplay1.shoppist.ui.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Mkhitar on 28.01.2015.
 */
public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {

    private ListView mListView;

    public CustomSwipeRefreshLayout(Context context) {
        super(context);
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setListView(ListView listView) {
        mListView = listView;
    }

    @Override
    public boolean canChildScrollUp() {
        if (mListView != null) {
            if (mListView.getChildAt(0) == null) return false;

            return mListView.getFirstVisiblePosition() > 0 ||
                    mListView.getChildAt(0) == null ||
                    mListView.getChildAt(0).getTop() < 0;

        } else {
            // Fall back to default implementation
            return super.canChildScrollUp();
        }
    }
}