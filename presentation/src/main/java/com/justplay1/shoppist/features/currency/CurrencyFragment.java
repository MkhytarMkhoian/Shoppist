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

package com.justplay1.shoppist.features.currency;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.CurrencyComponent;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.features.currency.add.AddCurrencyDialogFragment;

import com.justplay1.shoppist.shared.base.fragments.BaseListFragment;
import com.justplay1.shoppist.shared.widget.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.shared.widget.recyclerview.holders.BaseItemHolder;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CurrencyFragment extends BaseListFragment<CurrencyViewModel, CurrencyAdapter>
        implements CurrencyView, ShoppistRecyclerView.OnItemClickListener<BaseItemHolder>, CurrencyRouter {

    @Inject
    CurrencyPresenter presenter;

    public static CurrencyFragment newInstance() {
        Bundle args = new Bundle();
        CurrencyFragment fragment = new CurrencyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.attachRouter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        presenter.detachRouter();
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        getInjector(CurrencyComponent.class).inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_with_button;
    }

    @Override
    protected void initAdapter() {
        adapter = new CurrencyAdapter(getContext(), actionModeInteractionListener, recyclerView);
        adapter.setClickListener(this);
    }

    @Override
    protected void initRecyclerView(View view, Bundle savedInstanceState) {
        super.initRecyclerView(view, savedInstanceState);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showLoading() {
        emptyView.showProgressBar();
    }

    @Override
    public void hideLoading() {
        emptyView.hideProgressBar();
    }

    @Override
    public void showCurrencyEditDialog(CurrencyViewModel currency) {
        FragmentManager fm = getFragmentManager();
        AddCurrencyDialogFragment dialog = AddCurrencyDialogFragment.newInstance(currency);
        dialog.show(fm, AddCurrencyDialogFragment.class.getName());
    }

    @Override
    public void onClick(View v) {
        presenter.onAddButtonClick();
    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        presenter.onListItemClick(adapter.getItem(position));
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        holder.toggle();
        return true;
    }

    public void onCheckAllItemsClick() {
        adapter.checkAllItems();
    }

    public void onUnCheckAllItemsClick() {
        adapter.unCheckAllItems();
    }

    public void onDeleteCheckedItemsClick() {
        deleteItems(getString(R.string.delete_the_item),
                () -> adapter.deleteCheckedView(deleteItems -> presenter.deleteItems(deleteItems)));
    }

    public boolean isDeleteButtonEnable() {
        boolean deleteFlag = true;
        for (CurrencyViewModel currency : adapter.getCheckedItems()) {
            if (currency.getId().equals(CurrencyViewModel.NO_CURRENCY_ID)) {
                deleteFlag = false;
            }
        }
        return deleteFlag;
    }

    public boolean isCheckAllButtonEnable() {
        return !adapter.isAllItemsChecked();
    }

    @Override
    public void showData(List<CurrencyViewModel> data) {
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }
}
