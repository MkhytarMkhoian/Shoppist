package com.justplay1.shoppist.view.fragments.settings;

import com.jenzz.materialpreference.CheckBoxPreference;
import com.jenzz.materialpreference.PreferenceCategory;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.view.component.ColorCheckBoxPreference;
import com.justplay1.shoppist.view.component.ColorPreferenceCategory;

/**
 * Created by Mkhytar on 19.12.2015.
 */
public class NotificationSettingFragment extends BaseSettingFragment {

    private static final String NOTIFICATION_ENABLE_ID = "enable";
    private static final String VIBRATION_ID = "vibration";
    private static final String SOUND_ID = "sound";
    private static final String AVAILABLE_UPDATE_NOTIFICATION_ID = "available_update_notification_id";
    private static final String AVAILABLE_NEW_NOTIFICATION_ID = "available_new_notification_id";
    private static final String AVAILABLE_DELETE_NOTIFICATION_ID = "available_delete_notification_id";
    private static final String AVAILABLE_BOUGHT_NOTIFICATION_ID = "available_bought_notification_id";
    private static final String AVAILABLE_NOT_BOUGHT_NOTIFICATION_ID = "available_not_bought_notification_id";
    private static final String SYNC_CATEGORY = "Sync_category";

    private CheckBoxPreference mNotificationEnable;
    private CheckBoxPreference mVibration;
    private CheckBoxPreference mSound;
    private CheckBoxPreference mAvailableUpdateNotification;
    private CheckBoxPreference mAvailableNewNotification;
    private CheckBoxPreference mAvailableDeleteNotification;
    private CheckBoxPreference mAvailableBoughtNotification;
    private CheckBoxPreference mAvailableNotBoughtNotification;

    private PreferenceCategory mSyncCategory;

    public static NotificationSettingFragment newInstance() {
        return new NotificationSettingFragment();
    }

    @Override
    protected int getPreferencesResId() {
        return R.xml.notification_setting;
    }

    protected void initFrame() {
        super.initFrame();
        updateNotificationCategoryCheckBox();

        mNotificationEnable.setChecked(mPreferences.isNotificationEnable());
        mVibration.setChecked(mPreferences.isNotificationVibration());
        mSound.setChecked(mPreferences.isNotificationSound());
        mAvailableUpdateNotification.setChecked(mPreferences.isAvailableUpdateNotification());
        mAvailableNewNotification.setChecked(mPreferences.isAvailableNewNotification());
        mAvailableDeleteNotification.setChecked(mPreferences.isAvailableDeleteNotification());
        mAvailableBoughtNotification.setChecked(mPreferences.isAvailableBoughtNotification());
        mAvailableNotBoughtNotification.setChecked(mPreferences.isAvailableNotBoughtNotification());
    }

    @Override
    public boolean onPreferenceClick(android.preference.Preference preference) {
        switch (preference.getKey()) {
            case NOTIFICATION_ENABLE_ID:
                mPreferences.setNotificationEnable(((CheckBoxPreference) preference).isChecked());
                break;
            case VIBRATION_ID:
                mPreferences.setNotificationVibration(((CheckBoxPreference) preference).isChecked());
                break;
            case SOUND_ID:
                mPreferences.setNotificationSound(((CheckBoxPreference) preference).isChecked());
                break;
            case AVAILABLE_UPDATE_NOTIFICATION_ID:
                mPreferences.setAvailableUpdateNotification(((CheckBoxPreference) preference).isChecked());
                break;
            case AVAILABLE_DELETE_NOTIFICATION_ID:
                mPreferences.setAvailableDeleteNotification(((CheckBoxPreference) preference).isChecked());
                break;
            case AVAILABLE_BOUGHT_NOTIFICATION_ID:
                mPreferences.setAvailableBoughtNotification(((CheckBoxPreference) preference).isChecked());
                break;
            case AVAILABLE_NEW_NOTIFICATION_ID:
                mPreferences.setAvailableNewNotification(((CheckBoxPreference) preference).isChecked());
                break;
            case AVAILABLE_NOT_BOUGHT_NOTIFICATION_ID:
                mPreferences.setAvailableNotBoughtNotification(((CheckBoxPreference) preference).isChecked());
                break;
        }
        return true;
    }

