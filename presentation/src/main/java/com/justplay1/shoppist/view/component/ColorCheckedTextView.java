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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.util.AttributeSet;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.utils.DrawableUtils;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ColorCheckedTextView extends AppCompatCheckedTextView {

    public ColorCheckedTextView(Context context) {
        super(context);
    }

    public ColorCheckedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void setColor(@ColorInt int color) {
        StateListDrawable states = new StateListDrawable();

        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.abc_btn_radio_to_on_mtrl_000);
        Drawable checked = ContextCompat.getDrawable(getContext(), R.drawable.abc_btn_radio_to_on_mtrl_015);

        if (isEnabled()) {
            states.setColorFilter(DrawableUtils.getColorFilter(color));
        } else {
            states.setColorFilter(DrawableUtils.getColorFilter(Color.GRAY));
        }

        states.addState(new int[]{android.R.attr.stateNotNeeded}, drawable);
        states.addState(new int[]{android.R.attr.state_checked}, checked);
        states.addState(new int[]{android.R.attr.state_enabled}, drawable);
        states.addState(new int[]{-android.R.attr.state_enabled, android.R.attr.state_checked}, checked);
        states.addState(new int[]{-android.R.attr.state_enabled}, drawable);

        setCheckMarkDrawable(states);
    }
}
