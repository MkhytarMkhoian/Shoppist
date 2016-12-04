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
package com.justplay1.shoppist.features.currency.add;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.CurrencyComponent;
import com.justplay1.shoppist.di.components.DaggerCurrencyComponent;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.shared.base.fragments.dialog.BaseDialogFragment;
import com.justplay1.shoppist.utils.ShoppistUtils;
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
    AddCurrencyPresenter presenter;
    @Bind(R.id.name_edit)
    MaterialEditText nameEdit;
    private OnCompleteListener completeListener;

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
        CurrencyComponent component = getInjector(CurrencyComponent.class);
        if (component == null) {
            component = DaggerCurrencyComponent.builder()
                    .appComponent(App.get().getAppComponent())
                    .build();
            putInjector(CurrencyComponent.class.getName(), component);
        }
        component.inject(this);
    }

    @Override
    public void init(View view) {
        super.init(view);
        nameEdit.setPrimaryColor(preferences.getColorPrimary());
        nameEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.onCreate(getArguments(), savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter.attachView(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nameEdit.setSelection(nameEdit.getText().length());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
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
                final String name = ShoppistUtils.filterSpace(nameEdit.getText().toString());
                presenter.onPositiveButtonClick(name);
                break;
            case R.id.negative_button:
                presenter.onNegativeButtonClick();
                break;
        }
    }

    @Override
    public void onComplete(boolean isUpdate) {
        if (completeListener != null) {
            completeListener.onComplete(true);
        }
    }

    @Override
    public void showNameIsRequiredError() {
        nameEdit.setError(getString(R.string.currency_name_is_required));
    }

    @Override
    public void closeDialog() {
        ShoppistUtils.hideKeyboard(getContext(), nameEdit);
        dismiss();
    }

    @Override
    public void setName(String name) {
        nameEdit.setText(name);
    }

    @Override
    public void setDefaultUpdateTitle() {
        positiveButton.setText(R.string.update);
        getDialog().setTitle(R.string.edit_currency);
    }

    @Override
    public void setDefaultNewTitle() {
        getDialog().setTitle(R.string.new_currency);
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    public void setCompleteListener(OnCompleteListener listener) {
        completeListener = listener;
    }

    public interface OnCompleteListener {

        void onComplete(boolean isUpdate);
    }
}
