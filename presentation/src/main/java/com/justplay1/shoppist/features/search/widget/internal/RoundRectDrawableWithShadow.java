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
package com.justplay1.shoppist.features.search.widget.internal;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.Log;

import com.justplay1.shoppist.utils.ViewUtils;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * A rounded rectangle drawable which also includes a configurable shadow around.
 */
public class RoundRectDrawableWithShadow extends Drawable {

    private static final boolean DEBUG = false;

    private static final int SHADOW_COLOR_START = 0x37000000;
    private static final int SHADOW_COLOR_END = 0x03000000;
    private static final int SHADOW_INSET_DP = 1;

    @Retention(SOURCE)
    @IntDef(value = {LEFT, RIGHT, TOP, BOTTOM}, flag = true)
    @interface Gravity {
    }

    public static final int LEFT = 1 << 0;
    public static final int TOP = 1 << 1;
    public static final int RIGHT = 1 << 2;
    public static final int BOTTOM = 1 << 3;

    // used to calculate content padding
    private final static double COS_45 = Math.cos(Math.toRadians(45));

    private final static float SHADOW_MULTIPLIER = 1.5f;

    private final int insetShadow; // extra shadow to avoid gaps between card and shadow

    private final RectF cornerRect = new RectF();

    private Paint paint;

    private Paint cornerShadowPaint;

    private Paint edgeShadowPaint;

    private Paint boundsPaint;

    private final RectF cardBounds;

    private float cornerRadius;

    private Path cornerShadowPath;

    // updated value with inset
    private float maxShadowSize;

    // actual value set by developer
    private float rawMaxShadowSize;

    // multiplied value to account for shadow offset
    private float shadowSize;

    // actual value set by developer
    private float rawShadowSize;

    private boolean topLeftCorner, bottomLeftCorner, topRightCorner, bottomRightCorner;
    private boolean leftShadow, topShadow, rightShadow, bottomShadow;

    private boolean dirty = true;

    private boolean addPaddingForCorners = true;

    /**
     * If shadow size is set to a value above max shadow, we print a warning
     */
    private boolean printedShadowClipWarning = false;

