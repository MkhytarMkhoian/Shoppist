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

package com.justplay1.shoppist.view.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.CurrencyComponent;
import com.justplay1.shoppist.di.components.DaggerCurrencyComponent;
import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.CurrencyModule;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.presenter.CurrencyPresenter;
import com.justplay1.shoppist.view.CurrencyView;
import com.justplay1.shoppist.view.adapters.CurrencyAdapter;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;
import com.justplay1.shoppist.view.fragments.dialog.AddCurrencyDialogFragment;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CurrencyFragment extends BaseListFragment
        implements CurrencyView, ShoppistRecyclerView.OnItemClickListener, View.OnClickListener {

    @Inject
    CurrencyPresenter mPresenter;

    private CurrencyComponent mComponent;
    private FloatingActionButton mActionButton;
    private CurrencyAdapter mAdapter;

    public static CurrencyFragment newInstance() {

        Bundle args = new Bundle();

        CurrencyFragment fragment = new CurrencyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
    protected void init(View view) {
        super.init(view);
        mActionButton = (FloatingActionButton) view.findViewById(R.id.add_button);
        mActionButton.setBackgroundTintList(ColorStateList.valueOf(mPreferences.getColorPrimary()));
        mActionButton.setOnClickListener(this);
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
    protected void initRecyclerView(View view, Bundle savedInstanceState) {
        super.initRecyclerView(view, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_with_button;
    }

    @Override
    protected void initAdapter() {
        mAdapter = new CurrencyAdapter(getContext(), mActionModeInteractionListener, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
    public void showCurrencyAddDialog(CurrencyViewModel currency) {
        FragmentManager fm = getFragmentManager();
        AddCurrencyDialogFragment dialog = AddCurrencyDialogFragment.newInstance(currency);
        dialog.setCompleteListener(isUpdate -> mPresenter.loadData());
        dialog.show(fm, AddCurrencyDialogFragment.class.getName());
    }

    @Override
    public void onClick(View v) {
        mPresenter.onAddButtonClick();
    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        mPresenter.onListItemClick(mAdapter.getItem(position));
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        holder.toggle();
        return true;
    }

    public void onCheckAllItemsClick() {
        mAdapter.checkAllItems();
    }

    public void onUnCheckAllItemsClick() {
        mAdapter.unCheckAllItems();
    }

    public void onDeleteCheckedItemsClick() {
//        showDeleteDialog(getString(R.string.delete_the_category));
    }

    public boolean isDeleteButtonEnable() {
        boolean deleteFlag = true;
        for (CurrencyViewModel currency : mAdapter.getCheckedItems()) {
            if (currency.getId().equals(CurrencyViewModel.NO_CURRENCY_ID)) {
                deleteFlag = false;
            }
        }
        return deleteFlag;
    }

    @Override
    public void showData(List<CurrencyViewModel> data) {
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
    }
}
