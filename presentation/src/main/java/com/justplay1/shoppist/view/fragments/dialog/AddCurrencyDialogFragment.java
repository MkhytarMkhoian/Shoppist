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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.CurrencyComponent;
import com.justplay1.shoppist.di.components.DaggerCurrencyComponent;
import com.justplay1.shoppist.di.modules.CurrencyModule;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.presenter.AddCurrencyPresenter;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.AddCurrencyView;
import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mkhytar Mkhoian.
 */
public class AddCurrencyDialogFragment extends BaseDialogFragment
        implements AddCurrencyView {

    @Inject
    AddCurrencyPresenter mPresenter;
    @Bind(R.id.name_edit)
    MaterialEditText mNameEdit;
    private CurrencyComponent mComponent;
    private OnCompleteListener mCompleteListener;

    public static AddCurrencyDialogFragment newInstance(CurrencyViewModel currency) {
        Bundle args = new Bundle();
        args.putParcelable(CurrencyViewModel.class.getName(), currency);
        AddCurrencyDialogFragment fragment = new AddCurrencyDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        mComponent = DaggerCurrencyComponent.builder()
                .appComponent(App.get().getAppComponent())
                .build();
        mComponent.inject(this);
    }

    @Override
    public void init(View view) {
        super.init(view);
        mNameEdit.setPrimaryColor(mPreferences.getColorPrimary());
        mNameEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.onCreate(getArguments(), savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mPresenter.attachView(this);
        mPresenter.init();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mNameEdit.setSelection(mNameEdit.getText().length());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
        mPresenter.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_currency_editor_dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positive_button:
                final String name = ShoppistUtils.filterSpace(mNameEdit.getText().toString());
                mPresenter.onPositiveButtonClick(name);
                break;
            case R.id.negative_button:
                mPresenter.onNegativeButtonClick();
                break;
        }
    }

    @Override
    public void onComplete(boolean isUpdate) {
        if (mCompleteListener != null) {
            mCompleteListener.onComplete(true);
        }
    }

    @Override
    public void showNameIsRequiredError() {
        mNameEdit.setError(getString(R.string.currency_name_is_required));
    }

    @Override
    public void closeDialog() {
        ShoppistUtils.hideKeyboard(getContext(), mNameEdit);
        dismiss();
    }

    @Override
    public void setName(String name) {
        mNameEdit.setText(name);
    }

    @Override
    public void setDefaultUpdateTitle() {
        mPositiveButton.setText(R.string.update);
        getDialog().setTitle(R.string.edit_currency);
    }

    @Override
    public void setDefaultNewTitle() {
        getDialog().setTitle(R.string.new_currency);
    }

    @Override
    public void showLoading() {
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        mProgressDialog.dismiss();
    }

    public void setCompleteListener(OnCompleteListener listener) {
        mCompleteListener = listener;
    }

    public interface OnCompleteListener {

        void onComplete(boolean isUpdate);
    }
}
