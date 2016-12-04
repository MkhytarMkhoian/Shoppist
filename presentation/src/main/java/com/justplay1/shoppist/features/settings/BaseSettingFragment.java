/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.justplay1.shoppist.features.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.XmlRes;

import com.jenzz.materialpreference.Preference;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.preferences.AppPreferences;

import com.justplay1.shoppist.shared.widget.CustomProgressDialog;
import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseSettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    protected CustomProgressDialog progressDialog;

    @Inject
    AppPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
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
        progressDialog = new CustomProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);
    }
}
