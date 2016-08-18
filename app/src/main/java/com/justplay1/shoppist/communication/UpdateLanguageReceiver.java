package com.justplay1.shoppist.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Category;

import java.util.List;

/**
 * Created by Mkhitar on 30.01.2015.
 */
public class UpdateLanguageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {

            App.get().getCategoriesManager().updateLanguage(context, new ExecutorListener<List<Category>>() {
                @Override
                public void start() {

                }

                @Override
                public void complete(List<Category> result) {

                }

                @Override
                public void error(Exception e) {

                }
            });

            App.get().getUnitManager().updateLanguage(new ExecutorListener<List<String>>() {
                @Override
                public void start() {

                }

                @Override
                public void complete(List<String> result) {

                }

                @Override
                public void error(Exception e) {

                }
            });
        }
    }
}
