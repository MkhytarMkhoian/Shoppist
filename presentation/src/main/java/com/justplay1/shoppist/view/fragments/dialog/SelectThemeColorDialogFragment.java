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

package com.justplay1.shoppist.view.fragments.dialog;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.view.component.themedialog.ColorPickerPalette;
import com.justplay1.shoppist.view.component.themedialog.ColorPickerSwatch;
import com.justplay1.shoppist.utils.ShoppistUtils;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A dialog which takes in as input an array of colors and creates a palette allowing the user to
 * select a specific color swatch, which invokes a listener.
 */
public class SelectThemeColorDialogFragment extends DialogFragment implements ColorPickerSwatch.OnColorSelectedListener {

    public static final int SIZE_LARGE = 1;
    public static final int SIZE_SMALL = 2;

    protected static final String KEY_COLORS = "colors";
    protected static final String KEY_SELECTED_COLOR = "selected_color";

    private int[] mColorsPrimary;
    private int[] mColorPrimaryDark;
    private @ColorInt int mSelectedColor;
    private int mColumns = 4;
    private int mSize;

    @Bind(R.id.color_button)
    ColorPickerPalette mPalette;
    @Bind(android.R.id.progress)
    ProgressBar mProgress;
    private ColorPickerSwatch.OnColorSelectedListener mListener;

    public static SelectThemeColorDialogFragment newInstance(@ColorInt int selectedColor) {
        Bundle args = new Bundle();
        args.putInt(Const.SELECTED_COLOR, selectedColor);
        SelectThemeColorDialogFragment fragment = new SelectThemeColorDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnColorSelectedListener(ColorPickerSwatch.OnColorSelectedListener listener) {
        mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSize = ShoppistUtils.isTablet(getActivity()) ? SelectThemeColorDialogFragment.SIZE_LARGE : SelectThemeColorDialogFragment.SIZE_SMALL;
        mColorsPrimary = getResources().getIntArray(R.array.color_theme);
        mColorPrimaryDark = getResources().getIntArray(R.array.color_status_bar);

        if (getArguments() != null){
            mSelectedColor = getArguments().getInt(Const.SELECTED_COLOR);
        }

        if (savedInstanceState != null) {
            mColorsPrimary = savedInstanceState.getIntArray(KEY_COLORS);
            mSelectedColor = savedInstanceState.getInt(KEY_SELECTED_COLOR);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners_dialog);
        View view = inflater.inflate(R.layout.dialog_calendar_color_picker, container, false);
        ButterKnife.bind(this, view);

        mPalette.init(mSize, mColumns, this);
        if (mColorsPrimary != null) {
            showPaletteView();
        }
        return view;
    }

    @Override
    public void onColorSelected(int colorPrimary, int colorPrimaryDark) {
        if (mListener != null) {
            mListener.onColorSelected(colorPrimary, colorPrimaryDark);
        }

        if (colorPrimary != mSelectedColor) {
            mSelectedColor = colorPrimary;
            // Redraw palette to show checkmark on newly selected color before dismissing.
            mPalette.drawPalette(mColorsPrimary, mColorPrimaryDark, mSelectedColor);
        }
        dismiss();
    }

    public void showPaletteView() {
        if (mProgress != null && mPalette != null) {
            mProgress.setVisibility(View.GONE);
            refreshPalette();
            mPalette.setVisibility(View.VISIBLE);
        }
    }

    public void showProgressBarView() {
        if (mProgress != null && mPalette != null) {
            mProgress.setVisibility(View.VISIBLE);
            mPalette.setVisibility(View.GONE);
        }
    }

    public void setColors(int[] colors, int selectedColor) {
        if (mColorsPrimary != colors || mSelectedColor != selectedColor) {
            mColorsPrimary = colors;
            mSelectedColor = selectedColor;
            refreshPalette();
        }
    }

    public void setColors(int[] colors) {
        if (mColorsPrimary != colors) {
            mColorsPrimary = colors;
            refreshPalette();
        }
    }

    public void setSelectedColor(int color) {
        if (mSelectedColor != color) {
            mSelectedColor = color;
            refreshPalette();
        }
    }

    private void refreshPalette() {
        if (mPalette != null && mColorsPrimary != null) {
            mPalette.drawPalette(mColorsPrimary, mColorPrimaryDark, mSelectedColor);
        }
    }

    public int[] getColors() {
        return mColorsPrimary;
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(KEY_COLORS, mColorsPrimary);
        outState.putInt(KEY_SELECTED_COLOR, mSelectedColor);
    }
}