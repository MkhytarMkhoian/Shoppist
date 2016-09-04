package com.justplay1.shoppist.view.component.animboxes;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.justplay1.shoppist.R;

/**
 * Created by Mkhitar on 07.01.2015.
 */
public class SelectBoxView extends FrameLayout implements View.OnClickListener, Animation.AnimationListener {

    private int mSelectedStateColor;
    private int mNormalStateColor;
    private boolean isChecked;
    private String mInnerText;
    private ImageView mMainView;
    private TextView mInnerTextView;
    private SelectBoxCheckListener mEventListener;
    private Animation mToMiddleAnimation;
    private Animation mFromMiddleAnimation;
    private Animation mZoomAnimation;
    private int mDimen;
    private ShapeDrawable mShapeDrawable;
    private boolean isClickable = true;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SelectBoxView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SelectBoxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SelectBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SelectBoxView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_select_box, this);
        mMainView = (ImageView) findViewById(R.id.item_logo);
        mInnerTextView = (TextView) findViewById(R.id.logo_text);
        mMainView.setOnClickListener(this);

        OvalShape ovalShape = new OvalShape();
        mShapeDrawable = new ShapeDrawable(ovalShape);

        mDimen = getResources().getDimensionPixelSize(R.dimen.select_box_check);
        mSelectedStateColor = context.getResources().getColor(R.color.action_mode_toolbar_color);

        mToMiddleAnimation = AnimationUtils.loadAnimation(context, R.anim.to_middle);
        mToMiddleAnimation.setAnimationListener(this);
        mFromMiddleAnimation = AnimationUtils.loadAnimation(context, R.anim.from_middle);
        mZoomAnimation = AnimationUtils.loadAnimation(context, R.anim.zoom);
        mZoomAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private ShapeDrawable createShape(Integer color) {
        if (color == null) return null;

        OvalShape ovalShape = new OvalShape();
        mShapeDrawable.setShape(ovalShape);
        mShapeDrawable.getPaint().setColor(color);
        return mShapeDrawable;
    }

    @Override
    public void onClick(View v) {
        if (!isClickable) return;

        if (isChecked) {
            setChecked(false);
        } else {
            setChecked(true);
        }
    }

    @Override
    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean isClickable) {
        this.isClickable = isClickable;
    }

    public ImageView getMainView() {
        return mMainView;
    }

    public String getInnerText() {
        return mInnerText;
    }

    public void setInnerText(String innerText) {
        this.mInnerText = innerText;
        mInnerTextView.setText(innerText);
    }

    public void setInnerImage(@DrawableRes int resid) {
        mInnerTextView.setText("");
        mInnerTextView.setBackgroundResource(resid);
        mInnerTextView.getLayoutParams().height = mDimen;
        mInnerTextView.getLayoutParams().width = mDimen;
    }

    public void setInnerTextColor(int color) {
        mInnerTextView.setTextColor(color);
    }

    public void setEventListener(SelectBoxCheckListener eventListener) {
        this.mEventListener = eventListener;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
        doAnimation();
    }

    public void refresh() {
        switchBox();
    }

    public void refresh(boolean checked) {
        isChecked = checked;
        switchBox();
    }

    public int getSelectedStateColor() {
        return mSelectedStateColor;
    }

    public void setSelectedStateColor(int selectedStateColor) {
        this.mSelectedStateColor = selectedStateColor;
    }

    public int getNormalStateColor() {
        return mNormalStateColor;
    }

    public void setNormalStateColor(int normalStateColor) {
        this.mNormalStateColor = normalStateColor;
        setDrawable(mMainView, normalStateColor);
    }

    @SuppressWarnings("deprecation")
    private void setDrawable(View view, Integer color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(createShape(color));
        } else {
            view.setBackgroundDrawable(createShape(color));
        }
    }

    private void doAnimation() {
        if (mEventListener != null) {
            mEventListener.onCheck(isChecked);
        }
        clearAnimation();
        startAnimation(mToMiddleAnimation);
    }

    public void setInnerTextTypeface(Typeface innerTextTypeface) {
        mInnerTextView.setTypeface(innerTextTypeface);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        switchBox();
        clearAnimation();
        startAnimation(mFromMiddleAnimation);
        if (isChecked) {
            mInnerTextView.startAnimation(mZoomAnimation);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void switchBox() {
        if (isChecked) {
            mInnerTextView.setText("");
            mInnerTextView.setBackgroundResource(R.drawable.ic_check_white);
            mInnerTextView.getLayoutParams().height = mDimen;
            mInnerTextView.getLayoutParams().width = mDimen;
            setDrawable(mMainView, mSelectedStateColor);
        } else {
            setDrawable(mInnerTextView, null);
            mInnerTextView.setText(mInnerText);
            mInnerTextView.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
            mInnerTextView.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
            setDrawable(mMainView, mNormalStateColor);
        }
//        if (mHolder != null && mHolder.container != null) {
//            mHolder.container.setActivated(isChecked);
//        } else if (mHolder != null) {
//            mHolder.itemView.setActivated(isChecked);
//        }
    }

    public void onMovedToScrapHeap() {
        if (animate() != null) {
            animate().cancel();
        }
        if (mInnerTextView.animate() != null) {
            mInnerTextView.animate().cancel();
        }
    }
}
