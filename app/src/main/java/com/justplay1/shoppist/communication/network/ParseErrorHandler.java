package com.justplay1.shoppist.communication.network;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.App;
import com.parse.ParseException;

/**
 * Created by Mkhitar on 12.06.2015.
 */
public class ParseErrorHandler {

    public static void handleParseError(Exception e) {
        if (e instanceof ParseException) {
            handleParseError((ParseException) e);
        } else {
            e.printStackTrace();
        }
    }

    public static void handleParseError(ParseException e) {
        Context context = App.get().getTablesHolder().getContext();
        switch (e.getCode()) {
            case ParseException.INVALID_SESSION_TOKEN:
                new AlertDialog.Builder(context)
                        .setMessage("Session is no longer valid, please log out and log in again.")
                        .setCancelable(false).setPositiveButton("OK", null)
                        .create()
                        .show();
                break;
            case ParseException.CONNECTION_FAILED:
                showToast(context, context.getString(R.string.error_no_network_connection));
                break;
            case ParseException.OBJECT_NOT_FOUND:

                break;
            case ParseException.INVALID_ACL:

                break;
            case ParseException.TIMEOUT:

                break;
            case ParseException.INVALID_EMAIL_ADDRESS:
                showToast(context, R.string.com_parse_ui_invalid_email_toast);
                break;
            case ParseException.EMAIL_NOT_FOUND:
                showToast(context, R.string.com_parse_ui_invalid_email_toast);
                break;
            case ParseException.REQUEST_LIMIT_EXCEEDED:
                showToast(context, R.string.server_is_overloaded);
                break;
            case ParseException.SESSION_MISSING:

                break;
            case ParseException.ACCOUNT_ALREADY_LINKED:

                break;
            case ParseException.USERNAME_TAKEN:
                showToast(context, R.string.username_taken_toast);
                break;
            case ParseException.EMAIL_TAKEN:
                showToast(context, R.string.com_parse_ui_email_taken_toast);
                break;
            default:
                showToast(context, R.string.com_parse_ui_signup_failed_unknown_toast);
        }
    }

    public static void showToast(Context context, int id) {
        showToast(context, context.getString(id));
    }

    public static void showToast(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

        //TODO Snackbar
//        Snackbar.make(parentLayout, R.string.snackbar_text, Snackbar.LENGTH_LONG)
//                .setAction(R.string.snackbar_action, myOnClickListener)
//                .show();
    }
}
