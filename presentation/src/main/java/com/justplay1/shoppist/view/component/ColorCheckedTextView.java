package com.justplay1.shoppist.view.component;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.util.AttributeSet;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.utils.DrawableUtils;

/**
 * Created by Mkhitar on 21.05.2015.
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

        Resources res = getContext().getResources();
        Drawable drawable = res.getDrawable(R.drawable.abc_btn_radio_to_on_mtrl_000);
        Drawable checked = res.getDrawable(R.drawable.abc_btn_radio_to_on_mtrl_015);

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
