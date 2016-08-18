package com.justplay1.shoppist.utils;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * Created by Mkhytar on 26.10.2015.
 */
public class DrawableUtils {

    @SuppressWarnings("deprecation")
    public static void setViewDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    protected int makePressColor(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        r = (r - 30 < 0) ? 0 : r - 30;
        g = (g - 30 < 0) ? 0 : g - 30;
        b = (b - 30 < 0) ? 0 : b - 30;
        return Color.rgb(r, g, b);
    }

    public static Drawable setDrawableColor(int color, Drawable drawable) {
//        int red = (color & 0xFF0000) / 0xFFFF;
//        int green = (color & 0xFF00) / 0xFF;
//        int blue = color & 0xFF;
//
//        float[] matrix = {0, 0, 0, 0, red
//                , 0, 0, 0, 0, green
//                , 0, 0, 0, 0, blue
//                , 0, 0, 0, 1, 0};
//
//        ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);
//        drawable.mutate().setColorFilter(colorFilter);
        drawable.mutate().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
        return drawable;
    }

    public static ColorFilter getColorFilter(int color) {
        int red = (color & 0xFF0000) / 0xFFFF;
        int green = (color & 0xFF00) / 0xFF;
        int blue = color & 0xFF;

        float[] matrix = {0, 0, 0, 0, red
                , 0, 0, 0, 0, green
                , 0, 0, 0, 0, blue
                , 0, 0, 0, 1, 0};

        return new ColorMatrixColorFilter(matrix);
    }
}
