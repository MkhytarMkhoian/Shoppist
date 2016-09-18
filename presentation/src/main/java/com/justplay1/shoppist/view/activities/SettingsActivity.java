/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.justplay1.shoppist.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.view.fragments.settings.GeneralSettingFragment;
import com.justplay1.shoppist.view.fragments.settings.ListsSettingFragment;
import com.justplay1.shoppist.view.fragments.settings.MainSettingFragment;
import com.justplay1.shoppist.view.fragments.settings.SystemSettingFragment;

/**
 * Created by Mkhytar Mkhoian.
 */
public class SettingsActivity extends BaseActivity implements MainSettingFragment.MainSettingFragmentInteractionListener {

    public static final int GENERAL_SETTING = 1;
    public static final int LISTS_SETTING = 2;
    public static final int SYSTEM_SETTING = 3;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
    public void openSystemSetting() {
        mNavigator.navigateToSettingScreen(this, SYSTEM_SETTING);
    }
}
