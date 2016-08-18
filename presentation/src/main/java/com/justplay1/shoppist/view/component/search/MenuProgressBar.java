package com.justplay1.shoppist.view.component.search;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.ColorInt;
import android.widget.ProgressBar;

import com.justplay1.shoppist.utils.ViewUtils;

/**
 * Created by renaud on 01/01/16.
 */
public class MenuProgressBar extends ProgressBar {

    public MenuProgressBar(Context context, @ColorInt int mColor) {
        super(context);
        setColor(mColor);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(ViewUtils.dpToPx(48), ViewUtils.dpToPx(24));
    }

    public void setColor(@ColorInt int color) {
        getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }
}
