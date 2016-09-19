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

package com.justplay1.shoppist.view.component;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.justplay1.shoppist.R;

import java.util.Locale;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ColorPickerDialog extends Dialog implements SeekBar.OnSeekBarChangeListener {

    private
    @ColorInt
    int mButtonColor;

    private View colorView;
    private CustomSeekBar redSeekBar, greenSeekBar, blueSeekBar;
    private TextView redToolTip, greenToolTip, blueToolTip;
    private int red, green, blue, seekBarLeft;
    private Rect thumbRect;
    private Button mPositiveBtn, mNegativeBtn;

    public ColorPickerDialog(Context context, @ColorInt int buttonColor) {
        super(context);
        mButtonColor = buttonColor;
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_color_picker);

        mPositiveBtn = (Button) findViewById(R.id.positive_button);
        mNegativeBtn = (Button) findViewById(R.id.negative_button);
        mNegativeBtn.setTextColor(mButtonColor);
        mPositiveBtn.setTextColor(mButtonColor);

        colorView = findViewById(R.id.colorView);
        redSeekBar = (CustomSeekBar) findViewById(R.id.redSeekBar);
        greenSeekBar = (CustomSeekBar) findViewById(R.id.greenSeekBar);
        blueSeekBar = (CustomSeekBar) findViewById(R.id.blueSeekBar);

        seekBarLeft = redSeekBar.getPaddingLeft();

        redToolTip = (TextView) findViewById(R.id.redToolTip);
        greenToolTip = (TextView) findViewById(R.id.greenToolTip);
        blueToolTip = (TextView) findViewById(R.id.blueToolTip);

        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar.setOnSeekBarChangeListener(this);

        redSeekBar.setProgress(red);
        greenSeekBar.setProgress(green);
        blueSeekBar.setProgress(blue);
        colorView.setBackgroundColor(Color.rgb(red, green, blue));
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mPositiveBtn.setOnClickListener(listener);
        mNegativeBtn.setOnClickListener(listener);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        thumbRect = redSeekBar.getSeekBarThumb().getBounds();

        redToolTip.setX(seekBarLeft + thumbRect.left);
        if (red < 10)
            redToolTip.setText(String.format(Locale.getDefault(), "  %d", red));
        else if (red < 100)
            redToolTip.setText(String.format(Locale.getDefault(), " %d", red));
        else
            redToolTip.setText(String.format(Locale.getDefault(), "%d", red));

        thumbRect = greenSeekBar.getSeekBarThumb().getBounds();

        greenToolTip.setX(seekBarLeft + thumbRect.left);
        if (green < 10)
            greenToolTip.setText(String.format(Locale.getDefault(), "  %d", green));
        else if (red < 100)
            greenToolTip.setText(String.format(Locale.getDefault(), " %d", green));
        else
            greenToolTip.setText(String.format(Locale.getDefault(), "%d", green));

        thumbRect = blueSeekBar.getSeekBarThumb().getBounds();

        blueToolTip.setX(seekBarLeft + thumbRect.left);
        if (blue < 10)
            blueToolTip.setText(String.format(Locale.getDefault(), "  %d", blue));
        else if (blue < 100)
            blueToolTip.setText(String.format(Locale.getDefault(), " %d", blue));
        else
            blueToolTip.setText(String.format(Locale.getDefault(), "%d", blue));

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == R.id.redSeekBar) {

            red = progress;
            thumbRect = ((CustomSeekBar) seekBar).getSeekBarThumb().getBounds();

            redToolTip.setX(seekBarLeft + thumbRect.left);

            if (progress < 10)
                redToolTip.setText(String.format(Locale.getDefault(), "  %d", red));
            else if (progress < 100)
                redToolTip.setText(String.format(Locale.getDefault(), " %d", red));
            else
                redToolTip.setText(String.format(Locale.getDefault(), "%d", red));

        } else if (seekBar.getId() == R.id.greenSeekBar) {

            green = progress;
            thumbRect = ((CustomSeekBar) seekBar).getSeekBarThumb().getBounds();

            greenToolTip.setX(seekBar.getPaddingLeft() + thumbRect.left);
            if (progress < 10)
                greenToolTip.setText(String.format(Locale.getDefault(), "  %d", green));
            else if (progress < 100)
                greenToolTip.setText(String.format(Locale.getDefault(), " %d", green));
            else
                greenToolTip.setText(String.format(Locale.getDefault(), "%d", green));

        } else if (seekBar.getId() == R.id.blueSeekBar) {

            blue = progress;
            thumbRect = ((CustomSeekBar) seekBar).getSeekBarThumb().getBounds();

            blueToolTip.setX(seekBarLeft + thumbRect.left);
            if (progress < 10)
                blueToolTip.setText(String.format(Locale.getDefault(), "  %d", blue));
            else if (progress < 100)
                blueToolTip.setText(String.format(Locale.getDefault(), " %d", blue));
            else
                blueToolTip.setText(String.format(Locale.getDefault(), "%d", blue));

        }
        colorView.setBackgroundColor(Color.rgb(red, green, blue));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * Getter for the RED value of the RGB selected color
     *
     * @return RED Value Integer (0 - 255)
     */
    public int getRed() {
        return red;
    }

    /**
     * Getter for the GREEN value of the RGB selected color
     *
     * @return GREEN Value Integer (0 - 255)
     */
    public int getGreen() {
        return green;
    }


    /**
     * Getter for the BLUE value of the RGB selected color
     *
     * @return BLUE Value Integer (0 - 255)
     */
    public int getBlue() {
        return blue;
    }

    /**
     * Getter for the color as Android Color class value.
     * <p>
     * From Android Reference: The Color class defines methods for creating and converting color ints.
     * Colors are represented as packed ints, made up of 4 bytes: alpha, red, green, blue.
     * The values are unpremultiplied, meaning any transparency is stored solely in the alpha
     * component, and not in the color components.
     *
     * @return Selected color as Android Color class value.
     */
    public int getColor() {
        return Color.rgb(red, green, blue);
    }

    public void setColor(int color) {
        red = Color.red(color);
        green = Color.green(color);
        blue = Color.blue(color);

        redSeekBar.setProgress(red);
        greenSeekBar.setProgress(green);
        blueSeekBar.setProgress(blue);
        colorView.setBackgroundColor(Color.rgb(red, green, blue));
    }
}
