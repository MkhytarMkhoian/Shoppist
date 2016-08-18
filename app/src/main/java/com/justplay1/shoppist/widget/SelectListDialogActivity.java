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
import com.justplay1.shoppist.database.tables.ShoppingListTable;
import com.justplay1.shoppist.models.BaseModel;
import com.justplay1.shoppist.models.ShoppingList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Mkhytar on 14.10.2015.
 */
public class SelectListDialogActivity extends AppCompatActivity implements DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {

    public final static String WIDGET_PREF = "widget_pref";
    public final static String WIDGET_LIST_NAME = "widget_list_name_";
    public final static String WIDGET_LIST_ID = "widget_list_id_";

    private int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Intent resultValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // извлекаем ID конфигурируемого виджета
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // и проверяем его корректность
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
        // формируем intent ответа
        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        // отрицательный ответ
        setResult(RESULT_CANCELED, resultValue);

        showDialog();
    }

    private void showDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.select_list);
        alertDialog.setOnDismissListener(this);
        alertDialog.setOnCancelListener(this);

        final String[] data;
        final List<BaseModel> tmp;
        final long token = Binder.clearCallingIdentity();
        try {
            ShoppingListTable shoppingListTable = new ShoppingListTable(this);
            Collection<ShoppingList> shoppingLists = shoppingListTable.getAllShoppingLists().values();
            tmp = new ArrayList<>(shoppingLists.size());

            for (ShoppingList item : shoppingLists) {
                tmp.add(item);
            }
            data = new String[tmp.size()];
            for (int i = 0; i < tmp.size(); i++) {
                data[i] = tmp.get(i).getName();
            }
        } finally {
            Binder.restoreCallingIdentity(token);
        }

        alertDialog.setItems(data, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Записываем значения с экрана в Preferences
                SharedPreferences sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(WIDGET_LIST_NAME + widgetID, tmp.get(which).getName());
                editor.putString(WIDGET_LIST_ID + widgetID, tmp.get(which).getId());
                editor.commit();

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(SelectListDialogActivity.this);
                ShoppistWidgetProvider.updateWidget(SelectListDialogActivity.this, appWidgetManager, widgetID, sp);

                // положительный ответ
                setResult(RESULT_OK, resultValue);
                finish();
            }
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
