package com.justplay1.shoppist.view.fragments.settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.jenzz.materialpreference.Preference;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.presenter.AccountSettingPresenter;
import com.justplay1.shoppist.view.AccountSettingView;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 19.12.2015.
 */
public class AccountSettingFragment extends BaseSettingFragment implements AccountSettingView {

    private static final String LOGOUT_BTN_ID = "account_log_out";
    private static final String ACCOUNT_DELETE_ID = "account_delete";
    private static final String SYNC_ALL_DATA_ID = "account_sync";

    private Preference mLogoutBtn;
    private Preference mSyncAllDataBtn;
    private Preference mAccountDeleteBtn;

    @Inject
    AccountSettingPresenter mPresenter;

    public static AccountSettingFragment newInstance() {
        return new AccountSettingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.onCreate(getArguments(), savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mPresenter.init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    protected void initFrame() {
        super.initFrame();

        mLogoutBtn = (Preference) findPreference(LOGOUT_BTN_ID);
        mAccountDeleteBtn = (Preference) findPreference(ACCOUNT_DELETE_ID);
        mSyncAllDataBtn = (Preference) findPreference(SYNC_ALL_DATA_ID);

        mLogoutBtn.setOnPreferenceClickListener(this);
        mAccountDeleteBtn.setOnPreferenceClickListener(this);
        mSyncAllDataBtn.setOnPreferenceClickListener(this);
    }

    @Override
    public void disableAccountButtons() {
        mLogoutBtn.setEnabled(false);
        mAccountDeleteBtn.setEnabled(false);
        mSyncAllDataBtn.setEnabled(false);
    }

    @Override
    public void setAccountName(String name) {
        mLogoutBtn.setSummary(name);
    }

    @Override
    public void showLoading() {
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showError(String message) {

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

                break;
        }
        return true;
    }

    @Override
    protected int getPreferencesResId() {
        return R.xml.account_setting;
    }

    private void showLogoutDialog() {
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    mPresenter.logOut();
                    dialog.dismiss();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        };

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.account_log_out) + "?");
        builder.setPositiveButton(R.string.account_log_out, listener);
        builder.setNegativeButton(R.string.cancel, listener);
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(mPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(mPreferences.getColorPrimary());
    }

    private void showDeleteAccountDialog() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        mPresenter.deleteAccount();
                        dialog.dismiss();
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
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(mPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(mPreferences.getColorPrimary());
    }
}
