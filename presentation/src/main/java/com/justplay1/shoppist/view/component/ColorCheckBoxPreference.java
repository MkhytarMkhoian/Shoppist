package com.justplay1.shoppist.view.component;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.CheckBox;

import com.jenzz.materialpreference.CheckBoxPreference;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.utils.DrawableUtils;

/**
 * Created by Mkhitar on 25.04.2015.
 */
public class ColorCheckBoxPreference extends CheckBoxPreference {

    private
    @ColorInt
    int mColor;

    public ColorCheckBoxPreference(Context context, @ColorInt int color) {
        super(context);
        mColor = color;
    }


    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        CheckBox checkboxView = (CheckBox) view.findViewById(R.id.checkbox);
        StateListDrawable states = new StateListDrawable();

        Resources res = getContext().getResources();
        Drawable drawable = res.getDrawable(R.drawable.abc_btn_check_to_on_mtrl_000);
        Drawable checked = res.getDrawable(R.drawable.abc_btn_check_to_on_mtrl_015);

        if (isEnabled()) {
            states.setColorFilter(DrawableUtils.getColorFilter(mColor));
        } else {
            states.setColorFilter(DrawableUtils.getColorFilter(Color.GRAY));
        }

        states.addState(new int[]{android.R.attr.stateNotNeeded}, drawable);
        states.addState(new int[]{android.R.attr.state_checked}, checked);
        states.addState(new int[]{android.R.attr.state_enabled}, drawable);
        states.addState(new int[]{-android.R.attr.state_enabled, android.R.attr.state_checked}, checked);
        states.addState(new int[]{-android.R.attr.state_enabled}, drawable);

        checkboxView.setButtonDrawable(states);
    }
}
