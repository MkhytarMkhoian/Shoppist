package com.justplay1.shoppist.view.fragments.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.XmlRes;

import com.jenzz.materialpreference.Preference;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.view.component.CustomProgressDialog;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 19.12.2015.
 */
public abstract class BaseSettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    protected CustomProgressDialog mProgressDialog;

    @Inject
    ShoppistPreferences mPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        addPreferencesFromResource(getPreferencesResId());
        initFrame();
    }

    /**
     * Inject the dependencies
     */
    protected void injectDependencies() {
        App.get().getAppComponent().inject(this);
    }

    protected abstract
    @XmlRes
    int getPreferencesResId();

    protected void initFrame() {
        mProgressDialog = new CustomProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);
    }
}
