package com.justplay1.shoppist.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.AppComponent;
import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.navigation.Navigator;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ShoppistUtils;

import javax.inject.Inject;


/**
 * Created by Mkhitar on 05.03.2015.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    Navigator mNavigator;
    @Inject
    ShoppistPreferences mPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAppComponent().inject(this);
        setStatusBarColor();
    }

    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    protected AppComponent getAppComponent() {
        return ((App) getApplication()).getAppComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    public void replaceFragment(int id, Fragment frag, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(id, frag, tag);
        transaction.commit();
    }

    public boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

    public void setStatusBarColor() {
        setStatusBarColor(mPreferences.getColorPrimaryDark());
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShoppistUtils.setKeepScreenOn(getWindow(), mPreferences.isLockScreen());
    }

    public void finishActivityWithResult(Activity activity, int resultCode, Intent data) {
        activity.setResult(resultCode, data);
        finishActivity(activity);
    }

    public void finishActivity(Activity activity) {
        activity.finish();
        finishActivityAnimation(activity);
    }

    public void finishActivity() {
        finish();
        finishActivityAnimation(this);
    }

    private static void finishActivityAnimation(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.overridePendingTransition(R.anim.activity_close_enter_v21, R.anim.activity_close_exit_v21);
        } else {
            activity.overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit);
        }
    }
}
