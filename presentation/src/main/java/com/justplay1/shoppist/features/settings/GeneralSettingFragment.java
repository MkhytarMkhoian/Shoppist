/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.justplay1.shoppist.features.settings;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.jenzz.materialpreference.CheckBoxPreference;
import com.jenzz.materialpreference.Preference;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.shared.eventbus.ThemeUpdatedEvent;
import com.justplay1.shoppist.shared.eventbus.UiEventBus;
import com.justplay1.shoppist.utils.ColorThemeUpdater;
import com.justplay1.shoppist.features.settings.widget.ColorCheckBoxPreference;

/**
 * Created by Mkhytar Mkhoian.
 */
public class GeneralSettingFragment extends BaseSettingFragment implements ColorThemeUpdater {

    private static final String COLOR_THEME_BTN_ID = "color_theme";
    private static final String CONFIRM_TO_DELETE_BTN_ID = "confirm_to_delete";

    protected CheckBoxPreference confirmToDeleteBtn;

    public static GeneralSettingFragment newInstance() {
        return new GeneralSettingFragment();
    }

    @Override
    protected int getPreferencesResId() {
        return R.xml.general_setting;
    }

    protected void initFrame() {
        super.initFrame();
        updateGeneralCheckBox();

        Preference colorThemeBtn = (Preference) findPreference(COLOR_THEME_BTN_ID);
        colorThemeBtn.setOnPreferenceClickListener(this);
        confirmToDeleteBtn.setChecked(preferences.isNeedShowConfirmDeleteDialog());
    }

    @Override
    public boolean onPreferenceClick(android.preference.Preference preference) {
        switch (preference.getKey()) {
            case COLOR_THEME_BTN_ID:
                FragmentManager fm = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
                SelectThemeColorDialogFragment colorDialog = SelectThemeColorDialogFragment.newInstance(preferences.getColorPrimary());
                colorDialog.setOnColorSelectedListener((colorPrimary, colorPrimaryDark) -> {
                    preferences.setColorPrimary(colorPrimary);
                    preferences.setColorPrimaryDark(colorPrimaryDark);
                    updateTheme();
                });
                colorDialog.show(fm, SelectThemeColorDialogFragment.class.getName());
                break;
            case CONFIRM_TO_DELETE_BTN_ID:
                preferences.setConfirmDeleteDialog(((CheckBoxPreference) preference).isChecked());
                break;
        }
        return true;
    }

    protected void updateGeneralCheckBox() {
        if (findPreference(CONFIRM_TO_DELETE_BTN_ID) != null) {
            getPreferenceScreen().removePreference(findPreference(CONFIRM_TO_DELETE_BTN_ID));
            confirmToDeleteBtn = null;
        }

        if (confirmToDeleteBtn == null) {
            confirmToDeleteBtn = new ColorCheckBoxPreference(getActivity(), preferences.getColorPrimary());
            confirmToDeleteBtn.setKey(CONFIRM_TO_DELETE_BTN_ID);
            confirmToDeleteBtn.setTitle(R.string.confirm_to_delete);
            confirmToDeleteBtn.setSummary(R.string.confirm_to_delete_summary);
        }
        getPreferenceScreen().addPreference(confirmToDeleteBtn);
        confirmToDeleteBtn.setOnPreferenceClickListener(this);
    }

    @Override
    public void updateTheme() {
        ((SettingsActivity) getActivity()).refreshToolbarColor();
        ((SettingsActivity) getActivity()).setStatusBarColor();
        updateGeneralCheckBox();

        UiEventBus.instanceOf().post(new ThemeUpdatedEvent());
    }
}
