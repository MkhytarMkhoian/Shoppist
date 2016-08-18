package com.justplay1.shoppist.ui.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.DrawableUtils;

/**
 * Created by Mkhitar on 18.06.2015.
 */
public class ColorMultiCheckedBox extends AppCompatCheckBox {

    public ColorMultiCheckedBox(Context context) {
        super(context);
        init();
    }

    public ColorMultiCheckedBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorMultiCheckedBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        StateListDrawable states = new StateListDrawable();

        Resources res = getContext().getResources();
        Drawable drawable = res.getDrawable(R.drawable.abc_btn_check_to_on_mtrl_000);
        Drawable checked = res.getDrawable(R.drawable.abc_btn_check_to_on_mtrl_015);

        if (isEnabled()) {
            states.setColorFilter(DrawableUtils.getColorFilter(ShoppistPreferences.getColorPrimary()));
        } else {
            states.setColorFilter(DrawableUtils.getColorFilter(Color.GRAY));
        }

        states.addState(new int[]{android.R.attr.stateNotNeeded}, drawable);
        states.addState(new int[]{android.R.attr.state_checked}, checked);
        states.addState(new int[]{android.R.attr.state_enabled}, drawable);
        states.addState(new int[]{-android.R.attr.state_enabled, android.R.attr.state_checked}, checked);
        states.addState(new int[]{-android.R.attr.state_enabled}, drawable);

        setButtonDrawable(states);
    }
}
