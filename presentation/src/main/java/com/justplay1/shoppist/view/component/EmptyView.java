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

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.justplay1.shoppist.R;

/**
 * Created by Mkhytar Mkhoian.
 */
public class EmptyView extends RelativeLayout {

    private ProgressBarCircularIndeterminate mProgressBar;
    private TextView mInfoText;

    public EmptyView(Context context) {
        super(context);
        init(context);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_empty_list, this);
        mProgressBar = (ProgressBarCircularIndeterminate) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(INVISIBLE);
        mInfoText = (TextView) findViewById(R.id.empty_text);
    }

    public void showProgressBar() {
        mInfoText.setVisibility(INVISIBLE);
        mProgressBar.setVisibility(VISIBLE);
    }

    public void hideProgressBar() {
        mInfoText.setVisibility(VISIBLE);
        mProgressBar.setVisibility(INVISIBLE);
    }

    public void setProgressBarColor(int color) {
        mProgressBar.setBackgroundColor(color);
    }

    public void setInfoText(int resId) {
        mInfoText.setText(resId);
    }

    public void setInfoImage(@DrawableRes int resId) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
        mInfoText.setCompoundDrawables(null, drawable, null, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mInfoText.setCompoundDrawablesRelative(null, drawable, null, null);
        }
    }
}
