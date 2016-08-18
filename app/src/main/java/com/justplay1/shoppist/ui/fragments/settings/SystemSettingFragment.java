package com.justplay1.shoppist.ui.fragments.settings;

import com.jenzz.materialpreference.CheckBoxPreference;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.ui.views.ColorCheckBoxPreference;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ShoppistUtils;

/**
 * Created by Mkhytar on 19.12.2015.
 */
public class SystemSettingFragment extends BaseSettingFragment {

    public static final String LOCK_SCREEN_ID = "lockscreen";

    protected CheckBoxPreference mLockScreen;

    public static SystemSettingFragment newInstance() {
        return new SystemSettingFragment();
    }

    @Override
    protected int getPreferencesResId() {
        return R.xml.system_setting;
    }

    protected void initializeFrame() {
        super.initializeFrame();
        updateSystemCheckBox();
        mLockScreen.setChecked(ShoppistPreferences.isLockScreen());
    }

    @Override
    public boolean onPreferenceClick(android.preference.Preference preference) {
        switch (preference.getKey()) {
            case LOCK_SCREEN_ID:
                boolean isChecked = ((CheckBoxPreference) preference).isChecked();
                ShoppistPreferences.setLockScreen(isChecked);
                ShoppistUtils.setKeepScreenOn(getActivity().getWindow(), ShoppistPreferences.isLockScreen());
                break;
        }
        return true;
    }

    protected void updateSystemCheckBox() {
        if (findPreference(LOCK_SCREEN_ID) != null) {
            getPreferenceScreen().removePreference(findPreference(LOCK_SCREEN_ID));
        }

        if (mLockScreen == null) {
            mLockScreen = new ColorCheckBoxPreference(getActivity());
            mLockScreen.setKey(LOCK_SCREEN_ID);
            mLockScreen.setTitle(R.string.lockscreen);
            mLockScreen.setSummary(R.string.lockscreen_summary);
        }
        getPreferenceScreen().removePreference(mLockScreen);
        getPreferenceScreen().addPreference(mLockScreen);

        mLockScreen.setOnPreferenceClickListener(this);
    }
}
