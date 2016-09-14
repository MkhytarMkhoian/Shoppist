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

package com.justplay1.shoppist.view.fragments.settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.jenzz.materialpreference.CheckBoxPreference;
import com.jenzz.materialpreference.Preference;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.bus.ThemeUpdatedEvent;
import com.justplay1.shoppist.utils.ColorThemeUpdater;
import com.justplay1.shoppist.bus.UiEventBus;
import com.justplay1.shoppist.view.activities.SettingsActivity;
import com.justplay1.shoppist.view.component.ColorCheckBoxPreference;
import com.justplay1.shoppist.view.fragments.dialog.SelectThemeColorDialogFragment;

/**
 * Created by Mkhytar Mkhoian.
 */
public class GeneralSettingFragment extends BaseSettingFragment implements ColorThemeUpdater {

    private static final String COLOR_THEME_BTN_ID = "color_theme";
    private static final String CONFIRM_TO_DELETE_BTN_ID = "confirm_to_delete";
    private static final String LONG_ITEM_CLICK_ACTION_ID = "long_item_click_action_id";

    private Preference mColorThemeBtn;
    private Preference mLongItemClickActionBtn;

    protected CheckBoxPreference mConfirmToDeleteBtn;

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

        mColorThemeBtn = (Preference) findPreference(COLOR_THEME_BTN_ID);
        mColorThemeBtn.setOnPreferenceClickListener(this);
        mLongItemClickActionBtn.setOnPreferenceClickListener(this);
        mConfirmToDeleteBtn.setChecked(mPreferences.isNeedShowConfirmDeleteDialog());
    }

    @Override
    public boolean onPreferenceClick(android.preference.Preference preference) {
        switch (preference.getKey()) {
            case COLOR_THEME_BTN_ID:
                FragmentManager fm = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
                SelectThemeColorDialogFragment colorDialog = SelectThemeColorDialogFragment.newInstance(mPreferences.getColorPrimary());
                colorDialog.setOnColorSelectedListener((colorPrimary, colorPrimaryDark) -> {
                    mPreferences.setColorPrimary(colorPrimary);
                    mPreferences.setColorPrimaryDark(colorPrimaryDark);
                    updateTheme();
                });
                colorDialog.show(fm, SelectThemeColorDialogFragment.class.getName());
                break;
            case CONFIRM_TO_DELETE_BTN_ID:
                mPreferences.setConfirmDeleteDialog(((CheckBoxPreference) preference).isChecked());
                break;
            case LONG_ITEM_CLICK_ACTION_ID:
                showLongItemClickActionDialog();
                break;
        }
        return true;
    }

    protected void updateGeneralCheckBox() {
        if (findPreference(CONFIRM_TO_DELETE_BTN_ID) != null) {
            getPreferenceScreen().removePreference(findPreference(CONFIRM_TO_DELETE_BTN_ID));
            mConfirmToDeleteBtn = null;
        }
        if (findPreference(LONG_ITEM_CLICK_ACTION_ID) != null) {
            getPreferenceScreen().removePreference(findPreference(LONG_ITEM_CLICK_ACTION_ID));
            mLongItemClickActionBtn = null;
        }

        if (mConfirmToDeleteBtn == null) {
            mConfirmToDeleteBtn = new ColorCheckBoxPreference(getActivity(), mPreferences.getColorPrimary());
            mConfirmToDeleteBtn.setKey(CONFIRM_TO_DELETE_BTN_ID);
            mConfirmToDeleteBtn.setTitle(R.string.confirm_to_delete);
            mConfirmToDeleteBtn.setSummary(R.string.confirm_to_delete_summary);
        }
        getPreferenceScreen().addPreference(mConfirmToDeleteBtn);

        mConfirmToDeleteBtn.setOnPreferenceClickListener(this);

        if (mLongItemClickActionBtn == null) {
            mLongItemClickActionBtn = new Preference(getActivity());
            mLongItemClickActionBtn.setKey(LONG_ITEM_CLICK_ACTION_ID);
            mLongItemClickActionBtn.setTitle(R.string.long_item_click_action);
            switch (mPreferences.getLongItemClickAction()) {
                case 0:
                    mLongItemClickActionBtn.setSummary(R.string.select_item);
                    break;
                case 1:
                    mLongItemClickActionBtn.setSummary(R.string.edit_item);
                    break;
            }
        }
        getPreferenceScreen().addPreference(mLongItemClickActionBtn);
        mLongItemClickActionBtn.setOnPreferenceClickListener(this);
    }

    private void showLongItemClickActionDialog() {
        String[] swipeActions = {getString(R.string.select_item), getString(R.string.edit_item)};

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.dialog_list_item, swipeActions);

        final int[] selectedItem = {mPreferences.getLongItemClickAction()};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setSingleChoiceItems(adapter, selectedItem[0], (dialog, which) -> {
            selectedItem[0] = which;
        });

        DialogInterface.OnClickListener listener = (dialog, which) -> {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    mPreferences.setLongItemClickAction(selectedItem[0]);
                    mLongItemClickActionBtn.setSummary(adapter.getItem(selectedItem[0]));
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        };

        builder.setTitle(R.string.long_item_click_action);
        builder.setPositiveButton(R.string.choose, listener);
        builder.setNegativeButton(R.string.cancel, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(mPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(mPreferences.getColorPrimary());
    }

    @Override
    public void updateTheme() {
        ((SettingsActivity) getActivity()).refreshToolbarColor();
        ((SettingsActivity) getActivity()).setStatusBarColor();
        updateGeneralCheckBox();

        UiEventBus.instanceOf().post(new ThemeUpdatedEvent());
    }
}
