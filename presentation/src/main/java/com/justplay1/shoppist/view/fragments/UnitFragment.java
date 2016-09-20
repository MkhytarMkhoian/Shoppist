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

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.DaggerUnitsComponent;
import com.justplay1.shoppist.di.components.UnitsComponent;
import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.UnitsModule;
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
public class UnitFragment extends BaseListFragment
        implements UnitsView, ShoppistRecyclerView.OnItemClickListener<BaseItemHolder>, View.OnClickListener, UnitRouter {

    @Inject
    UnitsPresenter mPresenter;

    private UnitsComponent mComponent;
    private UnitsAdapter mAdapter;

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
        mPresenter.attachView(this);
        mPresenter.attachRouter(this);
        mPresenter.init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
        mPresenter.detachRouter();
    }

    @Override
    protected void initAdapter() {
        mAdapter = new UnitsAdapter(getContext(), mActionModeInteractionListener, mRecyclerView);
        mAdapter.setClickListener(this);
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        mComponent = DaggerUnitsComponent.builder()
                .appComponent(App.get().getAppComponent())
                .activityModule(new ActivityModule(getActivity()))
                .unitsModule(new UnitsModule())
                .build();
        mComponent.inject(this);
    }

    @Override
    protected void initRecyclerView(View view, Bundle savedInstanceState) {
        super.initRecyclerView(view, savedInstanceState);
        mRecyclerView.setAdapter(mAdapter);
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

    @Override
    public void openUnitEditDialog(UnitViewModel unit) {
        FragmentManager fm = getFragmentManager();
        AddUnitsDialogFragment dialog = AddUnitsDialogFragment.newInstance(unit);
        dialog.show(fm, AddUnitsDialogFragment.class.getName());
    }

    public void onCheckAllItemsClick() {
        mAdapter.checkAllItems();
    }

    public void onUnCheckAllItemsClick() {
        mAdapter.unCheckAllItems();
    }

    public void onDeleteCheckedItemsClick() {
        deleteItems(getString(R.string.delete_the_item),
                () -> mAdapter.deleteCheckedView(deleteItems -> mPresenter.deleteItems(deleteItems)));
    }

    public boolean isDeleteButtonEnable() {
        boolean deleteFlag = true;
        for (UnitViewModel unit : mAdapter.getCheckedItems()) {
            if (unit.getId().equals(UnitViewModel.NO_UNIT_ID)) {
                deleteFlag = false;
            }
        }
        return deleteFlag;
    }

    public boolean isCheckAllButtonEnable() {
        return !mAdapter.isAllItemsChecked();
    }

    @Override
    public void showData(List<UnitViewModel> data) {
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        mEmptyView.showProgressBar();
    }

    @Override
    public void hideLoading() {
        mEmptyView.hideProgressBar();
    }
}
