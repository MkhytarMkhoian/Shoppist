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
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.GoodsComponent;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.navigation.GoodsRouter;
import com.justplay1.shoppist.presenter.GoodsPresenter;
import com.justplay1.shoppist.view.GoodsView;
import com.justplay1.shoppist.view.adapters.GoodsAdapter;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;
import com.justplay1.shoppist.view.fragments.dialog.AddGoodsDialogFragment;
import com.justplay1.shoppist.view.fragments.dialog.AddUnitsDialogFragment;
import com.justplay1.shoppist.view.fragments.dialog.SelectCategoryDialogFragment;
import com.justplay1.shoppist.view.fragments.dialog.SelectUnitDialogFragment;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
public class GoodsFragment extends BaseExpandableListFragment
        implements ShoppistRecyclerView.OnItemClickListener<BaseItemHolder>, View.OnClickListener, GoodsView {

    @Inject
    GoodsPresenter mPresenter;

    private GoodsAdapter mAdapter;

    public static GoodsFragment newInstance() {

        Bundle args = new Bundle();

        GoodsFragment fragment = new GoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mPresenter.attachRouter((GoodsRouter) getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
        mPresenter.detachRouter();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        getInjector(GoodsComponent.class).inject(this);
    }

    @Override
    protected void initAdapter() {
        mAdapter = new GoodsAdapter(getContext(), mActionModeInteractionListener, mRecyclerView, mPreferences);
        mAdapter.setClickListener(this);
    }

    protected void initRecyclerView(View view, Bundle savedInstanceState) {
        super.initRecyclerView(view, savedInstanceState);
        initAdapter();

        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
        mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);

        mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(mAdapter);       // wrap for expanding

        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        animator.setSupportsChangeAnimations(false);

        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z1)));
        }

        mRecyclerViewExpandableItemManager.attachRecyclerView(mRecyclerView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_with_button;
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        holder.toggle();
        return true;
    }

    public boolean isEditButtonEnable() {
        boolean editFlag = true;
        if (mAdapter.getCheckedCount() != 1) {
            editFlag = false;
        }
        return editFlag;
    }

    public boolean isCheckAllButtonEnable() {
        return !mAdapter.isAllItemsChecked();
    }

    public void onChangeCategoryClick() {
        mPresenter.onChangeCategoryClick(mAdapter.getCheckedItems());
    }

    public void onChangeUnitClick() {
        mPresenter.onChangeUnitClick(mAdapter.getCheckedItems());
    }

    public void onCheckAllItemsClick() {
        mAdapter.checkAllItems();
    }

    public void onUnCheckAllItemsClick() {
        mAdapter.unCheckAllItems();
    }

    public void onDeleteCheckedItemsClick() {
        deleteItems(getString(R.string.delete_the_goods),
                () -> mAdapter.deleteCheckedView(deleteItems -> mPresenter.deleteItems(deleteItems)));
    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        mPresenter.onListItemClick(mAdapter.getChildItem(holder.groupPosition, holder.childPosition));
    }

    @Override
    public void onClick(View v) {
        mPresenter.onAddButtonClick();
    }

    @Override
    public void showChangeCategoryDialog(final List<ProductViewModel> editProducts) {
        FragmentManager fm = getFragmentManager();
        final SelectCategoryDialogFragment dialog = SelectCategoryDialogFragment.newInstance();
        dialog.setCompleteListener((category, isUpdate) -> mPresenter.changeCategory(category, editProducts));
        dialog.show(fm, AddUnitsDialogFragment.class.getName());
    }

    @Override
    public void showChangeUnitDialog(final List<ProductViewModel> editProducts) {
        FragmentManager fm = getFragmentManager();
        final SelectUnitDialogFragment dialog = SelectUnitDialogFragment.newInstance();
        dialog.setCompleteListener((unit, isUpdate) -> mPresenter.changeUnit(unit, editProducts));
        dialog.show(fm, AddUnitsDialogFragment.class.getName());
    }

    @Override
    public void showEditGoodsDialog(final ProductViewModel editProduct) {
        FragmentManager fm = getFragmentManager();
        AddGoodsDialogFragment dialog = AddGoodsDialogFragment.newInstance(editProduct);
        dialog.show(fm, AddUnitsDialogFragment.class.getName());
    }

    @Override
    public void showData(List<Pair<HeaderViewModel, List<ProductViewModel>>> data) {
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
        mRecyclerViewExpandableItemManager.expandAll();
    }

    @Override
    public void showLoading() {
        mEmptyView.showProgressBar();
    }

    @Override
    public void hideLoading() {
        mEmptyView.hideProgressBar();
    }

    @Override
    public Context context() {
        return getContext();
    }

    public void onExpandAllClick() {
        mRecyclerViewExpandableItemManager.expandAll();
    }

    public void onCollapseAllClick() {
        mRecyclerViewExpandableItemManager.collapseAll();
    }

    public void onSortByNameClick() {
        mPresenter.onSortByNameClick(mAdapter.getItems());
    }

    public void onSortByTimeCreatedClick() {
        mPresenter.onSortByTimeCreatedClick(mAdapter.getItems());
    }

    public void onSortByCategoryClick() {
        mPresenter.onSortByCategoryClick(mAdapter.getItems());
    }

    public void onSearchClick() {
        mPresenter.onSearchClick();
    }
}
