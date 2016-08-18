package com.justplay1.shoppist.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.justplay1.shoppist.R;

/**
 * Created by Mkhitar on 22.01.2015.
 */
public class EmptyView extends RelativeLayout {

    private ProgressBarCircularIndeterminate mProgressBar;
    private TextView mInfoText;

    public EmptyView(Context context) {
        super(context);
        init(context);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.empty_list, this);
        mProgressBar = (ProgressBarCircularIndeterminate) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(INVISIBLE);
        mInfoText = (TextView) findViewById(R.id.empty_text);
    }

    public void showProgressBar(){
        mInfoText.setVisibility(INVISIBLE);
        mProgressBar.setVisibility(VISIBLE);
    }

    public void hideProgressBar(){
        mInfoText.setVisibility(VISIBLE);
        mProgressBar.setVisibility(INVISIBLE);
    }

    public void setProgressBarColor(int color){
        mProgressBar.setBackgroundColor(color);
    }

    public void setInfoText(int resId){
        mInfoText.setText(resId);
    }

    public void setInfoImage(@DrawableRes int resId){
        Drawable drawable = getResources().getDrawable(resId);
        mInfoText.setCompoundDrawables(null, drawable, null, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mInfoText.setCompoundDrawablesRelative(null, drawable, null, null);
        }
    }
}
