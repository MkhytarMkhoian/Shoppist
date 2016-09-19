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

package com.justplay1.shoppist.view.component;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Mkhytar Mkhoian.
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