package com.justplay1.shoppist.view.component;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.TextView;

import com.jenzz.materialpreference.PreferenceCategory;
import com.justplay1.shoppist.R;

/**
 * Created by Mkhitar on 25.04.2015.
 */
public class ColorPreferenceCategory extends PreferenceCategory {

    private
    @ColorInt
    int mColor;

    public ColorPreferenceCategory(Context context, @ColorInt int color) {
        super(context);
        mColor = color;
    }


    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        TextView titleView = (TextView) view.findViewById(R.id.title);
        titleView.setTextColor(mColor);
    }
}
