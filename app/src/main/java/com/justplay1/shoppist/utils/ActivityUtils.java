package com.justplay1.shoppist.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Toast;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;

import java.util.Locale;

/**
 * Created by Mkhytar on 26.10.2015.
 */
public class ActivityUtils {

    public static final String NEED_FINISH_FLAG = "need_finish_flag";
    public static final String DATA = "data";
    public static final String FLAG = "flag";
    public static final String FROM_SERVER = "from_server";
    public static final String NEW_DATA = "NEW_DATA";
    public static final String OLD_ID = "old_id";
    public static final String NEW_ID = "new_id";

    private static void startActivityAnimation(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.overridePendingTransition(R.anim.activity_open_enter_v21, R.anim.activity_open_exit_v21);
        } else {
            activity.overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
        }
    }

    private static void finishActivityAnimation(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.overridePendingTransition(R.anim.activity_close_enter_v21, R.anim.activity_close_exit_v21);
        } else {
            activity.overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit);
        }
    }

    public static <T extends Activity> void startNextActivity(Activity activity, Class<T> activityClass, View transitionView, Bundle data) {
        Intent intent = new Intent(activity, activityClass);
        intent.putExtra(DATA, data);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
// TODO Transition
            activity.startActivity(intent);
            startActivityAnimation(activity);
        } else {
            activity.startActivity(intent);
            startActivityAnimation(activity);
        }
    }

    public static <T extends Activity> void startNextActivityForResult(Activity activity, Class<T> activityClass, int requestCode, View transitionView, Bundle data) {
        Intent intent = new Intent(activity, activityClass);
        intent.putExtra(DATA, data);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
// TODO Transition
            activity.startActivityForResult(intent, requestCode);
            startActivityAnimation(activity);
        } else {
            activity.startActivityForResult(intent, requestCode);
            startActivityAnimation(activity);
        }
    }

    public static <T extends Activity> void startNextActivity(Activity activity, Class<T> activityClass, int enterAnimId, int exitAnimId, Bundle data) {
        Intent intent = new Intent(activity, activityClass);
        intent.putExtra(DATA, data);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
// TODO Transition
            activity.startActivity(intent);
            startActivityAnimation(activity);
        } else {
            activity.startActivity(intent);
            startActivityAnimation(activity);
        }
    }

    public static <T extends Activity> void startNextActivity(Activity activity, Class<T> activityClass, View transitionView,
                                                              int x, int y, int startWidth, int startHeight, Bundle data) {
        Intent intent = new Intent(activity, activityClass);
        intent.putExtra(DATA, data);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // TODO Transition
            activity.startActivity(intent);
            startActivityAnimation(activity);
        } else {
            activity.startActivity(intent);
            startActivityAnimation(activity);
        }
    }

    public static <T extends Activity> void startSearchActivity(Activity activity, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // TODO Transition
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else {
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    public static void finishActivityWithResult(Activity activity, int resultCode, Intent data) {
        activity.setResult(resultCode, data);
        finishActivity(activity);
    }

    public static void finishActivity(Activity activity) {
        activity.finish();
        finishActivityAnimation(activity);
    }

    public static Bundle getBundleWithFlag(int flag) {
        Bundle bundle = new Bundle();
        bundle.putInt(FLAG, flag);
        return bundle;
    }

    public static int getFlagFromBundle(Bundle bundle) {
        if (bundle == null) {
            return -1;
        } else {
            return bundle.getInt(FLAG, -1);
        }
    }

    public static boolean getFlagFromServer(Bundle bundle) {
        return bundle != null && bundle.getBoolean(FROM_SERVER, false);
    }

    public static void getRemoveFlagFromBundle(Bundle bundle) {
        bundle.remove(FLAG);
    }

    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        }
    }

    public static void setStatusBarColor(Activity activity) {
        setStatusBarColor(activity, ShoppistPreferences.getColorPrimaryDark());
    }

    public static void startTextToSpeech(Activity context, String prompt, int requestCode) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, prompt);
        try {
            context.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(context, context.getString(R.string.recognition_not_present),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
