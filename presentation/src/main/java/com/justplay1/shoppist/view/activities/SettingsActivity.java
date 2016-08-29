package com.justplay1.shoppist.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.view.fragments.settings.AccountSettingFragment;
import com.justplay1.shoppist.view.fragments.settings.GeneralSettingFragment;
import com.justplay1.shoppist.view.fragments.settings.MainSettingFragment;
import com.justplay1.shoppist.view.fragments.settings.NotificationSettingFragment;
import com.justplay1.shoppist.view.fragments.settings.ListsSettingFragment;
import com.justplay1.shoppist.view.fragments.settings.SystemSettingFragment;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 19.12.2015.
 */
public class SettingsActivity extends BaseActivity implements MainSettingFragment.MainSettingFragmentInteractionListener{

    public static final int GENERAL_SETTING = 1;
    public static final int NOTIFICATION_SETTING = 2;
    public static final int LISTS_SETTING = 3;
    public static final int SYSTEM_SETTING = 4;
    public static final int ACCOUNT_SETTING = 5;

    private Toolbar mToolbar;

    public static Intent getCallingIntent(Context context, int settingId) {
        Intent callingIntent = new Intent(context, SettingsActivity.class);
        callingIntent.putExtra(Const.SETTING_ID, settingId);
        return callingIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_single_fragment);

        initToolbar();
        PreferenceFragment fragment = getFragmentInstance();

        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, fragment.getClass().getName())
                .commit();
    }

    private PreferenceFragment getFragmentInstance() {
        if (getIntent() != null) {
            int flag = getIntent().getIntExtra(Const.SETTING_ID, 0);
            switch (flag) {
                case GENERAL_SETTING:
                    mToolbar.setTitle(R.string.category_general);
                    return GeneralSettingFragment.newInstance();
//                case ACCOUNT_SETTING:
//                    mToolbar.setTitle(R.string.category_account);
//                    return AccountSettingFragment.newInstance();
                case NOTIFICATION_SETTING:
                    mToolbar.setTitle(R.string.category_notification);
                    return NotificationSettingFragment.newInstance();
                case LISTS_SETTING:
                    mToolbar.setTitle(R.string.shopping_lists);
                    return ListsSettingFragment.newInstance();
                case SYSTEM_SETTING:
                    mToolbar.setTitle(R.string.category_system);
                    return SystemSettingFragment.newInstance();
                default:
                    mToolbar.setTitle(R.string.title_activity_settings);
                    return new MainSettingFragment();
            }
        }
        return new MainSettingFragment();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.title_activity_settings);
        mToolbar.setBackgroundColor(mPreferences.getColorPrimary());
        mToolbar.setNavigationIcon(R.drawable.ic_back_white);
        mToolbar.setNavigationOnClickListener(v -> finishActivity());
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
    }

    public void refreshToolbarColor() {
        mToolbar.setBackgroundColor(mPreferences.getColorPrimary());
    }

    @Override
    public void onBackPressed() {
        finishActivity(this);
    }

    @Override
    public void openGeneralSetting() {
        mNavigator.navigateToSettingScreen(this, GENERAL_SETTING);
    }

    @Override
    public void openListsSetting() {
        mNavigator.navigateToSettingScreen(this, LISTS_SETTING);
    }

    @Override
    public void openNotificationSetting() {
        mNavigator.navigateToSettingScreen(this, NOTIFICATION_SETTING);
    }

    @Override
    public void openSystemSetting() {
        mNavigator.navigateToSettingScreen(this, SYSTEM_SETTING);
    }

    @Override
    public void openAccountSetting() {
        mNavigator.navigateToSettingScreen(this, ACCOUNT_SETTING);
    }
}
