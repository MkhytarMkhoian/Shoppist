package com.justplay1.shoppist.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Mkhytar on 12.09.2015.
 */
public class ExpandIndicator extends ImageView {

    private boolean isExpand = true;

    public ExpandIndicator(Context context) {
        super(context);
    }

    public ExpandIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExpandIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
        if (isExpand){
            animate().rotation(0).start();
        } else {
            animate().rotation(-180).start();
        }
    }

    public void toggle() {
        if (isExpand){
            animate().rotation(0).start();
            isExpand = false;
        } else {
            animate().rotation(180).start();
            isExpand = true;
        }
    }
}
