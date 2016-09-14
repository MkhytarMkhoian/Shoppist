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

package com.justplay1.shoppist.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.justplay1.shoppist.models.ListItemViewModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ShoppistUtils {

    private ShoppistUtils() {

    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String[] concat(String[] a, String[] b) {
        if (a == null) return b;
        int aLen = a.length;
        int bLen = b.length;
        String[] c = new String[aLen + bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    public static String getFirstCharacter(String name) {
        String s = name.trim();
        if (!s.equals("")) {
            return String.valueOf(s.charAt(0));
        }
        return s;
    }

    public static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "unknown";
        }
    }

    public static String buildShareString(Collection<ListItemViewModel> items) {
        StringBuilder textToSend = new StringBuilder();
        for (ListItemViewModel item : items) {
            textToSend.append(item.getName()).append(" ");
            if (item.getPrice() > 0) {
                textToSend.append("(").append(item.getPrice()).append(" ").append(item.getCurrency().getName());
                if (item.getQuantity() == 0) {
                    textToSend.append(")");
                }
            }
            if (item.getQuantity() > 0) {
                if (item.getPrice() == 0) {
                    textToSend.append("(");
                } else {
                    textToSend.append("/");
                }
                textToSend.append(item.getQuantity()).append(" ").append(item.getUnit().getShortName()).append(")");
            }
            textToSend.append("\n");
        }
        return textToSend.toString();
    }

    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static void setKeepScreenOn(Window window, boolean on) {
        if (on) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public static String toStringItemIds(List<String> ids) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            builder.append(ids.get(i));
            if (i < ids.size() - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public static double roundDouble(double value, int scale) {
        BigDecimal smallNumber = new BigDecimal(value);
        BigDecimal decimal = smallNumber.setScale(scale, RoundingMode.HALF_UP);
        return decimal.doubleValue();
    }

    public static String filterSpace(String text) {
        int lineBreakCount = 0;
        int spaceCount = 0;
        StringBuilder builder = new StringBuilder();
        for (char character : text.toCharArray()) {
            if (character == '\n') {
                lineBreakCount++;
            } else {
                lineBreakCount = 0;
            }
            if (character == ' ') {
                spaceCount++;
            } else {
                spaceCount = 0;
            }

            if (lineBreakCount > 2 || spaceCount > 2) {
                continue;
            }
            builder.append(character);
        }
        return builder.toString();
    }

    public static void showKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
