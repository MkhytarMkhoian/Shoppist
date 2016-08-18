package com.justplay1.shoppist.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Mkhytar on 14.10.2015.
 */
public class ShoppistWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ShoppistFactory(getApplicationContext(), intent);
    }
}
