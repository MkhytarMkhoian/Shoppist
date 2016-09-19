/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.justplay1.shoppist.view.component.search.internal;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static com.justplay1.shoppist.view.component.search.internal.RoundRectDrawableWithShadow.BOTTOM;
import static com.justplay1.shoppist.view.component.search.internal.RoundRectDrawableWithShadow.LEFT;
import static com.justplay1.shoppist.view.component.search.internal.RoundRectDrawableWithShadow.RIGHT;

public class SuggestionItemDecorator extends RecyclerView.ItemDecoration {

    private final RoundRectDrawableWithShadow drawable;

    public SuggestionItemDecorator(RoundRectDrawableWithShadow drawable) {
        this.drawable = drawable;
    }

    @Override
    public void getItemOffsets(Rect rect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int count = state.getItemCount();
        int shadows = LEFT | RIGHT;
        if (position == count - 1) shadows |= BOTTOM;
        drawable.setShadow(shadows);
        drawable.getPadding(rect);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int visibleCount = parent.getChildCount();
        int count = state.getItemCount();
        RecyclerView.Adapter adapter = parent.getAdapter();
        int adapterCount = adapter != null ? adapter.getItemCount() : 0;

        for (int i = 0; i < visibleCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            float translationX = ViewCompat.getTranslationX(view);
            float translationY = ViewCompat.getTranslationY(view);
            float alpha = ViewCompat.getAlpha(view);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

            int shadows = LEFT | RIGHT;
            if (position == count - 1 && adapterCount != 0) shadows |= BOTTOM;

            drawable.setAlpha((int) (255 * alpha));
            drawable.setShadow(shadows);
            drawable.setBounds(0, 0, parent.getWidth(), view.getHeight());
            int saved = canvas.save();
            canvas.translate(parent.getPaddingLeft() + translationX,
                    view.getTop() + params.topMargin + translationY);
            drawable.draw(canvas);
            canvas.restoreToCount(saved);
        }
    }

    public void setBackgroundColor(@ColorInt int color) {
        drawable.setColor(color);
    }

    public void setCornerRadius(float radius) {
        drawable.setCornerRadius(radius);
    }
}
