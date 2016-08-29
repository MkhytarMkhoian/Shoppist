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
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.view.component.themedialog.ColorPickerPalette;
import com.justplay1.shoppist.view.component.themedialog.ColorPickerSwatch;
import com.justplay1.shoppist.utils.ShoppistUtils;


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

    private ColorPickerPalette mPalette;
    private ProgressBar mProgress;
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
        mProgress = (ProgressBar) view.findViewById(android.R.id.progress);
        mPalette = (ColorPickerPalette) view.findViewById(R.id.color_picker);
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