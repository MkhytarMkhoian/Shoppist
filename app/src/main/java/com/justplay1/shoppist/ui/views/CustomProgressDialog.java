package com.justplay1.shoppist.ui.views;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;

/**
 * Created by Mkhitar on 13.05.2015.
 */
public class CustomProgressDialog extends ProgressDialog {

    public CustomProgressDialog(Context context) {
        super(context);
        refreshColor();
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        refreshColor();
    }

    public void refreshColor() {
        setColor(ShoppistPreferences.getColorPrimary());
    }

    public void setColor(int color) {
        CustomProgressBar bar = new CustomProgressBar(getContext());
        setIndeterminateDrawable(bar.getIndeterminateDrawable());
    }

    public class CustomProgressBar extends ProgressBar {
        public CustomProgressBar(Context context) {
            super(context);
            init();
        }

        public CustomProgressBar(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            init();
        }

        private void init(){
            setColor(ShoppistPreferences.getColorPrimary());
        }

        public void setColor(int color){
            getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }
}
