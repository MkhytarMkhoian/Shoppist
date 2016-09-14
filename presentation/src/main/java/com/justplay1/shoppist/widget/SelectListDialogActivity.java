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
import android.os.Binder;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.models.ListViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Mkhytar Mkhoian.
 */
public class SelectListDialogActivity extends AppCompatActivity
        implements DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {

    public final static String WIDGET_PREF = "widget_pref";
    public final static String WIDGET_LIST_NAME = "widget_list_name_";
    public final static String WIDGET_LIST_ID = "widget_list_id_";

    private int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Intent resultValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        setResult(RESULT_CANCELED, resultValue);

        showDialog();
    }

    private void showDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.select_list);
        alertDialog.setOnDismissListener(this);
        alertDialog.setOnCancelListener(this);

        final String[] data;
        final List<BaseViewModel> tmp;
        final long token = Binder.clearCallingIdentity();
        try {
//            ShoppingListTable shoppingListTable = new ShoppingListTable(this);
//            Collection<ListViewModel> shoppingLists = shoppingListTable.getAllShoppingLists().values();
            Collection<ListViewModel> shoppingLists = new ArrayList<>();
            tmp = new ArrayList<>(shoppingLists.size());

            for (ListViewModel item : shoppingLists) {
                tmp.add(item);
            }
            data = new String[tmp.size()];
            for (int i = 0; i < tmp.size(); i++) {
                data[i] = tmp.get(i).getName();
            }
        } finally {
            Binder.restoreCallingIdentity(token);
        }

        alertDialog.setItems(data, (dialog, which) -> {
            SharedPreferences sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(WIDGET_LIST_NAME + widgetID, tmp.get(which).getName());
            editor.putString(WIDGET_LIST_ID + widgetID, tmp.get(which).getId());
            editor.commit();

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(SelectListDialogActivity.this);
            ShoppistWidgetProvider.updateWidget(SelectListDialogActivity.this, appWidgetManager, widgetID, sp);

            setResult(RESULT_OK, resultValue);
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
