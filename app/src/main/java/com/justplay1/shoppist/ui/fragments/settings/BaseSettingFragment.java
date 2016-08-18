package com.justplay1.shoppist.ui.fragments.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.XmlRes;
import android.support.v7.app.AlertDialog;

import com.jenzz.materialpreference.Preference;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.utils.ActivityUtils;
import com.justplay1.shoppist.ui.views.CustomProgressDialog;

/**
 * Created by Mkhytar on 19.12.2015.
 */
public abstract class BaseSettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    protected AlertDialog mInfoDialog;
    protected CustomProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(getPreferencesResId());
        ActivityUtils.setStatusBarColor(getActivity());
        initializeFrame();
    }

    protected abstract @XmlRes int getPreferencesResId();

    protected void initializeFrame() {
        mProgressDialog = new CustomProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);
    }
}
