package com.justplay1.shoppist.ui.fragments.settings;

import com.jenzz.materialpreference.CheckBoxPreference;
import com.jenzz.materialpreference.PreferenceCategory;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.ui.views.ColorCheckBoxPreference;
import com.justplay1.shoppist.ui.views.ColorPreferenceCategory;

/**
 * Created by Mkhytar on 19.12.2015.
 */
public class NotificationSettingFragment extends BaseSettingFragment {

    public static final String NOTIFICATION_ENABLE_ID = "enable";
    public static final String VIBRATION_ID = "vibration";
    public static final String SOUND_ID = "sound";
    public static final String AVAILABLE_UPDATE_NOTIFICATION_ID = "available_update_notification_id";
    public static final String AVAILABLE_NEW_NOTIFICATION_ID = "available_new_notification_id";
    public static final String AVAILABLE_DELETE_NOTIFICATION_ID = "available_delete_notification_id";
    public static final String AVAILABLE_BOUGHT_NOTIFICATION_ID = "available_bought_notification_id";
    public static final String AVAILABLE_NOT_BOUGHT_NOTIFICATION_ID = "available_not_bought_notification_id";
    public static final String SYNC_CATEGORY = "Sync_category";

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

    protected void initializeFrame() {
        super.initializeFrame();
        updateNotificationCategoryCheckBox();

        mNotificationEnable.setChecked(ShoppistPreferences.isNotificationEnable());
        mVibration.setChecked(ShoppistPreferences.isNotificationVibration());
        mSound.setChecked(ShoppistPreferences.isNotificationSound());
        mAvailableUpdateNotification.setChecked(ShoppistPreferences.isAvailableUpdateNotification());
        mAvailableNewNotification.setChecked(ShoppistPreferences.isAvailableNewNotification());
        mAvailableDeleteNotification.setChecked(ShoppistPreferences.isAvailableDeleteNotification());
        mAvailableBoughtNotification.setChecked(ShoppistPreferences.isAvailableBoughtNotification());
        mAvailableNotBoughtNotification.setChecked(ShoppistPreferences.isAvailableNotBoughtNotification());
    }

    @Override
    public boolean onPreferenceClick(android.preference.Preference preference) {
        switch (preference.getKey()) {
            case NOTIFICATION_ENABLE_ID:
                ShoppistPreferences.setNotificationEnable(((CheckBoxPreference) preference).isChecked());
                break;
            case VIBRATION_ID:
                ShoppistPreferences.setNotificationVibration(((CheckBoxPreference) preference).isChecked());
                break;
            case SOUND_ID:
                ShoppistPreferences.setNotificationSound(((CheckBoxPreference) preference).isChecked());
                break;
            case AVAILABLE_UPDATE_NOTIFICATION_ID:
                ShoppistPreferences.setAvailableUpdateNotification(((CheckBoxPreference) preference).isChecked());
                break;
            case AVAILABLE_DELETE_NOTIFICATION_ID:
                ShoppistPreferences.setAvailableDeleteNotification(((CheckBoxPreference) preference).isChecked());
                break;
            case AVAILABLE_BOUGHT_NOTIFICATION_ID:
                ShoppistPreferences.setAvailableBoughtNotification(((CheckBoxPreference) preference).isChecked());
                break;
            case AVAILABLE_NEW_NOTIFICATION_ID:
                ShoppistPreferences.setAvailableNewNotification(((CheckBoxPreference) preference).isChecked());
                break;
            case AVAILABLE_NOT_BOUGHT_NOTIFICATION_ID:
                ShoppistPreferences.setAvailableNotBoughtNotification(((CheckBoxPreference) preference).isChecked());
                break;
        }
        return true;
    }

    protected void updateNotificationCategoryCheckBox() {
        getPreferenceScreen().removeAll();

        if (mNotificationEnable == null) {
            mNotificationEnable = new ColorCheckBoxPreference(getActivity());
            mNotificationEnable.setKey(NOTIFICATION_ENABLE_ID);
            mNotificationEnable.setTitle(R.string.notification_onn);
        }

        getPreferenceScreen().removePreference(mNotificationEnable);
        getPreferenceScreen().addPreference(mNotificationEnable);

        if (mSound == null) {
            mSound = new ColorCheckBoxPreference(getActivity());
            mSound.setKey(SOUND_ID);
            mSound.setTitle(R.string.notification_sound);
        }

        getPreferenceScreen().removePreference(mSound);
        getPreferenceScreen().addPreference(mSound);

        if (mVibration == null) {
            mVibration = new ColorCheckBoxPreference(getActivity());
            mVibration.setKey(VIBRATION_ID);
            mVibration.setTitle(R.string.notification_vibration);
        }
        getPreferenceScreen().removePreference(mVibration);
        getPreferenceScreen().addPreference(mVibration);

        if (getPreferenceScreen().findPreference(SYNC_CATEGORY) != null){
            getPreferenceScreen().removePreference(mSyncCategory);
        }

        if (mSyncCategory == null) {
            mSyncCategory = new ColorPreferenceCategory(getActivity());
            mSyncCategory.setKey(SYNC_CATEGORY);
            mSyncCategory.setTitle(R.string.synchronization);
        }
        getPreferenceScreen().removePreference(mSyncCategory);
        getPreferenceScreen().addPreference(mSyncCategory);

        if (mAvailableBoughtNotification == null) {
            mAvailableBoughtNotification = new ColorCheckBoxPreference(getActivity());
            mAvailableBoughtNotification.setKey(AVAILABLE_BOUGHT_NOTIFICATION_ID);
            mAvailableBoughtNotification.setTitle("Notify About Bought Goods");
        }
        mSyncCategory.removePreference(mAvailableBoughtNotification);
        mSyncCategory.addPreference(mAvailableBoughtNotification);

        if (mAvailableDeleteNotification == null) {
            mAvailableDeleteNotification = new ColorCheckBoxPreference(getActivity());
            mAvailableDeleteNotification.setKey(AVAILABLE_DELETE_NOTIFICATION_ID);
            mAvailableDeleteNotification.setTitle(R.string.available_delete_notification);
        }
        mSyncCategory.removePreference(mAvailableDeleteNotification);
        mSyncCategory.addPreference(mAvailableDeleteNotification);

        if (mAvailableNewNotification == null) {
            mAvailableNewNotification = new ColorCheckBoxPreference(getActivity());
            mAvailableNewNotification.setKey(AVAILABLE_NEW_NOTIFICATION_ID);
            mAvailableNewNotification.setTitle(R.string.available_new_notification);
        }
        mSyncCategory.removePreference(mAvailableNewNotification);
        mSyncCategory.addPreference(mAvailableNewNotification);

        if (mAvailableUpdateNotification == null) {
            mAvailableUpdateNotification = new ColorCheckBoxPreference(getActivity());
            mAvailableUpdateNotification.setKey(AVAILABLE_UPDATE_NOTIFICATION_ID);
            mAvailableUpdateNotification.setTitle(R.string.available_update_notification);
        }
        mSyncCategory.removePreference(mAvailableUpdateNotification);
        mSyncCategory.addPreference(mAvailableUpdateNotification);

        if (mAvailableNotBoughtNotification == null) {
            mAvailableNotBoughtNotification = new ColorCheckBoxPreference(getActivity());
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