    protected void updateNotificationCategoryCheckBox() {
        getPreferenceScreen().removeAll();

        if (mNotificationEnable == null) {
            mNotificationEnable = new ColorCheckBoxPreference(getActivity(), mPreferences.getColorPrimary());
            mNotificationEnable.setKey(NOTIFICATION_ENABLE_ID);
            mNotificationEnable.setTitle(R.string.notification_onn);
        }

        getPreferenceScreen().removePreference(mNotificationEnable);
        getPreferenceScreen().addPreference(mNotificationEnable);

        if (mSound == null) {
            mSound = new ColorCheckBoxPreference(getActivity(), mPreferences.getColorPrimary());
            mSound.setKey(SOUND_ID);
            mSound.setTitle(R.string.notification_sound);
        }

        getPreferenceScreen().removePreference(mSound);
        getPreferenceScreen().addPreference(mSound);

        if (mVibration == null) {
            mVibration = new ColorCheckBoxPreference(getActivity(), mPreferences.getColorPrimary());
            mVibration.setKey(VIBRATION_ID);
            mVibration.setTitle(R.string.notification_vibration);
        }
        getPreferenceScreen().removePreference(mVibration);
        getPreferenceScreen().addPreference(mVibration);

        if (getPreferenceScreen().findPreference(SYNC_CATEGORY) != null) {
            getPreferenceScreen().removePreference(mSyncCategory);
        }

        if (mSyncCategory == null) {
            mSyncCategory = new ColorPreferenceCategory(getActivity(), mPreferences.getColorPrimary());
            mSyncCategory.setKey(SYNC_CATEGORY);
            mSyncCategory.setTitle(R.string.synchronization);
        }
        getPreferenceScreen().removePreference(mSyncCategory);
        getPreferenceScreen().addPreference(mSyncCategory);

        if (mAvailableBoughtNotification == null) {
            mAvailableBoughtNotification = new ColorCheckBoxPreference(getActivity(), mPreferences.getColorPrimary());
            mAvailableBoughtNotification.setKey(AVAILABLE_BOUGHT_NOTIFICATION_ID);
            mAvailableBoughtNotification.setTitle("Notify About Bought Goods");
        }
        mSyncCategory.removePreference(mAvailableBoughtNotification);
        mSyncCategory.addPreference(mAvailableBoughtNotification);

        if (mAvailableDeleteNotification == null) {
            mAvailableDeleteNotification = new ColorCheckBoxPreference(getActivity(), mPreferences.getColorPrimary());
            mAvailableDeleteNotification.setKey(AVAILABLE_DELETE_NOTIFICATION_ID);
            mAvailableDeleteNotification.setTitle(R.string.available_delete_notification);
        }
        mSyncCategory.removePreference(mAvailableDeleteNotification);
        mSyncCategory.addPreference(mAvailableDeleteNotification);

        if (mAvailableNewNotification == null) {
            mAvailableNewNotification = new ColorCheckBoxPreference(getActivity(), mPreferences.getColorPrimary());
            mAvailableNewNotification.setKey(AVAILABLE_NEW_NOTIFICATION_ID);
            mAvailableNewNotification.setTitle(R.string.available_new_notification);
        }
        mSyncCategory.removePreference(mAvailableNewNotification);
        mSyncCategory.addPreference(mAvailableNewNotification);

        if (mAvailableUpdateNotification == null) {
            mAvailableUpdateNotification = new ColorCheckBoxPreference(getActivity(), mPreferences.getColorPrimary());
            mAvailableUpdateNotification.setKey(AVAILABLE_UPDATE_NOTIFICATION_ID);
            mAvailableUpdateNotification.setTitle(R.string.available_update_notification);
        }
        mSyncCategory.removePreference(mAvailableUpdateNotification);
        mSyncCategory.addPreference(mAvailableUpdateNotification);

        if (mAvailableNotBoughtNotification == null) {
            mAvailableNotBoughtNotification = new ColorCheckBoxPreference(getActivity(), mPreferences.getColorPrimary());
            mAvailableNotBoughtNotification.setKey(AVAILABLE_NOT_BOUGHT_NOTIFICATION_ID);
            mAvailableNotBoughtNotification.setTitle(R.string.available_not_bought_notification);
        }
        mSyncCategory.removePreference(mAvailableNotBoughtNotification);
        mSyncCategory.addPreference(mAvailableNotBoughtNotification);

        mSound.setDependency(NOTIFICATION_ENABLE_ID);
        mVibration.setDependency(NOTIFICATION_ENABLE_ID);
        mAvailableUpdateNotification.setDependency(NOTIFICATION_ENABLE_ID);
        mAvailableNewNotification.setDependency(NOTIFICATION_ENABLE_ID);
        mAvailableDeleteNotification.setDependency(NOTIFICATION_ENABLE_ID);
        mAvailableBoughtNotification.setDependency(NOTIFICATION_ENABLE_ID);
        mAvailableNotBoughtNotification.setDependency(NOTIFICATION_ENABLE_ID);

        mNotificationEnable.setOnPreferenceClickListener(this);
        mVibration.setOnPreferenceClickListener(this);
        mSound.setOnPreferenceClickListener(this);
        mAvailableUpdateNotification.setOnPreferenceClickListener(this);
        mAvailableNewNotification.setOnPreferenceClickListener(this);
        mAvailableDeleteNotification.setOnPreferenceClickListener(this);
        mAvailableBoughtNotification.setOnPreferenceClickListener(this);
        mAvailableNotBoughtNotification.setOnPreferenceClickListener(this);
    }
}
