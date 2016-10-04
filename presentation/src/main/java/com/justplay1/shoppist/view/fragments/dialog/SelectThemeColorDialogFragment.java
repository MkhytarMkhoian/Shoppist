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
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.component.themedialog.ColorPickerPalette;
import com.justplay1.shoppist.view.component.themedialog.ColorPickerSwatch;

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

    private int[] colorsPrimary;
    private int[] colorPrimaryDark;
    @ColorInt private int selectedColor;
    private int columns = 4;
    private int size;
    private ColorPickerSwatch.OnColorSelectedListener listener;

    @Bind(R.id.color_picker)
    ColorPickerPalette palette;
    @Bind(android.R.id.progress)
    ProgressBar progress;

    public static SelectThemeColorDialogFragment newInstance(@ColorInt int selectedColor) {
        Bundle args = new Bundle();
        args.putInt(Const.SELECTED_COLOR, selectedColor);
        SelectThemeColorDialogFragment fragment = new SelectThemeColorDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnColorSelectedListener(ColorPickerSwatch.OnColorSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        size = ShoppistUtils.isTablet(getActivity()) ? SelectThemeColorDialogFragment.SIZE_LARGE : SelectThemeColorDialogFragment.SIZE_SMALL;
        colorsPrimary = getResources().getIntArray(R.array.color_theme);
        colorPrimaryDark = getResources().getIntArray(R.array.color_status_bar);

        if (getArguments() != null) {
            selectedColor = getArguments().getInt(Const.SELECTED_COLOR);
        }

        if (savedInstanceState != null) {
            colorsPrimary = savedInstanceState.getIntArray(KEY_COLORS);
            selectedColor = savedInstanceState.getInt(KEY_SELECTED_COLOR);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners_dialog);
        View view = inflater.inflate(R.layout.dialog_calendar_color_picker, container, false);
        ButterKnife.bind(this, view);

        palette.init(size, columns, this);
        if (colorsPrimary != null) {
            showPaletteView();
        }
        return view;
    }

    @Override
    public void onColorSelected(int colorPrimary, int colorPrimaryDark) {
        if (listener != null) {
            listener.onColorSelected(colorPrimary, colorPrimaryDark);
        }

        if (colorPrimary != selectedColor) {
            selectedColor = colorPrimary;
            // Redraw palette to show checkmark on newly selected color before dismissing.
            palette.drawPalette(colorsPrimary, this.colorPrimaryDark, selectedColor);
        }
        dismiss();
    }

    public void showPaletteView() {
        if (progress != null && palette != null) {
            progress.setVisibility(View.GONE);
            refreshPalette();
            palette.setVisibility(View.VISIBLE);
        }
    }

    public void showProgressBarView() {
        if (progress != null && palette != null) {
            progress.setVisibility(View.VISIBLE);
            palette.setVisibility(View.GONE);
        }
    }

    public void setColors(int[] colors, int selectedColor) {
        if (colorsPrimary != colors || this.selectedColor != selectedColor) {
            colorsPrimary = colors;
            this.selectedColor = selectedColor;
            refreshPalette();
        }
    }

    public void setColors(int[] colors) {
        if (colorsPrimary != colors) {
            colorsPrimary = colors;
            refreshPalette();
        }
    }

    public void setSelectedColor(int color) {
        if (selectedColor != color) {
            selectedColor = color;
            refreshPalette();
        }
    }

    private void refreshPalette() {
        if (palette != null && colorsPrimary != null) {
            palette.drawPalette(colorsPrimary, colorPrimaryDark, selectedColor);
        }
    }

    public int[] getColors() {
        return colorsPrimary;
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(KEY_COLORS, colorsPrimary);
        outState.putInt(KEY_SELECTED_COLOR, selectedColor);
    }
}