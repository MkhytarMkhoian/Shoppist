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

package com.justplay1.shoppist.view.component.themedialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.justplay1.shoppist.R;

/**
 * Creates a circular swatch of a specified color.  Adds a checkmark if marked as checked.
 */
public class ColorPickerSwatch extends FrameLayout implements View.OnClickListener {
    private int colorsPrimary;
    private int colorPrimaryDark;
    private ImageView swatchImage;
    private ImageView checkmarkImage;
    private OnColorSelectedListener onColorSelectedListener;

    /**
     * Interface for a callback when a color square is selected.
     */
    public interface OnColorSelectedListener {

        /**
         * Called when a specific color square has been selected.
         */
        void onColorSelected(int colorPrimary, int colorPrimaryDark);
    }

    public ColorPickerSwatch(Context context) {
        super(context);
    }

    public ColorPickerSwatch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorPickerSwatch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorPickerSwatch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ColorPickerSwatch(Context context, int colorPrimary, int colorPrimaryDark, boolean checked, OnColorSelectedListener listener) {
        super(context);
        colorsPrimary = colorPrimary;
        this.colorPrimaryDark = colorPrimaryDark;
        onColorSelectedListener = listener;

        inflate(context, R.layout.calendar_color_picker_swatch, this);
        swatchImage = (ImageView) findViewById(R.id.color_picker_swatch);
        checkmarkImage = (ImageView) findViewById(R.id.color_picker_checkmark);
        setColor(colorPrimary);
        setChecked(checked);
        setOnClickListener(this);
    }

    protected void setColor(int color) {
        Drawable[] colorDrawable = new Drawable[]
                {ContextCompat.getDrawable(getContext(), R.drawable.calendar_color_picker_swatch)};
        swatchImage.setImageDrawable(new ColorStateDrawable(colorDrawable, color));
    }

    private void setChecked(boolean checked) {
        if (checked) {
            checkmarkImage.setVisibility(View.VISIBLE);
        } else {
            checkmarkImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (onColorSelectedListener != null) {
            onColorSelectedListener.onColorSelected(colorsPrimary, colorPrimaryDark);
        }
    }
}