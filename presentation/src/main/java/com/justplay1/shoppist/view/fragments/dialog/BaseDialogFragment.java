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

package com.justplay1.shoppist.view.fragments.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.HasInjector;
import com.justplay1.shoppist.preferences.AppPreferences;
import com.justplay1.shoppist.view.component.CustomProgressDialog;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseDialogFragment extends AppCompatDialogFragment
        implements View.OnClickListener {

    @Inject
    protected AppPreferences preferences;
    @Bind(R.id.positive_button)
    protected Button positiveButton;
    @Bind(R.id.negative_button)
    protected Button negativeButton;
    protected CustomProgressDialog progressDialog;

    @LayoutRes
    protected abstract int getLayoutId();

    private HasInjector hasInjector;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            hasInjector = (HasInjector) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement HasInjector");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
    }

    /**
     * Inject the dependencies
     */
    protected void injectDependencies() {
        App.get().getAppComponent().inject(this);
    }

    /**
     * Gets a component for dependency injection by its type.
     */
    @SuppressWarnings("unchecked")
    protected <C> C getInjector(Class<C> injectorType) {
        return injectorType.cast(hasInjector.getInjector(injectorType.getName()));
    }

    protected void putInjector(String id, Object component) {
        hasInjector.putInjector(id, component);
    }

    public void init(View view) {
        positiveButton.setTextColor(preferences.getColorPrimary());
        negativeButton.setTextColor(preferences.getColorPrimary());
        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);

        progressDialog = new CustomProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.please_wait));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners_dialog);
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        init(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
