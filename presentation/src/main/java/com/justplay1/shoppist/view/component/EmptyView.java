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

    private ProgressBarCircularIndeterminate progressBar;
    private TextView infoText;

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
        progressBar = (ProgressBarCircularIndeterminate) findViewById(R.id.progress_bar);
        progressBar.setVisibility(INVISIBLE);
        infoText = (TextView) findViewById(R.id.empty_text);
    }

    public void showProgressBar() {
        infoText.setVisibility(INVISIBLE);
        progressBar.setVisibility(VISIBLE);
    }

    public void hideProgressBar() {
        infoText.setVisibility(VISIBLE);
        progressBar.setVisibility(INVISIBLE);
    }

    public void setProgressBarColor(int color) {
        progressBar.setBackgroundColor(color);
    }

    public void setInfoText(int resId) {
        infoText.setText(resId);
    }

    public void setInfoImage(@DrawableRes int resId) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
        infoText.setCompoundDrawables(null, drawable, null, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            infoText.setCompoundDrawablesRelative(null, drawable, null, null);
        }
    }
}
