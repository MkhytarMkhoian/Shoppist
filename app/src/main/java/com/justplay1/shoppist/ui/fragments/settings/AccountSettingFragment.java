package com.justplay1.shoppist.ui.fragments.settings;

import android.app.Dialog;
import android.content.DialogInterface;

import com.jenzz.materialpreference.Preference;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.communication.network.ServerRequests;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.DialogUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.parse.ParseUser;

/**
 * Created by Mkhytar on 19.12.2015.
 */
public class AccountSettingFragment extends BaseSettingFragment {

    public static final String LOGOUT_BTN_ID = "account_log_out";
    public static final String ACCOUNT_DELETE_ID = "account_delete";
    public static final String SYNC_ALL_DATA_ID = "account_sync";
    public static final String SUBSCRIPTION_ID = "account_subscription";

    private Preference mLogoutBtn;
    private Preference mSyncAllDataBtn;
    private Preference mAccountDeleteBtn;
//    private Preference mSubscriptionBtn;

    public static AccountSettingFragment newInstance() {
        return new AccountSettingFragment();
    }

    @Override
    protected void initializeFrame() {
        super.initializeFrame();

        mLogoutBtn = (Preference) findPreference(LOGOUT_BTN_ID);
        mAccountDeleteBtn = (Preference) findPreference(ACCOUNT_DELETE_ID);
        mSyncAllDataBtn = (Preference) findPreference(SYNC_ALL_DATA_ID);
//        mSubscriptionBtn = (Preference) findPreference(SUBSCRIPTION_ID);
//
//        mSubscriptionBtn.setOnPreferenceClickListener(this);
        mLogoutBtn.setOnPreferenceClickListener(this);
        mAccountDeleteBtn.setOnPreferenceClickListener(this);
        mSyncAllDataBtn.setOnPreferenceClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
            String name = String.valueOf(ParseUser.getCurrentUser().get(ServerRequests.USER_OBJECT_NAME_FIELD));
            if (name.equals("") || name.equals("null")) {
                mLogoutBtn.setSummary(ParseUser.getCurrentUser().getUsername());
            } else {
                mLogoutBtn.setSummary(name);
            }
        } else {
            disableAccountButtons();
        }
    }

    private void disableAccountButtons() {
        mLogoutBtn.setEnabled(false);
        mAccountDeleteBtn.setEnabled(false);
        mSyncAllDataBtn.setEnabled(false);
//        mSubscriptionBtn.setEnabled(false);
    }

    @Override
    public boolean onPreferenceClick(android.preference.Preference preference) {
        switch (preference.getKey()) {
            case LOGOUT_BTN_ID:
                showLogoutDialog();
                break;
            case ACCOUNT_DELETE_ID:
                showDeleteAccountDialog();
                break;
            case SYNC_ALL_DATA_ID:
                ServerRequests.fullSync();
                break;
            case SUBSCRIPTION_ID:
//                ActivityUtils.startNextActivity(getActivity(), SubscriptionsActivity.class, null, null);
                break;
        }
        return true;
    }

    @Override
    protected int getPreferencesResId() {
        return R.xml.account_setting;
    }

    private void showLogoutDialog() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        ServerRequests.logout(getActivity(), new ExecutorListener<Boolean>() {
                            @Override
                            public void start() {
                                mProgressDialog.show();
                            }

                            @Override
                            public void complete(Boolean result) {
                                mLogoutBtn.setSummary("");
                                disableAccountButtons();

                                mProgressDialog.dismiss();
                                dialog.dismiss();
                            }

                            @Override
                            public void error(Exception e) {
                                mProgressDialog.dismiss();
                                DialogUtils.showErrorDialog(getActivity(),
                                        ShoppistUtils.getParseMessageFromException(getActivity(), e.getMessage()));
                            }
                        });

                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.account_log_out) + "?");
        builder.setPositiveButton(R.string.account_log_out, listener);
        builder.setNegativeButton(R.string.cancel, listener);
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ShoppistPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(ShoppistPreferences.getColorPrimary());
    }

    private void showDeleteAccountDialog() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        ServerRequests.deleteAccount(getActivity(), new ExecutorListener<Boolean>() {
                            @Override
                            public void start() {
                                mProgressDialog.show();
                            }

                            @Override
                            public void complete(Boolean result) {
                                mLogoutBtn.setSummary("");
                                disableAccountButtons();

                                mProgressDialog.dismiss();
                                dialog.dismiss();
                            }

                            @Override
                            public void error(Exception e) {
                                mProgressDialog.dismiss();
                                DialogUtils.showErrorDialog(getActivity(),
                                        ShoppistUtils.getParseMessageFromException(getActivity(), e.getMessage()));
                            }
                        });

                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setMessage(getActivity().getString(R.string.delete_account_message));
        builder.setPositiveButton(getString(R.string.delete), listener);
        builder.setNegativeButton(R.string.cancel, listener);
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ShoppistPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(ShoppistPreferences.getColorPrimary());
    }
}