    public RoundRectDrawableWithShadow(int backgroundColor, float radius,
                                       float shadowSize, float maxShadowSize) {
        if (DEBUG)
            Log.d(getClass().getSimpleName(), "RoundRectDrawableWithShadow(" + radius + "," + shadowSize + "," + maxShadowSize + ")");

        insetShadow = ViewUtils.dpToPx(SHADOW_INSET_DP);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setColor(backgroundColor);
        cornerShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        cornerShadowPaint.setStyle(Paint.Style.FILL);
        cornerRadius = (int) (radius + .5f);
        cardBounds = new RectF();
        edgeShadowPaint = new Paint(cornerShadowPaint);
        edgeShadowPaint.setAntiAlias(false);
        if (DEBUG) {
            boundsPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            boundsPaint.setColor(Color.BLACK);
            boundsPaint.setStyle(Paint.Style.STROKE);
            boundsPaint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1);
            cornerShadowPaint.setStrokeWidth(1);
        }
        setShadow(TOP | LEFT | RIGHT | BOTTOM);
        setShadowSize(shadowSize, maxShadowSize);
    }

    public int getColor() {
        return paint.getColor();
    }

    public void setShadow(@Gravity int flags) {
        topLeftCorner = (flags & (TOP | LEFT)) == (LEFT | TOP);
        bottomLeftCorner = (flags & (BOTTOM | LEFT)) == (LEFT | BOTTOM);
        topRightCorner = (flags & (TOP | RIGHT)) == (TOP | RIGHT);
        bottomRightCorner = (flags & (BOTTOM | RIGHT)) == (BOTTOM | RIGHT);

        leftShadow = (flags & LEFT) != 0;
        topShadow = (flags & TOP) != 0;
        rightShadow = (flags & RIGHT) != 0;
        bottomShadow = (flags & BOTTOM) != 0;
        dirty = true;
        invalidateSelf();
    }

    @Override
    public RoundRectDrawableWithShadow mutate() {
        return new RoundRectDrawableWithShadow(
                paint.getColor(), getCornerRadius(),
                getShadowSize(), getMaxShadowSize());
    }

    /**
     * Casts the value to an even integer.
     */
    private int toEven(float value) {
        int i = (int) (value + .5f);
        if (i % 2 == 1) {
            return i - 1;
        }
        return i;
    }

    public void setAddPaddingForCorners(boolean addPaddingForCorners) {
        this.addPaddingForCorners = addPaddingForCorners;
        invalidateSelf();
    }

    public boolean getAddPaddingForCorners() {
        return addPaddingForCorners;
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
        cornerShadowPaint.setAlpha(alpha);
        edgeShadowPaint.setAlpha(alpha);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        if (DEBUG) Log.d(getClass().getSimpleName(), "onBoundsChange@" + hashCode() + ": " +
                bounds + " (" + bounds.width() + "," + bounds.height() + ")");
        dirty = true;
    }

    public void setShadowSize(float shadowSize, float maxShadowSize) {
        if (shadowSize < 0 || maxShadowSize < 0) {
            throw new IllegalArgumentException("invalid shadow size");
        }
        shadowSize = toEven(shadowSize);
        maxShadowSize = toEven(maxShadowSize);
        if (shadowSize > maxShadowSize) {
            shadowSize = maxShadowSize;
            if (!printedShadowClipWarning) {
                printedShadowClipWarning = true;
            }
        }
        if (rawShadowSize == shadowSize && rawMaxShadowSize == maxShadowSize) {
            return;
        }
        rawShadowSize = shadowSize;
        rawMaxShadowSize = maxShadowSize;
        this.shadowSize = (int) (shadowSize * SHADOW_MULTIPLIER + insetShadow + .5f);
        this.maxShadowSize = maxShadowSize + insetShadow;
        dirty = true;
        invalidateSelf();
    }

    @Override
    public boolean getPadding(@NonNull Rect padding) {
        int vOffset = (int) Math.ceil(calculateVerticalPadding(rawMaxShadowSize, cornerRadius,
                addPaddingForCorners));
        int hOffset = (int) Math.ceil(calculateHorizontalPadding(rawMaxShadowSize, cornerRadius,
                addPaddingForCorners));
        padding.set(
                leftShadow ? hOffset : 0,
                topShadow ? vOffset : 0,
                rightShadow ? hOffset : 0,
                bottomShadow ? vOffset : 0);
        return true;
    }

    public static float calculateVerticalPadding(float maxShadowSize, float cornerRadius,
                                                 boolean addPaddingForCorners) {
        if (addPaddingForCorners) {
            return (float) (maxShadowSize * SHADOW_MULTIPLIER + (1 - COS_45) * cornerRadius);
        } else {
            return maxShadowSize * SHADOW_MULTIPLIER;
        }
    }

    public static float calculateHorizontalPadding(float maxShadowSize, float cornerRadius,
                                                   boolean addPaddingForCorners) {
        if (addPaddingForCorners) {
            return (float) (maxShadowSize + (1 - COS_45) * cornerRadius);
        } else {
            return maxShadowSize;
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
        cornerShadowPaint.setColorFilter(cf);
        edgeShadowPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setCornerRadius(float radius) {
        radius = (int) (radius + .5f);
        if (cornerRadius == radius) {
            return;
        }
        cornerRadius = radius;
        dirty = true;
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        if (dirty) {
            buildComponents(getBounds());
            dirty = false;
        }

        canvas.translate(0, rawShadowSize / 2);
        drawShadow(canvas);
        canvas.translate(0, -rawShadowSize / 2);
        drawBody(canvas, cardBounds, cornerRadius, paint);

        if (DEBUG) {
            Rect bounds = getBounds();
            canvas.drawRect(bounds, boundsPaint);
        }
    }

    private void drawShadow(Canvas canvas) {
        final float edgeShadowTop = -cornerRadius - shadowSize;
        final float inset = cornerRadius + insetShadow + rawShadowSize / 2;
        final boolean drawHorizontalEdges = cardBounds.width() - 2 * inset > 0;
        final boolean drawVerticalEdges = cardBounds.height() - 2 * inset > 0;
        int saved;

        // LT
        if (topLeftCorner || topShadow) {
            saved = canvas.save();
            canvas.translate(cardBounds.left + inset, cardBounds.top + inset);
            if (topLeftCorner) canvas.drawPath(cornerShadowPath, cornerShadowPaint);
            if (drawHorizontalEdges && topShadow) {
                canvas.drawRect(topLeftCorner ? 0 : -inset,
                        edgeShadowTop,
                        cardBounds.width() - (topRightCorner ? 2 * inset : 0),
                        -cornerRadius,
                        edgeShadowPaint);
            }
            canvas.restoreToCount(saved);
        }

        // RB
        if (bottomRightCorner || bottomShadow) {
            saved = canvas.save();
            canvas.translate(cardBounds.right - inset, cardBounds.bottom - inset);
            canvas.rotate(180f);
            if (bottomRightCorner) canvas.drawPath(cornerShadowPath, cornerShadowPaint);
            if (drawHorizontalEdges && bottomShadow) {
                canvas.drawRect(bottomLeftCorner ? 0 : -inset, edgeShadowTop,
                        cardBounds.width() - (bottomRightCorner ? 2 * inset : 0),
                        -cornerRadius + shadowSize,
                        edgeShadowPaint);
            }
            canvas.restoreToCount(saved);
        }

        // LB
        if (bottomLeftCorner || leftShadow) {
            saved = canvas.save();
            canvas.translate(cardBounds.left + inset, cardBounds.bottom - inset);
            canvas.rotate(270f);
            if (bottomLeftCorner) canvas.drawPath(cornerShadowPath, cornerShadowPaint);
            if (drawVerticalEdges && leftShadow) {
                canvas.drawRect(bottomLeftCorner ? 0 : -(inset - insetShadow),
                        edgeShadowTop,
                        cardBounds.height() - (topLeftCorner ? 2 * inset : inset - insetShadow),
                        -cornerRadius,
                        edgeShadowPaint);
            }
            canvas.restoreToCount(saved);
        }

        // RT
        if (topRightCorner || rightShadow) {
            saved = canvas.save();
            canvas.translate(cardBounds.right - inset, cardBounds.top + inset);
            canvas.rotate(90f);
            if (topRightCorner) canvas.drawPath(cornerShadowPath, cornerShadowPaint);
            if (drawVerticalEdges && rightShadow) {
                canvas.drawRect(topRightCorner ? 0 : -(inset + insetShadow),
                        edgeShadowTop,
                        cardBounds.height() - (bottomRightCorner ? 2 * inset : inset + insetShadow),
                        -cornerRadius,
                        edgeShadowPaint);
            }
            canvas.restoreToCount(saved);
        }
    }

    protected void drawBody(Canvas canvas, RectF bounds, float cornerRadius,
                            Paint paint) {
        final float twoRadius = cornerRadius * 2;
        final float innerWidth = bounds.width() - twoRadius - 1;
        final float innerHeight = bounds.height() - twoRadius - 1;

        // increment it to account for half pixels.
        if (cornerRadius >= 1f) {
            cornerRadius += .5f;
            if (topLeftCorner || topRightCorner || bottomRightCorner || bottomLeftCorner) {
                cornerRect.set(-cornerRadius, -cornerRadius, cornerRadius, cornerRadius);
                int saved = canvas.save();
                canvas.translate(bounds.left + cornerRadius, bounds.top + cornerRadius);
                if (topLeftCorner) canvas.drawArc(cornerRect, 180, 90, true, paint);
                canvas.translate(innerWidth, 0);
                canvas.rotate(90);
                if (topRightCorner) canvas.drawArc(cornerRect, 180, 90, true, paint);
                canvas.translate(innerHeight, 0);
                canvas.rotate(90);
                if (bottomRightCorner) canvas.drawArc(cornerRect, 180, 90, true, paint);
                canvas.translate(innerWidth, 0);
                canvas.rotate(90);
                if (bottomLeftCorner) canvas.drawArc(cornerRect, 180, 90, true, paint);
                canvas.restoreToCount(saved);
            }
            //draw top and bottom pieces
            if (topShadow)
                canvas.drawRect(
                        bounds.left + (topLeftCorner ? cornerRadius - 1 : 0),
                        bounds.top,
                        bounds.right - (topRightCorner ? cornerRadius - 1 : 0),
                        bounds.top + cornerRadius,
                        paint);
            if (bottomShadow)
                canvas.drawRect(
                        bounds.left + (bottomLeftCorner ? cornerRadius - 1 : 0),
                        bounds.bottom - cornerRadius + 1f,
                        bounds.right - (bottomRightCorner ? cornerRadius - 1 : 0),
                        bounds.bottom, paint);
        }
////                center
        if (DEBUG) Log.d(getClass().getSimpleName(), "drawBody:" + new RectF(bounds.left,
                bounds.top + (topShadow ? Math.max(0, cornerRadius - 1) : 0),
                bounds.right,
                bounds.bottom - (bottomShadow ? cornerRadius - 1 : 0)));

        canvas.drawRect(bounds.left,
                bounds.top + (topShadow ? Math.max(0, cornerRadius - 1) : 0),
                bounds.right,
                bounds.bottom - (bottomShadow ? cornerRadius - 1 : 0)
                , paint);
    }

    private void buildShadowCorners() {
        RectF innerBounds = new RectF(-cornerRadius, -cornerRadius, cornerRadius, cornerRadius);
        RectF outerBounds = new RectF(innerBounds);
        outerBounds.inset(-shadowSize, -shadowSize);

        if (cornerShadowPath == null) {
            cornerShadowPath = new Path();
        } else {
            cornerShadowPath.reset();
        }
        cornerShadowPath.setFillType(Path.FillType.EVEN_ODD);
        cornerShadowPath.moveTo(-cornerRadius, 0);
        cornerShadowPath.rLineTo(-shadowSize, 0);
        // outer arc
        cornerShadowPath.arcTo(outerBounds, 180f, 90f, false);
        // inner arc
        cornerShadowPath.arcTo(innerBounds, 270f, -90f, false);
        cornerShadowPath.close();
        float startRatio = cornerRadius / (cornerRadius + shadowSize);
        cornerShadowPaint.setShader(new RadialGradient(0, 0, cornerRadius + shadowSize,
                new int[]{SHADOW_COLOR_START, SHADOW_COLOR_START, SHADOW_COLOR_END},
                new float[]{0f, startRatio, 1f}
                , Shader.TileMode.CLAMP));

        // we offset the content shadowSize/2 pixels up to make it more realistic.
        // this is why edge shadow shader has some extra space
        // When drawing bottom edge shadow, we use that extra space.
        edgeShadowPaint.setShader(new LinearGradient(0, -cornerRadius + shadowSize, 0,
                -cornerRadius - shadowSize,
                new int[]{SHADOW_COLOR_START, SHADOW_COLOR_START, SHADOW_COLOR_END},
                new float[]{0f, .5f, 1f}, Shader.TileMode.CLAMP));
        edgeShadowPaint.setAntiAlias(false);
    }

    private void buildComponents(Rect bounds) {
        // Card is offset SHADOW_MULTIPLIER * maxShadowSize to account for the shadow shift.
        // We could have different top-bottom offsets to avoid extra gap above but in that case
        // center aligning Views inside the CardView would be problematic.
        final float verticalOffset = rawMaxShadowSize * SHADOW_MULTIPLIER;
        cardBounds.set(
                bounds.left + (leftShadow ? rawMaxShadowSize : 0),
                bounds.top + (topShadow ? verticalOffset : 0),
                bounds.right - (rightShadow ? rawMaxShadowSize : 0),
                bounds.bottom - (bottomShadow ? verticalOffset : 0));
        buildShadowCorners();
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public void getMaxShadowAndCornerPadding(Rect into) {
        getPadding(into);
    }

    public void setShadowSize(float size) {
        setShadowSize(size, rawMaxShadowSize);
    }

    public void setMaxShadowSize(float size) {
        setShadowSize(rawShadowSize, size);
    }

    public float getShadowSize() {
        return rawShadowSize;
    }

    public float getMaxShadowSize() {
        return rawMaxShadowSize;
    }

    public float getMinWidth() {
        final float content = 2 *
                Math.max(rawMaxShadowSize, cornerRadius + insetShadow + rawMaxShadowSize / 2);
        return content + (rawMaxShadowSize + insetShadow) * 2;
    }

    public float getMinHeight() {
        final float content = 2 * Math.max(rawMaxShadowSize, cornerRadius + insetShadow
                + rawMaxShadowSize * SHADOW_MULTIPLIER / 2);
        return content + (rawMaxShadowSize * SHADOW_MULTIPLIER + insetShadow) * 2;
    }

    public void setColor(int color) {
        paint.setColor(color);
        invalidateSelf();
    }
}
