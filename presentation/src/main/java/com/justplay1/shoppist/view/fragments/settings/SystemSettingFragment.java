package com.justplay1.shoppist.view.fragments.settings;

import com.jenzz.materialpreference.CheckBoxPreference;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.component.ColorCheckBoxPreference;

/**
 * Created by Mkhytar on 19.12.2015.
 */
public class SystemSettingFragment extends BaseSettingFragment {

    private static final String LOCK_SCREEN_ID = "lockscreen";

    protected CheckBoxPreference mLockScreen;

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
        mLockScreen.setChecked(mPreferences.isLockScreen());
    }

    @Override
    public boolean onPreferenceClick(android.preference.Preference preference) {
        switch (preference.getKey()) {
            case LOCK_SCREEN_ID:
                boolean isChecked = ((CheckBoxPreference) preference).isChecked();
                mPreferences.setLockScreen(isChecked);
                ShoppistUtils.setKeepScreenOn(getActivity().getWindow(), mPreferences.isLockScreen());
                break;
        }
        return true;
    }

    protected void updateSystemCheckBox() {
        if (findPreference(LOCK_SCREEN_ID) != null) {
            getPreferenceScreen().removePreference(findPreference(LOCK_SCREEN_ID));
        }

        if (mLockScreen == null) {
            mLockScreen = new ColorCheckBoxPreference(getActivity(), mPreferences.getColorPrimary());
            mLockScreen.setKey(LOCK_SCREEN_ID);
            mLockScreen.setTitle(R.string.lockscreen);
            mLockScreen.setSummary(R.string.lockscreen_summary);
        }
        getPreferenceScreen().removePreference(mLockScreen);
        getPreferenceScreen().addPreference(mLockScreen);

        mLockScreen.setOnPreferenceClickListener(this);
    }
}
