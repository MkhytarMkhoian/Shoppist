package com.justplay1.shoppist.ui.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.ui.fragments.settings.AccountSettingFragment;
import com.justplay1.shoppist.ui.fragments.settings.GeneralSettingFragment;
import com.justplay1.shoppist.ui.fragments.settings.MainSettingFragment;
import com.justplay1.shoppist.ui.fragments.settings.NotificationSettingFragment;
import com.justplay1.shoppist.ui.fragments.settings.ShoppingListsSettingFragment;
import com.justplay1.shoppist.ui.fragments.settings.SystemSettingFragment;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ActivityUtils;

/**
 * Created by Mkhytar on 19.12.2015.
 */
public class SettingsActivity extends AppCompatActivity {

    public static final int GENERAL_SETTING = 1;
    public static final int NOTIFICATION_SETTING = 2;
    public static final int SHOPPING_LISTS_SETTING = 3;
    public static final int SYSTEM_SETTING = 4;
    public static final int ACCOUNT_SETTING = 5;

    public static final String SETTING_ID = "setting_id";

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initializeToolbar();
        PreferenceFragment fragment = getFragmentInstance();

        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, fragment.getClass().getName())
                .commit();
    }

    private PreferenceFragment getFragmentInstance() {
        if (getIntent() != null) {
            Bundle data = getIntent().getBundleExtra(ActivityUtils.DATA);
            if (data != null) {
                int flag = data.getInt(SETTING_ID, 0);
                switch (flag) {
                    case GENERAL_SETTING:
                        mToolbar.setTitle(R.string.category_general);
                        return GeneralSettingFragment.newInstance();
                    case ACCOUNT_SETTING:
                        mToolbar.setTitle(R.string.category_account);
                        return AccountSettingFragment.newInstance();
                    case NOTIFICATION_SETTING:
                        mToolbar.setTitle(R.string.category_notification);
                        return NotificationSettingFragment.newInstance();
                    case SHOPPING_LISTS_SETTING:
                        mToolbar.setTitle(R.string.shopping_lists);
                        return ShoppingListsSettingFragment.newInstance();
                    case SYSTEM_SETTING:
                        mToolbar.setTitle(R.string.category_system);
                        return SystemSettingFragment.newInstance();
                    default:
                        mToolbar.setTitle(R.string.title_activity_settings);
                        return new MainSettingFragment();
                }
            }
        }
        return new MainSettingFragment();
    }

    private void initializeToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.title_activity_settings);
        mToolbar.setBackgroundColor(ShoppistPreferences.getColorPrimary());
        mToolbar.setNavigationIcon(R.drawable.ic_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.finishActivity(SettingsActivity.this);
            }
        });
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
    }

    public void refreshToolbarColor() {
        mToolbar.setBackgroundColor(ShoppistPreferences.getColorPrimary());
    }

    @Override
    public void onBackPressed() {
        ActivityUtils.finishActivity(this);
    }
}
