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

import com.jenzz.materialpreference.CheckBoxPreference;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.component.ColorCheckBoxPreference;

/**
 * Created by Mkhytar Mkhoian.
 */
public class SystemSettingFragment extends BaseSettingFragment {

    private static final String LOCK_SCREEN_ID = "lockscreen";

    protected CheckBoxPreference lockScreen;

    public static SystemSettingFragment newInstance() {
        return new SystemSettingFragment();
    }

    @Override
    protected int getPreferencesResId() {
        return R.xml.system_setting;
    }

    protected void initFrame() {
        super.initFrame();
        updateSystemCheckBox();
        lockScreen.setChecked(preferences.isLockScreen());
    }

    @Override
    public boolean onPreferenceClick(android.preference.Preference preference) {
        switch (preference.getKey()) {
            case LOCK_SCREEN_ID:
                boolean isChecked = ((CheckBoxPreference) preference).isChecked();
                preferences.setLockScreen(isChecked);
                ShoppistUtils.setKeepScreenOn(getActivity().getWindow(), preferences.isLockScreen());
                break;
        }
        return true;
    }

    protected void updateSystemCheckBox() {
        if (findPreference(LOCK_SCREEN_ID) != null) {
            getPreferenceScreen().removePreference(findPreference(LOCK_SCREEN_ID));
        }

        if (lockScreen == null) {
            lockScreen = new ColorCheckBoxPreference(getActivity(), preferences.getColorPrimary());
            lockScreen.setKey(LOCK_SCREEN_ID);
            lockScreen.setTitle(R.string.lockscreen);
            lockScreen.setSummary(R.string.lockscreen_summary);
        }
        getPreferenceScreen().removePreference(lockScreen);
        getPreferenceScreen().addPreference(lockScreen);

        lockScreen.setOnPreferenceClickListener(this);
    }
}
