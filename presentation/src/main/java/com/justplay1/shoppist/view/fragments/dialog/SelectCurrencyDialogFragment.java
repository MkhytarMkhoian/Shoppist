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
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.CurrencyComponent;
import com.justplay1.shoppist.di.components.DaggerCurrencyComponent;
import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.CurrencyModule;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.presenter.SelectCurrencyPresenter;
import com.justplay1.shoppist.view.SelectCurrencyView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
public class SelectCurrencyDialogFragment extends BaseSelectItemDialogFragment<CurrencyViewModel>
        implements SelectCurrencyView {

    @Inject
    SelectCurrencyPresenter mPresenter;
    private CurrencyComponent mComponent;

    public static SelectCurrencyDialogFragment newInstance(CurrencyViewModel currency) {
        Bundle args = new Bundle();
        args.putParcelable(CurrencyViewModel.class.getName(), currency);
        SelectCurrencyDialogFragment fragment = new SelectCurrencyDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        mComponent = DaggerCurrencyComponent.builder()
                .appComponent(App.get().getAppComponent())
                .activityModule(new ActivityModule(getActivity()))
                .currencyModule(new CurrencyModule())
                .build();
        mComponent.inject(this);
    }

    @Override
    public void init(View view) {
        super.init(view);
        mPositiveButton.setText(R.string.select);
        getDialog().setTitle(R.string.currency);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.onCreate(getArguments(), savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mPresenter.init();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
        mPresenter.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_currency_dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button:
                mPresenter.onAddCurrencyClick();
                break;
            case R.id.edit_button:
                mPresenter.onEditCurrencyClick(mSelectView.getSelectedItem());
                break;
            case R.id.positive_button:
                mPresenter.onPositiveButtonClick(mSelectView.getSelectedItem());
                break;
            case R.id.negative_button:
                mPresenter.onNegativeButtonClick();
                break;
        }
    }

    @Override
    public void showCurrencyDialog(CurrencyViewModel editCurrency) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AddCurrencyDialogFragment dialog = AddCurrencyDialogFragment.newInstance(editCurrency);
        dialog.setCompleteListener(isUpdate -> mPresenter.loadCurrencies());
        dialog.show(fm, AddCurrencyDialogFragment.class.getName());
    }

    @Override
    public void setCurrencies(List<CurrencyViewModel> data) {
        mSelectView.setData(data);
    }

    @Override
    public void selectCurrency(String id) {
        mSelectView.selectItem(id);
    }

    @Override
    public void onComplete(CurrencyViewModel currency, boolean isUpdate) {
        if (mCompleteListener != null) {
            mCompleteListener.onComplete(currency, isUpdate);
        }
    }

    @Override
    public void closeDialog() {
        dismiss();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
