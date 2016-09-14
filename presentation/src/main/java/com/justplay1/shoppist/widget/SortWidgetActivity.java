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

package com.justplay1.shoppist.widget;

import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.justplay1.shoppist.R;

/**
 * Created by Mkhytar Mkhoian.
 */
public class SortWidgetActivity extends AppCompatActivity implements DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {

    public final static String WIDGET_SORT = "widget_sort_";

    private int mWidgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Intent mResultValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mWidgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mWidgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        mResultValue = new Intent();
        mResultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetID);

        setResult(RESULT_CANCELED, mResultValue);

        showDialog();
    }

    private void showDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.select_sort);
        alertDialog.setOnDismissListener(this);
        alertDialog.setOnCancelListener(this);

        final String[] data = new String[]{getString(R.string.sort_by_name), getString(R.string.sort_by_priority),
                    getString(R.string.sort_by_category), getString(R.string.sort_by_time_created)};

        alertDialog.setItems(data, (dialog, which) -> {
            SharedPreferences sp = getSharedPreferences(SelectListDialogActivity.WIDGET_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(WIDGET_SORT + mWidgetID, which + 1);
            editor.commit();

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(SortWidgetActivity.this);
            ShoppistWidgetProvider.updateWidget(SortWidgetActivity.this, appWidgetManager, mWidgetID, sp);

            setResult(RESULT_OK, mResultValue);
            finish();
        });

        alertDialog.show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        finish();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        finish();
    }
}
