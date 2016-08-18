package com.justplay1.shoppist.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.jenzz.materialpreference.PreferenceCategory;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;

/**
 * Created by Mkhitar on 25.04.2015.
 */
public class ColorPreferenceCategory extends PreferenceCategory {

    public ColorPreferenceCategory(Context context) {
        super(context);
    }

    public ColorPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ColorPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    public void onBindViewHolder(PreferenceViewHolder holder) {
//        super.onBindViewHolder(holder);
//
//        TextView titleView = (TextView) holder.findViewById(R.id.title);
//        titleView.setTextColor(ShoppistPreferences.getColorPrimary());
//    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        TextView titleView = (TextView) view.findViewById(R.id.title);
        titleView.setTextColor(ShoppistPreferences.getColorPrimary());
    }
}
