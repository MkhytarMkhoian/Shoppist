package com.justplay1.shoppist.view.component;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by Mkhitar on 13.05.2015.
 */
public class CustomProgressDialog extends ProgressDialog {

    public CustomProgressDialog(Context context) {
        super(context);
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public void setColor(int color) {
        CustomProgressBar bar = new CustomProgressBar(getContext());
        bar.setColor(color);
        setIndeterminateDrawable(bar.getIndeterminateDrawable());
    }

    public class CustomProgressBar extends ProgressBar {
        public CustomProgressBar(Context context) {
            super(context);
        }

        public CustomProgressBar(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public void setColor(int color) {
            getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }
}
