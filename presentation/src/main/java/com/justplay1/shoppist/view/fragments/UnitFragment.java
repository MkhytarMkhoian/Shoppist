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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.UnitsComponent;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.navigation.UnitRouter;
import com.justplay1.shoppist.presenter.UnitsPresenter;
import com.justplay1.shoppist.view.UnitsView;
import com.justplay1.shoppist.view.adapters.UnitsAdapter;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;
import com.justplay1.shoppist.view.fragments.dialog.AddUnitsDialogFragment;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
public class UnitFragment extends BaseListFragment<UnitViewModel, UnitsAdapter>
        implements UnitsView, ShoppistRecyclerView.OnItemClickListener<BaseItemHolder>, View.OnClickListener, UnitRouter {

    @Inject
    UnitsPresenter presenter;

    public static UnitFragment newInstance() {
        Bundle args = new Bundle();
        UnitFragment fragment = new UnitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_with_button;
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
    protected void initAdapter() {
        adapter = new UnitsAdapter(getContext(), actionModeInteractionListener, recyclerView);
        adapter.setClickListener(this);
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        getInjector(UnitsComponent.class).inject(this);
    }

    @Override
    protected void initRecyclerView(View view, Bundle savedInstanceState) {
        super.initRecyclerView(view, savedInstanceState);
        recyclerView.setAdapter(adapter);
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

    @Override
    public void openUnitEditDialog(UnitViewModel unit) {
        FragmentManager fm = getFragmentManager();
        AddUnitsDialogFragment dialog = AddUnitsDialogFragment.newInstance(unit);
        dialog.show(fm, AddUnitsDialogFragment.class.getName());
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
        for (UnitViewModel unit : adapter.getCheckedItems()) {
            if (unit.getId().equals(UnitViewModel.NO_UNIT_ID)) {
                deleteFlag = false;
            }
        }
        return deleteFlag;
    }

    public boolean isCheckAllButtonEnable() {
        return !adapter.isAllItemsChecked();
    }

    @Override
    public void showData(List<UnitViewModel> data) {
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        emptyView.showProgressBar();
    }

    @Override
    public void hideLoading() {
        emptyView.hideProgressBar();
    }
}
