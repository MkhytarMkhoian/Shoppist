package com.justplay1.shoppist.widget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.RemoteViews;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.database.tables.ShoppingListItemTable;
import com.justplay1.shoppist.models.ShoppingList;
import com.justplay1.shoppist.models.ShoppingListItem;
import com.justplay1.shoppist.models.Status;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.ui.activities.AddListItemActivity;
import com.justplay1.shoppist.utils.ActivityUtils;

/**
 * Created by Mkhytar on 13.10.2015.
 */
public class ShoppistWidgetProvider extends AppWidgetProvider {

    public static final String ACTION = "com.justplay1.shoppist.action";
    public static final String ACTION_ON_CHECK_ITEM_CLICK = "com.justplay1.shoppist.check_item_onclick";
    public static final String UPDATE_ALL_WIDGETS = "com.justplay1.shoppist.update_all_widgets";
    public static final String ACTION_ON_LIST_ITEM_CLICK = "com.justplay1.shoppist.item_onclick";
    public static final String ITEM_POSITION = "item_position";
    public static final String LIST_ITEM = "list_item";
    public static final String ITEM_STATUS = "item_status";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        updateAll(context, appWidgetManager, appWidgetIds);
    }

    static void updateAll(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences sp = context.getSharedPreferences(
                SelectListDialogActivity.WIDGET_PREF, Context.MODE_PRIVATE);

        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager, i, sp);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equalsIgnoreCase(ACTION_ON_LIST_ITEM_CLICK)) {
            int itemPos = intent.getIntExtra(ITEM_POSITION, -1);
            ShoppingListItem listItem = intent.getParcelableExtra(LIST_ITEM);

            if (intent.getStringExtra(ACTION).equalsIgnoreCase(ACTION_ON_CHECK_ITEM_CLICK)) {
                ShoppingListItemTable shoppingListTable = new ShoppingListItemTable(context);
                if (intent.getBooleanExtra(ITEM_STATUS, false)) {
                    listItem.setStatus(Status.NOT_DONE);
                } else {
                    listItem.setStatus(Status.DONE);
                }
                try {
                    shoppingListTable.update(listItem);
                } catch (RemoteException | OperationApplicationException e) {
                    e.printStackTrace();
                }

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                SharedPreferences sp = context.getSharedPreferences(SelectListDialogActivity.WIDGET_PREF, Activity.MODE_PRIVATE);
                int id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
                appWidgetManager.notifyAppWidgetViewDataChanged(id, R.id.list_view);
                return;
            }

            Intent startIntent = new Intent(context, AddListItemActivity.class);
            startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            ShoppingList list = new ShoppingList();
            list.setId(listItem.getParentListId());

            Bundle bundle = new Bundle();
            bundle.putParcelable(ShoppingList.class.getName(), list);
            bundle.putParcelable(ShoppingListItem.class.getName(), listItem);
            startIntent.putExtra(ActivityUtils.DATA, bundle);

            context.startActivity(startIntent);
        } else if (intent.getAction().equalsIgnoreCase(UPDATE_ALL_WIDGETS)) {
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);

            updateAll(context, appWidgetManager, ids);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        // Удаляем Preferences
        SharedPreferences.Editor editor = context.getSharedPreferences(
                SelectListDialogActivity.WIDGET_PREF, Context.MODE_PRIVATE).edit();
        for (int widgetID : appWidgetIds) {
            editor.remove(SelectListDialogActivity.WIDGET_LIST_ID + widgetID);
            editor.remove(SelectListDialogActivity.WIDGET_LIST_NAME + widgetID);
            editor.remove(SortWidgetActivity.WIDGET_SORT + widgetID);
        }
        editor.commit();
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    static void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, SharedPreferences sp) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);

        setHeader(rv, context, appWidgetId, sp);
        setList(rv, context, appWidgetId, sp);
        setListClick(rv, context, appWidgetId, sp);

        appWidgetManager.updateAppWidget(appWidgetId, rv);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view);
    }

    static void setList(RemoteViews rv, Context context, int appWidgetId, SharedPreferences sp) {
        Intent adapter = new Intent(context, ShoppistWidgetService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        adapter.putExtra(SelectListDialogActivity.WIDGET_LIST_NAME + appWidgetId,
                sp.getString(SelectListDialogActivity.WIDGET_LIST_NAME + appWidgetId, ""));
        adapter.putExtra(SelectListDialogActivity.WIDGET_LIST_ID + appWidgetId,
                sp.getString(SelectListDialogActivity.WIDGET_LIST_ID + appWidgetId, ""));
        adapter.putExtra(SortWidgetActivity.WIDGET_SORT + appWidgetId,
                sp.getInt(SortWidgetActivity.WIDGET_SORT + appWidgetId, 4));

        Uri data = Uri.parse(adapter.toUri(Intent.URI_INTENT_SCHEME));
        adapter.setData(data);
        rv.setRemoteAdapter(R.id.list_view, adapter);
        rv.setEmptyView(R.id.list_view, R.id.empty);
    }

    static void setListClick(RemoteViews rv, Context context, int appWidgetId, SharedPreferences sp) {
        Intent listClickIntent = new Intent(context, ShoppistWidgetProvider.class);
        listClickIntent.setAction(ACTION_ON_LIST_ITEM_CLICK);
        PendingIntent listClickPIntent = PendingIntent.getBroadcast(context, 0, listClickIntent, 0);
        rv.setPendingIntentTemplate(R.id.list_view, listClickPIntent);
    }

    static void setHeader(RemoteViews rv, Context context, int appWidgetId, SharedPreferences sp) {
        rv.setInt(R.id.toolbar, "setBackgroundColor", ShoppistPreferences.getColorPrimary());

        Intent updIntent = new Intent(context, SelectListDialogActivity.class);
        updIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        updIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent updPIntent = PendingIntent.getActivity(context, 0, updIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.select_list_btn, updPIntent);

        Intent sortBtnClickIntent = new Intent(context, SortWidgetActivity.class);
        sortBtnClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent sortBtnClickPIntent = PendingIntent.getActivity(context, 0, sortBtnClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.sort_btn, sortBtnClickPIntent);

        String listId = sp.getString(SelectListDialogActivity.WIDGET_LIST_ID + appWidgetId, null);
        String listName = sp.getString(SelectListDialogActivity.WIDGET_LIST_NAME + appWidgetId, null);
        if (listId == null) return;
        rv.setTextViewText(R.id.select_list_btn, listName);

        Intent onClickIntent = new Intent();
        Bundle bundle = new Bundle();
        onClickIntent.setClass(context, AddListItemActivity.class);

        ShoppingList list = new ShoppingList();
        list.setId(listId);
        bundle.putParcelable(ShoppingList.class.getName(), list);
        onClickIntent.putExtra(ActivityUtils.DATA, bundle);
        onClickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent onClickPIntent = PendingIntent.getActivity(context, 0, onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.add_btn, onClickPIntent);
    }

    public static void updateAllWidget(Context context) {
        Intent intent = new Intent(context, ShoppistWidgetProvider.class);
        intent.setAction(ShoppistWidgetProvider.UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        try {
            pIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
