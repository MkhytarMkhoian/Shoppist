/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.justplay1.shoppist.shared.widget.animboxes;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.justplay1.shoppist.R;

/**
 * Created by Mkhytar Mkhoian.
 */
public class SelectBoxView extends FrameLayout implements View.OnClickListener, Animation.AnimationListener {

    @Bind(R.id.item_logo)
    ImageView mainView;
    @Bind(R.id.logo_text)
    TextView innerTextView;

    private int selectedStateColor;
    private int normalStateColor;
    private boolean isChecked;
    private String innerText;
    private SelectBoxCheckListener eventListener;
    private Animation toMiddleAnimation;
    private Animation fromMiddleAnimation;
    private Animation zoomAnimation;
    private int dimen;
    private ShapeDrawable shapeDrawable;
    private boolean isClickable = true;
    private boolean isAttachedToWindow;

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
        ButterKnife.bind(this);

        mainView.setOnClickListener(this);
        OvalShape ovalShape = new OvalShape();
        shapeDrawable = new ShapeDrawable(ovalShape);

        dimen = getResources().getDimensionPixelSize(R.dimen.select_box_check);
        selectedStateColor = ContextCompat.getColor(context, R.color.action_mode_toolbar_color);

        toMiddleAnimation = AnimationUtils.loadAnimation(context, R.anim.to_middle);
        toMiddleAnimation.setAnimationListener(this);
        fromMiddleAnimation = AnimationUtils.loadAnimation(context, R.anim.from_middle);
        zoomAnimation = AnimationUtils.loadAnimation(context, R.anim.zoom);
        zoomAnimation.setAnimationListener(new Animation.AnimationListener() {
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
        shapeDrawable.setShape(ovalShape);
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

    @Override
    public void onClick(View v) {
        if (!isClickable) return;

        if (isChecked) {
            setCheckedWithAnim(false);
        } else {
            setCheckedWithAnim(true);
        }
    }

    @Override
    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean isClickable) {
        this.isClickable = isClickable;
    }

    public void setInnerText(String innerText) {
        this.innerText = innerText;
        innerTextView.setText(innerText);
    }

    public void setInnerImage(@DrawableRes int resid) {
        innerTextView.setText("");
        innerTextView.setBackgroundResource(resid);
        innerTextView.getLayoutParams().height = dimen;
        innerTextView.getLayoutParams().width = dimen;
    }

    public void setInnerTextColor(int color) {
        innerTextView.setTextColor(color);
    }

    public void setEventListener(SelectBoxCheckListener eventListener) {
        this.eventListener = eventListener;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setCheckedWithAnim(boolean checked) {
        isChecked = checked;
        if (isAttachedToWindow) {
            doAnimation();
        } else {
            switchBox();
        }
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
        switchBox();
    }

    public int getSelectedStateColor() {
        return selectedStateColor;
    }

    public void setSelectedStateColor(int selectedStateColor) {
        this.selectedStateColor = selectedStateColor;
    }

    public int getNormalStateColor() {
        return normalStateColor;
    }

    public void setNormalStateColor(int normalStateColor) {
        this.normalStateColor = normalStateColor;
        setDrawable(mainView, normalStateColor);
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
        if (eventListener != null) {
            eventListener.onCheck(isChecked);
        }
        clearAnimation();
        startAnimation(toMiddleAnimation);
    }

    public void setInnerTextTypeface(Typeface innerTextTypeface) {
        innerTextView.setTypeface(innerTextTypeface);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        switchBox();
        clearAnimation();
        startAnimation(fromMiddleAnimation);
        if (isChecked) {
            innerTextView.startAnimation(zoomAnimation);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void switchBox() {
        if (isChecked) {
            innerTextView.setText("");
            innerTextView.setBackgroundResource(R.drawable.ic_check_white);
            innerTextView.getLayoutParams().height = dimen;
            innerTextView.getLayoutParams().width = dimen;
            setDrawable(mainView, selectedStateColor);
        } else {
            setDrawable(innerTextView, null);
            innerTextView.setText(innerText);
            innerTextView.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
            innerTextView.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
            setDrawable(mainView, normalStateColor);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachedToWindow = false;
        if (animate() != null) {
            animate().cancel();
        }
        if (innerTextView.animate() != null) {
            innerTextView.animate().cancel();
        }
    }
}
