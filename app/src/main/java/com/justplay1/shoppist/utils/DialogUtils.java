package com.justplay1.shoppist.utils;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;

/**
 * Created by Mkhytar on 01.11.2015.
 */
public class DialogUtils {

    public static void showErrorDialog(Context context, String message) {
        showErrorDialog(context, message, null);
    }

    public static void showErrorDialog(Context context, String message, int titleRes) {
        showErrorDialog(context, message, context.getString(titleRes));
    }

    public static void showErrorDialog(Context context, String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        if (title != null) {
            builder.setTitle(title);
        }
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ShoppistPreferences.getColorPrimary());
    }

    public static void showRateDialog(final Context context) {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            context.startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            context.startActivity(intent);
                        }
                        ShoppistPreferences.setNeedShowRateDialog(31);
                        dialog.dismiss();
                    case Dialog.BUTTON_NEGATIVE:
                        ShoppistPreferences.setNeedShowRateDialog(31);
                        dialog.dismiss();
                        break;
                    case Dialog.BUTTON_NEUTRAL:
                        ShoppistPreferences.setNeedShowRateDialog(0);
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.rate_shoppist);
        builder.setMessage(R.string.rate_info);
        builder.setNeutralButton(R.string.no_later, listener);
        builder.setPositiveButton(R.string.rate, listener);
        builder.setNegativeButton(R.string.no, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ShoppistPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(ShoppistPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEUTRAL).setTextColor(ShoppistPreferences.getColorPrimary());
    }

    public static void showNoteDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(context.getString(R.string.note));
        builder.setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showGoToShoppistProDialog(final Context context) {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        Uri uri = Uri.parse("market://details?id=" + "com.justplay1.shoppist");
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            context.startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + "com.justplay1.shoppist"));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            context.startActivity(intent);
                        }
                        dialog.dismiss();
                    case Dialog.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.shoppist_pro);
        builder.setMessage(context.getString(R.string.pro_account_info));
        builder.setPositiveButton(R.string.buy, listener);
        builder.setNegativeButton(R.string.no_later, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ShoppistPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(ShoppistPreferences.getColorPrimary());
    }
}
