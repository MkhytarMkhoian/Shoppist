package com.justplay1.shoppist.ui.views.search;

import android.content.Context;
import android.graphics.PorterDuff;
import android.widget.ProgressBar;

import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ViewUtils;

/**
 * Created by renaud on 01/01/16.
 */
public class MenuProgressBar extends ProgressBar {

    public MenuProgressBar(Context context) {
        super(context);
        setColor(ShoppistPreferences.getColorPrimary());
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(ViewUtils.dpToPx(48), ViewUtils.dpToPx(24));
    }

    public void setColor(int color){
        getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }
}
